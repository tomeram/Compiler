/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Visitor;

public class ExpressionBlock
extends Expression {
    private Expression expression;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public ExpressionBlock(Expression expression) {
        super(expression.getLine());
        this.expression = expression;
    }

    public Expression getExpression() {
        return this.expression;
    }
}

