/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.ASTNode;
import IC.AST.Type;
import IC.AST.Visitor;

public class Formal
extends ASTNode {
    private Type type;
    private String name;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public Formal(Type type, String name) {
        super(type.getLine());
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }
}

