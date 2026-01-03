package symboltable;

import java.util.*;


public class SymbolTable {
    private final Map<String, List<Symbol>> table;
    private final Stack<String> scopeStack;
    private int scopeCounter;

    public SymbolTable() {
        this.table = new HashMap<>();
        this.scopeStack = new Stack<>();
        this.scopeCounter = 0;
        enterScope("global");
    }


    public void enterScope(String scopeName) {
        if (scopeName == null || scopeName.isEmpty()) {
            scopeName = "scope_" + (scopeCounter++);
        }
        scopeStack.push(scopeName);
    }


    public void exitScope() {
        if (!scopeStack.isEmpty() && !scopeStack.peek().equals("global")) {
            scopeStack.pop();
        }
    }


    public String getCurrentScope() {
        return scopeStack.isEmpty() ? "global" : scopeStack.peek();
    }


    public void addSymbol(Symbol symbol) {
        String name = symbol.getName();

        if (!table.containsKey(name)) {
            table.put(name, new ArrayList<>());
        }

        table.get(name).add(symbol);
    }


    public void addSymbol(String name, String type, String dataType, int lineNumber) {
        Symbol symbol = new Symbol(name, type, dataType, lineNumber, getCurrentScope());
        addSymbol(symbol);
    }




    public List<Symbol> getAllSymbols() {
        List<Symbol> allSymbols = new ArrayList<>();
        for (List<Symbol> symbolList : table.values()) {
            allSymbols.addAll(symbolList);
        }
        return allSymbols;
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



}
