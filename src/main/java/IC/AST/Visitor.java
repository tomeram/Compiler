/*
 * Decompiled with CFR 0_110.
 */
package IC.AST;

import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
import IC.AST.CallStatement;
import IC.AST.Continue;
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
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.Return;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;

public interface Visitor {
    public Object visit(Program var1);

    public Object visit(ICClass var1);

    public Object visit(Field var1);

    public Object visit(VirtualMethod var1);

    public Object visit(StaticMethod var1);

    public Object visit(LibraryMethod var1);

    public Object visit(Formal var1);

    public Object visit(PrimitiveType var1);

    public Object visit(UserType var1);

    public Object visit(Assignment var1);

    public Object visit(CallStatement var1);

    public Object visit(Return var1);

    public Object visit(If var1);

    public Object visit(While var1);

    public Object visit(Break var1);

    public Object visit(Continue var1);

    public Object visit(StatementsBlock var1);

    public Object visit(LocalVariable var1);

    public Object visit(VariableLocation var1);

    public Object visit(ArrayLocation var1);

    public Object visit(StaticCall var1);

    public Object visit(VirtualCall var1);

    public Object visit(This var1);

    public Object visit(NewClass var1);

    public Object visit(NewArray var1);

    public Object visit(Length var1);

    public Object visit(MathBinaryOp var1);

    public Object visit(LogicalBinaryOp var1);

    public Object visit(MathUnaryOp var1);

    public Object visit(LogicalUnaryOp var1);

    public Object visit(Literal var1);

    public Object visit(ExpressionBlock var1);
}

