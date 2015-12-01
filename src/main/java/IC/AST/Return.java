/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Statement;
import IC.AST.Visitor;

public class Return
extends Statement {
    private Expression value = null;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public Return(int line) {
        super(line);
    }

    public Return(int line, Expression value) {
        this(line);
        this.value = value;
    }

    public boolean hasValue() {
        if (this.value != null) {
            return true;
        }
        return false;
    }

    public Expression getValue() {
        return this.value;
    }
}

