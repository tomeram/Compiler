/*
 * Decompiled with CFR 0_110.
 */
package IC.lir;

public class LirMethodInfo {
    private String class_name;
    private int offset;

    public LirMethodInfo(String class_name, int offset) {
        this.class_name = class_name;
        this.offset = offset;
    }

    public String getClass_name() {
        return this.class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}

