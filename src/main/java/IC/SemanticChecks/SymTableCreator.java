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
import IC.SemanticChecks.SymTables.BlockTable;
import IC.SemanticChecks.SymTables.ClassTable;
import IC.SemanticChecks.SymTables.MethodTable;
import IC.SemanticChecks.SymTables.ProgramTable;
import IC.SemanticChecks.SymTables.SymTable;
import IC.SemanticChecks.SymTables.Symbols.MethodSym;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import IC.TypeTable.TypeTable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SymTableCreator
implements Visitor {
    public static ProgramTable GlobalSymTable;
    private String filePath;
    public SymTable parent;
    public static int in_while;
    public static boolean lib_mode;
    private static boolean main;

    static {
        in_while = 0;
        lib_mode = false;
        main = false;
    }

    public SymTableCreator(String path) {
        this.filePath = path;
    }

    public ProgramTable createSymTables(Program library_program, Program main_program) {
        ProgramTable program = null;
        if (library_program != null) {
            lib_mode = true;
            GlobalSymTable = program = (ProgramTable)library_program.accept(this);
            lib_mode = false;
        }
        program = (ProgramTable)main_program.accept(this);
        return program;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static SymEntry findSym(String id, SymTable table, Kind kind, int line) {
        if (table == null || ProgramTable.class.equals(table.getClass())) {
            if (line < 0) {
                return null;
            }
            Compiler.errorExit("Undeclared symbol \"" + id + "\".", line);
        }
        if (Kind.METHOD.equals((Object)kind)) {
            while (!ClassTable.class.equals(table.getClass())) {
                table = table.getParent();
            }
            SymEntry res = table.getSym(id, kind);
            if (res != null) {
                return res;
            }
            if (ClassTable.class.equals(table.getParent().getClass())) {
                return SymTableCreator.findSym(id, table.getParent(), kind, line);
            }
        } else if (Kind.FIELD.equals((Object)kind)) {
            SymEntry res = table.getSym(id, kind);
            if (res == null) {
                if (table.getParent() != null) {
                    if (!MethodTable.class.equals(table.getClass())) {
                        return SymTableCreator.findSym(id, table.getParent(), kind, line);
                    }
                    ClassTable parent = (ClassTable)table.getParent();
                    MethodSym method = (MethodSym)parent.getSym(table.getID(), Kind.METHOD);
                    if (method == null || !method.isStatic_method()) return SymTableCreator.findSym(id, table.getParent(), kind, line);
                    Compiler.errorExit("Undeclared symbol \"" + id + "\".", line);
                }
            } else {
                if (ClassTable.class.equals(table.getClass()) || res.getNode().getLine() <= line) return res;
                return SymTableCreator.findSym(id, table.getParent(), kind, line);
            }
        }
        if (line <= -1) return null;
        Compiler.errorExit("Undeclared symbol \"" + id + "\".", line);
        return null;
    }

    public static void method_visit(Visitor visit, Method method, MethodTable table) {
        SymTableCreator.checkForMain(method);
        SymTableCreator sem = (SymTableCreator)visit;
        sem.parent = table;
        for (Formal formal : method.getFormals()) {
            formal.accept(visit);
            table.addParameter(formal);
        }
        for (Statement statement : method.getStatements()) {
            sem.parent = table;
            if (LocalVariable.class.equals(statement.getClass())) {
                LocalVariable lcl = (LocalVariable)statement;
                lcl.accept(visit);
                table.addLocalVariable(lcl);
                continue;
            }
            if (StatementsBlock.class.equals(statement.getClass())) {
                table.addChild((SymTable)statement.accept(visit));
                continue;
            }
            if (While.class.equals(statement.getClass())) {
                SymTableCreator.cascadingWhile(table, (While)statement, visit);
                sem.parent = table;
                statement.accept(visit);
                continue;
            }
            if (If.class.equals(statement.getClass())) {
                SymTableCreator.cascadingIfs(table, (If)statement, visit);
                sem.parent = table;
                statement.accept(sem);
                continue;
            }
            statement.accept(visit);
        }
    }

    public static void cascadingIfs(SymTable table, If if_stmt, Visitor visit) {
        LocalVariable lcl;
        Statement statement;
        SymTableCreator sem = (SymTableCreator)visit;
        if (if_stmt.getOperation() != null) {
            statement = if_stmt.getOperation();
            if (If.class.equals(statement.getClass())) {
                SymTableCreator.cascadingIfs(table, (If)statement, visit);
                sem.parent = table;
                statement.accept(visit);
            } else if (StatementsBlock.class.equals(statement.getClass())) {
                table.addChild((SymTable)statement.accept(visit));
            } else if (While.class.equals(statement.getClass())) {
                SymTableCreator.cascadingWhile(table, (While)statement, visit);
                sem.parent = table;
                statement.accept(visit);
            } else if (LocalVariable.class.equals(statement.getClass())) {
                lcl = (LocalVariable)statement;
                lcl.accept(visit);
                table.addSym(lcl);
            } else {
                statement.accept(visit);
            }
        }
        sem.parent = table;
        if (if_stmt.getElseOperation() != null) {
            statement = if_stmt.getElseOperation();
            if (If.class.equals(statement.getClass())) {
                SymTableCreator.cascadingIfs(table, (If)statement, visit);
                sem.parent = table;
                statement.accept(visit);
            } else if (StatementsBlock.class.equals(statement.getClass())) {
                table.addChild((SymTable)statement.accept(visit));
            } else if (While.class.equals(statement.getClass())) {
                SymTableCreator.cascadingWhile(table, (While)statement, visit);
                sem.parent = table;
                statement.accept(visit);
            } else if (LocalVariable.class.equals(statement.getClass())) {
                lcl = (LocalVariable)statement;
                lcl.accept(visit);
                table.addSym(lcl);
            } else {
                statement.accept(visit);
            }
        }
    }

    public static void cascadingWhile(SymTable table, While while_stmt, Visitor visit) {
        ++in_while;
        SymTableCreator sem = (SymTableCreator)visit;
        if (while_stmt.getOperation() != null) {
            Statement statement = while_stmt.getOperation();
            if (If.class.equals(statement.getClass())) {
                SymTableCreator.cascadingIfs(table, (If)statement, visit);
                sem.parent = table;
                statement.accept(visit);
            } else if (StatementsBlock.class.equals(statement.getClass())) {
                table.addChild((SymTable)statement.accept(visit));
            } else if (While.class.equals(statement.getClass())) {
                SymTableCreator.cascadingWhile(table, (While)statement, visit);
                sem.parent = table;
                statement.accept(visit);
            } else if (Break.class.equals(statement.getClass())) {
                statement.setEnclosing_scope(table);
            } else if (LocalVariable.class.equals(statement.getClass())) {
                LocalVariable lcl = (LocalVariable)statement;
                lcl.accept(visit);
                table.addSym(lcl);
            } else {
                statement.accept(visit);
            }
        }
        --in_while;
    }

    public static void checkForMain(Method method) {
        if (!"main".equals(method.getName())) {
            return;
        }
        if (main) {
            Compiler.errorExit("Duplicate \"main\" method declaration.", method.getLine());
        } else if (!StaticMethod.class.equals(method.getClass())) {
            Compiler.errorExit("Illeagal \"main\" method declaration: Main method shuold be static.", method.getLine());
        } else if (!"void".equals(method.getType().getName())) {
            Compiler.errorExit("Illeagal \"main\" method declaration: Wrong return type \"" + method.getType().getName() + "\".", method.getLine());
        } else if (method.getFormals().size() != 1) {
            Compiler.errorExit("Illeagal \"main\" method declaration: Wrong number of arguments given.", method.getLine());
        }
        Formal arg = method.getFormals().get(0);
        if (!"string".equals(arg.getType().getName())) {
            Compiler.errorExit("Illeagal \"main\" method declaration: Wrong argument type \"" + arg.getType().getName() + "\".\nArgument should be of type \"string\".", method.getLine());
        } else if (arg.getType().getDimension() != 1) {
            Compiler.errorExit("Illeagal \"main\" method declaration: Wrong argument given, should be an array.", method.getLine());
        } else if (!"args".equals(arg.getName())) {
            Compiler.errorExit("Illeagal \"main\" method declaration: Wrong argument name \"" + arg.getName() + "\".", method.getLine());
        }
        main = true;
    }

    @Override
    public Object visit(Program program) {
        if (GlobalSymTable == null) {
            GlobalSymTable = new ProgramTable(this.filePath);
        }
        for (ICClass icClass : program.getClasses()) {
            GlobalSymTable.addSym(icClass);
            this.parent = GlobalSymTable;
            ClassTable table = (ClassTable)icClass.accept(this);
            if (icClass.hasSuperClass()) {
                if (icClass.getName().equals(icClass.getSuperClassName())) {
                    Compiler.errorExit("Class cannot extend itself.", icClass.getLine());
                }
                String sup = icClass.getSuperClassName();
                table.setExtend(sup);
                ClassTable super_class = SymTableCreator.findClassInTree(icClass.getSuperClassName(), icClass.getLine());
                if (super_class != null) {
                    super_class.addChild(table);
                    table.setParent(super_class);
                    icClass.setEnclosing_scope(super_class);
                }
            } else {
                GlobalSymTable.addChild(table);
            }
            TypeTable.addToClass(icClass.getName(), icClass.getSuperClassName());
        }
        if (!lib_mode && !main) {
            Compiler.errorExit("No \"main\" method declared.", 0);
        }
        return GlobalSymTable;
    }

    public static ClassTable findClassInTree(ICClass icc, Map<String, SymTable> tables) {
        for (Map.Entry<String, SymTable> table : tables.entrySet()) {
            if (!ClassTable.class.equals(table.getValue().getClass())) continue;
            ClassTable class_t = (ClassTable)table.getValue();
            if (icc.getName().equals(class_t.getExtend()) || class_t.getExtend() != null && class_t.getExtend().equals(icc.getSuperClassName())) continue;
            if (table.getValue().getID().equals(icc.getSuperClassName())) {
                return class_t;
            }
            ClassTable ret = SymTableCreator.findClassInTree(icc, class_t.getChildren());
            if (ret == null) continue;
            return ret;
        }
        return null;
    }

    public static ClassTable findClassInTree(String icc_name, int line) {
        ClassTable res = SymTableCreator.findClassInTree(new ICClass(0, icc_name, icc_name, null, null), GlobalSymTable.getChildren());
        if (res == null) {
            Compiler.errorExit("Undeclared class \"" + icc_name + "\"", line);
        }
        return res;
    }

    @Override
    public Object visit(ICClass icClass) {
        icClass.setEnclosing_scope(this.parent);
        ClassTable ClassSymTable = new ClassTable(icClass.getName(), this.parent);
        for (Field field : icClass.getFields()) {
            this.parent = ClassSymTable;
            ClassSymTable.addSym(field);
            ClassSymTable.addChild((SymTable)field.accept(this));
        }
        for (Method method : icClass.getMethods()) {
            ClassSymTable.addSym(method);
            this.parent = ClassSymTable;
            ClassSymTable.addChild((SymTable)method.accept(this));
        }
        return ClassSymTable;
    }

    @Override
    public Object visit(Field field) {
        field.setEnclosing_scope(this.parent);
        return null;
    }

    @Override
    public Object visit(VirtualMethod method) {
        method.setEnclosing_scope(this.parent);
        MethodTable table = new MethodTable(method.getName(), this.parent);
        SymTableCreator.method_visit(this, method, table);
        return table;
    }

    @Override
    public Object visit(StaticMethod method) {
        method.setEnclosing_scope(this.parent);
        MethodTable table = new MethodTable(method.getName(), this.parent);
        SymTableCreator.method_visit(this, method, table);
        return table;
    }

    @Override
    public Object visit(LibraryMethod method) {
        method.setEnclosing_scope(this.parent);
        MethodTable table = new MethodTable(method.getName(), this.parent);
        SymTableCreator.method_visit(this, method, table);
        return table;
    }

    @Override
    public Object visit(Formal formal) {
        formal.setEnclosing_scope(this.parent);
        return null;
    }

    @Override
    public Object visit(PrimitiveType type) {
        type.setEnclosing_scope(this.parent);
        return null;
    }

    @Override
    public Object visit(UserType type) {
        type.setEnclosing_scope(this.parent);
        return null;
    }

    @Override
    public Object visit(Assignment assignment) {
        assignment.setEnclosing_scope(this.parent);
        if (assignment.getAssignment() != null) {
            assignment.getAssignment().accept(this);
        }
        if (assignment.getVariable() != null) {
            assignment.getVariable().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(CallStatement callStatement) {
        callStatement.setEnclosing_scope(this.parent);
        if (callStatement.getCall() != null) {
            callStatement.getCall().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Return returnStatement) {
        returnStatement.setEnclosing_scope(this.parent);
        if (returnStatement.getValue() != null) {
            returnStatement.getValue().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(If ifStatement) {
        SymTable sup = this.parent;
        ifStatement.setEnclosing_scope(sup);
        if (ifStatement.getCondition() != null) {
            ifStatement.getCondition().accept(this);
        }
        this.parent = sup;
        if (ifStatement.getOperation() != null) {
            ifStatement.getOperation().accept(this);
        }
        this.parent = sup;
        if (ifStatement.getElseOperation() != null) {
            ifStatement.getElseOperation().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(While whileStatement) {
        whileStatement.setEnclosing_scope(this.parent);
        if (whileStatement.getCondition() != null) {
            whileStatement.getCondition().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Break breakStatement) {
        if (in_while == 0) {
            Compiler.errorExit("Break statement outside on while.", breakStatement.getLine());
        }
        breakStatement.setEnclosing_scope(this.parent);
        return null;
    }

    @Override
    public Object visit(Continue continueStatement) {
        if (in_while == 0) {
            Compiler.errorExit("Continue statement outside on while.", continueStatement.getLine());
        }
        continueStatement.setEnclosing_scope(this.parent);
        return null;
    }

    @Override
    public Object visit(StatementsBlock statementsBlock) {
        statementsBlock.setEnclosing_scope(this.parent);
        BlockTable table = new BlockTable(String.valueOf(statementsBlock.getLine()), this.parent);
        for (Statement statement : statementsBlock.getStatements()) {
            this.parent = table;
            if (LocalVariable.class.equals(statement.getClass())) {
                table.addSym((LocalVariable)statement);
                statement.accept(this);
                continue;
            }
            if (StatementsBlock.class.equals(statement.getClass())) {
                table.addChild((SymTable)statement.accept(this));
                continue;
            }
            if (While.class.equals(statement.getClass())) {
                SymTableCreator.cascadingWhile(table, (While)statement, this);
                statement.accept(this);
                continue;
            }
            if (If.class.equals(statement.getClass())) {
                SymTableCreator.cascadingIfs(table, (If)statement, this);
                statement.accept(this);
                continue;
            }
            statement.accept(this);
        }
        return table;
    }

    @Override
    public Object visit(LocalVariable localVariable) {
        localVariable.setEnclosing_scope(this.parent);
        if (localVariable.hasInitValue()) {
            localVariable.getInitValue().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(VariableLocation location) {
        location.setEnclosing_scope(this.parent);
        if (location.getLocation() != null) {
            location.getLocation().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ArrayLocation location) {
        location.setEnclosing_scope(this.parent);
        if (location.getArray() != null) {
            location.getArray().accept(this);
        }
        if (location.getIndex() != null) {
            location.getIndex().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(StaticCall call) {
        call.setEnclosing_scope(this.parent);
        for (Expression exp : call.getArguments()) {
            exp.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(VirtualCall call) {
        call.setEnclosing_scope(this.parent);
        if (call.getLocation() != null) {
            call.getLocation().accept(this);
        }
        for (Expression node : call.getArguments()) {
            node.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(This thisExpression) {
        SymTable method = this.parent;
        while (!MethodTable.class.equals(method.getClass())) {
            method = method.getParent();
        }
        MethodSym sym2 = (MethodSym)method.getParent().getSym(method.getID(), Kind.METHOD);
        if (sym2.isStatic_method()) {
            Compiler.errorExit("Cannot contain \"this\" reference in a static method.", thisExpression.getLine());
        }
        thisExpression.setEnclosing_scope(this.parent);
        return null;
    }

    @Override
    public Object visit(NewClass newClass) {
        newClass.setEnclosing_scope(this.parent);
        return null;
    }

    @Override
    public Object visit(NewArray newArray) {
        newArray.setEnclosing_scope(this.parent);
        TypeTable.addVariable(newArray.getType());
        if (newArray.getSize() != null) {
            newArray.getSize().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Length length) {
        length.setEnclosing_scope(this.parent);
        if (length.getArray() != null) {
            length.getArray().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(MathBinaryOp binaryOp) {
        binaryOp.setEnclosing_scope(this.parent);
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
        binaryOp.setEnclosing_scope(this.parent);
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
        unaryOp.setEnclosing_scope(this.parent);
        if (unaryOp.getOperand() != null) {
            unaryOp.getOperand().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(LogicalUnaryOp unaryOp) {
        unaryOp.setEnclosing_scope(this.parent);
        if (unaryOp.getOperand() != null) {
            unaryOp.getOperand().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Literal literal) {
        literal.setEnclosing_scope(this.parent);
        return null;
    }

    @Override
    public Object visit(ExpressionBlock expressionBlock) {
        expressionBlock.setEnclosing_scope(this.parent);
        if (expressionBlock.getExpression() != null) {
            expressionBlock.getExpression().accept(this);
        }
        return null;
    }
}

