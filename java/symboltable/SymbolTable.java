package symboltable;

import type.Type;
import type.ClassType;
import type.MethodType;
import type.PrimitiveType;
import typecheck.TypeCheckException;
import type.enums.TYPE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SymbolTable {
    public static Environment table;
    public Context state;

    public SymbolTable() {
        table = new Environment();
        state = new Context();
        ifCounter = 0;
        whileCounter = 0;
        nullCounter = 0;
    }

    public volatile int ifCounter;
    public volatile int whileCounter;
    public volatile int nullCounter;
    public volatile int andCounter;

    public synchronized int getWhileCounter() {
        return whileCounter++;
    }

    public synchronized int getAndCounter() {
        return andCounter++;
    }

    public synchronized int getNullCounter() {
        return nullCounter++;
    }

    public synchronized int getIfCounter() {
        int count = ifCounter;
        ifCounter++;
        return count;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (var entry : table.classes().entrySet()) {
            string.append("\n--------------------------------------\n");
            string.append(String.format("\033[;35mClass:\033[0m %s\n\n", entry.getKey().toString()));
            if (entry.getValue().hasParent())
                string.append("\033[;36mParents:\033[0m [");
            int j = 0;
            for (var parent : entry.getValue().parents) {
                if (j != entry.getValue().parents.size() - 1)
                    string.append(parent.toString()).append(", ");
                else
                    string.append(parent.toString()).append("]\n\n");
                ++j;
            }
            if (!entry.getValue().getFields().isEmpty())
                string.append("\033[;34mFields:\033[0m\n");
            for (var field : entry.getValue().getFields().entrySet()) {
                String type = "";
                if (field.getValue().type == TYPE.PRIMITIVE)
                    type = ((PrimitiveType) field.getValue()).subType;
                else if (field.getValue().type == TYPE.CLASS)
                    type = ((ClassType) field.getValue()).classID();
                else if (field.getValue().type == TYPE.ARRAY)
                    type = "int[]";
                string.append(String.format("%-8s", field.getKey().toString())).append(" ->   ").append(type).append('\n');
            }
            string.append('\n');
            if (!entry.getValue().getMethods().isEmpty())
                string.append("\033[;32mMethods:\033[0m\n");
            for (var method : entry.getValue().getMethods().entrySet()) {
                StringBuilder signature = new StringBuilder();
                signature.append(String.format("%s", method.getKey().toString())).append("(");
                int i = 0;
                String type = "";
                for (Symbol param : method.getValue().getParameterOrder()) {
                    if (method.getValue().getParameters().get(param).type == TYPE.PRIMITIVE)
                        type = ((PrimitiveType) method.getValue().getParameters().get(param)).subType;
                    else if (method.getValue().getParameters().get(param).type == TYPE.CLASS)
                        type = ((ClassType) method.getValue().getParameters().get(param)).classID();
                    else if (method.getValue().getParameters().get(param).type == TYPE.ARRAY)
                        type = "int[]";
                    if (i != method.getValue().getParameterOrder().size() - 1)
                        signature.append(type).append(' ').append(param.toString()).append(',').append(' ');
                    else
                        signature.append(type).append(' ').append(param.toString());
                    ++i;
                }
                type = "";
                if (method.getValue().getReturnType().type == TYPE.PRIMITIVE)
                    type = ((PrimitiveType) method.getValue().getReturnType()).subType;
                else if (method.getValue().getReturnType().type == TYPE.CLASS)
                    type = ((ClassType) method.getValue().getReturnType()).classID();
                else if (method.getValue().getReturnType().type == TYPE.ARRAY)
                    type = "int[]";
                signature.append(") -> ").append(type);
                string.append(String.format("%-25s  >\033[;31m   Locals: \033[0m[", signature));
                i = 0;
                for (var local : method.getValue().getVariables().entrySet()) {
                    type = "";
                    if (local.getValue().type == TYPE.PRIMITIVE)
                        type = ((PrimitiveType) local.getValue()).subType;
                    else if (local.getValue().type == TYPE.CLASS)
                        type = ((ClassType) local.getValue()).classID();
                    else if (local.getValue().type == TYPE.ARRAY)
                        type = "int[]";
                    if (i != method.getValue().getVariables().size() - 1)
                        string.append(type).append(' ').append(local.getKey().toString()).append("  ");
                    else
                        string.append(type).append(' ').append(local.getKey().toString());
                    ++i;
                }
                string.append("]\n");
            }
        }
        return string.toString();
    }

    public boolean addClass(String className, ClassType classType) {
        return table.addClass(className, classType);
    }

    public boolean addMethod(String className, String methodName, MethodType methodType) {
        return table.addMethod(className, methodName, methodType);
    }

    public boolean addField(String className, String fieldName, Type fieldType) {
        return table.addField(className, fieldName, fieldType);
    }

    public boolean addParameter(String className, String methodName, String parameterName, Type parameterType) {
        return table.addParameter(className, methodName, parameterName, parameterType);
    }

    public boolean addVariable(String className, String methodName, String variableName, Type variableType) {
        return table.addVariable(className, methodName, variableName, variableType);
    }

    public String getMainClassName() {
        for (var entry : table.getClassTypes().entrySet()) {
            if (entry.getValue().isMain())
                return entry.getKey().toString();
        }
        return null;
    }

    public boolean subType(Type t2, Type t1) {
        if (t1.type != t2.type) {
            return false;
        }
        if (t1.type == TYPE.PRIMITIVE)
            if (!((PrimitiveType) t1).subType.equals(((PrimitiveType) t2).subType))
                return false;
        if (t1.type == TYPE.CLASS) {
            if (((ClassType) t1).classID().equals(((ClassType) t2).classID()))
                return true;
            Set<String> parents = new HashSet<>();
            ClassType currentClass = (ClassType) this.getFullClassType(((ClassType) t2).classID());
            while (currentClass.hasParent()) {
                parents.add(currentClass.parentName());
                currentClass = (ClassType) this.typeC(currentClass.parentName());
            }
            ClassType otherClass = (ClassType) t1;
            return parents.contains(otherClass.classID());
        }
        return true;
    }

    public Set<String> getClassNames() {
        table.getClassNames();
        HashSet<String> classNames = new HashSet();
        for (Symbol name : table.getClassNames())
            classNames.add(name.toString());
        return classNames;
    }

    public boolean isPV(String classID, String methodID, String id) {
        ClassType classType = this.typeC(classID);
        MethodType methodType = classType.getMethodType(methodID);
        boolean b = methodType.isPV(id);
        return b;
    }

    public ClassType getFullClassType(String classID) {
        ClassType t = table.type(classID);
        ClassType full = new ClassType(t);
        while (t.parentName() != null) {
            ClassType parent = table.type(t.parentName());
            full.parents.add(Symbol.symbol(parent.classID()));
            boolean checks = full.inherit(parent);
            if (!checks)
                return null;
            t = parent;
        }
        return full;
    }

    public ClassType typeC(String id) {
        return table.getClassType(id);
    }

    public MethodType typeM(String classID, String id) {
        return table.getMethodTypeInfo(classID, id);
    }

    public Type typeF(String classID, String id) {
        return table.getFieldTypeInfo(classID, id);
    }

    public Type typePVF(String classID, String methodID, String id) {
        Type type;
        type = table.getParameterTypeInfo(classID, methodID, id);
        if (type != null)
            return type;
        type = table.getVariableTypeInfo(classID, methodID, id);
        if (type != null)
            return type;
        return table.getFieldTypeInfo(classID, id);
    }

    public Type typePV(String classID, String methodID, String id) {
        Type type;
        type = table.getParameterTypeInfo(classID, methodID, id);
        if (type != null)
            return type;
        type = table.getVariableTypeInfo(classID, methodID, id);
        return type;
    }

    public int pvf(String classID, String methodID, String id) {
        int type = -1;
        if (table.getParameterTypeInfo(state.classID, state.methodID, id) != null)
            type = 0;
        if (table.getVariableTypeInfo(state.classID, state.methodID, id) != null)
            type = 1;
        if (table.getFieldTypeInfo(state.classID, id) != null)
            type = 2;
        return type;
    }

    public Map<Symbol, ClassType> classes() {
        return table.classes();
    }

    public void inheritAll() {
        Map<Symbol, ClassType> updated = new HashMap();
        for (var entry : table.classes().entrySet()) {
            if (!entry.getValue().hasParent()) {
                updated.put(entry.getKey(), entry.getValue());
                continue;
            }
            ClassType full = getFullClassType(entry.getValue().classID());
            if (full == null)
                throw new TypeCheckException();
            updated.put(entry.getKey(), full);
        }
        table.setClasses(updated);
    }

    public void printVapor() {
        table.classes().forEach(this::printVMT);
        printMethods(table.main());
        table.classes().forEach(this::printMethods);
        final String allocArray = "func AllocArray(size)\n  bytes = MulS(size 4)\n  bytes = Add(bytes 4)\n  v = HeapAllocZ(bytes)\n  [v] = size\n  ret v";
        System.out.println(allocArray);
    }

    private void printMethods(ClassType classType) {
        classType.getMethods().forEach(this::printMethod);
    }

    private void printMethods(Symbol symbol, ClassType classType) {
        if (classType.isMain())
            return;
        classType.getMethods().forEach((methodID, methodType) -> {
                    if (methodType.vapor.classID.equals(classType.classID())) {
                        this.printMethod(methodID, methodType);
                    } else {
                        if (classType.hasParent()) {
                            ClassType parent = this.typeC(classType.parentName());
                            if (!parent.getMethods().containsValue(methodType)) {
                                this.printMethod(methodID, methodType);
                            }
                        } else {
                            this.printMethod(methodID, methodType);
                        }
                    }
                }
        );
    }

    private void printMethod(Symbol symbol, MethodType methodType) {
        System.out.println(methodType.vapor.getSignature());
        methodType.vapor.statements().lines().forEach((line) -> {
            System.out.println("  " + line);
        });
        System.out.println();
    }

    private void printVMT(Symbol symbol, ClassType classType) {
        if (classType.isMain())
            return;
        System.out.println(classType.vapor.vmt());
    }
}
