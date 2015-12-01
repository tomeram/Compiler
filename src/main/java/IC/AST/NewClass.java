/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.New;
import IC.AST.Visitor;

public class NewClass
extends New {
    private String name;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public NewClass(int line, String name) {
        super(line);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

