/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks.SymTables.Symbols;

import IC.AST.ASTNode;
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;

public class ClassSymbol
extends SymEntry {
    public ClassSymbol(String id, ASTNode node) {
        super(id, node, Kind.CLASS);
        this.type = id;
    }
}

