/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks.SymTables;

import IC.AST.ASTNode;
import IC.AST.ICClass;
import IC.Compiler;
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTables.SymTable;
import IC.SemanticChecks.SymTables.Symbols.ClassSymbol;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ProgramTable
extends SymTable {
    private Map<String, ClassSymbol> classes = new LinkedHashMap<String, ClassSymbol>();

    public ProgramTable(String fileName) {
        super(fileName, null);
    }

    @Override
    public void addSym(Object obj) {
        ICClass ic = (ICClass)obj;
        String id = ic.getName();
        ClassSymbol sym2 = new ClassSymbol(id, ic);
        if (!this.containsSym(id, null)) {
            this.classes.put(id, sym2);
        } else {
            ASTNode node = (ASTNode)obj;
            Compiler.errorExit("Duplicate class definition.", node.getLine());
        }
    }

    @Override
    public boolean containsSym(String id, Kind kind) {
        return this.classes.containsKey(id);
    }

    @Override
    public SymEntry getSym(String id, Kind kind) {
        return this.classes.get(id);
    }

    @Override
    public void print_table() {
        System.out.println("Global Symbol Table: " + this.getID());
        for (Map.Entry<String, ClassSymbol> e : this.classes.entrySet()) {
            System.out.println("    Class: " + e.getKey());
        }
        this.printChildrenTables();
        this.printChildrenScopes();
    }
}

