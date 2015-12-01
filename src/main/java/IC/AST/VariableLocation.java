/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Location;
import IC.AST.Visitor;

public class VariableLocation
extends Location {
    private Expression location = null;
    private String name;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public VariableLocation(int line, String name) {
        super(line);
        this.name = name;
    }

    public VariableLocation(int line, Expression location, String name) {
        this(line, name);
        this.location = location;
    }

    public boolean isExternal() {
        if (this.location != null) {
            return true;
        }
        return false;
    }

    public Expression getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }
}

