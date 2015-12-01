/*
 * Decompiled with CFR 0_110.
 */
package IC.lir.Instructions;

import IC.lir.Instructions.Operand;

public class Label
extends Operand {
    public final String name;
    protected static int numberOfLabels = 0;

    public Label(String name) {
        this.name = name;
        ++numberOfLabels;
    }

    public static int getNumberOfLabels() {
        return numberOfLabels;
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
        Label other = (Label)obj;
        if (this.name == null ? other.name != null : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}

