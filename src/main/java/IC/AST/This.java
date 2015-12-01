/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Visitor;

public class This
extends Expression {
    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public This(int line) {
        super(line);
    }
}

