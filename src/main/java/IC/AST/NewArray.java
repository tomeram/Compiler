/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.New;
import IC.AST.Type;
import IC.AST.Visitor;

public class NewArray
extends New {
    private Type type;
    private Expression size;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public NewArray(Type type, Expression size) {
        super(type.getLine());
        this.type = type;
        this.size = size;
    }

    public Type getType() {
        return this.type;
    }

    public Expression getSize() {
        return this.size;
    }
}

