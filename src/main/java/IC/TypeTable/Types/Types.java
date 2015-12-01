/*
 * Decompiled with CFR 0_110.
 */
package IC.TypeTable.Types;

public abstract class Types {
    private int id;
    private String name;

    public Types(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public abstract boolean subtypeof(Types var1);
}

