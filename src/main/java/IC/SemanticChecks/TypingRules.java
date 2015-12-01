/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks;

import IC.AST.ASTNode;
import IC.AST.ArrayLocation;
import IC.AST.Expression;
import IC.AST.ExpressionBlock;
import IC.AST.Length;
import IC.AST.Literal;
import IC.AST.LogicalBinaryOp;
import IC.AST.LogicalUnaryOp;
import IC.AST.MathBinaryOp;
import IC.AST.MathUnaryOp;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.StaticCall;
import IC.AST.This;
import IC.AST.Type;
import IC.AST.UnaryOp;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.BinaryOps;
import IC.Compiler;
import IC.LiteralTypes;
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTableCreator;
import IC.SemanticChecks.SymTables.ClassTable;
import IC.SemanticChecks.SymTables.SymTable;
import IC.SemanticChecks.SymTables.Symbols.MethodSym;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import IC.SemanticChecks.TypingRulesVisitor;
import IC.TypeTable.TypeTable;
import IC.TypeTable.Types.ClassType;
import IC.UnaryOps;
import java.util.LinkedList;
import java.util.List;

public class TypingRules {
    public static boolean checkIfExtends(String type1, String type2) {
        if (type2.equals("void")) {
            return false;
        }
        if (type2.equals("null")) {
            if (type1.equals("int") || type1.equals("boolean")) {
                return false;
            }
            return true;
        }
        if (type1.endsWith("[]") || type2.endsWith("[]")) {
            return type1.equals(type2);
        }
        if (type1.equals("int") || type1.equals("boolean") || type1.equals("string") || type2.equals("int") || type2.equals("boolean") || type2.equals("string")) {
            return type1.equals(type2);
        }
        String curr = type2;
        String currParent = TypeTable.getClasses().get(curr).getExtend();
        while (curr != null) {
            if (curr.equals(type1)) {
                return true;
            }
            curr = currParent;
            if (currParent == null) break;
            currParent = TypeTable.getClasses().get(curr).getExtend();
        }
        return false;
    }

    public static String evaluate_type(Literal expr) {
        LiteralTypes type = expr.getType();
        if (type == LiteralTypes.TRUE || type == LiteralTypes.FALSE) {
            return "boolean";
        }
        if (type == LiteralTypes.INTEGER) {
            return "int";
        }
        if (type == LiteralTypes.STRING) {
            return "string";
        }
        if (type == LiteralTypes.NULL) {
            return "null";
        }
        return null;
    }

    public static String evaluate_type(MathBinaryOp expr) {
        if (TypingRules.evaluate_type(expr.getFirstOperand()).equals("int") && TypingRules.evaluate_type(expr.getSecondOperand()).equals("int")) {
            return "int";
        }
        if (TypingRules.evaluate_type(expr.getFirstOperand()).equals("string") && TypingRules.evaluate_type(expr.getSecondOperand()).equals("string") && expr.getOperator() == BinaryOps.PLUS) {
            return "string";
        }
        Compiler.errorExit("Invalid types for math binary op.", expr.getLine());
        return null;
    }

    public static String evaluate_type(LogicalBinaryOp expr) {
        if (expr.getOperator() == BinaryOps.LAND || expr.getOperator() == BinaryOps.LOR) {
            if (TypingRules.evaluate_type(expr.getFirstOperand()).equals("boolean") && TypingRules.evaluate_type(expr.getSecondOperand()).equals("boolean")) {
                return "boolean";
            }
            Compiler.errorExit("Expected boolean for logical binary op.", expr.getLine());
        } else if (expr.getOperator() == BinaryOps.GT || expr.getOperator() == BinaryOps.GTE || expr.getOperator() == BinaryOps.LT || expr.getOperator() == BinaryOps.LTE) {
            if (TypingRules.evaluate_type(expr.getFirstOperand()).equals("int") && TypingRules.evaluate_type(expr.getSecondOperand()).equals("int")) {
                return "boolean";
            }
            Compiler.errorExit("Expected int types for logical binary op.", expr.getLine());
        } else if (expr.getOperator() == BinaryOps.EQUAL || expr.getOperator() == BinaryOps.NEQUAL) {
            String type2;
            String type1 = TypingRules.evaluate_type(expr.getFirstOperand());
            if (TypingRules.checkIfExtends(type1, type2 = TypingRules.evaluate_type(expr.getSecondOperand())) || TypingRules.checkIfExtends(type2, type1)) {
                return "boolean";
            }
            Compiler.errorExit("Incompatiable types for logical binary op.", expr.getLine());
        }
        return null;
    }

