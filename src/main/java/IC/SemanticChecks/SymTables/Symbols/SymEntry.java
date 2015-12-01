/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks.SymTables.Symbols;

import IC.AST.ASTNode;
import IC.SemanticChecks.Kind;

public abstract class SymEntry {
    private String id;
    private ASTNode node;
    protected String type;
    private Kind kind;
    private int uniqueId = -1;
    static int shadowVarCounter = 1;

    public SymEntry(String id, ASTNode node, Kind kind) {
        this.id = id;
        this.kind = kind;
        this.node = node;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Kind getKind() {
        return this.kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public ASTNode getNode() {
        return this.node;
    }

    public void setNode(ASTNode node) {
        this.node = node;
    }

    public String getType() {
        return this.type;
    }

    public int getUniqueId() {
        if (this.uniqueId == -1) {
            this.uniqueId = shadowVarCounter++;
        }
        return this.uniqueId;
    }

    public void setUniqueId(int i) {
        this.uniqueId = i;
    }
}

