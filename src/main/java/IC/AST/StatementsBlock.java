/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Statement;
import IC.AST.Visitor;
import java.util.List;

public class StatementsBlock
extends Statement {
    private List<Statement> statements;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public StatementsBlock(int line, List<Statement> statements) {
        super(line);
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return this.statements;
    }
}

