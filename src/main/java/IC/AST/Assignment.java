/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Location;
import IC.AST.Statement;
import IC.AST.Visitor;

public class Assignment
extends Statement {
    private Location variable;
    private Expression assignment;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public Assignment(Location variable, Expression assignment) {
        super(variable.getLine());
        this.variable = variable;
        this.assignment = assignment;
    }

    public Location getVariable() {
        return this.variable;
    }

    public Expression getAssignment() {
        return this.assignment;
    }
}

