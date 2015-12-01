/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.BinOpInstr;
import IC.lir.Instructions.Operand;
import IC.lir.Instructions.Operator;

public class CompareInstr
extends BinOpInstr {
    public CompareInstr(Operand src, Operand dst) {
        super(src, dst, Operator.SUB);
    }

    @Override
    public String toString() {
        return "Compare " + this.src + "," + this.dst;
    }
}

