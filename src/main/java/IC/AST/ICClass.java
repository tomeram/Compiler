/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.ASTNode;
import IC.AST.Field;
import IC.AST.Method;
import IC.AST.Visitor;
import java.util.List;

public class ICClass
extends ASTNode {
    private String name;
    private String superClassName = null;
    private List<Field> fields;
    private List<Method> methods;

    @Override
    public Object accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public ICClass(int line, String name, List<Field> fields, List<Method> methods) {
        super(line);
        this.name = name;
        this.fields = fields;
        this.methods = methods;
    }

    public ICClass(int line, String name, String superClassName, List<Field> fields, List<Method> methods) {
        this(line, name, fields, methods);
        this.superClassName = superClassName;
    }

    public String getName() {
        return this.name;
    }

    public boolean hasSuperClass() {
        if (this.superClassName != null) {
            return true;
        }
        return false;
    }

    public String getSuperClassName() {
        return this.superClassName;
    }

    public List<Field> getFields() {
        return this.fields;
    }

    public List<Method> getMethods() {
        return this.methods;
    }
}

