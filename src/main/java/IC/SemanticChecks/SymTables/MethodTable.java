/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks.SymTables;

import IC.AST.ASTNode;
import IC.AST.Formal;
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

public class MethodTable
extends SymTable {
    private Map<String, FieldSym> parameters = new LinkedHashMap<String, FieldSym>();
    private Map<String, FieldSym> fields = new LinkedHashMap<String, FieldSym>();

    public MethodTable(String id, SymTable parent) {
        super(id, parent);
    }

    @Override
    public void addSym(Object obj) {
        LocalVariable var;
        if (!LocalVariable.class.equals(obj.getClass())) {
            ASTNode node = (ASTNode)obj;
            Compiler.errorExit("Wrong type inside satement block.", node.getLine());
        }
        if (this.fields.containsKey((var = (LocalVariable)obj).getName())) {
            Compiler.errorExit("Duplicate variable definition.", var.getLine());
        }
        this.fields.put(var.getName(), new FieldSym(var, var.getName(), var.getType()));
    }

    public void addLocalVariable(LocalVariable local) {
        if (this.parameters.containsKey(local.getName()) || this.fields.containsKey(local.getName())) {
            Compiler.errorExit("Error: Duplicate field name.", local.getLine());
        }
        this.fields.put(local.getName(), new FieldSym(local, local.getName(), local.getType()));
    }

    public void addParameter(Formal parameter) {
        if (this.parameters.containsKey(parameter.getName()) || this.fields.containsKey(parameter.getName())) {
            Compiler.errorExit("Duplicate field name.", parameter.getLine());
        }
        this.parameters.put(parameter.getName(), new FieldSym(parameter, parameter.getName(), parameter.getType()));
    }

    @Override
    public boolean containsSym(String id, Kind kind) {
        if ((this.parameters.containsKey(id) || this.fields.containsKey(id)) && Kind.FIELD.equals((Object)kind)) {
            return true;
        }
        return false;
    }

    @Override
    public SymEntry getSym(String id, Kind kind) {
        if (!Kind.FIELD.equals((Object)kind)) {
            return null;
        }
        if (this.parameters.containsKey(id)) {
            return this.parameters.get(id);
        }
        return this.fields.get(id);
    }

    public Map<String, FieldSym> getParameters() {
        return this.parameters;
    }

    @Override
    public void print_table() {
        System.out.println("\nMethod Symbol Table: " + this.getID());
        for (Map.Entry<String, FieldSym> e2 : this.parameters.entrySet()) {
            System.out.println("    Parameter: " + e2.getValue().getFieldType() + " " + e2.getKey());
        }
        for (Map.Entry<String, FieldSym> e2 : this.fields.entrySet()) {
            System.out.println("    Local variable: " + e2.getValue().getFieldType() + " " + e2.getKey());
        }
        this.printChildrenTables();
        this.printChildrenScopes();
    }
}

