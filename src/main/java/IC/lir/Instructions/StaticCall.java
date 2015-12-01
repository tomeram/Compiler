/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.Label;
import IC.lir.Instructions.ParamOpPair;
import IC.lir.Instructions.Reg;
import java.util.Iterator;
import java.util.List;

public class StaticCall
extends Instruction {
    public final Label func;
    public final List<ParamOpPair> args;
    public final Reg dst;

    public StaticCall(Label func, List<ParamOpPair> args, Reg dst) {
        this.func = func;
        this.args = args;
        this.dst = dst;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("StaticCall " + this.func + "(");
        Iterator<ParamOpPair> argIter = this.args.iterator();
        while (argIter.hasNext()) {
            result.append(argIter.next());
            if (!argIter.hasNext()) continue;
            result.append(", ");
        }
        result.append(")," + this.dst);
        return result.toString();
    }
}

