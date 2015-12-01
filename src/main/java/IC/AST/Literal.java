/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Expression;
import IC.AST.Visitor;
import IC.LiteralTypes;

public class Literal
extends Expression {
    private LiteralTypes type;
    private Object value;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public Literal(int line, LiteralTypes type) {
        super(line);
        this.type = type;
        this.value = type.getValue();
    }

    public Literal(int line, LiteralTypes type, Object value) {
        this(line, type);
        this.value = value;
    }

    public LiteralTypes getType() {
        return this.type;
    }

    public Object getValue() {
        return this.value;
    }
}

