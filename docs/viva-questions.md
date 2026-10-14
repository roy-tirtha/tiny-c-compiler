# Viva Questions and Answers

1. **What is a compiler?** A program that translates source code into another form and reports errors.
2. **Compiler versus interpreter?** A compiler translates before execution; an interpreter executes by analyzing during execution.
3. **What are compiler phases?** Lexical analysis, syntax analysis, semantic analysis, intermediate-code generation, and later optimization/code generation.
4. **What is a token?** A categorized piece of source text, such as `INT` or `IDENTIFIER`.
5. **What is a lexeme?** The actual characters matched for a token.
6. **Why are regular languages useful?** Token patterns such as numbers and identifiers can be recognized with regular rules.
7. **What does the lexer do?** It converts characters into tokens and tracks locations.
8. **What is syntax analysis?** Checking token order against a grammar.
9. **What is a context-free grammar?** Productions that describe nested language structures using nonterminals.
10. **What parser is used here?** A handwritten recursive-descent parser.
11. **What is recursive descent?** Parser methods call methods for grammar rules and consume tokens from left to right.
12. **Why remove left recursion?** Direct left recursion causes recursive-descent methods to call themselves forever.
13. **How is precedence implemented?** Each precedence level has its own parser method.
14. **What is associativity?** The grouping direction of equal-precedence operators; these binary levels are left-associative.
15. **AST versus parse tree?** An AST keeps meaningful program structure and omits grammar-only punctuation.
16. **What is semantic analysis?** Checking meaning beyond grammar, including declarations and types.
17. **What is a symbol table?** A mapping from names to information such as their types.
18. **What is a declaration?** Introducing a variable and its type.
19. **What is scope?** The region where a name can be used; this version has one scope.
20. **Why reject undeclared variables?** Their storage and type are unknown.
21. **What is intermediate representation?** A machine-independent representation between source and machine code.
22. **What is three-address code?** Instructions with at most one operation and explicit operands/result.
23. **Why use temporaries?** Complex expressions need named intermediate results.
24. **Why use labels?** Branches and loops need named jump targets.
25. **How does an if work in TAC?** It emits `IF_FALSE` to an else/end label and then emits branch code.
26. **How does a while work?** It emits a start label, condition jump, body, back jump, and end label.
27. **How is boolean data represented?** Boolean expressions have internal type `BOOL`; TAC keeps their condition form.
28. **Why must semantic errors stop generation?** Invalid programs must not produce misleading code.
29. **How are lexical errors reported?** The lexer throws a categorized error with line and column.
30. **Why are comments removed by the lexer?** They do not affect program meaning.
31. **What tests are important?** Valid tokens/programs, precedence, errors, control flow, and deterministic output.
32. **What is not supported?** Functions, arrays, pointers, structs, strings, block comments, and native machine code.
33. **Why not call GCC?** The educational goal is to implement the compiler phases ourselves.
34. **What is a limitation of one scope?** A name cannot be safely redeclared inside a separate nested block.
35. **What future work is natural?** Nested scopes, functions, richer diagnostics, optimizations, and a target-code backend.
