package symboltable;


public class Symbol {
    private final String name;
    private final String type;
    private final String dataType;
    private final int lineNumber;
    private final String scope;

    public Symbol(String name, String type, String dataType, int lineNumber, String scope) {
        this.name = name;
        this.type = type;
        this.dataType = dataType;
        this.lineNumber = lineNumber;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDataType() {
        return dataType;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return String.format("%-20s %-15s %-15s %-10d %s",
                name, type, dataType, lineNumber, scope);
    }
}
