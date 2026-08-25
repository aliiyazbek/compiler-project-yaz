package generator;

import ast.base.ASTNode;
import ast.frontend.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The code-generation back end: walks a Jinja AST together with a
 * {@link TemplateContext} and emits finished HTML.
 *
 * <p>This is deliberately an <em>evaluating</em> walk rather than the tree-tagging
 * that {@link Generator} performs. {@code Generator} annotates the template tree
 * with {@link JinjaBoundDataNode}s so the binding is visible in a tree dump, but
 * that shape cannot produce correct output: a {@code {% for %}} over three rows
 * has to emit its body <em>three times, each with different values</em>, whereas
 * tagging leaves one body carrying three stacked markers. So the emitter keeps
 * its own environment and re-walks the loop body once per row.
 *
 * <p>What it handles:
 * <ul>
 *   <li>{@code {{ expr }}} — substituted from the context, HTML-escaped.</li>
 *   <li>{@code {% for x in xs %}} — body re-emitted per element, with {@code x}
 *       (and {@code loop.index}) bound.</li>
 *   <li>{@code {% if cond %}} — emitted only when the condition is truthy;
 *       supports {@code ==}, {@code !=}, {@code <}, {@code >} and bare truthiness.</li>
 *   <li>{@code {% extends %}} / {@code {% block %}} — the child's blocks are
 *       resolved into the parent layout, so an inheriting template renders whole.</li>
 *   <li>{@code {{ ... }}} <b>inside attribute values</b>, which the grammar hands
 *       over as one opaque STRING token and so must be interpolated here.</li>
 * </ul>
 *
 * <p>Because the lexer skips whitespace, the original spacing is not recoverable
 * from the AST. The emitter therefore pretty-prints: block-level elements go on
 * their own indented lines, inline content stays on one line.
 */
public class HtmlEmitter {

    /** Tags that are void in HTML — emitted as <tag ...> with no closing tag. */
    private static final List<String> VOID_TAGS = java.util.Arrays.asList(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr");

    /** Tags whose content stays on the same line as the tag itself. */
    private static final List<String> INLINE_TAGS = java.util.Arrays.asList(
            "a", "span", "b", "i", "em", "strong", "small", "label",
            "h1", "h2", "h3", "h4", "h5", "h6", "p", "title", "button", "li", "td", "th");

    private final StringBuilder out = new StringBuilder();
    private final List<String> log = new ArrayList<>();

    /** Blocks defined by the child template, keyed by block name. */
    private Map<String, JinjaBlockNode> childBlocks = new HashMap<>();

    private int expressionsResolved;
    private int expressionsUnresolved;
    private int loopIterations;
    private int conditionsTaken;
    private int conditionsSkipped;

    public List<String> getLog() {
        return log;
    }

    public int getExpressionsResolved() {
        return expressionsResolved;
    }

    public int getExpressionsUnresolved() {
        return expressionsUnresolved;
    }

    public int getLoopIterations() {
        return loopIterations;
    }

    public int getConditionsTaken() {
        return conditionsTaken;
    }

    public int getConditionsSkipped() {
        return conditionsSkipped;
    }

    // ------------------------------------------------------------------
    // entry point
    // ------------------------------------------------------------------

    /**
     * Render one template.
     *
     * @param templateName the template's file name, for logging
     * @param root         the template's Jinja AST
     * @param context      the values render_template would have supplied
     * @param layouts      parsed ASTs of candidate parent templates, keyed by file
     *                     name, so {@code {% extends %}} can be resolved
     */
    public String emit(String templateName, ASTNode root, TemplateContext context,
                       Map<String, ASTNode> layouts) {
        out.setLength(0);
        if (root == null) {
            log.add(templateName + ": no AST to emit.");
            return "";
        }
        if (context == null) {
            context = new TemplateContext();
            log.add(templateName + ": no render_template context; "
                    + "template variables will render as empty.");
        }

        ASTNode tree = resolveInheritance(templateName, root, layouts);

        out.append("<!DOCTYPE html>\n");
        emitChildren(tree, context, 0, true);
        return out.toString();
    }

    /**
     * If the template extends a layout, splice the child's blocks into the
     * parent's tree and render that instead.
     */
    private ASTNode resolveInheritance(String templateName, ASTNode root,
                                       Map<String, ASTNode> layouts) {
        JinjaExtendsNode extendsNode = null;
        for (ASTNode child : root.getChildren()) {
            if (child instanceof JinjaExtendsNode) {
                extendsNode = (JinjaExtendsNode) child;
                break;
            }
        }
        if (extendsNode == null) {
            return root;
        }

        String parentName = ContextBuilder.stripQuotes(extendsNode.getTemplateName());
        ASTNode parent = layouts == null ? null : layouts.get(parentName);
        if (parent == null) {
            log.add(templateName + ": extends '" + parentName
                    + "' but that layout was not parsed; rendering child alone.");
            return root;
        }

        childBlocks = new HashMap<>();
        collectBlocks(root, childBlocks);
        log.add(templateName + ": extends '" + parentName
                + "', overriding blocks " + childBlocks.keySet());
        return parent;
    }

    private void collectBlocks(ASTNode node, Map<String, JinjaBlockNode> into) {
        if (node instanceof JinjaBlockNode) {
            JinjaBlockNode block = (JinjaBlockNode) node;
            into.put(block.getBlockName(), block);
        }
        for (ASTNode child : node.getChildren()) {
            collectBlocks(child, into);
        }
    }

    // ------------------------------------------------------------------
    // the emitting walk
    // ------------------------------------------------------------------

    private void emitChildren(ASTNode node, TemplateContext ctx, int depth, boolean block) {
        // Adjacent Text nodes are separate tokens (the lexer split on skipped
        // whitespace), so gather runs of them and re-join with single spaces.
        List<ASTNode> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            ASTNode child = children.get(i);

            if (isTextLike(child)) {
                int j = i;
                StringBuilder run = new StringBuilder();
                while (j < children.size() && isTextLike(children.get(j))) {
                    String piece = renderTextLike(children.get(j), ctx);
                    if (!piece.isEmpty()) {
                        if (run.length() > 0 && needsSpaceBefore(piece)) {
                            run.append(' ');
                        }
                        run.append(piece);
                    }
                    j++;
                }
                if (run.length() > 0) {
                    if (block) {
                        indent(depth);
                        out.append(run).append('\n');
                    } else {
                        out.append(run);
                    }
                }
                i = j - 1;
                continue;
            }

            emitNode(child, ctx, depth, block);
        }
    }

