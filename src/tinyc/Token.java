package tinyc;

public record Token(Type type, String lexeme, int line, int column) {
    public enum Type {
        INT, FLOAT, IF, ELSE, WHILE, READ, PRINT,
        IDENTIFIER, INTEGER, REAL,
        PLUS, MINUS, STAR, SLASH, BANG,
        LESS, GREATER, LESS_EQUAL, GREATER_EQUAL, EQUAL_EQUAL,
        NOT_EQUAL, AND_AND, OR_OR, ASSIGN,
        LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, SEMICOLON, COMMA,
        EOF
    }

    @Override public String toString() { return type + "('" + lexeme + "') @" + line + ":" + column; }
}
