/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Formal;
import IC.AST.Method;
import IC.AST.Statement;
import IC.AST.Type;
import IC.AST.Visitor;
import java.util.List;

public class VirtualMethod
extends Method {
    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public VirtualMethod(Type type, String name, List<Formal> formals, List<Statement> statements) {
        super(type, name, formals, statements);
    }
}

