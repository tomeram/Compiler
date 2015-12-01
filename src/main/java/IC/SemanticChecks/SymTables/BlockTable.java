/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks.SymTables;

import IC.AST.ASTNode;
import IC.AST.LocalVariable;
import IC.AST.Type;
import IC.Compiler;
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTables.SymTable;
import IC.SemanticChecks.SymTables.Symbols.FieldSym;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BlockTable
extends SymTable {
    private Map<String, FieldSym> locals = new LinkedHashMap<String, FieldSym>();

    public BlockTable(String id, SymTable parent) {
        super(id, parent);
    }

    @Override
    public String getID() {
        return "statement block in " + this.getParentString();
    }

    @Override
    public void addSym(Object obj) {
        LocalVariable var;
        if (!LocalVariable.class.equals(obj.getClass())) {
            ASTNode node = (ASTNode)obj;
            Compiler.errorExit("Wrong type inside satement block.", node.getLine());
        }
        if (this.locals.containsKey((var = (LocalVariable)obj).getName())) {
            Compiler.errorExit("Duplicate variable definition.", var.getLine());
        }
        this.locals.put(var.getName(), new FieldSym(var, var.getName(), var.getType()));
    }

    @Override
    public boolean containsSym(String id, Kind kind) {
        if (Kind.FIELD.equals((Object)kind) && this.locals.containsKey(id)) {
            return true;
        }
        return false;
    }

    @Override
    public SymEntry getSym(String id, Kind kind) {
        if (!Kind.FIELD.equals((Object)kind)) {
            return null;
        }
        return this.locals.get(id);
    }

    @Override
    public void print_table() {
        System.out.println("\nStatement Block Symbol Table ( located in " + this.getParentString() + " )");
        for (Map.Entry<String, FieldSym> e : this.locals.entrySet()) {
            System.out.println("    Local variable: " + e.getValue().getFieldType() + " " + e.getKey());
        }
        this.printChildrenTables();
        this.printChildrenScopes();
    }

    private String getParentString() {
        StringBuffer res = new StringBuffer();
        if (this.getParent() != null && this.getParent().getID().matches("\\d*")) {
            BlockTable parent = (BlockTable)this.getParent();
            res.append("statement block in " + parent.getParentString());
        } else if (this.getParent() != null) {
            res.append(this.getParent().getID());
        }
        return res.toString();
    }
}

