package tinyc;
import java.util.*;
import tinyc.AstNode.Type;
public final class SymbolTable {
    private final Map<String, Type> symbols = new LinkedHashMap<>();
    public void declare(String name, Type type, int line, int column) { if (symbols.containsKey(name)) throw new CompilerError(CompilerError.Category.SEMANTIC, "duplicate declaration of '" + name + "'", line, column); symbols.put(name, type); }
    public Type lookup(String name, int line, int column) { Type t = symbols.get(name); if (t == null) throw new CompilerError(CompilerError.Category.SEMANTIC, "undeclared variable '" + name + "'", line, column); return t; }
    public boolean contains(String name) { return symbols.containsKey(name); }
}
