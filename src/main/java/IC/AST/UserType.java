/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Type;
import IC.AST.Visitor;

public class UserType
extends Type {
    private String name;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public UserType(int line, String name) {
        super(line);
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}

