package tinyc;

import java.util.*;

public final class Lexer {
    private final String source; private final List<Token> tokens = new ArrayList<>();
    private int current, line = 1, column = 1;
    private static final Map<String, Token.Type> KEYWORDS = Map.of(
        "int", Token.Type.INT, "float", Token.Type.FLOAT, "if", Token.Type.IF,
        "else", Token.Type.ELSE, "while", Token.Type.WHILE, "read", Token.Type.READ, "print", Token.Type.PRINT);
    public Lexer(String source) { this.source = source; }
    public List<Token> scan() {
        while (!atEnd()) scanToken();
        tokens.add(new Token(Token.Type.EOF, "", line, column)); return tokens;
    }
    private void scanToken() {
        int startIndex = current; char c = advance();
        if (Character.isWhitespace(c)) { if (c == '\n') { line++; column = 1; } return; }
        int startLine = line, startColumn = column - 1;
        if (c == '/' && match('/')) { while (!atEnd() && peek() != '\n') advance(); return; }
        if (Character.isLetter(c) || c == '_') { identifier(startLine, startColumn); return; }
        if (Character.isDigit(c)) { number(startLine, startColumn); return; }
        Token.Type type = switch (c) {
            case '+' -> Token.Type.PLUS; case '-' -> Token.Type.MINUS; case '*' -> Token.Type.STAR;
            case '/' -> Token.Type.SLASH; case '(' -> Token.Type.LEFT_PAREN; case ')' -> Token.Type.RIGHT_PAREN;
            case '{' -> Token.Type.LEFT_BRACE; case '}' -> Token.Type.RIGHT_BRACE; case ';' -> Token.Type.SEMICOLON;
            case ',' -> Token.Type.COMMA; case '!' -> match('=') ? Token.Type.NOT_EQUAL : Token.Type.BANG;
            case '=' -> match('=') ? Token.Type.EQUAL_EQUAL : Token.Type.ASSIGN;
            case '<' -> match('=') ? Token.Type.LESS_EQUAL : Token.Type.LESS;
            case '>' -> match('=') ? Token.Type.GREATER_EQUAL : Token.Type.GREATER;
            case '&' -> { if (match('&')) yield Token.Type.AND_AND; throw error("expected '&' after '&'", startLine, startColumn); }
            case '|' -> { if (match('|')) yield Token.Type.OR_OR; throw error("expected '|' after '|' ", startLine, startColumn); }
            default -> throw error("unexpected character '" + c + "'", startLine, startColumn);
        }; tokens.add(new Token(type, source.substring(startIndex, current), startLine, startColumn));
    }
    private void identifier(int l, int c) { while (Character.isLetterOrDigit(peek()) || peek() == '_') advance(); String s = source.substring(current - (column - c), current); tokens.add(new Token(KEYWORDS.getOrDefault(s, Token.Type.IDENTIFIER), s, l, c)); }
    private void number(int l, int c) {
        while (Character.isDigit(peek())) advance(); boolean real = false;
        if (peek() == '.') { real = true; advance(); if (!Character.isDigit(peek())) throw error("digits required after decimal point", l, c); while (Character.isDigit(peek())) advance(); }
        if (Character.isLetter(peek()) || peek() == '_') throw error("malformed numeric literal", l, c);
        tokens.add(new Token(real ? Token.Type.REAL : Token.Type.INTEGER, source.substring(current - (column - c), current), l, c));
    }
    private char advance() { char c = source.charAt(current++); column++; return c; }
    private boolean match(char expected) { if (atEnd() || source.charAt(current) != expected) return false; current++; column++; return true; }
    private char peek() { return atEnd() ? '\0' : source.charAt(current); }
    private boolean atEnd() { return current >= source.length(); }
    private CompilerError error(String message, int l, int c) { return new CompilerError(CompilerError.Category.LEXICAL, message, l, c); }
}
