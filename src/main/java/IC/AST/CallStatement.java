/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Call;
import IC.AST.Statement;
import IC.AST.Visitor;

public class CallStatement
extends Statement {
    private Call call;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public CallStatement(Call call) {
        super(call.getLine());
        this.call = call;
    }

    public Call getCall() {
        return this.call;
    }
}

