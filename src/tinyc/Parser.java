package tinyc;

import java.util.*;
import static tinyc.Token.Type.*;
import tinyc.AstNode.*;

public final class Parser {
    private final List<Token> tokens; private int current;
    public Parser(List<Token> tokens) { this.tokens = tokens; }
    public Program parse() { List<Stmt> s = new ArrayList<>(); while (!check(EOF)) s.add(statement()); return new Program(s); }
    private Stmt statement() {
        if (match(INT, FLOAT)) return declaration(previous());
        if (match(IF)) return ifStatement(); if (match(WHILE)) return whileStatement();
        if (match(PRINT)) return callPrint(); if (match(READ)) return callRead();
        if (match(LEFT_BRACE)) return block();
        if (check(IDENTIFIER) && checkNext(ASSIGN)) { String n = advance().lexeme(); consume(ASSIGN, "expected '='"); Expr e = expression(); consume(SEMICOLON, "expected ';'"); return new Assignment(n, e); }
        throw error(peek(), "expected a declaration, assignment, print, read, if, while, or block");
    }
    private Stmt declaration(Token kind) { Token name = consume(IDENTIFIER, "expected variable name"); Expr init = null; if (match(ASSIGN)) init = expression(); consume(SEMICOLON, "expected ';' after declaration"); return new Declaration(kind.type() == INT ? Type.INT : Type.FLOAT, name.lexeme(), init); }
    private Stmt callPrint() { consume(LEFT_PAREN, "expected '(' after print"); Expr e = expression(); consume(RIGHT_PAREN, "expected ')' after print expression"); consume(SEMICOLON, "expected ';' after print"); return new Print(e); }
    private Stmt callRead() { consume(LEFT_PAREN, "expected '(' after read"); Token n = consume(IDENTIFIER, "read requires a variable name"); consume(RIGHT_PAREN, "expected ')' after read variable"); consume(SEMICOLON, "expected ';' after read"); return new Read(n.lexeme()); }
    private Stmt ifStatement() { consume(LEFT_PAREN, "expected '(' after if"); Expr c = expression(); consume(RIGHT_PAREN, "expected ')' after condition"); Stmt t = statement(); Stmt e = match(ELSE) ? statement() : null; return new If(c, t, e); }
    private Stmt whileStatement() { consume(LEFT_PAREN, "expected '(' after while"); Expr c = expression(); consume(RIGHT_PAREN, "expected ')' after condition"); return new While(c, statement()); }
    private Block block() { List<Stmt> s = new ArrayList<>(); while (!check(RIGHT_BRACE) && !check(EOF)) s.add(statement()); consume(RIGHT_BRACE, "expected '}' after block"); return new Block(s); }
    private Expr expression() { return or(); }
    private Expr or() { Expr e = and(); while (match(OR_OR)) e = new Binary(e, "||", and()); return e; }
    private Expr and() { Expr e = equality(); while (match(AND_AND)) e = new Binary(e, "&&", equality()); return e; }
    private Expr equality() { Expr e = comparison(); while (match(EQUAL_EQUAL, NOT_EQUAL)) e = new Binary(e, previous().lexeme(), comparison()); return e; }
    private Expr comparison() { Expr e = term(); while (match(LESS, LESS_EQUAL, GREATER, GREATER_EQUAL)) e = new Binary(e, previous().lexeme(), term()); return e; }
    private Expr term() { Expr e = factor(); while (match(PLUS, MINUS)) e = new Binary(e, previous().lexeme(), factor()); return e; }
    private Expr factor() { Expr e = unary(); while (match(STAR, SLASH)) e = new Binary(e, previous().lexeme(), unary()); return e; }
    private Expr unary() { if (match(BANG, MINUS)) return new Unary(previous().lexeme(), unary()); return primary(); }
    private Expr primary() {
        if (match(INTEGER)) return new Literal(previous().lexeme(), Type.INT); if (match(REAL)) return new Literal(previous().lexeme(), Type.FLOAT);
        if (match(IDENTIFIER)) return new Variable(previous().lexeme()); if (match(LEFT_PAREN)) { Expr e = expression(); consume(RIGHT_PAREN, "expected ')' after expression"); return e; }
        throw error(peek(), "expected expression");
    }
    private Token consume(Token.Type t, String message) { if (check(t)) return advance(); throw error(peek(), message); }
    private boolean match(Token.Type... types) { for (Token.Type t : types) if (check(t)) { advance(); return true; } return false; }
    private boolean check(Token.Type t) { return peek().type() == t; } private boolean checkNext(Token.Type t) { return current + 1 < tokens.size() && tokens.get(current + 1).type() == t; }
    private Token advance() { if (!check(EOF)) current++; return previous(); } private Token previous() { return tokens.get(current - 1); } private Token peek() { return tokens.get(current); }
    private CompilerError error(Token t, String m) { return new CompilerError(CompilerError.Category.SYNTAX, m, t.line(), t.column()); }
}
