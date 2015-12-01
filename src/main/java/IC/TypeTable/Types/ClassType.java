/*
 * Decompiled with CFR 0_110.
 */
package IC.TypeTable.Types;

import IC.TypeTable.TypeTable;
import IC.TypeTable.Types.Types;

public class ClassType
extends Types {
    private int extnd_id = 0;
    private String extnd = null;
    private String prt;

    public ClassType(String c_name, int counter, String ext) {
        super(c_name, counter);
        if (ext == null) {
            this.prt = "Class: " + c_name;
        } else {
            this.extnd = ext;
            this.extnd_id = TypeTable.getClasses().get(this.extnd).getID();
            this.prt = "Class: " + c_name + ", Superclass ID: " + this.extnd_id;
        }
    }

    @Override
    public boolean subtypeof(Types t) {
        if (t == null) {
            return false;
        }
        if (t.getClass().equals(ClassType.class)) {
            if (t.getName().equals(this.getName())) {
                return true;
            }
            if (this.extnd != null) {
                return TypeTable.getClasses().get(this.extnd).subtypeof(t);
            }
        }
        return false;
    }

    public String getExtend() {
        return this.extnd;
    }

    public String toPrintStr() {
        return this.prt;
    }
}

