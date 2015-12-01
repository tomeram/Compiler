/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Visitor;

public class Length
extends Expression {
    private Expression array;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public Length(Expression array) {
        super(array.getLine());
        this.array = array;
    }

    public Expression getArray() {
        return this.array;
    }
}