    /**
     * Whether a space belongs before this fragment when re-joining a text run.
     *
     * The lexer skips whitespace, so the original spacing is unrecoverable and
     * fragments are re-joined with single spaces. Closing punctuation is the one
     * case where that reads wrong ("delete Ali ?"), so it attaches directly.
     */
    private static boolean needsSpaceBefore(String piece) {
        char first = piece.charAt(0);
        return ".,;:!?)]}".indexOf(first) < 0;
    }

    /** Text and Jinja expressions both flow into the same inline run. */
    private boolean isTextLike(ASTNode node) {
        return node instanceof TextNode || node instanceof JinjaExpressionNode;
    }

    private String renderTextLike(ASTNode node, TemplateContext ctx) {
        if (node instanceof TextNode) {
            return ((TextNode) node).getText();
        }
        JinjaExpressionNode expr = (JinjaExpressionNode) node;
        return resolveExpression(expr.getExpressionText(), ctx);
    }

    private void emitNode(ASTNode node, TemplateContext ctx, int depth, boolean block) {
        if (node instanceof JinjaExtendsNode) {
            return; // handled by resolveInheritance
        }

        if (node instanceof HtmlElementNode) {
            emitElement((HtmlElementNode) node, ctx, depth);

        } else if (node instanceof JinjaForNode) {
            emitFor((JinjaForNode) node, ctx, depth);

        } else if (node instanceof JinjaIfNode) {
            emitIf((JinjaIfNode) node, ctx, depth);

        } else if (node instanceof JinjaBlockNode) {
            emitBlock((JinjaBlockNode) node, ctx, depth);

        } else if (node instanceof JinjaBoundDataNode) {
            // A marker left by Generator's tree-tagging pass; it is metadata about
            // the binding, not template content, so it must not reach the HTML.
            return;

        } else if (node instanceof DocumentNode) {
            emitChildren(node, ctx, depth, block);

        } else if (isTextLike(node)) {
            String text = renderTextLike(node, ctx);
            if (!text.isEmpty()) {
                if (block) {
                    indent(depth);
                    out.append(text).append('\n');
                } else {
                    out.append(text);
                }
            }
        }
        // CSS nodes only appear when compiling a .css file, never in a template.
    }

