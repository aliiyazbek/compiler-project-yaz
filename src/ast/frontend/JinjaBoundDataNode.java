package ast.frontend;

import ast.base.ASTNode;

/**
 * A node injected into the Jinja AST by the {@link generator.Generator}. It does
 * NOT come from parsing a template; it represents one concrete value that the
 * generator carried over from the Python data array (the {@code products = [...]}
 * list in the backend) and bound into the template tree.
 *
 * Two flavours are produced:
 * <ul>
 *   <li>An <b>iteration</b> binding: for a {@code {% for product in products %}}
 *       loop the generator attaches one of these per Python list element, so the
 *       template tree shows the real number of iterations and their source data.</li>
 *   <li>A <b>resolved expression</b> binding: for a {@code {{ product.name }}}
 *       expression the generator attaches the concrete value ("Ali Yazbek") it
 *       looked up in the bound element.</li>
 * </ul>
 */
public class JinjaBoundDataNode extends ASTNode {

    private final String binding;     // e.g. "product[0]" or "product.name"
    private final String resolvedValue; // the concrete value from the Python data

    public JinjaBoundDataNode(int lineNumber, String binding, String resolvedValue) {
        super("JinjaBoundData", lineNumber);
        this.binding = binding;
        this.resolvedValue = resolvedValue;
    }

    public String getBinding() {
        return binding;
    }

    public String getResolvedValue() {
        return resolvedValue;
    }

    @Override
    public String getNodeInfo() {
        return String.format("[Line %d] %s: %s = %s",
                lineNumber, nodeName, binding, resolvedValue);
    }
}
