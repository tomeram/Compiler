/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Memory;
import IC.lir.Instructions.Operand;

public class ParamOpPair {
    public final Memory param;
    public final Operand op;

    public ParamOpPair(Memory param, Operand reg) {
        this.param = param;
        this.op = reg;
    }

    public String toString() {
        return this.param + "=" + this.op;
    }
}

