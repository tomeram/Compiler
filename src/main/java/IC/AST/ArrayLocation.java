/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Location;
import IC.AST.Visitor;

public class ArrayLocation
extends Location {
    private Expression array;
    private Expression index;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public ArrayLocation(Expression array, Expression index) {
        super(array.getLine());
        this.array = array;
        this.index = index;
    }

    public Expression getArray() {
        return this.array;
    }

    public Expression getIndex() {
        return this.index;
    }
}

