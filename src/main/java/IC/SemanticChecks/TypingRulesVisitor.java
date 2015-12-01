/*
 * Decompiled with CFR 0_110.
 */
package IC.SemanticChecks;

import IC.AST.ASTNode;
import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
import IC.AST.Call;
import IC.AST.CallStatement;
import IC.AST.Continue;
import IC.AST.Expression;
import IC.AST.ExpressionBlock;
import IC.AST.Field;
import IC.AST.Formal;
import IC.AST.ICClass;
import IC.AST.If;
import IC.AST.Length;
import IC.AST.LibraryMethod;
import IC.AST.Literal;
import IC.AST.LocalVariable;
import IC.AST.Location;
import IC.AST.LogicalBinaryOp;
import IC.AST.LogicalUnaryOp;
import IC.AST.MathBinaryOp;
import IC.AST.MathUnaryOp;
import IC.AST.Method;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.Return;
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.Type;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;
import IC.Compiler;
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTableCreator;
import IC.SemanticChecks.SymTables.ClassTable;
import IC.SemanticChecks.SymTables.MethodTable;
import IC.SemanticChecks.SymTables.ProgramTable;
import IC.SemanticChecks.SymTables.SymTable;
import IC.SemanticChecks.SymTables.Symbols.MethodSym;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import IC.SemanticChecks.TypingRules;
import java.util.List;

