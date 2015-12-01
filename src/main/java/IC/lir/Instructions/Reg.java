/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Operand;

public class Reg
extends Operand {
    public final String name;
    protected static int numberOfRegisters = 0;

    public Reg(String name) {
        this.name = name;
        ++numberOfRegisters;
    }

    public static int getNumberOfRegisters() {
        return numberOfRegisters;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int hashCode() {
        int PRIME = 31;
        int result = 1;
        result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
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
        Reg other = (Reg)obj;
        if (this.name == null ? other.name != null : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}

