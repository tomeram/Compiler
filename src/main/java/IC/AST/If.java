/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Statement;
import IC.AST.Visitor;

public class If
extends Statement {
    private Expression condition;
    private Statement operation;
    private Statement elseOperation = null;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public If(Expression condition, Statement operation, Statement elseOperation) {
        this(condition, operation);
        this.elseOperation = elseOperation;
    }

    public If(Expression condition, Statement operation) {
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

    public boolean hasElse() {
        if (this.elseOperation != null) {
            return true;
        }
        return false;
    }

    public Statement getElseOperation() {
        return this.elseOperation;
    }
}

