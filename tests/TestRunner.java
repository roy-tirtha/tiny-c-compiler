import tinyc.*;
import java.util.*;

public final class TestRunner {
    private static int passed;
    public static void main(String[] args) {
        check("lexer", () -> new Lexer("int x = 1.5; // c\n if (x >= 1 && x != 2) print(x);").scan().size() == 21);
        check("precedence", () -> new CodeGenerator().generate(new Parser(new Lexer("int x = 2 + 3 * 4;").scan()).parse()).get(0).equals("t1 = 3 * 4"));
        check("control flow", () -> new CodeGenerator().generate(new Parser(new Lexer("int i=0; while(i<2){print(i); i=i+1;}").scan()).parse()).stream().anyMatch(s -> s.startsWith("IF_FALSE")));
        check("semantic failure", () -> fails("print(x);"));
        check("syntax failure", () -> fails("int x = ;"));
        check("malformed number", () -> lexFails("float x = 1.;"));
        check("invalid character", () -> lexFails("int x = 1 @ 2;"));
        check("read statement", () -> new Parser(new Lexer("int x; read(x);").scan()).parse().statements().size() == 2);
        System.out.println("Passed " + passed + " tests.");
    }
    private static boolean fails(String s) { try { var p = new Parser(new Lexer(s).scan()).parse(); new SemanticAnalyzer().analyze(p); return false; } catch (CompilerError e) { return true; } }
    private static boolean lexFails(String s) { try { new Lexer(s).scan(); return false; } catch (CompilerError e) { return e.category() == CompilerError.Category.LEXICAL; } }
    private static void check(String name, java.util.function.BooleanSupplier test) { if (!test.getAsBoolean()) throw new AssertionError(name); passed++; System.out.println("PASS: " + name); }
}
