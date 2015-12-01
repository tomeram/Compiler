/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Label;

public class JumpInstr
extends Instruction {
    public final Label label;

    public JumpInstr(Label label) {
        this.label = label;
    }

    public String toString() {
        return "Jump " + this.label;
    }
}

