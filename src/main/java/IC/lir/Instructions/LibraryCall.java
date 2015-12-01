/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Label;
import IC.lir.Instructions.Operand;
import IC.lir.Instructions.Reg;
import java.util.Iterator;
import java.util.List;

public class LibraryCall
extends Instruction {
    public final Label func;
    public final List<Operand> args;
    public final Reg dst;

    public LibraryCall(Label func, List<Operand> args, Reg dst) {
        this.func = func;
        this.args = args;
        this.dst = dst;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Library " + this.func + "(");
        Iterator<Operand> argIter = this.args.iterator();
        while (argIter.hasNext()) {
            result.append(argIter.next());
            if (!argIter.hasNext()) continue;
            result.append(", ");
        }
        result.append(")," + this.dst);
        return result.toString();
    }
}

