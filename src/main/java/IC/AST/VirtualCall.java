/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Call;
import IC.AST.Expression;
import IC.AST.Visitor;
import java.util.List;

public class VirtualCall
extends Call {
    private Expression location = null;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public VirtualCall(int line, String name, List<Expression> arguments) {
        super(line, name, arguments);
    }

    public VirtualCall(int line, Expression location, String name, List<Expression> arguments) {
        this(line, name, arguments);
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
}