    private void emitElement(HtmlElementNode element, TemplateContext ctx, int depth) {
        String tag = element.getTagName();
        boolean isVoid = VOID_TAGS.contains(tag.toLowerCase());
        boolean inline = INLINE_TAGS.contains(tag.toLowerCase());

        indent(depth);
        out.append('<').append(tag);
        for (ASTNode child : element.getChildren()) {
            if (child instanceof HtmlAttributeNode) {
                emitAttribute((HtmlAttributeNode) child, ctx);
            }
        }

        if (element.isSelfClosing() || isVoid) {
            out.append(isVoid ? ">" : " />").append('\n');
            return;
        }

        out.append('>');

        // Does this element have any non-attribute content at all?
        boolean hasContent = false;
        for (ASTNode child : element.getChildren()) {
            if (!(child instanceof HtmlAttributeNode)) {
                hasContent = true;
                break;
            }
        }

        if (!hasContent) {
            out.append("</").append(tag).append(">\n");
            return;
        }

        if (inline && isSimpleContent(element)) {
            // e.g. <h1>Our Team</h1> — keep it on one line.
            emitContentOnly(element, ctx, depth, false);
            out.append("</").append(tag).append(">\n");
        } else {
            out.append('\n');
            emitContentOnly(element, ctx, depth + 1, true);
            indent(depth);
            out.append("</").append(tag).append(">\n");
        }
    }

    /** Emit an element's children, skipping its attributes. */
    private void emitContentOnly(HtmlElementNode element, TemplateContext ctx,
                                 int depth, boolean block) {
        DocumentNode wrapper = new DocumentNode(element.getLineNumber());
        for (ASTNode child : element.getChildren()) {
            if (!(child instanceof HtmlAttributeNode)) {
                wrapper.addChild(child);
            }
        }
        emitChildren(wrapper, ctx, depth, block);
    }

    /** True when the element contains only text/expressions — no nested tags. */
    private boolean isSimpleContent(HtmlElementNode element) {
        for (ASTNode child : element.getChildren()) {
            if (child instanceof HtmlAttributeNode || isTextLike(child)) {
                continue;
            }
            if (child instanceof JinjaBoundDataNode) {
                continue;
            }
            return false;
        }
        return true;
    }

    private void emitAttribute(HtmlAttributeNode attr, TemplateContext ctx) {
        out.append(' ').append(attr.getAttributeName());
        String value = attr.getAttributeValue();
        if (value == null) {
            return;
        }
        // The grammar delivers the whole quoted value as one STRING token, so any
        // {{ ... }} inside it is still unparsed text and must be substituted here.
        String interpolated = interpolate(ContextBuilder.stripQuotes(value), ctx);
        out.append("=\"").append(escapeAttribute(interpolated)).append('"');
    }

    private void emitFor(JinjaForNode forNode, TemplateContext ctx, int depth) {
        String collectionPath = forNode.getCollectionExpression();
        TemplateContext.Value collection = ctx.resolve(collectionPath);

        if (collection == null || !collection.isList()) {
            log.add("  loop over '" + collectionPath
                    + "' has no list value in context; body skipped.");
            return;
        }

        String iterVar = forNode.getIteratorVariable();
        List<TemplateContext.Value> items = collection.getList();

        for (int index = 0; index < items.size(); index++) {
            TemplateContext iterationCtx = ctx.copy();
            iterationCtx.put(iterVar, items.get(index));

            // Expose Jinja's loop.index / loop.index0 helpers.
            Map<String, TemplateContext.Value> loopFields = new LinkedHashMap<>();
            loopFields.put("index", TemplateContext.Value.of(String.valueOf(index + 1)));
            loopFields.put("index0", TemplateContext.Value.of(String.valueOf(index)));
            loopFields.put("first", TemplateContext.Value.of(index == 0 ? "True" : ""));
            loopFields.put("last", TemplateContext.Value.of(
                    index == items.size() - 1 ? "True" : ""));
            loopFields.put("length", TemplateContext.Value.of(String.valueOf(items.size())));
            iterationCtx.put("loop", TemplateContext.Value.ofDict(loopFields));

            loopIterations++;
            emitLoopBody(forNode, iterationCtx, depth);
        }
    }

    /** Emit a for-loop's body, skipping any binding markers Generator injected. */
    private void emitLoopBody(JinjaForNode forNode, TemplateContext ctx, int depth) {
        DocumentNode body = new DocumentNode(forNode.getLineNumber());
        for (ASTNode child : forNode.getChildren()) {
            if (child instanceof JinjaBoundDataNode) {
                continue;
            }
            body.addChild(child);
        }
        emitChildren(body, ctx, depth, true);
    }

    private void emitIf(JinjaIfNode ifNode, TemplateContext ctx, int depth) {
        if (evaluateCondition(ifNode.getCondition(), ctx)) {
            conditionsTaken++;
            emitChildren(ifNode, ctx, depth, true);
        } else {
            conditionsSkipped++;
            log.add("  condition '" + ifNode.getCondition()
                    + "' was false; that branch was not emitted.");
        }
    }

