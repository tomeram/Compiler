/*
 * Decompiled with CFR 0_110.
 */
package IC.TypeTable.Types;

import IC.TypeTable.Types.Types;

public class ArrayType
extends Types {
    public ArrayType(String name, int id) {
        super(name, id);
    }

    @Override
    public boolean subtypeof(Types t) {
        if (t == null) {
            return false;
        }
        if (t.getClass().equals(ArrayType.class) && t.getName().equals(this.getName())) {
            return true;
        }
        return false;
    }
}

