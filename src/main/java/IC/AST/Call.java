/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import java.util.List;

public abstract class Call
extends Expression {
    private String name;
    private List<Expression> arguments;

    protected Call(int line, String name, List<Expression> arguments) {
        super(line);
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return this.name;
    }

    public List<Expression> getArguments() {
        return this.arguments;
    }
}

