/*
 * Decompiled with CFR 0_110.
 */
package IC.lir;

import IC.SemanticChecks.SymTables.ClassTable;
import IC.SemanticChecks.SymTables.Symbols.FieldSym;
import IC.SemanticChecks.SymTables.Symbols.MethodSym;
import IC.lir.LirMethodInfo;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LirClassLayout {
    private String name;
    private LinkedHashMap<String, LirMethodInfo> methods;
    private LinkedHashMap<String, Integer> fields;

    public LirClassLayout(String name) {
        this.name = name;
        this.methods = new LinkedHashMap();
        this.fields = new LinkedHashMap();
    }

    public LirClassLayout(String name, ClassTable table, LirClassLayout parent) {
        this(name);
        if (parent != null) {
            this.addParent(parent);
        }
        for (String method : table.getMethods().keySet()) {
            this.putMethod(method, table.getID());
        }
        for (String field : table.getFields().keySet()) {
            this.putField(field);
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, LirMethodInfo> getMethods() {
        return this.methods;
    }

    public void setMethods(LinkedHashMap<String, LirMethodInfo> methods) {
        this.methods = methods;
    }

    public Map<String, Integer> getFields() {
        return this.fields;
    }

    public void setFields(LinkedHashMap<String, Integer> fields) {
        this.fields = fields;
    }

    public void addParent(LirClassLayout parent) {
        this.methods.putAll(parent.getMethods());
        this.fields.putAll(parent.getFields());
    }

    public void putMethod(String method, String p_class) {
        if ("main".equals(method)) {
            return;
        }
        if (!this.methods.containsKey(method)) {
            this.methods.put(method, new LirMethodInfo(p_class, this.methods.size()));
        } else {
            this.methods.put(method, new LirMethodInfo(p_class, this.methods.get(method).getOffset()));
        }
    }

    public void putField(String name) {
        this.fields.put(name, this.fields.size() + 1);
    }

    public int classSize() {
        return (this.fields.size() + 1) * 4;
    }

    public void printLayout(FileWriter fw) throws IOException {
        fw.write("_DV_" + this.name + ": [");
        StringBuffer buff = new StringBuffer();
        for (Map.Entry<String, LirMethodInfo> method : this.methods.entrySet()) {
            LirMethodInfo inf = method.getValue();
            buff.append("_" + inf.getClass_name() + "_" + method.getKey() + ",");
        }
        String res = "";
        if (buff.length() != 0) {
            res = buff.substring(0, buff.length() - 1);
        }
        fw.write(String.valueOf(res) + "]\n");
    }

    public void printFields(FileWriter fw) throws IOException {
        fw.write("## " + this.name + ": [");
        StringBuffer buff = new StringBuffer();
        for (Map.Entry<String, Integer> field : this.getFields().entrySet()) {
            buff.append("(" + field.getValue() + ":" + field.getKey() + "),");
        }
        String res = "";
        if (buff.length() != 0) {
            res = buff.substring(0, buff.length() - 1);
        }
        fw.write(String.valueOf(res) + "]\n");
    }
}

