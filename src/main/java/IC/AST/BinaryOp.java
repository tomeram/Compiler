/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.BinaryOps;

public abstract class BinaryOp
extends Expression {
    private Expression operand1;
    private BinaryOps operator;
    private Expression operand2;

    protected BinaryOp(Expression operand1, BinaryOps operator, Expression operand2) {
        super(operand1.getLine());
        this.operand1 = operand1;
        this.operator = operator;
        this.operand2 = operand2;
    }

    public BinaryOps getOperator() {
        return this.operator;
    }

    public Expression getFirstOperand() {
        return this.operand1;
    }

    public Expression getSecondOperand() {
        return this.operand2;
    }
}

