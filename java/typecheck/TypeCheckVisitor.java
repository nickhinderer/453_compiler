package typecheck;

import symboltable.SymbolTable;
import syntaxtree.*;
import type.ArrayType;
import type.enums.TYPE;
import visitor.GJDepthFirst;
import type.Type;

import java.util.Enumeration;

import type.*;

public class TypeCheckVisitor extends GJDepthFirst<Type, SymbolTable> {
    public TypeCheckVisitor() {
    }

    public Type visit(MainClass n, SymbolTable st) {
        Type _ret = new TypeChecksType();
        st.state.classID = n.f1.f0.tokenImage;
        st.state.methodID = "main";
        st.state.method = true;
        if (n.f15.accept(this, st) == null)
            return null;
        st.state.method = false;
        return _ret;
    }

    public Type visit(ClassDeclaration n, SymbolTable st) {
        st.state.classID = n.f1.f0.tokenImage;
        if (n.f4.accept(this, st) == null)
            return null;
        return new TypeChecksType();
    }

    public Type visit(ClassExtendsDeclaration n, SymbolTable st) {
        st.state.classID = n.f1.f0.tokenImage;
        n.f1.accept(this, st);
        n.f3.accept(this, st);
        if (n.f6.accept(this, st) == null)
            return null;
        return new TypeChecksType();
    }

    public Type visit(MethodDeclaration n, SymbolTable st) {
        st.state.methodID = n.f2.f0.tokenImage;
        st.state.method = true;
        if (n.f8.accept(this, st) == null)
            return null;
        Type actualReturnType = n.f10.accept(this, st);
        if (actualReturnType == null)
            return null;
        Type declaredReturnType = st.typeM(st.state.classID, st.state.methodID).getReturnType();
        if (declaredReturnType.type != actualReturnType.type)
            return null;
        if (declaredReturnType.type == TYPE.PRIMITIVE)
            if (!((PrimitiveType) declaredReturnType).subType.equals(((PrimitiveType) actualReturnType).subType))
                return null;
        if (declaredReturnType.type == TYPE.CLASS)
            if (!((ClassType) declaredReturnType).classID().equals(((ClassType) actualReturnType).classID()))
                return null;
        st.state.method = false;
        return new TypeChecksType();
    }

    public Type visit(PrintStatement n, SymbolTable st) {
        Type t = n.f2.accept(this, st);
        if (!isInt(t))
            return null;
        return new TypeChecksType();
    }

