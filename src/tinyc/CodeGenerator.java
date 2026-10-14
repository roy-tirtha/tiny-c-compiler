package tinyc;

import tinyc.AstNode.*;
import java.util.*;

public final class CodeGenerator {
    private final List<String> lines = new ArrayList<>(); private int temp, label;
    public List<String> generate(Program p) { for (Stmt s : p.statements()) stmt(s); return List.copyOf(lines); }
    private void stmt(Stmt s) {
        if (s instanceof Declaration d) { if (d.initializer() != null) lines.add(d.name() + " = " + expr(d.initializer())); }
        else if (s instanceof Assignment a) lines.add(a.name() + " = " + expr(a.value()));
        else if (s instanceof Print p) lines.add("PRINT " + expr(p.expression()));
        else if (s instanceof Read r) lines.add("READ " + r.name());
        else if (s instanceof Block b) b.statements().forEach(this::stmt);
        else if (s instanceof If i) { String end = label(); String otherwise = i.elseBranch() == null ? end : label(); lines.add("IF_FALSE " + expr(i.condition()) + " GOTO " + otherwise); stmt(i.thenBranch()); if (i.elseBranch() != null) { lines.add("GOTO " + end); lines.add(otherwise + ":"); stmt(i.elseBranch()); } lines.add(end + ":"); }
        else if (s instanceof While w) { String start = label(), end = label(); lines.add(start + ":"); lines.add("IF_FALSE " + expr(w.condition()) + " GOTO " + end); stmt(w.body()); lines.add("GOTO " + start); lines.add(end + ":"); }
    }
    private String expr(Expr e) {
        if (e instanceof Literal l) return l.value(); if (e instanceof Variable v) return v.name();
        if (e instanceof Unary u) { String x = expr(u.expression()), t = temp(); lines.add(t + " = " + u.operator() + x); return t; }
        Binary b = (Binary)e; String l = expr(b.left()), r = expr(b.right()), t = temp(); lines.add(t + " = " + l + " " + b.operator() + " " + r); return t;
    }
    private String temp() { return "t" + (++temp); } private String label() { return "L" + (++label); }
}
