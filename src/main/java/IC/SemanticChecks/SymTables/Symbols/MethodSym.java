/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks.SymTables.Symbols;

import IC.AST.ASTNode;
import IC.AST.Formal;
import IC.AST.LibraryMethod;
import IC.AST.Method;
import IC.AST.StaticMethod;
import IC.AST.Type;
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTables.Symbols.FieldSym;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import IC.TypeTable.TypeTable;
import java.util.List;

public class MethodSym
extends SymEntry {
    private boolean static_method = false;
    private Type return_type;

    public MethodSym(Method method) {
        super(method.getName(), method, Kind.METHOD);
        this.type = TypeTable.addToMethods(method);
        if (StaticMethod.class.equals(method.getClass()) || LibraryMethod.class.equals(method.getClass())) {
            this.static_method = true;
        }
        this.return_type = method.getType();
    }

    public boolean isStatic_method() {
        return this.static_method;
    }

    public Type getReturn_type() {
        return this.return_type;
    }

    public String method_type() {
        if (this.isStatic_method()) {
            return "Static";
        }
        return "Virtual";
    }

    public String getInOut() {
        StringBuffer buf = new StringBuffer("{");
        Method method = (Method)this.getNode();
        if (!method.getFormals().isEmpty()) {
            for (Formal formal : method.getFormals()) {
                buf.append(String.valueOf(FieldSym.getFieldString(formal.getType())) + ", ");
            }
            buf = buf.delete(buf.length() - 2, buf.length());
        }
        buf.append(" -> " + FieldSym.getFieldString(this.return_type) + "}");
        return buf.toString();
    }
}

