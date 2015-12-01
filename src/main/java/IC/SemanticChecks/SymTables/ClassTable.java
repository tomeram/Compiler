/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks.SymTables;

import IC.AST.ASTNode;
import IC.AST.Field;
import IC.AST.Method;
import IC.AST.Type;
import IC.Compiler;
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTables.SymTable;
import IC.SemanticChecks.SymTables.Symbols.FieldSym;
import IC.SemanticChecks.SymTables.Symbols.MethodSym;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ClassTable
extends SymTable {
    private Map<String, FieldSym> fields = new LinkedHashMap<String, FieldSym>();
    private Map<String, MethodSym> methods = new LinkedHashMap<String, MethodSym>();
    private String extend;

    public ClassTable(String id, SymTable parent) {
        super(id, parent);
    }

    @Override
    public void addSym(Object obj) {
        if (Field.class.isAssignableFrom(obj.getClass())) {
            Field field = (Field)obj;
            if (this.containsSym(field.getName(), Kind.FIELD) || this.containsSym(field.getName(), Kind.METHOD)) {
                Compiler.errorExit("Duplicate field definition.", field.getLine());
            }
            this.fields.put(field.getName(), new FieldSym(field, field.getName(), field.getType()));
        } else if (Method.class.isAssignableFrom(obj.getClass())) {
            Method method = (Method)obj;
            if (this.containsSym(method.getName(), Kind.METHOD) || this.containsSym(method.getName(), Kind.FIELD)) {
                Compiler.errorExit("Duplicfate method definitions.", method.getLine());
            }
            this.methods.put(method.getName(), new MethodSym(method));
        }
    }

    @Override
    public SymEntry getSym(String id, Kind kind) {
        if (Kind.METHOD == kind) {
            return this.methods.get(id);
        }
        return this.fields.get(id);
    }

    @Override
    public boolean containsSym(String id, Kind kind) {
        if (Kind.METHOD == kind) {
            return this.methods.containsKey(id);
        }
        return this.fields.containsKey(id);
    }

    @Override
    public void print_table() {
        System.out.println("\nClass Symbol Table: " + this.getID());
        for (Map.Entry<String, FieldSym> field : this.fields.entrySet()) {
            System.out.println("    Field: " + field.getValue().getFieldType() + " " + field.getKey());
        }
        for (Map.Entry method : this.methods.entrySet()) {
            System.out.println("    " + ((MethodSym)method.getValue()).method_type() + " method: " + ((MethodSym)method.getValue()).getId() + " " + ((MethodSym)method.getValue()).getInOut());
        }
        this.printChildrenTables();
        this.printChildrenScopes();
    }

    public String getExtend() {
        return this.extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public Map<String, MethodSym> getMethods() {
        return this.methods;
    }

    public Map<String, FieldSym> getFields() {
        return this.fields;
    }
}

