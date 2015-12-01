/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Operand;

public class Immediate
extends Operand {
    public final int val;
    protected static int numberOfImmeidates = 0;

    public Immediate(int val) {
        this.val = val;
        ++numberOfImmeidates;
    }

    public static int getNumberOfImmediates() {
        return numberOfImmeidates;
    }

    @Override
    public String toString() {
        return new Integer(this.val).toString();
    }

    public int hashCode() {
        int PRIME = 31;
        int result = 1;
        result = 31 * result + this.val;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Immediate other = (Immediate)obj;
        if (this.val != other.val) {
            return false;
        }
        return true;
    }
}

