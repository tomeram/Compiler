/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Statement;
import IC.AST.Visitor;

public class While
extends Statement {
    private Expression condition;
    private Statement operation;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public While(Expression condition, Statement operation) {
        super(condition.getLine());
        this.condition = condition;
        this.operation = operation;
    }

    public Expression getCondition() {
        return this.condition;
    }

    public Statement getOperation() {
        return this.operation;
    }
}