    public static String evaluate_type(UnaryOp expr) {
        if (expr.getOperator() == UnaryOps.UMINUS) {
            if (TypingRules.evaluate_type(expr.getOperand()).equals("int")) {
                return "int";
            }
            Compiler.errorExit("Expected int type for unary op.", expr.getLine());
        } else if (expr.getOperator() == UnaryOps.LNEG) {
            if (TypingRules.evaluate_type(expr.getOperand()).equals("boolean")) {
                return "boolean";
            }
            Compiler.errorExit("Expected boolean type for unary op.", expr.getLine());
        }
        return null;
    }

    public static String evaluate_type(ArrayLocation expr) {
        if (TypingRules.evaluate_type(expr.getIndex()).equals("int")) {
            String type = TypingRules.evaluate_type(expr.getArray());
            if (type.endsWith("[]")) {
                return TypingRules.remove_dim(type);
            }
            Compiler.errorExit("Expected array type.", expr.getLine());
        } else {
            Compiler.errorExit("Expected int type for array index.", expr.getLine());
        }
        return null;
    }

    public static String evaluate_type(NewClass expr) {
        if (TypeTable.getClasses().containsKey(expr.getName())) {
            return expr.getName();
        }
        Compiler.errorExit("Can't create instance, class " + expr.getName() + " has not been declared.", expr.getLine());
        return null;
    }

    public static String evaluate_type(NewArray expr) {
        if (TypingRules.evaluate_type(expr.getSize()).equals("int")) {
            if (TypingRules.valid_iterable_type(expr.getType().getName())) {
                StringBuffer sb = new StringBuffer(String.valueOf(expr.getType().getName()) + "[]");
                int i = 0;
                while (i < expr.getType().getDimension()) {
                    sb.append("[]");
                    ++i;
                }
                return sb.toString();
            }
            Compiler.errorExit("Invalid array type.", expr.getLine());
        } else {
            Compiler.errorExit("Expected int type for array size.", expr.getLine());
        }
        return null;
    }

    public static String evaluate_type(Length expr) {
        if (TypingRules.evaluate_type(expr.getArray()).endsWith("[]")) {
            return "int";
        }
        Compiler.errorExit("Expected array type.", expr.getLine());
        return null;
    }

    public static String evaluate_type(VariableLocation expr) {
        if (expr.isExternal()) {
            ClassTable Class_Table;
            String res;
            String field_name = expr.getName();
            String Class_Name = TypingRules.evaluate_type(expr.getLocation());
            if (Class_Name.contains("[]")) {
                Compiler.errorExit("Cannot call class field from type \"" + Class_Name + "\"", expr.getLine());
            }
            if ((res = TypingRules.get_Type(field_name, Class_Table = SymTableCreator.findClassInTree(Class_Name, expr.getLine()), Kind.FIELD, expr.getLine())) != null) {
                return res;
            }
        } else {
            SymEntry sym2 = SymTableCreator.findSym(expr.getName(), expr.getEnclosing_scope(), Kind.FIELD, expr.getLine());
            if (TypingRulesVisitor.static_mode && ClassTable.class.equals(sym2.getNode().getEnclosing_scope().getClass())) {
                Compiler.errorExit("Invalid use of class field \"" + expr.getName() + "\" in static method.", expr.getLine());
            }
            return TypingRules.get_Type(expr.getName(), expr.getEnclosing_scope(), Kind.FIELD, expr.getLine());
        }
        Compiler.errorExit("Invalid field.", expr.getLine());
        return null;
    }

    public static String evaluate_type(VirtualCall expr) {
        LinkedList<String> args = new LinkedList<String>();
        for (Expression e_arg : expr.getArguments()) {
            args.add(TypingRules.evaluate_type(e_arg));
        }
        if (expr.isExternal()) {
            String Class_Name = TypingRules.evaluate_type(expr.getLocation());
            if (Class_Name.contains("[]")) {
                Compiler.errorExit("Cannot dynamicly call method from array type \"" + Class_Name + "\"", expr.getLine());
            }
            ClassTable Class_Table = SymTableCreator.findClassInTree(Class_Name, expr.getLine());
            String res = TypingRules.check_method_call(args, TypingRules.get_Type(expr.getName(), Class_Table, Kind.METHOD, expr.getLine()));
            if (res != null) {
                return res;
            }
            Compiler.errorExit("Incompatible args for method call.", expr.getLine());
        } else {
            String res;
            MethodSym sym2 = (MethodSym)SymTableCreator.findSym(expr.getName(), expr.getEnclosing_scope(), Kind.METHOD, expr.getLine());
            if (!sym2.isStatic_method() && TypingRulesVisitor.static_mode) {
                Compiler.errorExit("Invalid call to a virtual method inside a static method.", expr.getLine());
            }
            if ((res = TypingRules.check_method_call(args, TypingRules.get_Type(expr.getName(), expr.getEnclosing_scope(), Kind.METHOD, expr.getLine()))) != null) {
                return res;
            }
            Compiler.errorExit("Incompatible args for method call.", expr.getLine());
        }
        return null;
    }

