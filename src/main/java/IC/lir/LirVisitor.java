package IC.lir;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import IC.BinaryOps;
import IC.LiteralTypes;
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
import IC.SemanticChecks.Kind;
import IC.SemanticChecks.SymTableCreator;
import IC.SemanticChecks.TypingRules;
import IC.SemanticChecks.SymTables.ClassTable;
import IC.SemanticChecks.SymTables.MethodTable;
import IC.SemanticChecks.SymTables.ProgramTable;
import IC.SemanticChecks.SymTables.SymTable;
import IC.SemanticChecks.SymTables.Symbols.FieldSym;
import IC.SemanticChecks.SymTables.Symbols.MethodSym;
import IC.SemanticChecks.SymTables.Symbols.SymEntry;
import IC.lir.Instructions.ArrayLengthInstr;
import IC.lir.Instructions.BinOpInstr;
import IC.lir.Instructions.CompareInstr;
import IC.lir.Instructions.Cond;
import IC.lir.Instructions.CondJumpInstr;
import IC.lir.Instructions.Immediate;
import IC.lir.Instructions.Instruction;
import IC.lir.Instructions.JumpInstr;
import IC.lir.Instructions.Label;
import IC.lir.Instructions.LabelInstr;
import IC.lir.Instructions.LibraryCall;
import IC.lir.Instructions.Memory;
import IC.lir.Instructions.MoveArrayInstr;
import IC.lir.Instructions.MoveFieldInstr;
import IC.lir.Instructions.MoveInstr;
import IC.lir.Instructions.Operand;
import IC.lir.Instructions.Operator;
import IC.lir.Instructions.ParamOpPair;
import IC.lir.Instructions.Reg;
import IC.lir.Instructions.ReturnInstr;
import IC.lir.Instructions.UnaryOpInstr;

public class LirVisitor implements Visitor {

    List<Instruction> lirInstructionList = new ArrayList<Instruction>();
    private Map<String, LirClassLayout> classes = new LinkedHashMap<String, LirClassLayout>();
    private Map<String, String> str_literals = new LinkedHashMap<String, String>();
    private ProgramTable program_table;
    private int regCount = 1;
    private int lableCount = 0;
    private int currWhile = -1;
    boolean expect_mem = false;

    public void printLirCode(FileWriter fw) throws IOException {
        printLiteralsAndDispatch(fw);

        System.out.println();

        for (Instruction instr : lirInstructionList) {
            // Print new line before func label
            if (instr.getClass().equals(IC.lir.Instructions.LabelInstr.class)) {
                fw.write("\n");
            }
            fw.write(instr.toString() + "\n");
        }
    }

    public void printLiteralsAndDispatch(FileWriter fw) throws IOException {
        for (Map.Entry<String, String> lable : this.str_literals.entrySet()) {
            fw.write(lable.getValue() + ": \"" + lable.getKey() + "\"\n");
        }

        fw.write("\n");

        for (Map.Entry<String, LirClassLayout> class_layout : classes.entrySet()) {
            if (class_layout.getKey().equals("Library"))
                continue;
            class_layout.getValue().printLayout(fw);
        }

        fw.write("\n");
        fw.write("########################\n");
        fw.write("## Class Fields Offsets\n");

        for (Map.Entry<String, LirClassLayout> class_layout : classes.entrySet()) {
            if (class_layout.getKey().equals("Library"))
                continue;
            class_layout.getValue().printFields(fw);
        }
        fw.write("########################\n");
    }

    public LirVisitor(ProgramTable table) {
        this.program_table = table;
        buildDispatchTable(this.program_table);
    }

    private void buildDispatchTable(ProgramTable table) {
        Map<String, SymTable> classes = table.getChildren();

        for (Map.Entry<String, SymTable> entry : classes.entrySet()) {
            if (ClassTable.class.equals(entry.getValue().getClass())) {
                buildDispatchTable((ClassTable) entry.getValue(), null);
            }
        }
    }

    private void buildDispatchTable(ClassTable table, LirClassLayout parent) {
        LirClassLayout layout = new LirClassLayout(table.getID(), table, parent);

        classes.put(table.getID(), layout);

        for (Map.Entry<String, SymTable> entry : table.getChildren().entrySet()) {
            if (ClassTable.class.equals(entry.getValue().getClass())) {
                buildDispatchTable((ClassTable) entry.getValue(), layout);
            }
        }
    }

