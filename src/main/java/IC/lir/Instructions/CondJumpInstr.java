/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Cond;
import IC.lir.Instructions.JumpInstr;
import IC.lir.Instructions.Label;

public class CondJumpInstr
extends JumpInstr {
    public final Cond cond;

    public CondJumpInstr(Label label, Cond cond) {
        super(label);
        this.cond = cond;
    }

    @Override
    public String toString() {
        return "Jump" + (Object)((Object)this.cond) + " " + this.label;
    }
}

