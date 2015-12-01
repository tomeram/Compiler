/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Label;

public class LabelInstr
extends Instruction {
    public final Label label;

    public LabelInstr(Label label) {
        this.label = label;
    }

    public String toString() {
        return this.label + ":";
    }
}

