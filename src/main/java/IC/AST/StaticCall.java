/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Call;
import IC.AST.Expression;
import IC.AST.Visitor;
import java.util.List;

public class StaticCall
extends Call {
    private String className;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public StaticCall(int line, String className, String name, List<Expression> arguments) {
        super(line, name, arguments);
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }
}

