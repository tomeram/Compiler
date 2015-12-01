/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Operand;
import IC.lir.Instructions.Operator;

public class BinOpInstr
extends Instruction {
    public final Operand src;
    public final Operand dst;
    public final Operator op;

    public BinOpInstr(Operand src, Operand dst, Operator op) {
        this.src = src;
        this.dst = dst;
        this.op = op;
    }

    public String toString() {
        return (Object)((Object)this.op) + " " + this.src + "," + this.dst;
    }
}

