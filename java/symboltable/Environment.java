package symboltable;

import type.Type;
import type.ClassType;
import type.MethodType;

import java.util.*;

public class Environment {
    public static Map<Symbol, ClassType> classes;

    public Environment() {
        classes = new HashMap<>();
    }

    public ClassType main() {
        for (var entry : classes.entrySet()) {
            if (entry.getValue().isMain())
                return entry.getValue();
        }
        return null;
    }

    public boolean addClass(String className, ClassType classType) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null) {
            classes.put(c, classType);
            return true;
        } else return false;
    }

    public Map<Symbol, ClassType> getClasses() {
        return classes;
    }

    public boolean addMethod(String className, String methodName, MethodType methodType) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return false;
        return classes.get(c).addMethod(methodName, methodType);
    }

    public boolean addField(String className, String fieldName, Type fieldType) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return false;
        return classes.get(c).addField(fieldName, fieldType);
    }

    public boolean addParameter(String className, String methodName, String parameterName, Type parameterType) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return false;
        return classes.get(c).addParameter(methodName, parameterName, parameterType);
    }

    public boolean addVariable(String className, String methodName, String variableName, Type variableType) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return false;
        return classes.get(c).addVariable(methodName, variableName, variableType);
    }

    public ClassType getClassType(String className) {
        Symbol c = Symbol.symbol(className);
        return classes.get(c);
    }

    public MethodType getMethodTypeInfo(String className, String methodName) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return null;
        return classes.get(c).getMethodType(methodName);
    }

    public Type getFieldTypeInfo(String className, String fieldName) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return null;
        return classes.get(c).getFieldType(fieldName);
    }

    public Type getParameterTypeInfo(String className, String methodName, String parameterName) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return null;
        return classes.get(c).getParameterTypeInfo(methodName, parameterName);
    }

    public Type getVariableTypeInfo(String className, String methodName, String variableName) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return null;
        return classes.get(c).getVaraibleTypeInfo(methodName, variableName);
    }

    public Set<Symbol> getClassNames() {
        return classes.keySet();
    }

    public Map<Symbol, ClassType> getClassTypes() {
        return classes;
    }

    public Type type(String className, String methodName, String name) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return null;
        return classes.get(c).type(methodName, name);
    }

    public MethodType type(String className, String methodName) {
        Symbol c = Symbol.symbol(className);
        if (classes.get(c) == null)
            return null;
        return classes.get(c).type(methodName);
    }

    public ClassType type(String className) {
        Symbol c = Symbol.symbol(className);
        return classes.get(c);
    }

    public Map<Symbol, ClassType> classes() {
        return classes;
    }

    public void setClasses(Map<Symbol, ClassType> updated) {
        classes = updated;
    }
}
