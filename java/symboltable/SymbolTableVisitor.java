package symboltable;

import syntaxtree.NodeChoice;
import syntaxtree.Goal;
import syntaxtree.MainClass;
import syntaxtree.ClassDeclaration;
import syntaxtree.ClassExtendsDeclaration;
import syntaxtree.VarDeclaration;
import syntaxtree.MethodDeclaration;
import syntaxtree.FormalParameter;
import syntaxtree.Identifier;
import type.*;
import typecheck.TypeCheckException;
import visitor.GJDepthFirst;
import visitor.GJNoArguDepthFirst;
import visitor.GJVoidDepthFirst;

public class SymbolTableVisitor extends GJVoidDepthFirst<SymbolTable> {
    public SymbolTable st;

    public SymbolTable getSymbolTable() {
        return st;
    }

    public type.Type getType(NodeChoice n) {
        type.Type type = null;
        switch (n.which) {
            case 0:
                type = new ArrayType();
                break;
            case 1:
                type = new PrimitiveType("boolean");
                break;
            case 2:
                type = new PrimitiveType("int");
                break;
            case 3:
                String className = ((Identifier) n.choice).f0.tokenImage;
                type = new ClassType(className, null);
                break;
        }
        return type;
    }

    @Override
    public void visit(Goal n, SymbolTable st) {
        SymbolTable symbolTable = new SymbolTable();
        n.f0.accept(this, symbolTable);
        n.f1.accept(this, symbolTable);
        symbolTable.inheritAll();
        this.st = symbolTable;
    }

    @Override
    public void visit(MainClass n, SymbolTable st) {
        String className = n.f1.f0.tokenImage;
        ClassType classType = new ClassType(className, null, true);
        if (!st.addClass(className, classType))
            throw new TypeCheckException("Redefinition of of class '" + className + "'");
        st.state.classID = className;
        String methodName = "main";
        type.Type returnType = new PrimitiveType("void");
        MethodType methodType = new MethodType(returnType, className);
        if (!st.addMethod(st.state.classID, methodName, methodType))
            throw new TypeCheckException("Redefinition of of method '" + methodName + "' in class '" + st.state.classID + "'");
        st.state.methodID = methodName;
        st.state.variable = true;
        n.f14.accept(this, st);
        st.state.variable = false;
    }

    @Override
    public void visit(ClassDeclaration n, SymbolTable st) {
        String className = n.f1.f0.tokenImage;
        ClassType classType = new ClassType(className, null);
        if (!st.addClass(className, classType))
            throw new TypeCheckException("Redefinition of of class '" + className + "'");
        st.state.classID = className;
        st.state.field = true;
        n.f3.accept(this, st);
        st.state.field = false;
        n.f4.accept(this, st);
    }

    @Override
    public void visit(ClassExtendsDeclaration n, SymbolTable st) {
        String className = n.f1.f0.tokenImage;
        String parentName = n.f3.f0.tokenImage;
        ClassType classType = new ClassType(className, parentName);
        classType.parents.add(Symbol.symbol(parentName));
        if (!st.addClass(className, classType))
            throw new TypeCheckException("Redefinition of of class '" + className + "'");
        st.state.classID = className;
        st.state.field = true;
        n.f5.accept(this, st);
        st.state.field = false;
        n.f6.accept(this, st);
    }

    @Override
    public void visit(MethodDeclaration n, SymbolTable st) {
        String methodName = n.f2.f0.tokenImage;
        type.Type returnType = getType(n.f1.f0);
        MethodType methodType = new MethodType(returnType, st.state.classID);
        if (!st.addMethod(st.state.classID, methodName, methodType))
            throw new TypeCheckException("Redefinition of of method '" + methodName + "' in class '" + st.state.classID + "'");
        st.state.methodID = methodName;
        st.state.parameter = true;
        n.f4.accept(this, st);
        st.state.parameter = false;
        st.state.variable = true;
        n.f7.accept(this, st);
        st.state.variable = false;
    }

    @Override
    public void visit(VarDeclaration n, SymbolTable st) {
        type.Type type = getType(n.f0.f0);
        String name = n.f1.f0.tokenImage;
        if (st.state.field)
            if (!st.addField(st.state.classID, name, type))
                throw new TypeCheckException("Redefinition of of field '" + name + "' in class '" + st.state.classID + "'");
        if (st.state.variable)
            if (!st.addVariable(st.state.classID, st.state.methodID, name, type))
                throw new TypeCheckException("Redefinition of variable '" + name + "' in method '" + st.state.methodID + "' in class '" + st.state.classID + "'");
    }

    @Override
    public void visit(FormalParameter n, SymbolTable st) {
        type.Type type = getType(n.f0.f0);
        String name = n.f1.f0.tokenImage;
        if (st.state.parameter)
            if (!st.addParameter(st.state.classID, st.state.methodID, name, type))
                throw new TypeCheckException("Redefinition of variable '" + name + "' in method '" + st.state.methodID + "' in class '" + st.state.classID + "'");
    }
}