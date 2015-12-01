/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.ASTNode;

public abstract class Type
extends ASTNode {
    private int dimension = 0;

    protected Type(int line) {
        super(line);
    }

    public abstract String getName();

    public int getDimension() {
        return this.dimension;
    }

    public void incrementDimension() {
        ++this.dimension;
    }
}

