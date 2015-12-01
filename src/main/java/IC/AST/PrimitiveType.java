/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Type;
import IC.AST.Visitor;
import IC.DataTypes;

public class PrimitiveType
extends Type {
    private DataTypes type;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public PrimitiveType(int line, DataTypes type) {
        super(line);
        this.type = type;
    }

    @Override
    public String getName() {
        return this.type.getDescription();
    }
}