    public static String evaluate_type(StaticCall expr) {
        String res;
        String Class_Name = expr.getClassName();
        LinkedList<String> args = new LinkedList<String>();
        for (Expression e_arg : expr.getArguments()) {
            args.add(TypingRules.evaluate_type(e_arg));
        }
        ClassTable Class_Table = SymTableCreator.findClassInTree(Class_Name, expr.getLine());
        MethodSym method_sym = (MethodSym)SymTableCreator.findSym(expr.getName(), Class_Table, Kind.METHOD, expr.getLine());
        if (method_sym == null) {
            Compiler.errorExit("Static method \"" + expr.getName() + "\" does not exist in class \"" + Class_Name + "\".", expr.getLine());
        }
        if (!method_sym.isStatic_method()) {
            Compiler.errorExit("Cannot call Virtual Method with a static method call.", expr.getLine());
        }
        if ((res = TypingRules.check_method_call(args, TypingRules.get_Type(expr.getName(), Class_Table, Kind.METHOD, expr.getLine()))) != null) {
            return res;
        }
        Compiler.errorExit("Incompatible args for method call.", expr.getLine());
        return null;
    }

    private static String check_method_call(List<String> args, String method_str_type) {
        String[] method_type = method_str_type.replaceAll("\\{", "").replaceAll("\\}", "").split("->");
        String return_type = method_type[1].trim();
        String[] method_args = method_type[0].split(",");
        if (method_args[0].trim().equals("") && args.size() == 0) {
            return return_type;
        }
        if (args.size() != method_args.length) {
            return null;
        }
        int i = 0;
        while (i < args.size()) {
            if (!TypingRules.checkIfExtends(args.get(i), method_args[i].trim())) {
                return null;
            }
            ++i;
        }
        return return_type;
    }

    public static String evaluate_type(ExpressionBlock expr) {
        return TypingRules.evaluate_type(expr.getExpression());
    }

    public static String evaluate_type(This expr) {
        SymTable table = expr.getEnclosing_scope();
        while (!ClassTable.class.equals(table.getClass())) {
            table = table.getParent();
        }
        return table.getID();
    }

    public static String evaluate_type(Expression expr) {
        if (Literal.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((Literal)expr);
        }
        if (MathBinaryOp.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((MathBinaryOp)expr);
        }
        if (LogicalBinaryOp.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((LogicalBinaryOp)expr);
        }
        if (MathUnaryOp.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((MathUnaryOp)expr);
        }
        if (LogicalUnaryOp.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((LogicalUnaryOp)expr);
        }
        if (NewClass.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((NewClass)expr);
        }
        if (ArrayLocation.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((ArrayLocation)expr);
        }
        if (NewArray.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((NewArray)expr);
        }
        if (Length.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((Length)expr);
        }
        if (VariableLocation.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((VariableLocation)expr);
        }
        if (VirtualCall.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((VirtualCall)expr);
        }
        if (StaticCall.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((StaticCall)expr);
        }
        if (ExpressionBlock.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((ExpressionBlock)expr);
        }
        if (This.class.equals(expr.getClass())) {
            return TypingRules.evaluate_type((This)expr);
        }
        return "-2";
    }

    public static String get_Type(String name, SymTable scope, Kind kind, int line) {
        SymEntry entry = SymTableCreator.findSym(name, scope, kind, line);
        return entry.getType();
    }

    public static String remove_dim(String type) {
        return type.substring(0, type.length() - 2);
    }

    public static boolean valid_iterable_type(String name) {
        if (name.equals("int") || name.equals("string") || name.equals("boolean")) {
            return true;
        }
        if (TypeTable.getClasses().containsKey(name)) {
            return true;
        }
        return false;
    }
}