    public Type visit(Expression n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    public Type visit(PlusExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        if (t1 == null || t1.type != TYPE.PRIMITIVE || !((PrimitiveType) t1).subType.equals("int"))
            return null;
        Type t2 = n.f2.accept(this, st);
        if (t2 == null || t2.type != TYPE.PRIMITIVE || !((PrimitiveType) t2).subType.equals("int"))
            return null;
        return new PrimitiveType("int");
    }

    public Type visit(MinusExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        if (t1 == null || t1.type != TYPE.PRIMITIVE || !((PrimitiveType) t1).subType.equals("int"))
            return null;
        Type t2 = n.f2.accept(this, st);
        if (t2 == null || t2.type != TYPE.PRIMITIVE || !((PrimitiveType) t2).subType.equals("int"))
            return null;
        return new PrimitiveType("int");
    }

    public Type visit(TimesExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        if (t1 == null || t1.type != TYPE.PRIMITIVE || !((PrimitiveType) t1).subType.equals("int"))
            return null;
        Type t2 = n.f2.accept(this, st);
        if (t2 == null || t2.type != TYPE.PRIMITIVE || !((PrimitiveType) t2).subType.equals("int"))
            return null;
        return new PrimitiveType("int");
    }

    public Type visit(TrueLiteral n, SymbolTable st) {
        return new PrimitiveType("boolean");
    }

    public Type visit(FalseLiteral n, SymbolTable st) {
        return new PrimitiveType("boolean");
    }

    public Type visit(IntegerLiteral n, SymbolTable st) {
        return new PrimitiveType("int");
    }

    public Type visit(PrimaryExpression n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    public Type visit(Identifier n, SymbolTable st) {
        return st.typePVF(st.state.classID, st.state.methodID, n.f0.tokenImage);
    }

    public Type visit(VarDeclaration n, SymbolTable st) {
        Type _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    public Type visit(AssignmentStatement n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        if (t1 == null || t2 == null)
            return null;
        if (!st.subType(t2, t1))
            return null;
        if (t1.type == TYPE.PRIMITIVE)
            if (!((PrimitiveType) t1).subType.equals(((PrimitiveType) t2).subType))
                return null;
        return new TypeChecksType();
    }

    public Type visit(Statement n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    public Type visit(IfStatement n, SymbolTable st) {
        Type t1 = n.f2.accept(this, st);
        if (!isBoolean(t1))
            return null;
        if (n.f4.accept(this, st) == null)
            return null;
        if (n.f6.accept(this, st) == null)
            return null;
        return new TypeChecksType();
    }

    public Type visit(CompareExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        if (isInt(t1) && isInt(t2))
            return new PrimitiveType("boolean");
        return null;
    }

    public Type visit(MessageSend n, SymbolTable st) {
        String classID = ((ClassType) n.f0.accept(this, st)).classID();
        ClassType c = st.typeC(classID);
        MethodType m = st.typeM(classID, n.f2.f0.tokenImage);
        boolean checks = true;
        if (n.f4.present())
            checks = validateParameters((ExpressionList) n.f4.node, m, st);
        else {
            if (m.parameterCount() != 0)
                return null;
        }
        if (checks)
            return m.getReturnType();
        else
            return null;
    }

    public boolean validateParameters(ExpressionList n, MethodType m, SymbolTable st) {
        Type t = n.f0.accept(this, st);
        int params = 0;
        if (!matchesExpectedParameter(t, m, st, 0))
            return false;
        else params++;
        if (n.f1.present()) {
            int count = 0;
            for (Enumeration<Node> e = n.f1.elements(); e.hasMoreElements(); ) {
                t = e.nextElement().accept(this, st);
                if (!matchesExpectedParameter(t, m, st, count + 1))
                    return false;
                count++;
                params++;
            }
        }
        return params == m.parameterCount();
    }

    public boolean matchesExpectedParameter(Type type, MethodType m, SymbolTable st, int index) {
        if (type == null)
            return false;
        String parameter = m.parameter(index);
        if (parameter == null)
            return false;
        return st.subType(type, m.getParameterType(parameter));
    }

    public boolean isBoolean(Type t) {
        if (t == null)
            return false;
        if (t.type != TYPE.PRIMITIVE)
            return false;
        return ((PrimitiveType) t).subType.equals("boolean");
    }

    public boolean isInt(Type t) {
        if (t == null)
            return false;
        if (t.type != TYPE.PRIMITIVE)
            return false;
        return ((PrimitiveType) t).subType.equals("int");
    }

    public boolean isArray(Type t) {
        if (t == null)
            return false;
        return t.type == TYPE.ARRAY;
    }

    public Type visit(NodeListOptional n, SymbolTable st) {
        Type checks = new TypeChecksType();
        if (n.present()) {
            int _count = 0;
            for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                if (e.nextElement().accept(this, st) == null) {
                    checks = null;
                }
                _count++;
            }
            return checks;
        } else
            return checks;
    }

    public Type visit(NodeList n, SymbolTable st) {
        Type _ret = null;
        int _count = 0;
        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this, st);
            _count++;
        }
        return _ret;
    }

    public Type visit(NodeOptional n, SymbolTable st) {
        if (n.present())
            return n.node.accept(this, st);
        else
            return null;
    }

    public Type visit(NodeSequence n, SymbolTable st) {
        Type _ret = null;
        int _count = 0;
        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this, st);
            _count++;
        }
        return _ret;
    }

    public Type visit(NodeToken n, SymbolTable st) {
        return null;
    }

    public Type visit(Goal n, SymbolTable st) {
        Type _ret = null;
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f1.accept(this, st);
        if (t1 == null || t2 == null)
            return null;
        return new TypeChecksType();
    }

    public Type visit(TypeDeclaration n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    public Type visit(FormalParameterList n, SymbolTable st) {
        Type _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    public Type visit(FormalParameter n, SymbolTable st) {
        Type _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    public Type visit(FormalParameterRest n, SymbolTable st) {
        Type _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    public Type visit(syntaxtree.Type n, SymbolTable st) {
        Type _ret = null;
        n.f0.accept(this, st);
        return _ret;
    }

    public Type visit(ArrayType n, SymbolTable st) {
        return new ArrayType();
    }

    public Type visit(BooleanType n, SymbolTable st) {
        return null;
    }

    public Type visit(IntegerType n, SymbolTable st) {
        Type _ret = null;
        n.f0.accept(this, st);
        return _ret;
    }

    public Type visit(Block n, SymbolTable st) {
        return n.f1.accept(this, st);
    }

    public Type visit(ArrayAssignmentStatement n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        Type t3 = n.f5.accept(this, st);
        if (!isArray(t1) || !isInt(t2) || !isInt(t3))
            return null;
        return new TypeChecksType();
    }

    public Type visit(WhileStatement n, SymbolTable st) {
        Type t1 = n.f2.accept(this, st);
        if (!t1.equals(new PrimitiveType("boolean"))) {
            return null;
        }
        Type t2 = n.f4.accept(this, st);
        if (t2 == null)
            return null;
        return new TypeChecksType();
    }

    public Type visit(AndExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        if (isBoolean(t1) && isBoolean(t2))
            return t1;
        return null;
    }

    public Type visit(ArrayLookup n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        if (!isArray(t1) || !isInt(t2))
            return null;
        return t2;
    }

    public Type visit(ArrayLength n, SymbolTable st) {
        Type t = n.f0.accept(this, st);
        if (!isArray(t))
            return null;
        return new PrimitiveType("int");
    }

    public Type visit(ExpressionList n, SymbolTable st) {
        Type _ret = null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    public Type visit(ExpressionRest n, SymbolTable st) {
        return n.f1.accept(this, st);
    }

    public Type visit(ThisExpression n, SymbolTable st) {
        return st.typeC(st.state.classID);
    }

    public Type visit(ArrayAllocationExpression n, SymbolTable st) {
        Type t = n.f3.accept(this, st);
        if (!isInt(t))
            return null;
        return new ArrayType();
    }

    public Type visit(AllocationExpression n, SymbolTable st) {
        return st.typeC(n.f1.f0.tokenImage);
    }

    public Type visit(NotExpression n, SymbolTable st) {
        Type t = n.f1.accept(this, st);
        if (!isBoolean(t))
            return null;
        return t;
    }

    public Type visit(BracketExpression n, SymbolTable st) {
        return n.f1.accept(this, st);
    }
}
