package symboltable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A single lexical scope in the symbol table.
 *
 * Scopes form a tree: every scope (except "global") has a parent, and name
 * resolution walks from the current scope up through its ancestors. This is
 * what makes the symbol table actually scoped, instead of a flat name->list map.
 */
public class Scope {

    private final String name;
    private final Scope parent;
    private final List<Scope> children;

    /** Symbols declared directly in this scope, keyed by name (declaration order preserved). */
    private final Map<String, Symbol> symbols;

    public Scope(String name, Scope parent) {
        this.name = name;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.symbols = new LinkedHashMap<>();
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public String getName() {
        return name;
    }

    public Scope getParent() {
        return parent;
    }

    public List<Scope> getChildren() {
        return children;
    }

    /**
     * Declare a symbol in THIS scope.
     *
     * @return the previously declared symbol with the same name in this scope,
     *         or null if this is the first declaration. A non-null return means
     *         a redeclaration (useful later for semantic checks).
     */
    public Symbol declare(Symbol symbol) {
        return symbols.put(symbol.getName(), symbol);
    }

    /** Look up a name in THIS scope only (no parent walk). */
    public Symbol resolveLocal(String name) {
        return symbols.get(name);
    }

    /** Look up a name in this scope, then walk up through parent scopes. */
    public Symbol resolve(String name) {
        for (Scope s = this; s != null; s = s.parent) {
            Symbol found = s.symbols.get(name);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /** Symbols declared directly in this scope, in declaration order. */
    public List<Symbol> getSymbols() {
        return new ArrayList<>(symbols.values());
    }
}
