/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

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
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;
import IC.BinaryOps;
import IC.LiteralTypes;
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTableCreator;
import IC.SemanticChecks.SymTables.SymTable;
import IC.SemanticChecks.TypingRules;
import IC.UnaryOps;
import java.util.List;

public class PrettyPrinter
implements Visitor {
    private int depth = 0;
    private String ICFilePath;

    public PrettyPrinter(String ICFilePath) {
        this.ICFilePath = ICFilePath;
    }

    private void indent(StringBuffer output, ASTNode node) {
        output.append("\n");
        int i = 0;
        while (i < this.depth) {
            output.append("  ");
            ++i;
        }
        if (node != null) {
            output.append(String.valueOf(node.getLine()) + ": ");
        }
    }

    private void indent(StringBuffer output) {
        this.indent(output, null);
    }

    @Override
    public Object visit(Program program) {
        StringBuffer output = new StringBuffer();
        this.indent(output);
        output.append("Abstract Syntax Tree: " + this.ICFilePath + "\n");
        for (ICClass icClass : program.getClasses()) {
            output.append(icClass.accept(this));
        }
        return output.toString();
    }

    @Override
    public Object visit(ICClass icClass) {
        StringBuffer output = new StringBuffer();
        this.indent(output, icClass);
        output.append("Declaration of class: " + icClass.getName());
        if (icClass.hasSuperClass()) {
            output.append(", subclass of " + icClass.getSuperClassName());
        }
        output.append(", Type: " + icClass.getName());
        String symtable = icClass.getEnclosing_scope().getID();
        if (symtable.equals(this.ICFilePath)) {
            symtable = "Global";
        }
        output.append(", Symbol table: " + symtable);
        this.depth += 2;
        for (Field field : icClass.getFields()) {
            output.append(field.accept(this));
        }
        for (Method method : icClass.getMethods()) {
            output.append(method.accept(this));
        }
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(PrimitiveType type) {
        StringBuffer output = new StringBuffer();
        this.indent(output, type);
        output.append("Primitive data type: ");
        if (type.getDimension() > 0) {
            output.append(String.valueOf(type.getDimension()) + "-dimensional array of ");
        }
        output.append(type.getName());
        return output.toString();
    }

    @Override
    public Object visit(UserType type) {
        StringBuffer output = new StringBuffer();
        this.indent(output, type);
        output.append("User-defined data type: ");
        if (type.getDimension() > 0) {
            output.append(String.valueOf(type.getDimension()) + "-dimensional array of ");
        }
        output.append(type.getName());
        return output.toString();
    }

    @Override
    public Object visit(Field field) {
        StringBuffer output = new StringBuffer();
        this.indent(output, field);
        output.append("Declaration of field: " + field.getName());
        output.append(", Type: " + TypingRules.get_Type(field.getName(), field.getEnclosing_scope(), Kind.FIELD, -1));
        output.append(", Symbol table: " + field.getEnclosing_scope().getID());
        return output.toString();
    }

    @Override
    public Object visit(LibraryMethod method) {
        StringBuffer output = new StringBuffer();
        this.indent(output, method);
        output.append("Declaration of library method: " + method.getName());
        output.append(", Type: " + TypingRules.get_Type(method.getName(), method.getEnclosing_scope(), Kind.METHOD, -1));
        output.append(", Symbol table: " + method.getEnclosing_scope().getID());
        this.depth += 2;
        for (Formal formal : method.getFormals()) {
            output.append(formal.accept(this));
        }
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(Formal formal) {
        StringBuffer output = new StringBuffer();
        this.indent(output, formal);
        output.append("Parameter: " + formal.getName());
        output.append(", Type: " + TypingRules.get_Type(formal.getName(), formal.getEnclosing_scope(), Kind.FIELD, -1));
        output.append(", Symbol table: " + formal.getEnclosing_scope().getID());
        return output.toString();
    }

    @Override
    public Object visit(VirtualMethod method) {
        StringBuffer output = new StringBuffer();
        this.indent(output, method);
        output.append("Declaration of virtual method: " + method.getName());
        output.append(", Type: " + TypingRules.get_Type(method.getName(), method.getEnclosing_scope(), Kind.METHOD, -1));
        output.append(", Symbol table: " + method.getEnclosing_scope().getID());
        this.depth += 2;
        for (Formal formal : method.getFormals()) {
            output.append(formal.accept(this));
        }
        for (Statement statement : method.getStatements()) {
            output.append(statement.accept(this));
        }
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(StaticMethod method) {
        StringBuffer output = new StringBuffer();
        this.indent(output, method);
        output.append("Declaration of static method: " + method.getName());
        output.append(", Type: " + TypingRules.get_Type(method.getName(), method.getEnclosing_scope(), Kind.METHOD, -1));
        output.append(", Symbol table: " + method.getEnclosing_scope().getID());
        this.depth += 2;
        for (Formal formal : method.getFormals()) {
            output.append(formal.accept(this));
        }
        for (Statement statement : method.getStatements()) {
            output.append(statement.accept(this));
        }
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(Assignment assignment) {
        StringBuffer output = new StringBuffer();
        this.indent(output, assignment);
        output.append("Assignment statement");
        output.append(", Symbol table: " + assignment.getEnclosing_scope().getID());
        this.depth += 2;
        output.append(assignment.getVariable().accept(this));
        output.append(assignment.getAssignment().accept(this));
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(CallStatement callStatement) {
        StringBuffer output = new StringBuffer();
        this.indent(output, callStatement);
        output.append("Method call statement");
        output.append(", Symbol table: " + callStatement.getEnclosing_scope().getID());
        ++this.depth;
        output.append(callStatement.getCall().accept(this));
        --this.depth;
        return output.toString();
    }

    @Override
    public Object visit(Return returnStatement) {
        StringBuffer output = new StringBuffer();
        this.indent(output, returnStatement);
        output.append("Return statement");
        if (returnStatement.hasValue()) {
            output.append(", with return value");
        }
        output.append(", Symbol table: " + returnStatement.getEnclosing_scope().getID());
        if (returnStatement.hasValue()) {
            ++this.depth;
            ++this.depth;
            output.append(returnStatement.getValue().accept(this));
            --this.depth;
            --this.depth;
        }
        return output.toString();
    }

    @Override
    public Object visit(If ifStatement) {
        StringBuffer output = new StringBuffer();
        this.indent(output, ifStatement);
        output.append("If statement");
        if (ifStatement.hasElse()) {
            output.append(", with Else operation");
        }
        output.append(", Symbol table: " + ifStatement.getEnclosing_scope().getID());
        this.depth += 2;
        output.append(ifStatement.getCondition().accept(this));
        output.append(ifStatement.getOperation().accept(this));
        if (ifStatement.hasElse()) {
            output.append(ifStatement.getElseOperation().accept(this));
        }
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(While whileStatement) {
        StringBuffer output = new StringBuffer();
        this.indent(output, whileStatement);
        output.append("While statement");
        output.append(", Symbol table: " + whileStatement.getEnclosing_scope().getID());
        this.depth += 2;
        output.append(whileStatement.getCondition().accept(this));
        output.append(whileStatement.getOperation().accept(this));
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(Break breakStatement) {
        StringBuffer output = new StringBuffer();
        this.indent(output, breakStatement);
        output.append("Break statement");
        output.append(", Symbol table: " + breakStatement.getEnclosing_scope().getID());
        return output.toString();
    }

    @Override
    public Object visit(Continue continueStatement) {
        StringBuffer output = new StringBuffer();
        this.indent(output, continueStatement);
        output.append("Continue statement");
        output.append(", Symbol table: " + continueStatement.getEnclosing_scope().getID());
        return output.toString();
    }

    @Override
    public Object visit(StatementsBlock statementsBlock) {
        StringBuffer output = new StringBuffer();
        this.indent(output, statementsBlock);
        output.append("Block of statements");
        output.append(", Symbol table: " + statementsBlock.getEnclosing_scope().getID());
        this.depth += 2;
        for (Statement statement : statementsBlock.getStatements()) {
            output.append(statement.accept(this));
        }
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(LocalVariable localVariable) {
        StringBuffer output = new StringBuffer();
        this.indent(output, localVariable);
        output.append("Declaration of local variable: " + localVariable.getName());
        if (localVariable.hasInitValue()) {
            output.append(", with initial value");
            ++this.depth;
        }
        ++this.depth;
        output.append(", Type: " + TypingRules.get_Type(localVariable.getName(), localVariable.getEnclosing_scope(), Kind.FIELD, -1));
        output.append(", Symbol table: " + localVariable.getEnclosing_scope().getID());
        if (localVariable.hasInitValue()) {
            output.append(localVariable.getInitValue().accept(this));
            --this.depth;
        }
        --this.depth;
        return output.toString();
    }

    @Override
    public Object visit(VariableLocation location) {
        StringBuffer output = new StringBuffer();
        this.indent(output, location);
        output.append("Reference to variable: " + location.getName());
        SymTable Class_Table = location.getEnclosing_scope();
        if (location.isExternal()) {
            output.append(", in external scope");
            String Class_Name = TypingRules.evaluate_type(location.getLocation());
            Class_Table = SymTableCreator.findClassInTree(Class_Name, location.getLine());
        }
        output.append(", Type: " + TypingRules.get_Type(location.getName(), Class_Table, Kind.FIELD, -1));
        output.append(", Symbol table: " + location.getEnclosing_scope().getID());
        if (location.isExternal()) {
            ++this.depth;
            ++this.depth;
            output.append(location.getLocation().accept(this));
            --this.depth;
            --this.depth;
        }
        return output.toString();
    }

    @Override
    public Object visit(ArrayLocation location) {
        StringBuffer output = new StringBuffer();
        this.indent(output, location);
        output.append("Reference to array");
        output.append(", Type: " + TypingRules.evaluate_type(location));
        output.append(", Symbol table: " + location.getEnclosing_scope().getID());
        this.depth += 2;
        output.append(location.getArray().accept(this));
        output.append(location.getIndex().accept(this));
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(StaticCall call) {
        StringBuffer output = new StringBuffer();
        this.indent(output, call);
        output.append("Call to static method: " + call.getName() + ", in class " + call.getClassName());
        output.append(", Symbol table: " + call.getEnclosing_scope().getID());
        this.depth += 2;
        for (Expression argument : call.getArguments()) {
            output.append(argument.accept(this));
        }
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(VirtualCall call) {
        StringBuffer output = new StringBuffer();
        this.indent(output, call);
        output.append("Call to virtual method: " + call.getName());
        if (call.isExternal()) {
            output.append(", in external scope");
        }
        output.append(", Symbol table: " + call.getEnclosing_scope().getID());
        this.depth += 2;
        if (call.isExternal()) {
            output.append(call.getLocation().accept(this));
        }
        for (Expression argument : call.getArguments()) {
            output.append(argument.accept(this));
        }
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(This thisExpression) {
        StringBuffer output = new StringBuffer();
        this.indent(output, thisExpression);
        output.append("Reference to 'this' instance");
        output.append(", Symbol table: " + thisExpression.getEnclosing_scope().getID());
        return output.toString();
    }

    @Override
    public Object visit(NewClass newClass) {
        StringBuffer output = new StringBuffer();
        this.indent(output, newClass);
        output.append("Instantiation of class: " + newClass.getName());
        output.append(", Type: " + newClass.getName());
        output.append(", Symbol table: " + newClass.getEnclosing_scope().getID());
        return output.toString();
    }

    @Override
    public Object visit(NewArray newArray) {
        StringBuffer output = new StringBuffer();
        this.indent(output, newArray);
        output.append("Array allocation");
        output.append(", Type: " + TypingRules.evaluate_type(newArray));
        output.append(", Symbol table: " + newArray.getEnclosing_scope().getID());
        this.depth += 2;
        output.append(newArray.getSize().accept(this));
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(Length length) {
        StringBuffer output = new StringBuffer();
        this.indent(output, length);
        output.append("Reference to array length");
        output.append(", Symbol table: " + length.getEnclosing_scope().getID());
        ++this.depth;
        output.append(length.getArray().accept(this));
        --this.depth;
        return output.toString();
    }

    @Override
    public Object visit(MathBinaryOp binaryOp) {
        StringBuffer output = new StringBuffer();
        this.indent(output, binaryOp);
        output.append("Mathematical binary operation: " + binaryOp.getOperator().getDescription());
        output.append(", Type: " + TypingRules.evaluate_type(binaryOp));
        output.append(", Symbol table: " + binaryOp.getEnclosing_scope().getID());
        this.depth += 2;
        output.append(binaryOp.getFirstOperand().accept(this));
        output.append(binaryOp.getSecondOperand().accept(this));
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(LogicalBinaryOp binaryOp) {
        StringBuffer output = new StringBuffer();
        this.indent(output, binaryOp);
        output.append("Logical binary operation: " + binaryOp.getOperator().getDescription());
        output.append(", Type: " + TypingRules.evaluate_type(binaryOp));
        output.append(", Symbol table: " + binaryOp.getEnclosing_scope().getID());
        this.depth += 2;
        output.append(binaryOp.getFirstOperand().accept(this));
        output.append(binaryOp.getSecondOperand().accept(this));
        this.depth -= 2;
        return output.toString();
    }

    @Override
    public Object visit(MathUnaryOp unaryOp) {
        StringBuffer output = new StringBuffer();
        this.indent(output, unaryOp);
        output.append("Mathematical unary operation: " + unaryOp.getOperator().getDescription());
        output.append(", Type: " + TypingRules.evaluate_type(unaryOp));
        output.append(", Symbol table: " + unaryOp.getEnclosing_scope().getID());
        ++this.depth;
        output.append(unaryOp.getOperand().accept(this));
        --this.depth;
        return output.toString();
    }

    @Override
    public Object visit(LogicalUnaryOp unaryOp) {
        StringBuffer output = new StringBuffer();
        this.indent(output, unaryOp);
        output.append("Logical unary operation: " + unaryOp.getOperator().getDescription());
        output.append(", Type: " + TypingRules.evaluate_type(unaryOp));
        output.append(", Symbol table: " + unaryOp.getEnclosing_scope().getID());
        ++this.depth;
        output.append(unaryOp.getOperand().accept(this));
        --this.depth;
        return output.toString();
    }

    @Override
    public Object visit(Literal literal) {
        StringBuffer output = new StringBuffer();
        this.indent(output, literal);
        output.append(String.valueOf(literal.getType().getDescription()) + ": " + literal.getType().toFormattedString(literal.getValue()));
        output.append(", Type: " + TypingRules.evaluate_type(literal));
        output.append(", Symbol table: " + literal.getEnclosing_scope().getID());
        return output.toString();
    }

    @Override
    public Object visit(ExpressionBlock expressionBlock) {
        StringBuffer output = new StringBuffer();
        this.indent(output, expressionBlock);
        output.append("Parenthesized expression");
        output.append(", Type: " + TypingRules.evaluate_type(expressionBlock));
        output.append(", Symbol table: " + expressionBlock.getEnclosing_scope().getID());
        ++this.depth;
        output.append(expressionBlock.getExpression().accept(this));
        --this.depth;
        return output.toString();
    }
}

