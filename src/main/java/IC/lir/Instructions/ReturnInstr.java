/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Operand;

public class ReturnInstr
extends Instruction {
    public final Operand dst;

    public ReturnInstr(Operand dst) {
        this.dst = dst;
    }

    public String toString() {
        return "Return " + this.dst;
    }
}

