/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.ASTNode;
import IC.AST.Formal;
import IC.AST.Statement;
import IC.AST.Type;
import java.util.List;

public abstract class Method
extends ASTNode {
    protected Type type;
    protected String name;
    protected List<Formal> formals;
    protected List<Statement> statements;

    protected Method(Type type, String name, List<Formal> formals, List<Statement> statements) {
        super(type.getLine());
        this.type = type;
        this.name = name;
        this.formals = formals;
        this.statements = statements;
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public List<Formal> getFormals() {
        return this.formals;
    }

    public List<Statement> getStatements() {
        return this.statements;
    }
}