    private void emitBlock(JinjaBlockNode blockNode, TemplateContext ctx, int depth) {
        // When rendering a parent layout, a child's block of the same name wins.
        JinjaBlockNode override = childBlocks.get(blockNode.getBlockName());
        emitChildren(override != null ? override : blockNode, ctx, depth, true);
    }

    // ------------------------------------------------------------------
    // expressions and conditions
    // ------------------------------------------------------------------

    /**
     * Resolve a {@code {{ ... }}} expression to its display text.
     * Unknown names render as empty, matching Jinja's default undefined.
     */
    private String resolveExpression(String rawExpression, TemplateContext ctx) {
        String expr = rawExpression == null ? "" : rawExpression.trim();

        // A quoted literal inside {{ }} needs no lookup.
        if (expr.length() >= 2
                && (expr.charAt(0) == '"' || expr.charAt(0) == '\'')) {
            expressionsResolved++;
            return escapeHtml(ContextBuilder.stripQuotes(expr));
        }

        TemplateContext.Value value = ctx.resolve(expr);
        if (value == null) {
            expressionsUnresolved++;
            log.add("  {{ " + expr + " }} is not in the context; rendered as empty.");
            return "";
        }
        expressionsResolved++;
        return escapeHtml(value.render());
    }

    /** Substitute every {{ ... }} occurrence inside a raw string. */
    private String interpolate(String raw, TemplateContext ctx) {
        if (raw == null || raw.indexOf("{{") < 0) {
            return raw == null ? "" : raw;
        }
        StringBuilder result = new StringBuilder();
        int cursor = 0;
        while (true) {
            int start = raw.indexOf("{{", cursor);
            if (start < 0) {
                result.append(raw, cursor, raw.length());
                break;
            }
            int end = raw.indexOf("}}", start + 2);
            if (end < 0) {
                result.append(raw, cursor, raw.length());
                break;
            }
            result.append(raw, cursor, start);
            String expr = raw.substring(start + 2, end).trim();
            TemplateContext.Value value = ctx.resolve(expr);
            if (value == null) {
                expressionsUnresolved++;
                log.add("  {{ " + expr + " }} (in an attribute) is not in the context.");
            } else {
                expressionsResolved++;
                result.append(value.render());
            }
            cursor = end + 2;
        }
        return result.toString();
    }

    /**
     * Evaluate a {@code {% if %}} condition.
     *
     * The grammar strips whitespace, so a condition arrives as a single run such
     * as {@code product.name} or {@code product.id==1}. Comparison operators are
     * handled explicitly; anything else falls back to truthiness.
     */
    private boolean evaluateCondition(String condition, TemplateContext ctx) {
        if (condition == null || condition.trim().isEmpty()) {
            return false;
        }
        String expr = condition.trim();

        String[] operators = {"==", "!=", ">=", "<=", ">", "<"};
        for (String op : operators) {
            int at = expr.indexOf(op);
            if (at <= 0) {
                continue;
            }
            String leftText = expr.substring(0, at).trim();
            String rightText = expr.substring(at + op.length()).trim();
            String left = operandValue(leftText, ctx);
            String right = operandValue(rightText, ctx);

            Integer a = tryParse(left);
            Integer b = tryParse(right);
            if (a != null && b != null) {
                switch (op) {
                    case "==": return a.intValue() == b.intValue();
                    case "!=": return a.intValue() != b.intValue();
                    case ">":  return a > b;
                    case "<":  return a < b;
                    case ">=": return a >= b;
                    case "<=": return a <= b;
                }
            }
            switch (op) {
                case "==": return left.equals(right);
                case "!=": return !left.equals(right);
                default:   return false;
            }
        }

        TemplateContext.Value value = ctx.resolve(expr);
        return value != null && value.isTruthy();
    }

    /** A condition operand is either a literal or a context path. */
    private String operandValue(String token, TemplateContext ctx) {
        if (token.isEmpty()) {
            return "";
        }
        char first = token.charAt(0);
        if (first == '"' || first == '\'') {
            return ContextBuilder.stripQuotes(token);
        }
        if (Character.isDigit(first)) {
            return token;
        }
        TemplateContext.Value value = ctx.resolve(token);
        return value == null ? "" : value.render();
    }

    private static Integer tryParse(String s) {
        try {
            return Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ------------------------------------------------------------------
    // output helpers
    // ------------------------------------------------------------------

    private void indent(int depth) {
        for (int i = 0; i < depth; i++) {
            out.append("    ");
        }
    }

    private static String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }

    private static String escapeAttribute(String text) {
        return escapeHtml(text).replace("\"", "&quot;");
    }
}
