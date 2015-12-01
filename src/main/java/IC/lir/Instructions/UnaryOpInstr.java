/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Operand;
import IC.lir.Instructions.Operator;

public class UnaryOpInstr
extends Instruction {
    public final Operand dst;
    public final Operator op;

    public UnaryOpInstr(Operand dst, Operator op) {
        this.dst = dst;
        this.op = op;
    }

    public String toString() {
        return (Object)((Object)this.op) + " " + this.dst;
    }
}

