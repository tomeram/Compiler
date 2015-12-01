/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Statement;
import IC.AST.Type;
import IC.AST.Visitor;

public class LocalVariable
extends Statement {
    private Type type;
    private String name;
    private Expression initValue = null;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public LocalVariable(Type type, String name) {
        super(type.getLine());
        this.type = type;
        this.name = name;
    }

    public LocalVariable(Type type, String name, Expression initValue) {
        this(type, name);
        this.initValue = initValue;
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasInitValue() {
        if (this.initValue != null) {
            return true;
        }
        return false;
    }

    public Expression getInitValue() {
        return this.initValue;
    }
}

