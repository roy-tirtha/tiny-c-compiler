# Mini Compiler for Tiny C Language 🧩

This is a dependency-free Java mini compiler for an intentionally small C-like language. It demonstrates lexical analysis, recursive-descent syntax analysis, AST construction, semantic checking, and three-address-code generation. It does **not** generate native machine code or claim to support ISO C.

## Features

- `int` and `float` declarations, initialization, assignment
- Arithmetic, comparisons, logical operators, unary minus and parentheses
- `print(expr)` and `read(variable)` built-ins
- `if`/`else`, `while`, brace-delimited blocks, and `//` comments
- Deterministic temporaries, labels, and useful categorized diagnostics

## Build and run

Requires a JDK (Java 17+; Java 25 is supported). From the project root:

```bash
rm -rf out
mkdir -p out
javac -d out src/tinyc/*.java tests/TestRunner.java
java -cp out TestRunner
java -cp out tinyc.Main examples/basic.tc
```

The invalid example should fail:

```bash
java -cp out tinyc.Main examples/invalid.tc
```

## Example output

For `int result = 2 + 3 * 4;`, the TAC includes:

```text
t1 = 3 * 4
t2 = 2 + t1
result = t2
```

## Pipeline and files

`Lexer` → tokens → `Parser` → AST → `SemanticAnalyzer`/`SymbolTable` → `CodeGenerator` → TAC. See [architecture](docs/architecture.md), [grammar](docs/grammar.md), and [viva questions](docs/viva-questions.md).

```text
src/tinyc/       Java compiler implementation
examples/        valid and invalid Tiny C programs
tests/           standard-library test harness
docs/            architecture, grammar, viva preparation
```

## Limitations and future work

There are no functions, arrays, pointers, structs, strings, preprocessing directives, block comments, nested scopes, or native-code backend. A natural extension is nested symbol-table scopes followed by functions and a target machine or virtual-machine backend.

## Viva study order

Start with `Token.java` and `Lexer.java`, then study the precedence methods in `Parser.java`, the AST records in `AstNode.java`, semantic rules in `SemanticAnalyzer.java`, and finally TAC traversal in `CodeGenerator.java`. Run each example while tracing the corresponding phase.
