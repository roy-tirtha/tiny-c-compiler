package tinyc;
import tinyc.AstNode.*;
import java.util.*;
public final class SemanticAnalyzer {
    private final SymbolTable table = new SymbolTable();
    public void analyze(Program p) { for (Stmt s : p.statements()) stmt(s); }
    private void stmt(Stmt s) {
        if (s instanceof Declaration d) { table.declare(d.name(), d.type(), 0, 0); if (d.initializer() != null) requireAssignable(d.type(), expr(d.initializer()), "initializer"); }
        else if (s instanceof Assignment a) requireAssignable(table.lookup(a.name(), 0, 0), expr(a.value()), "assignment");
        else if (s instanceof Read r) table.lookup(r.name(), 0, 0);
        else if (s instanceof Print p) expr(p.expression());
        else if (s instanceof Block b) b.statements().forEach(this::stmt);
        else if (s instanceof If i) { requireBool(expr(i.condition()), "if condition"); stmt(i.thenBranch()); if (i.elseBranch() != null) stmt(i.elseBranch()); }
        else if (s instanceof While w) { requireBool(expr(w.condition()), "while condition"); stmt(w.body()); }
    }
    private Type expr(Expr e) {
        if (e instanceof Literal l) return l.type(); if (e instanceof Variable v) return table.lookup(v.name(), 0, 0);
        if (e instanceof Unary u) { Type t = expr(u.expression()); if (u.operator().equals("!") && t != Type.BOOL) throw bad("! requires a boolean expression"); if (u.operator().equals("-") && !numeric(t)) throw bad("unary '-' requires a numeric expression"); return u.operator().equals("!") ? Type.BOOL : t; }
        Binary b = (Binary)e; Type l = expr(b.left()), r = expr(b.right());
        if (Set.of("+", "-", "*", "/").contains(b.operator())) { if (!numeric(l) || !numeric(r)) throw bad("arithmetic requires numeric expressions"); return l == Type.FLOAT || r == Type.FLOAT ? Type.FLOAT : Type.INT; }
        if (Set.of("<", "<=", ">", ">=").contains(b.operator())) { if (!numeric(l) || !numeric(r)) throw bad("comparison requires numeric expressions"); return Type.BOOL; }
        if (Set.of("==", "!=").contains(b.operator())) { if (!numeric(l) || !numeric(r)) throw bad("equality requires numeric expressions"); return Type.BOOL; }
        if (Set.of("&&", "||").contains(b.operator())) { requireBool(l, "logical operator"); requireBool(r, "logical operator"); return Type.BOOL; }
        throw bad("unknown operator");
    }
    private boolean numeric(Type t) { return t == Type.INT || t == Type.FLOAT; }
    private void requireBool(Type t, String where) { if (t != Type.BOOL) throw bad(where + " requires a comparison or logical expression"); }
    private void requireAssignable(Type target, Type value, String where) { if (target == Type.INT && value == Type.FLOAT) throw bad("cannot assign float to int in " + where); if (value == Type.BOOL) throw bad("cannot assign boolean to numeric variable"); }
    private CompilerError bad(String m) { return new CompilerError(CompilerError.Category.SEMANTIC, m, 0, 0); }
}
