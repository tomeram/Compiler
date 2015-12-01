/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Statement;
import IC.AST.Visitor;

public class Break
extends Statement {
    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public Break(int line) {
        super(line);
    }
}

