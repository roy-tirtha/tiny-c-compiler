package tinyc;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public final class Main {
    public static void main(String[] args) {
        if (args.length != 1) { System.err.println("Usage: java -cp out tinyc.Main <file.tc>"); System.exit(2); }
        try {
            String source = Files.readString(Path.of(args[0]), StandardCharsets.UTF_8);
            System.out.println("== Lexical Analysis =="); List<Token> tokens = new Lexer(source).scan(); System.out.println("Tokens: " + (tokens.size() - 1));
            System.out.println("== Syntax Analysis =="); AstNode.Program program = new Parser(tokens).parse(); System.out.println("AST: valid");
            System.out.println("== Semantic Analysis =="); new SemanticAnalyzer().analyze(program); System.out.println("Semantics: valid");
            System.out.println("== Three-Address Code =="); for (String line : new CodeGenerator().generate(program)) System.out.println(line);
        } catch (CompilerError e) { System.err.println(e.getMessage()); System.exit(1); }
          catch (NoSuchFileException e) { System.err.println("File not found: " + e.getFile()); System.exit(1); }
          catch (InvalidPathException e) { System.err.println("Invalid file path: " + e.getInput()); System.exit(1); }
          catch (Exception e) { System.err.println("Could not compile file: " + e.getMessage()); System.exit(1); }
    }
}
