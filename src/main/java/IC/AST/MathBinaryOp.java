/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.BinaryOp;
import IC.AST.Expression;
import IC.AST.Visitor;
import IC.BinaryOps;

public class MathBinaryOp
extends BinaryOp {
    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public MathBinaryOp(Expression operand1, BinaryOps operator, Expression operand2) {
        super(operand1, operator, operand2);
    }
}

