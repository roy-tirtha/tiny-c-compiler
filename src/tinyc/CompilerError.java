package tinyc;

public class CompilerError extends RuntimeException {
    public enum Category { LEXICAL, SYNTAX, SEMANTIC }
    private final Category category;
    private final int line;
    private final int column;

    public CompilerError(Category category, String message, int line, int column) {
        super(message); this.category = category; this.line = line; this.column = column;
    }
    public Category category() { return category; }
    public int line() { return line; }
    public int column() { return column; }
    @Override public String getMessage() { return category + " error at " + line + ":" + column + ": " + super.getMessage(); }
}
