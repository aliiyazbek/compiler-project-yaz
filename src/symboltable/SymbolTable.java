package symboltable;

import java.util.ArrayList;
import java.util.List;

/**
 * Symbol table backed by a real tree of nested {@link Scope}s.
 *
 * The visitor drives this with enterScope()/exitScope() as it walks function
 * definitions, so a variable declared inside one function is genuinely separate
 * from a same-named variable in another function (or in global scope) -- not
 * merely tagged with a scope string. Name resolution (resolve) walks outward
 * from the current scope through its ancestors, the way lexical scoping works.
 */
public class SymbolTable {

    private final Scope globalScope;
    private Scope currentScope;
    private int scopeCounter;

    public SymbolTable() {
        this.globalScope = new Scope("global", null);
        this.currentScope = globalScope;
        this.scopeCounter = 0;
    }

    /** Open a new child scope under the current one and descend into it. */
    public void enterScope(String scopeName) {
        if (scopeName == null || scopeName.isEmpty()) {
            scopeName = "scope_" + (scopeCounter++);
        }
        currentScope = new Scope(scopeName, currentScope);
    }

    /** Return to the parent of the current scope (never pops past global). */
    public void exitScope() {
        if (currentScope.getParent() != null) {
            currentScope = currentScope.getParent();
        }
    }

    public String getCurrentScope() {
        return currentScope.getName();
    }

    public Scope getCurrentScopeObject() {
        return currentScope;
    }

    public Scope getGlobalScope() {
        return globalScope;
    }

    /** Declare a pre-built symbol in the current scope. */
    public Symbol addSymbol(Symbol symbol) {
        currentScope.declare(symbol);
        return symbol;
    }

    /**
     * Declare a symbol in the current scope and return it, so callers (e.g. the
     * type-inference pass) can refine it afterwards.
     */
    public Symbol addSymbol(String name, String type, String dataType, int lineNumber) {
        Symbol symbol = new Symbol(name, type, dataType, lineNumber, getCurrentScope());
        return addSymbol(symbol);
    }

    /** Resolve a name from the current scope outward through enclosing scopes. */
    public Symbol resolve(String name) {
        return currentScope.resolve(name);
    }

    /** Resolve a name in the current scope only. */
    public Symbol resolveLocal(String name) {
        return currentScope.resolveLocal(name);
    }

    /** Flatten every symbol across all scopes (used for reporting). */
    public List<Symbol> getAllSymbols() {
        List<Symbol> all = new ArrayList<>();
        collect(globalScope, all);
        return all;
    }

    private void collect(Scope scope, List<Symbol> out) {
        out.addAll(scope.getSymbols());
        for (Scope child : scope.getChildren()) {
            collect(child, out);
        }
    }

    public void print() {
        System.out.println("\n=== Symbol Table ===");
        System.out.println(String.format("%-20s %-15s %-15s %-10s %s",
                "Name", "Type", "Data Type", "Line", "Scope"));
        System.out.println("=".repeat(80));

        List<Symbol> allSymbols = getAllSymbols();
        allSymbols.sort((a, b) -> {
            int scopeComp = a.getScope().compareTo(b.getScope());
            if (scopeComp != 0) return scopeComp;
            return Integer.compare(a.getLineNumber(), b.getLineNumber());
        });

        for (Symbol symbol : allSymbols) {
            System.out.println(symbol);
        }

        System.out.println("=".repeat(80));
        System.out.println("Total symbols: " + allSymbols.size());
    }

    /**
     * Pretty-print the scope tree itself (indented), so the nesting is visible
     * rather than just a flat list tagged with scope names.
     */
    public void printScopeTree() {
        System.out.println("\n=== Scope Tree ===");
        printScope(globalScope, 0);
    }

    private void printScope(Scope scope, int indent) {
        String pad = "  ".repeat(indent);
        System.out.println(pad + "Scope: " + scope.getName());
        for (Symbol s : scope.getSymbols()) {
            System.out.println(pad + "  - " + s.getName()
                    + " (" + s.getType() + ", " + s.getDataType() + ")");
        }
        for (Scope child : scope.getChildren()) {
            printScope(child, indent + 1);
        }
    }
}
