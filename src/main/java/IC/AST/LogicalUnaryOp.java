/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.UnaryOp;
import IC.AST.Visitor;
import IC.UnaryOps;

public class LogicalUnaryOp
extends UnaryOp {
    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public LogicalUnaryOp(UnaryOps operator, Expression operand) {
        super(operator, operand);
    }
}

