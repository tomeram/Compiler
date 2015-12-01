/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.Field;
import IC.AST.Method;
import java.util.LinkedList;

public class FieldMethodLists {
    private LinkedList<Field> field_list = new LinkedList();
    private LinkedList<Method> method_list = new LinkedList();

    public LinkedList<Field> getField_list() {
        return this.field_list;
    }

    public void setField_list(LinkedList<Field> field_list) {
        this.field_list = field_list;
    }

    public LinkedList<Method> getMethod_list() {
        return this.method_list;
    }

    public void setMethod_list(LinkedList<Method> method_list) {
        this.method_list = method_list;
    }

    public void addMethod(Method m) {
        this.method_list.add(m);
    }

    public void addField(Field f) {
        this.field_list.addFirst(f);
    }
}

