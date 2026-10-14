# Tiny C Grammar

The parser implements this EBNF (whitespace and `//` comments may appear between tokens):

```text
program       = { statement } ;
statement     = declaration | assignment | print | read | if | while | block ;
declaration   = ("int" | "float") identifier [ "=" expression ] ";" ;
assignment    = identifier "=" expression ";" ;
print         = "print" "(" expression ")" ";" ;
read          = "read" "(" identifier ")" ";" ;
if            = "if" "(" expression ")" statement [ "else" statement ] ;
while         = "while" "(" expression ")" statement ;
block         = "{" { statement } "}" ;
expression    = logical_or ;
logical_or    = logical_and { "||" logical_and } ;
logical_and   = equality { "&&" equality } ;
equality      = comparison { ("==" | "!=") comparison } ;
comparison    = term { ("<" | "<=" | ">" | ">=") term } ;
term          = factor { ("+" | "-") factor } ;
factor        = unary { ("*" | "/") unary } ;
unary         = ("!" | "-") unary | primary ;
primary       = integer | real | identifier | "(" expression ")" ;
```

Logical and comparison expressions have type `bool` internally and are represented by the TAC conditions directly. Variables are only `int` or `float`; an `int` may not receive a `float` or boolean value.
