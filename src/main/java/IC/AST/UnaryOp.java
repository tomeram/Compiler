/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.UnaryOps;

public abstract class UnaryOp
extends Expression {
    private UnaryOps operator;
    private Expression operand;

    protected UnaryOp(UnaryOps operator, Expression operand) {
        super(operand.getLine());
        this.operator = operator;
        this.operand = operand;
    }

    public UnaryOps getOperator() {
        return this.operator;
    }

    public Expression getOperand() {
        return this.operand;
    }
}

