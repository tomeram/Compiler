/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Visitor;
import IC.SemanticChecks.SymTables.SymTable;

public abstract class ASTNode {
    private int line;
    private SymTable enclosing_scope;

    public abstract Object accept(Visitor var1);

    protected ASTNode(int line) {
        this.line = line;
    }

    public int getLine() {
        return this.line;
    }

    public SymTable getEnclosing_scope() {
        return this.enclosing_scope;
    }

    public void setEnclosing_scope(SymTable enclosing_scope) {
        this.enclosing_scope = enclosing_scope;
    }
}

