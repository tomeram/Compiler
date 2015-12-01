/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Operand;

public class MoveArrayInstr
extends Instruction {
    public final Operand base;
    public final Operand offset;
    public final Operand mem;
    public final boolean isLoad;

    public MoveArrayInstr(Operand base, Operand offset, Operand mem, boolean isLoad) {
        this.base = base;
        this.offset = offset;
        this.mem = mem;
        this.isLoad = isLoad;
    }

    public String toString() {
        if (this.isLoad) {
            return "MoveArray " + this.base + "[" + this.offset + "]," + this.mem;
        }
        return "MoveArray " + this.mem + "," + this.base + "[" + this.offset + "]";
    }
}

