package type;

import symboltable.Symbol;
import type.enums.TYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodType extends Type {
    private Map<Symbol, Type> parameters;
    private List<Symbol> order;
    private Map<Symbol, Type> variables;
    private Type returnType;
    public VaporMethodType vapor;
    public String classID;

    public MethodType(MethodType other, String classID) {
        this.parameters = new HashMap<>(other.parameters);
        this.returnType = other.returnType;
        this.order = new ArrayList<>(other.order);
        this.variables = new HashMap<>(other.variables);
        this.type = TYPE.METHOD;
        this.classID = classID;
    }

    public MethodType(Type returnType, String currentClass) {
        this.returnType = returnType;
        parameters = new HashMap<>();
        order = new ArrayList<>();
        variables = new HashMap<>();
        this.type = TYPE.METHOD;
        this.classID = currentClass;
    }

    public List<Symbol> getOrder() {
        return order;
    }

    public boolean addParameter(String parameterName, Type parameterType) {
        Symbol p = Symbol.symbol(parameterName);
        if (parameters.get(p) == null) {
            if (variables.get(p) != null)
                return false;
            parameters.put(p, parameterType);
            order.add(p);
            return true;
        } else return false;
    }

    public boolean addVariable(String variableName, Type variableType) {
        Symbol v = Symbol.symbol(variableName);
        if (variables.get(v) == null) {
            if (parameters.get(v) != null)
                return false;
            variables.put(v, variableType);
            return true;
        } else return false;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Type getParameterType(String parameterName) {
        Symbol p = Symbol.symbol(parameterName);
        return parameters.get(p);
    }

    public Type getVariableType(String variableName) {
        Symbol v = Symbol.symbol(variableName);
        return variables.get(v);
    }

    public Type type(String name) {
        Symbol id = Symbol.symbol(name);
        if (parameters.containsKey(id))
            return parameters.get(id);
        if (variables.containsKey(id))
            return variables.get(id);
        return null;
    }

    public String parameter(int index) {
        if (index >= order.size())
            return null;
        return order.get(index).toString();
    }

    public int parameterCount() {
        return parameters.size();
    }

    public boolean equals(MethodType other) {
        if (this.parameterCount() != other.parameterCount())
            return false;
        for (int i = 0; i < parameterCount(); i++) {
            Type p1 = parameters.get(order.get(i));
            Type p2 = other.parameters.get(other.order.get(i));
            if (!p1.equals(p2))
                return false;
        }
        if (!returnType.equals(other.returnType))
            return false;
        return true;
    }

    @Override
    public boolean equals(Type other) {
        if (other.type != TYPE.METHOD)
            return false;
        else return this.equals((MethodType) other);
    }

    public boolean isPV(String id) {
        Symbol pv = Symbol.symbol(id);
        return parameters.containsKey(pv) || variables.containsKey(pv);
    }

    public Map<Symbol, Type> getParameters() {
        return this.parameters;
    }

    public List<Symbol> getParameterOrder() {
        return this.order;
    }

    public Map<Symbol, Type> getVariables() {
        return this.variables;
    }
}
