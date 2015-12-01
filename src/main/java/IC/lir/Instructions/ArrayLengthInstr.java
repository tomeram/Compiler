/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Operand;
import IC.lir.Instructions.Reg;

public class ArrayLengthInstr
extends Instruction {
    public final Operand arr;
    public final Reg dst;

    public ArrayLengthInstr(Operand arr, Reg dst) {
        this.arr = arr;
        this.dst = dst;
    }

    public String toString() {
        return "ArrayLength " + this.arr + "," + this.dst;
    }
}

