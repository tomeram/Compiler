/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Memory;
import IC.lir.Instructions.Operand;

public class MoveInstr
extends Instruction {
    public final Operand src;
    public final Operand dst;

    public MoveInstr(Operand src, Operand dst) {
        this.src = src;
        this.dst = dst;
        if (src instanceof Memory && dst instanceof Memory) {
            throw new RuntimeException("Encountered " + this + " with two memory operands!");
        }
    }

    public String toString() {
        return "Move " + this.src + "," + this.dst;
    }
}

