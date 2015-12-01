/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks.SymTables.Symbols;

import IC.AST.ASTNode;
import IC.AST.Field;
import IC.AST.Formal;
import IC.AST.LocalVariable;
import IC.AST.Type;
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import IC.TypeTable.TypeTable;

public class FieldSym
extends SymEntry {
    public FieldSym(ASTNode node, String id, Type type) {
        super(id, node, Kind.FIELD);
        this.type = TypeTable.addVariable(type);
    }

    public String getFieldType() {
        if (Field.class.equals(this.getNode().getClass())) {
            Field field = (Field)this.getNode();
            return FieldSym.getFieldString(field.getType());
        }
        if (Formal.class.equals(this.getNode().getClass())) {
            Formal formal = (Formal)this.getNode();
            return FieldSym.getFieldString(formal.getType());
        }
        if (LocalVariable.class.equals(this.getNode().getClass())) {
            LocalVariable local = (LocalVariable)this.getNode();
            return FieldSym.getFieldString(local.getType());
        }
        return null;
    }

    public static String getFieldString(Type type) {
        StringBuffer arr_dim = new StringBuffer();
        int i = 0;
        while (i < type.getDimension()) {
            arr_dim.append("[]");
            ++i;
        }
        return String.valueOf(type.getName()) + arr_dim.toString();
    }
}

