/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks.SymTables;

import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class SymTable {
    private String id;
    private Map<String, SymTable> children;
    private SymTable parent;

    public SymTable(String id, SymTable parent) {
        this.id = id;
        this.parent = parent;
        this.children = new LinkedHashMap<String, SymTable>();
    }

    public Map<String, SymTable> getChildren() {
        return this.children;
    }

    public void setParent(SymTable parent) {
        this.parent = parent;
    }

    public SymTable getParent() {
        return this.parent;
    }

    public abstract void addSym(Object var1);

    public String getID() {
        return this.id;
    }

    public void addChild(SymTable child) {
        if (child != null) {
            this.children.put(child.id, child);
        }
    }

    public SymTable getChild(String id) {
        return this.children.get(id);
    }

    public void printChildrenTables() {
        if (this.getChildren().isEmpty()) {
            return;
        }
        StringBuffer children_str = new StringBuffer("Children tables: ");
        for (Map.Entry<String, SymTable> e : this.getChildren().entrySet()) {
            if (e.getKey().matches("\\d*")) {
                children_str.append("statement block in " + this.getID() + ", ");
                continue;
            }
            children_str.append(String.valueOf(e.getKey()) + ", ");
        }
        System.out.println(children_str.substring(0, children_str.length() - 2));
    }

    public void printChildrenScopes() {
        for (Map.Entry<String, SymTable> e : this.getChildren().entrySet()) {
            SymTable curr = e.getValue();
            curr.print_table();
        }
    }

    public abstract boolean containsSym(String var1, Kind var2);

    public abstract SymEntry getSym(String var1, Kind var2);

    public abstract void print_table();
}

