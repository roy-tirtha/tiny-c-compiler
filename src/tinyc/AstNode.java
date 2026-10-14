package tinyc;

import java.util.*;

public final class AstNode {
    private AstNode() {}
    public enum Type { INT, FLOAT, BOOL }
    public record Program(List<Stmt> statements) implements Ast {}
    public sealed interface Ast permits Program, Stmt, Expr {}
    public sealed interface Stmt extends Ast permits Declaration, Assignment, Print, Read, Block, If, While {}
    public sealed interface Expr extends Ast permits Literal, Variable, Unary, Binary {}
    public record Declaration(Type type, String name, Expr initializer) implements Stmt {}
    public record Assignment(String name, Expr value) implements Stmt {}
    public record Print(Expr expression) implements Stmt {}
    public record Read(String name) implements Stmt {}
    public record Block(List<Stmt> statements) implements Stmt {}
    public record If(Expr condition, Stmt thenBranch, Stmt elseBranch) implements Stmt {}
    public record While(Expr condition, Stmt body) implements Stmt {}
    public record Literal(String value, Type type) implements Expr {}
    public record Variable(String name) implements Expr {}
    public record Unary(String operator, Expr expression) implements Expr {}
    public record Binary(Expr left, String operator, Expr right) implements Expr {}
}