public class TypingRulesVisitor
implements Visitor {
    public static boolean static_mode = false;

    @Override
    public Object visit(Program program) {
        for (ICClass icClass : program.getClasses()) {
            icClass.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ICClass icClass) {
        for (Method method : icClass.getMethods()) {
            method.accept(this);
        }
        for (Field field : icClass.getFields()) {
            field.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Field field) {
        SymEntry sym2 = SymTableCreator.findSym(field.getName(), field.getEnclosing_scope(), Kind.METHOD, -1);
        if (sym2 != null) {
            Compiler.errorExit("Illegal variable name \"" + field.getName() + "\", conflict with method in superclass.", field.getLine());
        }
        if ((sym2 = SymTableCreator.findSym(field.getName(), field.getEnclosing_scope().getParent(), Kind.FIELD, -1)) != null) {
            Compiler.errorExit("Illegal variable name \"" + field.getName() + "\", conflict with variable in superclass.", field.getLine());
        }
        return null;
    }

    public void method_visit(Visitor visit, Method method) {
        SymEntry sym_f;
        MethodSym sym2 = (MethodSym)SymTableCreator.findSym(method.getName(), method.getEnclosing_scope().getParent(), Kind.METHOD, -1);
        if (sym2 != null) {
            String method_type = SymTableCreator.findSym(method.getName(), method.getEnclosing_scope(), Kind.METHOD, -1).getType();
            if (!method_type.equals(sym2.getType())) {
                Compiler.errorExit("Illegal method override,  conflict with method \"" + method.getName() + "\" in superclass.", method.getLine());
            }
            if (sym2.isStatic_method() && !StaticMethod.class.equals(method.getClass())) {
                Compiler.errorExit("Illegal method override,  conflict with method \"" + method.getName() + "\" in superclass.", method.getLine());
            }
            if (!sym2.isStatic_method() && StaticMethod.class.equals(method.getClass())) {
                Compiler.errorExit("Illegal method override,  conflict with method \"" + method.getName() + "\" in superclass.", method.getLine());
            }
        }
        if ((sym_f = SymTableCreator.findSym(method.getName(), method.getEnclosing_scope().getParent(), Kind.FIELD, -1)) != null) {
            Compiler.errorExit("Illegal method name \"" + method.getName() + "\", conflict with variable in superclass.", method.getLine());
        }
        for (Formal formal : method.getFormals()) {
            formal.accept(this);
        }
        for (Statement statement : method.getStatements()) {
            statement.accept(this);
        }
    }

    @Override
    public Object visit(VirtualMethod method) {
        this.method_visit(this, method);
        return null;
    }

    @Override
    public Object visit(StaticMethod method) {
        static_mode = true;
        this.method_visit(this, method);
        static_mode = false;
        return null;
    }

    @Override
    public Object visit(LibraryMethod method) {
        this.method_visit(this, method);
        return null;
    }

    @Override
    public Object visit(Formal formal) {
        String c = formal.getType().getName();
        if ("string".equals(c) || "int".equals(c) || "boolean".equals(c)) {
            return null;
        }
        SymTableCreator.findClassInTree(c, formal.getLine());
        return null;
    }

    @Override
    public Object visit(PrimitiveType type) {
        return null;
    }

    @Override
    public Object visit(UserType type) {
        return null;
    }

    @Override
    public Object visit(Assignment assignment) {
        String var_type;
        String assign_type;
        if (assignment.getAssignment() != null) {
            assignment.getAssignment().accept(this);
        }
        if (assignment.getVariable() != null) {
            assignment.getVariable().accept(this);
        }
        if (!TypingRules.checkIfExtends(var_type = TypingRules.evaluate_type(assignment.getVariable()), assign_type = TypingRules.evaluate_type(assignment.getAssignment()))) {
            Compiler.errorExit("Invalid types for assignment.", assignment.getLine());
        }
        return null;
    }

    @Override
    public Object visit(CallStatement callStatement) {
        if (callStatement.getCall() != null) {
            callStatement.getCall().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Return returnStatement) {
        if (returnStatement.getValue() != null) {
            returnStatement.getValue().accept(this);
        }
        SymTable table = returnStatement.getEnclosing_scope();
        while (!MethodTable.class.equals(table.getClass())) {
            table = table.getParent();
        }
        String method_name = table.getID();
        table = table.getParent();
        String type = TypingRules.get_Type(method_name, table, Kind.METHOD, returnStatement.getLine());
        String method_ret = type.split(" -> ")[1].replaceAll("\\}", "").trim();
        if (returnStatement.getValue() == null) {
            if (!method_ret.equals("void")) {
                Compiler.errorExit("Incompatible return type for method.", returnStatement.getLine());
            }
        } else if (!TypingRules.checkIfExtends(method_ret, TypingRules.evaluate_type(returnStatement.getValue()))) {
            Compiler.errorExit("Incompatible return type for method.", returnStatement.getLine());
        }
        return null;
    }

    @Override
    public Object visit(If ifStatement) {
        if (ifStatement.getCondition() != null) {
            ifStatement.getCondition().accept(this);
        }
        if (!TypingRules.evaluate_type(ifStatement.getCondition()).equals("boolean")) {
            Compiler.errorExit("Expeceted boolean expression in if condition.", ifStatement.getLine());
        }
        ifStatement.getOperation().accept(this);
        if (ifStatement.hasElse()) {
            ifStatement.getElseOperation().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(While whileStatement) {
        if (whileStatement.getCondition() != null) {
            whileStatement.getCondition().accept(this);
        }
        if (!TypingRules.evaluate_type(whileStatement.getCondition()).equals("boolean")) {
            Compiler.errorExit("Expeceted boolean expression in while condition.", whileStatement.getLine());
        }
        whileStatement.getOperation().accept(this);
        return null;
    }

    @Override
    public Object visit(Break breakStatement) {
        return null;
    }

    @Override
    public Object visit(Continue continueStatement) {
        return null;
    }

    @Override
    public Object visit(StatementsBlock statementsBlock) {
        for (Statement s : statementsBlock.getStatements()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(LocalVariable localVariable) {
        if (UserType.class.equals(localVariable.getType().getClass())) {
            SymTableCreator.findClassInTree(localVariable.getType().getName(), localVariable.getType().getLine());
        }
        if (localVariable.hasInitValue()) {
            localVariable.getInitValue().accept(this);
            String init_type = TypingRules.evaluate_type(localVariable.getInitValue());
            String var_type = TypingRules.get_Type(localVariable.getName(), localVariable.getEnclosing_scope(), Kind.FIELD, localVariable.getLine());
            if (!TypingRules.checkIfExtends(var_type, init_type)) {
                Compiler.errorExit("Type mismatch in assignment for variable \"" + localVariable.getName() + "\".", localVariable.getLine());
            }
        }
        return null;
    }

    @Override
    public Object visit(VariableLocation location) {
        if (location.isExternal()) {
            location.getLocation().accept(this);
        } else if (!this.declared(location.getName(), location.getEnclosing_scope(), location.getLine())) {
            Compiler.errorExit("Undeclared variable \"" + location.getName() + "\".", location.getLine());
        }
        return null;
    }

    private boolean declared(String name, SymTable scope, int line) {
        while (!ClassTable.class.equals(scope.getClass())) {
            SymEntry sym2 = scope.getSym(name, Kind.FIELD);
            if (sym2 != null) {
                if (sym2.getNode().getLine() >= line) {
                    scope = scope.getParent();
                    while (!ProgramTable.class.equals(scope.getClass())) {
                        sym2 = scope.getSym(name, Kind.FIELD);
                        if (sym2 != null) {
                            return true;
                        }
                        scope = scope.getParent();
                    }
                    return false;
                }
                return true;
            }
            scope = scope.getParent();
        }
        SymTableCreator.findSym(name, scope, Kind.FIELD, line);
        return true;
    }

    @Override
    public Object visit(ArrayLocation location) {
        if (location.getIndex() != null) {
            location.getIndex().accept(this);
        }
        if (location.getArray() != null) {
            location.getArray().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(StaticCall call) {
        TypingRules.evaluate_type(call);
        return null;
    }

    @Override
    public Object visit(VirtualCall call) {
        TypingRules.evaluate_type(call);
        return null;
    }

    @Override
    public Object visit(This thisExpression) {
        return null;
    }

    @Override
    public Object visit(NewClass newClass) {
        return null;
    }

    @Override
    public Object visit(NewArray newArray) {
        if (newArray.getSize() != null) {
            newArray.getSize().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Length length) {
        if (length.getArray() != null) {
            length.getArray().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(MathBinaryOp binaryOp) {
        if (binaryOp.getFirstOperand() != null) {
            binaryOp.getFirstOperand().accept(this);
        }
        if (binaryOp.getSecondOperand() != null) {
            binaryOp.getSecondOperand().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(LogicalBinaryOp binaryOp) {
        if (binaryOp.getFirstOperand() != null) {
            binaryOp.getFirstOperand().accept(this);
        }
        if (binaryOp.getSecondOperand() != null) {
            binaryOp.getSecondOperand().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(MathUnaryOp unaryOp) {
        if (unaryOp.getOperand() != null) {
            unaryOp.getOperand().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(LogicalUnaryOp unaryOp) {
        if (unaryOp.getOperand() != null) {
            unaryOp.getOperand().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Literal literal) {
        return null;
    }

    @Override
    public Object visit(ExpressionBlock expressionBlock) {
        if (expressionBlock.getExpression() != null) {
            expressionBlock.getExpression().accept(this);
        }
        return null;
    }
}

