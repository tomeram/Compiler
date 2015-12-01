/*
 * Decompiled with CFR 0_110.
 */
package IC.TypeTable;

import IC.AST.Formal;
import IC.AST.Method;
import IC.AST.Type;
import IC.TypeTable.Types.ArrayType;
import IC.TypeTable.Types.ClassType;
import IC.TypeTable.Types.MethodType;
import IC.TypeTable.Types.PrimitiveType;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TypeTable {
    private static Map<String, ClassType> classes = new LinkedHashMap<String, ClassType>();
    private static Map<String, ArrayType> arrayTypes = new LinkedHashMap<String, ArrayType>();
    private static Map<String, PrimitiveType> primitive = new LinkedHashMap<String, PrimitiveType>();
    private static Map<String, MethodType> methods = new LinkedHashMap<String, MethodType>();
    private static int counter = 6;

    public static Map<String, ClassType> getClasses() {
        return classes;
    }

    public static Map<String, ArrayType> getArrayTypes() {
        return arrayTypes;
    }

    public static Map<String, PrimitiveType> getPrimitive() {
        return primitive;
    }

    public static void addToClass(String name, String ext) {
        ClassType new_class = new ClassType(name, counter, ext);
        classes.put(name, new_class);
        ++counter;
    }

    public static String addToMethods(Method method) {
        StringBuffer sb = new StringBuffer("{");
        for (Formal formal : method.getFormals()) {
            if (formal.getType().getDimension() > 0) {
                sb.append(TypeTable.addArrayType(formal.getType()));
            } else {
                sb.append(formal.getType().getName());
                TypeTable.addPrimitive(formal.getType());
            }
            sb.append(", ");
        }
        if (sb.length() > 2) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(" -> ");
        if (method.getType().getDimension() > 0) {
            sb.append(TypeTable.addArrayType(method.getType()));
        } else {
            sb.append(method.getType().getName());
            TypeTable.addPrimitive(method.getType());
        }
        sb.append("}");
        if (!methods.containsKey(sb.toString())) {
            methods.put(sb.toString(), new MethodType(sb.toString(), counter));
            ++counter;
        }
        return sb.toString();
    }

    public static String addVariable(Type type) {
        if (type.getDimension() > 0) {
            return TypeTable.addArrayType(type);
        }
        return TypeTable.addPrimitive(type);
    }

    public static String addArrayType(Type type) {
        StringBuffer arr_dim = new StringBuffer();
        int i = 0;
        while (i < type.getDimension()) {
            arr_dim.append("[]");
            if (!arrayTypes.containsKey(String.valueOf(type.getName()) + arr_dim.toString())) {
                arrayTypes.put(String.valueOf(type.getName()) + arr_dim.toString(), new ArrayType(String.valueOf(type.getName()) + arr_dim.toString(), counter));
                ++counter;
            }
            ++i;
        }
        return String.valueOf(type.getName()) + arr_dim.toString();
    }

    public static String addPrimitive(Type type) {
        if (type.getName().equals("int")) {
            primitive.put("int", new PrimitiveType("int", 1));
            return "int";
        }
        if (type.getName().equals("boolean")) {
            primitive.put("boolean", new PrimitiveType("boolean", 2));
            return "boolean";
        }
        if (type.getName().equals("null")) {
            primitive.put("null", new PrimitiveType("null", 3));
            return "null";
        }
        if (type.getName().equals("string")) {
            primitive.put("string", new PrimitiveType("string", 4));
            return "string";
        }
        if (type.getName().equals("void")) {
            primitive.put("void", new PrimitiveType("void", 5));
            return "void";
        }
        return type.getName();
    }

    public static void print_TypeTable(String f_name) {
        System.out.println("\nType Table: " + f_name);
        TypeTable.print_Primitives();
        for (Map.Entry<String, ClassType> cls : classes.entrySet()) {
            System.out.println("    " + cls.getValue().getID() + ": " + cls.getValue().toPrintStr());
        }
        for (Map.Entry arr : arrayTypes.entrySet()) {
            System.out.println("    " + ((ArrayType)arr.getValue()).getID() + ": Array type: " + (String)arr.getKey());
        }
        for (Map.Entry method : methods.entrySet()) {
            System.out.println("    " + ((MethodType)method.getValue()).getID() + ": Method type: " + (String)method.getKey());
        }
    }

    private static void print_Primitives() {
        System.out.println("    1: Primitive type: int");
        System.out.println("    2: Primitive type: boolean");
        System.out.println("    3: Primitive type: null");
        System.out.println("    4: Primitive type: string");
        System.out.println("    5: Primitive type: void");
    }
}