    public String addStringLiteral(String literal) {
        String lable;

        if (!this.str_literals.containsKey(literal)) {
            lable = "str_" + this.str_literals.size();
            this.str_literals.put(literal, lable);
        } else {
            lable = this.str_literals.get(literal);
        }

        return lable;
    }



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
        return null;
    }

    public void method_visit(Visitor visit, Method method) {
        String className = method.getEnclosing_scope().getID();

        String labelName = "_" + className + "_" + method.getName();
        if (method.getName().equals("main"))
            labelName = "_ic_main";
        lirInstructionList.add(new LabelInstr(new Label(labelName)));

        for (Formal formal : method.getFormals())
            formal.accept(this);

        for (Statement statement : method.getStatements()) {
            statement.accept(this);
        }

        // Add return/exit instruction
        if (labelName.equals("_ic_main")) {
            List<Operand> operList = new LinkedList<Operand>();
            operList.add(new Immediate(0));
            lirInstructionList.add(new LibraryCall(new Label("__exit"), operList, new Reg("Rdummy")));
        } else {
            lirInstructionList.add(new ReturnInstr(new Reg("Rdummy")));
        }
    }

    @Override
    public Object visit(VirtualMethod method) {
        method_visit(this, method);
        return null;
    }

    @Override
    public Object visit(StaticMethod method) {
        method_visit(this, method);
        return null;
    }

    @Override
    public Object visit(LibraryMethod method) {
        method_visit(this, method);
        return null;
    }

    @Override
    public Object visit(Formal formal) {
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
        String asgnmnt = (String) assignment.getAssignment().accept(this);
        expect_mem = true;
        String var = (String) assignment.getVariable().accept(this);
        expect_mem = false;

        if (VariableLocation.class.equals(assignment.getVariable().getClass())) {
            if (!((VariableLocation)assignment.getVariable()).isExternal()) {
                Instruction moveNode;
                if (var.contains(".")) {
                    // It's a class field - need MoveField
                    String[] varSplit = var.split("\\.");
                    moveNode = new MoveFieldInstr(new Reg(varSplit[0]) ,new Immediate(Integer.parseInt(varSplit[1])) , new Reg(asgnmnt), false);
                } else {
                    moveNode = new MoveInstr(new Reg(asgnmnt), new Memory(var));
                }
                lirInstructionList.add(moveNode);
            }
        }

        return null;
    }

    @Override
    public Object visit(CallStatement callStatement) {
        if(callStatement.getCall() != null) {
            return callStatement.getCall().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Return returnStatement) {
        String result;
        if (returnStatement.getValue() != null)
            result = (String) returnStatement.getValue().accept(this);
        else
            result = "Rdummy";
        lirInstructionList.add(new ReturnInstr(new Reg(result)));
        return result;
    }

    @Override
    public Object visit(If ifStatement) {
        if (!ifStatement.hasElse()) {
            Label end_label = new Label("_if_end_" + lableCount);
            lableCount++;
            String condResult = (String) ifStatement.getCondition().accept(this);
            lirInstructionList.add(new CompareInstr(new Immediate(0), new Reg(condResult)));
            lirInstructionList.add(new CondJumpInstr(end_label, Cond.True));
            ifStatement.getOperation().accept(this);
            lirInstructionList.add(new LabelInstr(end_label));
        }
        else {
            Label false_label = new Label("_if_false_" + lableCount);
            Label end_label = new Label("_if_end_" + lableCount);
            lableCount++;
            String condResult = (String) ifStatement.getCondition().accept(this);
            lirInstructionList.add(new CompareInstr(new Immediate(0), new Reg(condResult)));
            lirInstructionList.add(new CondJumpInstr(false_label, Cond.True));
            ifStatement.getOperation().accept(this);
            lirInstructionList.add(new JumpInstr(end_label));
            lirInstructionList.add(new LabelInstr(false_label));
            ifStatement.getElseOperation().accept(this);
            lirInstructionList.add(new LabelInstr(end_label));
        }
        return null;
    }

    @Override
    public Object visit(While whileStatement) {
        int oldWhile = currWhile;
        currWhile = lableCount;
        Label test_label = new Label("_while_condition_" + lableCount);
        Label end_label = new Label("_while_end_" + lableCount);
        lableCount++;

        lirInstructionList.add(new LabelInstr(test_label));

        String condResult = (String) whileStatement.getCondition().accept(this);

        lirInstructionList.add(new CompareInstr(new Immediate(0), new Reg(condResult)));

        lirInstructionList.add(new CondJumpInstr(end_label, Cond.True));

        whileStatement.getOperation().accept(this);

        lirInstructionList.add(new JumpInstr(test_label));

        lirInstructionList.add(new LabelInstr(end_label));

        currWhile = oldWhile;

        return null;
    }

    @Override
    public Object visit(Break breakStatement) {
        lirInstructionList.add(new JumpInstr(new Label("_while_end_" + currWhile)));
        return null;
    }

    @Override
    public Object visit(Continue continueStatement) {
        lirInstructionList.add(new JumpInstr(new Label("_while_condition_" + currWhile)));
        return null;
    }

    @Override
    public Object visit(StatementsBlock statementsBlock) {
        for (Statement statement : statementsBlock.getStatements()) {
            statement.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(LocalVariable localVariable) {
        String var = null;
        if (localVariable.getInitValue() != null) {
            String reg = (String) localVariable.getInitValue().accept(this);
            var = getVarShadow(localVariable);
            Instruction node = new MoveInstr(new Reg(reg), new Memory(var));
            lirInstructionList.add(node);
        }
        return var;
    }

    @Override
    public Object visit(VariableLocation location) {
        if (!location.isExternal()) {
            FieldSym sym = (FieldSym) SymTableCreator.findSym(location.getName(), location.getEnclosing_scope(), Kind.FIELD, location.getLine());
            SymTable parent = sym.getNode().getEnclosing_scope();

            String var = getVarShadow(location);
            if (expect_mem) {
                if (ClassTable.class.equals(parent.getClass())) {
                    regCount++;
                    Reg reg = new Reg("R" + regCount);
                    Instruction ins = new MoveInstr(new Memory("this"), reg);
                    regCount--;
                    lirInstructionList.add(ins);
                    String class_name = parent.getID();
                    int offset = classes.get(class_name).getFields().get(location.getName());

                    return reg.toString() + "." + offset;
                } else {
                    //System.out.println("hey");
                    return var;
                }
            } else {


                if (ClassTable.class.equals(parent.getClass())) {
                    regCount++;
                    Reg reg = new Reg("R" + regCount);
                    Instruction ins = new MoveInstr(new Memory("this"), reg);
                    regCount--;
                    lirInstructionList.add(ins);

                    String class_name = parent.getID();
                    int offset = classes.get(class_name).getFields().get(location.getName());

                    lirInstructionList.add(new MoveFieldInstr(reg, new Immediate(offset), new Reg("R" + regCount), !expect_mem));

                    return "R" + regCount;


                }

                Instruction node = new MoveInstr(new Reg(var), new Reg("R" + regCount));
                lirInstructionList.add(node);
                return "R" + (regCount);
            }

        }

        regCount++;
        String locRes;
        if (expect_mem) {
            expect_mem = !expect_mem;
            locRes = (String) location.getLocation().accept(this);
            expect_mem = !expect_mem;
        } else {
            locRes = (String) location.getLocation().accept(this);
        }
        regCount--;

        regCount++;
        Operand base;
        base = new Reg("R" + regCount);

        if (expect_mem) {
            lirInstructionList.add(new MoveInstr(new Memory(locRes), base));
        }

        regCount--;

        String class_name = TypingRules.evaluate_type(location.getLocation());
        int offset = classes.get(class_name).getFields().get(location.getName());

        lirInstructionList.add(new MoveFieldInstr(base, new Immediate(offset), new Reg("R" + regCount), !expect_mem));

        return "R" + regCount;
    }

    private String getVarShadow(VariableLocation location) {
        SymTable scope = location.getEnclosing_scope();
        String name = location.getName();

        if (location.isExternal()) {
            // Get the scope of the variable class
            Expression loc = location.getLocation();

            String type = TypingRules.evaluate_type(loc);

            scope = SymTableCreator.findClassInTree(type, location.getLine());
        }

        SymEntry sym = SymTableCreator.findSym(name,scope,Kind.FIELD,location.getLine());

        return "v" + sym.getUniqueId() + "_" +location.getName();
    }

    private String getVarShadow(LocalVariable location) {
        SymTable scope = location.getEnclosing_scope();
        String name = location.getName();

        SymEntry sym = SymTableCreator.findSym(name,scope,Kind.FIELD,location.getLine());

        return "v" + sym.getUniqueId() + "_" + location.getName();
    }

    @Override
    public Object visit(ArrayLocation location) {
        String arr = null;
        String index = null;
        regCount++;
        if (expect_mem) {
            expect_mem = false;
            arr = (String) location.getArray().accept(this);
            regCount++;
            index = (String) location.getIndex().accept(this);
            expect_mem = true;
        } else {
            arr = (String) location.getArray().accept(this);
            regCount++;
            index = (String) location.getIndex().accept(this);
        }

        //String index = (String) location.getIndex().accept(this);

        regCount ++;
        Reg arr_reg = new Reg("R" + regCount);
        regCount -= 3;

        lirInstructionList.add(new MoveInstr(new Memory(arr), arr_reg));
        lirInstructionList.add(new MoveArrayInstr(arr_reg, new Reg(index), new Reg("R" + regCount), !expect_mem));
        if (expect_mem) {
            return null;
        } else {
            return "R" + regCount;
        }

    }



    @Override
    public Object visit(StaticCall call) {
        int oldRegCount = regCount;

        if ("Library".equals(call.getClassName())) {
            // Convert param expr list to operand list
            List<Operand> operandList = new LinkedList<Operand>();
            for (Expression e : call.getArguments()) {
                regCount++;
                operandList.add(new Reg((String) e.accept(this)));
            }

            lirInstructionList.add(new LibraryCall(new Label("__" + call.getName()), operandList, new Reg("R" + oldRegCount)));

        } else {
            // Static function

            handleStaticCall(call, call.getClassName(),call.getName(),call.getLine(), oldRegCount);

        }
        regCount = oldRegCount;
        return "R" + regCount;
    }


    public String handleStaticCall(Call call, String className, String methodName, int line, int oldRegCount) {
        ClassTable class_table = (ClassTable) SymTableCreator.findClassInTree(className, line);
        MethodSym method_sym = (MethodSym) SymTableCreator.findSym(methodName, class_table, Kind.METHOD, line);
        MethodTable methTable = (MethodTable) method_sym.getNode().getEnclosing_scope().getChild(methodName);//(methodName);//(className).getChild(methodName);
        // Get ParamOpPair list
        List<ParamOpPair> paramList = getParamPairList(methTable.getParameters(), call.getArguments());

        LirMethodInfo meth_info = classes.get(className).getMethods().get(methodName);
        lirInstructionList.add(new IC.lir.Instructions.StaticCall(new Label("_" + meth_info.getClass_name() + "_" + methodName), paramList, new Reg("R" + oldRegCount)));
        return null;
    }


    // Receives an ASTnode, travels up the scopes until reaches its class
    public String getASTNodeEnclosingClass(ASTNode node) {
        SymTable curr = node.getEnclosing_scope();
        while (!curr.getClass().equals(IC.SemanticChecks.SymTables.ClassTable.class)) {
            curr = curr.getParent();
        }
        return curr.getID();
    }

    public MethodTable getASTNodeEnclosingMethod(ASTNode node) {
        SymTable curr = node.getEnclosing_scope();
        while (!curr.getClass().equals(IC.SemanticChecks.SymTables.MethodTable.class)) {
            curr = curr.getParent();
        }
        return (MethodTable) curr;
    }


    @Override
    public Object visit(VirtualCall call) {
        int oldRegCount = regCount;
        String type = null;
        String ObjRes = null;

        if (!call.isExternal()) {
            MethodTable meth_table =  getASTNodeEnclosingMethod(call);
            ClassTable meth_parent = (ClassTable) meth_table.getParent();
            MethodSym meth_sym = (MethodSym) SymTableCreator.findSym(call.getName(), meth_table, Kind.METHOD, call.getLine());
            if (meth_sym.isStatic_method()) {
                handleStaticCall(call, meth_parent.getID(),call.getName(),call.getLine(), oldRegCount);
                regCount = oldRegCount;
                return "R" + regCount;
            } else {
                type = getASTNodeEnclosingClass(call);
                regCount++;
                lirInstructionList.add(new MoveInstr(new Memory("this"), new Reg("R" + regCount)));
                ObjRes = "R" + regCount;
            }
        }
        else {
            type = TypingRules.evaluate_type(call.getLocation());
            regCount++;
            ObjRes = (String) call.getLocation().accept(this);

        }

        int offset = (classes.get(type).getMethods().get(call.getName()).getOffset());

        // Get the method table to get method parameters list
        ClassTable classTable = (ClassTable) SymTableCreator.findClassInTree(type, call.getLine());
        SymEntry methodSym = SymTableCreator.findSym(call.getName(), classTable, Kind.METHOD, call.getLine());
        classTable = (ClassTable) methodSym.getNode().getEnclosing_scope();
        MethodTable methTable = (MethodTable) classTable.getChild(call.getName());

        // Make paramOpPair list
        List<ParamOpPair> paramList = getParamPairList(methTable.getParameters(), call.getArguments());

        lirInstructionList.add(new IC.lir.Instructions.VirtualCall(new Reg(ObjRes), new Immediate(offset), paramList, new Reg("R" + oldRegCount)));
        regCount = oldRegCount;
        return "R" + regCount;
    }


    // Returns ParamOpPair list for static calls and virtual calls
    public List<ParamOpPair> getParamPairList(Map<String, FieldSym> params, List<Expression> arguments) {
        List<ParamOpPair> paramList = new LinkedList<ParamOpPair>();
        int i = 0;
        String currReg;
        for (Entry<String, FieldSym> e : params.entrySet()) {
            regCount++;
            currReg = (String) arguments.get(i).accept(this);
            paramList.add(new ParamOpPair(new Memory("v" + e.getValue().getUniqueId() + "_" + e.getKey()), new Reg(currReg)));
            i++;
        }
        return paramList;
    }




    @Override
    public Object visit(This thisExpression) {
        lirInstructionList.add(new MoveInstr(new Memory("this"), new Reg("R" + regCount)));
        return "R" + regCount;
    }

    @Override
    public Object visit(NewClass newClass) {
        List<Operand> args = new ArrayList<Operand>();
        args.add(new Immediate(classes.get(newClass.getName()).classSize()));
        Instruction node = new LibraryCall(new Label("__allocateObject"), args, new Reg("R" + regCount));

        lirInstructionList.add(node);

        node = new MoveFieldInstr(new Reg("R" + regCount), new Immediate(0), new Label("_DV_" + newClass.getName()), false);
        lirInstructionList.add(node);

        return "R" + regCount;
    }

    @Override
    public Object visit(NewArray newArray) {
        regCount++;
        String size = (String) newArray.getSize().accept(this);
        regCount--;

        Instruction node;
        int type_size = 4;

//		if (UserType.class.isAssignableFrom(newArray.getType().getClass())) {
//			type_size = classes.get(newArray.getType().getName()).classSize();
//		}

        node = new BinOpInstr(new Immediate(type_size), new Reg(size), Operator.MUL);
        lirInstructionList.add(node);

        List<Operand> args = new ArrayList<Operand>();

        args.add(new Reg(size));

        node = new LibraryCall(new Label("__allocateArray"), args, new Reg("R" + regCount));
        lirInstructionList.add(node);

        return "R" + regCount;
    }

    @Override
    public Object visit(Length length) {

        String arr = (String) length.getArray().accept(this);

        Instruction node = new ArrayLengthInstr(new Reg(arr), new Reg("R" + regCount));
        lirInstructionList.add(node);

        return "R" + regCount;
    }

    @Override
    public Object visit(MathBinaryOp binaryOp) {

        String firstOp = (String) binaryOp.getFirstOperand().accept(this);
        regCount++;
        String secOp = (String) binaryOp.getSecondOperand().accept(this);
        regCount--;

        if (("string").equals(TypingRules.evaluate_type(binaryOp
                .getFirstOperand()))) {
            List<Operand> operands = new ArrayList<Operand>();
            operands.add(new Reg(firstOp));
            operands.add(new Reg(secOp));

            Instruction node = new LibraryCall(new Label("__stringCat"),
                    operands, new Reg(firstOp));

            lirInstructionList.add(node);
            return firstOp;
        }

        Operator mathOp = null;
        if (binaryOp.getOperator() == BinaryOps.DIVIDE)
            mathOp = Operator.DIV;
        else if (binaryOp.getOperator() == BinaryOps.MINUS)
            mathOp = Operator.SUB;
        else if (binaryOp.getOperator() == BinaryOps.MOD)
            mathOp = Operator.MOD;
        else if (binaryOp.getOperator() == BinaryOps.PLUS)
            mathOp = Operator.ADD;
        else if (binaryOp.getOperator() == BinaryOps.MULTIPLY)
            mathOp = Operator.MUL;

        Instruction node = new BinOpInstr(new Reg(secOp), new Reg(firstOp),
                mathOp);
        lirInstructionList.add(node);

        return firstOp;
    }

    @Override
    public Object visit(LogicalBinaryOp binaryOp) {
        String firstOp = (String) binaryOp.getFirstOperand().accept(this);
        regCount++;
        String secOp = (String) binaryOp.getSecondOperand().accept(this);
        regCount--;

        Instruction node = null;

        switch (binaryOp.getOperator()) {
            case LAND:
                node = new BinOpInstr(new Reg(secOp), new Reg(firstOp),
                        Operator.AND);
                lirInstructionList.add(node);
                break;
            case LOR:
                node = new BinOpInstr(new Reg(secOp), new Reg(firstOp), Operator.OR);
                lirInstructionList.add(node);
                break;
            default:
                lirInstructionList.add(new CompareInstr(new Reg(secOp), new Reg(firstOp)));
                Label true_lable = new Label("_jump_true_" + lableCount);
                Label false_lable = new Label("_jump_false_" + lableCount);
                Label endif_lable = new Label("_jump_endif_" + lableCount);

                lableCount++;

                switch (binaryOp.getOperator()) {
                    case GT:
                        lirInstructionList.add(new CondJumpInstr(false_lable, Cond.LE));
                        break;
                    case GTE:
                        lirInstructionList.add(new CondJumpInstr(false_lable, Cond.L));
                        break;
                    case LT:
                        lirInstructionList.add(new CondJumpInstr(false_lable, Cond.GE));
                        break;
                    case LTE:
                        lirInstructionList.add(new CondJumpInstr(false_lable, Cond.G));
                        break;
                    case EQUAL:
                        lirInstructionList.add(new CondJumpInstr(false_lable, Cond.False));
                        break;
                    case NEQUAL:
                        lirInstructionList.add(new CondJumpInstr(false_lable, Cond.True));
                        break;
                    default:
                        break;
                }

                // Save true if the result is true
                lirInstructionList.add(new LabelInstr(true_lable));
                lirInstructionList.add(new MoveInstr(new Immediate(1), new Reg(firstOp)));
                lirInstructionList.add(new JumpInstr(endif_lable));

                // Save false if the result is false
                lirInstructionList.add(new LabelInstr(false_lable));
                lirInstructionList.add(new MoveInstr(new Immediate(0), new Reg(firstOp)));

                lirInstructionList.add(new LabelInstr(endif_lable));

                break;
        }

        return firstOp;
    }

    @Override
    public Object visit(MathUnaryOp unaryOp) {
        String oper = (String) unaryOp.getOperand().accept(this);

        Instruction node2 = new UnaryOpInstr(new Reg(oper), Operator.NEG);

        lirInstructionList.add(node2);
        return "R" + regCount;
    }

    @Override
    public Object visit(LogicalUnaryOp unaryOp) {
        String oper = (String) unaryOp.getOperand().accept(this);

        Instruction node = new UnaryOpInstr(new Reg(oper), Operator.NOT);
        lirInstructionList.add(node);
        return oper;
    }

    @Override
    public Object visit(Literal literal) {
        // regCount++;
        String result = "";
        Instruction node = null;
        if (literal.getType().equals(LiteralTypes.INTEGER)) {
            node = new MoveInstr(new Immediate(Integer.parseInt((String) literal.getValue())), new Reg("R" + regCount));
            result = "R" + regCount;
        }
        if (literal.getType().equals(LiteralTypes.TRUE)) {
            node = new MoveInstr(new Immediate(1), new Reg("R" + regCount));
            result = "R" + regCount;
        }
        if (literal.getType().equals(LiteralTypes.FALSE)) {
            node = new MoveInstr(new Immediate(0), new Reg("R" + regCount));
            result = "R" + regCount;
        }
        if (literal.getType().equals(LiteralTypes.NULL)) {
            node = new MoveInstr(new Immediate(0), new Reg("R" + regCount));

            result = "R" + regCount;
        }
        if (literal.getType().equals(LiteralTypes.STRING)) {
            String label = addStringLiteral((String) literal.getValue());
            node = new MoveInstr(new Label(label), new Reg("R" + regCount));

            result = "R" + regCount;
        }
        lirInstructionList.add(node);
        return result;
    }

    @Override
    public Object visit(ExpressionBlock expressionBlock) {
        return expressionBlock.getExpression().accept(this);
    }

}
