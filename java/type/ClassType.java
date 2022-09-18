package type;

import symboltable.Symbol;
import type.enums.TYPE;

import java.util.*;

public class ClassType extends Type {
    private Map<Symbol, MethodType> methods;
    private List<Symbol> methodsOrder;
    private Map<Symbol, Type> fields;
    private List<Symbol> fieldsOrder;
    private final Symbol name;
    private Symbol parent;
    private boolean main;
    public Set<Symbol> parents;
    public VaporClassType vapor;

    public ClassType(String className, String parentName) {
        name = Symbol.symbol(className);
        if (parentName != null)
            parent = Symbol.symbol(parentName);
        methods = new HashMap<>();
        methodsOrder = new ArrayList<>();
        fieldsOrder = new ArrayList<>();
        fields = new HashMap<>();
        this.main = false;
        this.type = TYPE.CLASS;
        this.parents = new HashSet<>();
    }

    public ClassType(ClassType classType) {
        this.methods = new HashMap<>(classType.methods);
        this.methodsOrder = new ArrayList<Symbol>(classType.methodsOrder);
        this.fieldsOrder = new ArrayList<>(classType.fieldsOrder);
        this.fields = new HashMap<>(classType.fields);
        this.name = Symbol.symbol(classType.name.toString());
        if (classType.parent != null)
            this.parent = Symbol.symbol(classType.parent.toString());
        this.main = classType.main;
        this.type = TYPE.CLASS;
        this.parents = new HashSet<>();
    }

    public ClassType(String className, String parentName, boolean main) {
        name = Symbol.symbol(className);
        if (parentName != null)
            parent = Symbol.symbol(parentName);
        methods = new HashMap<>();
        methodsOrder = new ArrayList<>();
        fieldsOrder = new ArrayList<>();
        fields = new HashMap<>();
        this.main = main;
        this.type = TYPE.CLASS;
        this.parents = new HashSet<>();
    }

    public Map<Symbol, MethodType> getMethods() {
        return methods;
    }

    public String parentName() {
        if (parent != null)
            return parent.toString();
        return null;
    }

    public void addParent(String parentName) {
        this.parent = Symbol.symbol(parentName);
    }

    public boolean addMethod(String methodName, MethodType methodType) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null) {
            methods.put(m, methodType);
            methodsOrder.add(m);
            return true;
        } else return false;
    }

    public boolean addField(String fieldName, Type fieldType) {
        Symbol f = Symbol.symbol(fieldName);
        if (fields.get(f) == null) {
            fields.put(f, fieldType);
            fieldsOrder.add(f);
            return true;
        } else return false;
    }

    public boolean addParameter(String methodName, String parameterName, Type parameterType) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return false;
        return methods.get(m).addParameter(parameterName, parameterType);
    }

    public boolean addVariable(String methodName, String variableName, Type variableType) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return false;
        return methods.get(m).addVariable(variableName, variableType);
    }

    public MethodType getMethodType(String methodName) {
        Symbol m = Symbol.symbol(methodName);
        return methods.get(m);
    }

    public Type getFieldType(String fieldName) {
        Symbol f = Symbol.symbol(fieldName);
        return fields.get(f);
    }

    public Type getParameterTypeInfo(String methodName, String parameterName) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return null;
        return methods.get(m).getParameterType(parameterName);
    }

    public Type getVaraibleTypeInfo(String methodName, String variableName) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return null;
        return methods.get(m).getVariableType(variableName);
    }

    public boolean isMain() {
        return main;
    }

    public int getMethodCount() {
        return methods.size();
    }

    public int getFieldCount() {
        return fields.size();
    }

    public Type type(String methodName, String name) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return null;
        return methods.get(m).type(name);
    }

    public MethodType type(String methodName) {
        Symbol m = Symbol.symbol(methodName);
        return methods.get(m);
    }

    public MethodType typeMethod(String methodName) {
        Symbol m = Symbol.symbol(methodName);
        return methods.get(m);
    }

    public List<Symbol> getMethodsOrder() {
        return methodsOrder;
    }

    public boolean inherit(ClassType parent) {
        for (var entry : parent.methods.entrySet()) {
            if (this.methods.containsKey(entry.getKey())) {
                if (!this.methods.get(entry.getKey()).equals(parent.methods.get(entry.getKey())))
                    return false;
                else {
                }
            } else {
                this.methods.put(entry.getKey(), entry.getValue());
            }
            for (int i = parent.methodsOrder.size() - 1; i >= 0; i--) {
                if (!this.methods.containsKey(parent.methodsOrder.get(i)))
                    this.methodsOrder.add(0, parent.methodsOrder.get(i));
                else {
                    if (!this.methodsOrder.contains(parent.methodsOrder.get(i)))
                        this.methodsOrder.add(parent.methodsOrder.get(i));
                }
            }
        }
        for (var entry : parent.fields.entrySet()) {
            if (!this.fields.containsKey(entry.getKey())) {
                this.fields.put(entry.getKey(), entry.getValue());
            }
        }
        for (int i = parent.fieldsOrder.size() - 1; i >= 0; i--) {
            this.fieldsOrder.add(0, parent.fieldsOrder.get(i));
        }
        return true;
    }

    public boolean equals(ClassType other) {
        return this.name.toString().equals(other.name.toString());
    }

    public String classID() {
        return name.toString();
    }

    @Override
    public boolean equals(Type other) {
        if (other.type != TYPE.CLASS)
            return false;
        else return this.equals((ClassType) other);
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public List<Symbol> getFieldsOrder() {
        return fieldsOrder;
    }

    public Type typeF(String fieldID) {
        Symbol id = Symbol.symbol(fieldID);
        return fields.get(id);
    }

    public Map<Symbol, Type> getFields() {
        return this.fields;
    }
}
