/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.ASTNode;
import IC.AST.ICClass;
import IC.AST.Visitor;
import java.util.List;

public class Program
extends ASTNode {
    private List<ICClass> classes;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public Program(List<ICClass> classes) {
        super(0);
        this.classes = classes;
    }

    public List<ICClass> getClasses() {
        return this.classes;
    }
}

