/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Operand;

public class MoveFieldInstr
extends Instruction {
    public final Operand base;
    public final Operand offset;
    public final Operand mem;
    public final boolean isLoad;

    public MoveFieldInstr(Operand base, Operand offset, Operand mem, boolean isLoad) {
        this.base = base;
        this.offset = offset;
        this.mem = mem;
        this.isLoad = isLoad;
    }

    public String toString() {
        if (this.isLoad) {
            return "MoveField " + this.base + "." + this.offset + "," + this.mem;
        }
        return "MoveField " + this.mem + "," + this.base + "." + this.offset;
    }
}

