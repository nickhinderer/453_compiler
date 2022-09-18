//package com.company;
//
//import SymbolTable.Global;
//import SymbolTable.Symbol;
//import Type.Type;
//
//import java.util.Map;
//
//class Context2 {
//    public String classID;
//    public String methodID;
//    public boolean method;
//
//    public Context2() {
//        /*variable = false; parameter = false; field = false; _class = false;*/
//        method = false;
//    }
//}
//
//public class SymbolTable2 {
//    private static Global table;
//    public Context2 state;
//
//    public SymbolTable2() {
//        table = new Global();
//        state = new Context2();
//    }
//
//    public boolean addClass(String classID, ClassType classType) {
//        return table.addClass(classID, classType);
//    }
//
//    public boolean addMethod(String classID, String methodID, MethodType methodType) {
//        return table.addMethod(classID, methodID, methodType);
//    }
//
//    public boolean addField(String classID, String fieldName, Type fieldType) {
//        return table.addField(classID, fieldName, fieldType);
//    }
//
//    public boolean addParameter(String classID, String methodID, String parameterName, Type parameterType) {
//        return table.addParameter(classID, methodID, parameterName, parameterType);
//    }
//
//    public boolean addVariable(String classID, String methodID, String variableName, Type variableType) {
//        return table.addVariable(classID, methodID, variableName, variableType);
//    }
//
////    public ClassType getClassTypeInfo(String classID) {
////        return table.getClassType(classID);
////    }
//
//
//    public String getMainClassName() {
//        for (var entry : table.getClassTypes().entrySet()) { //for (Map.Entry<Symbol, ClassType> class_t : table.getClassTypes().entrySet()) {
//            if (entry.getValue().isMain())
//                return entry.getKey().toString();
//        }
//        return null;
//
//    }
//
//    public boolean subType(Type t1, Type t2) { //add same type method
//        if (t1.type == TYPE.PRIMITIVE && t2.type == TYPE.PRIMITIVE)
//            if (!((PrimitiveType) t1).subType.equals(((PrimitiveType) t2).subType))
//                return false;
//        if (t1.type == TYPE.CLASS)
//            if (((ClassType) t1).classID().equals("A"))
//                return false;
//        return true;
//    }
//
////    public Set<String> getClassNames() {
////        table.getClassNames();
////        HashSet<String> classNames = new HashSet();
////        for (Symbol name : table.getClassNames())  //for (Map.Entry<Symbol, ClassType> class_t : table.getClassTypes().entrySet()) {
////            classNames.add(name.toString());
////        return classNames;
////    }
//
//
//    private ClassType getFullClassType(String classID) {
//        ClassType t = table.type(classID);
//        ClassType full = new ClassType(t);
//        while (t.parentName() != null) {
//            ClassType parent = table.type(t.parentName());
//            boolean checks = full.inherit(parent);
//            if (!checks)
//                return null;
//            t = parent;
//        }
//        return full;
//    }
//
//    public ClassType typeC(String id) {
//        return getFullClassType(id);
//    }
//
//    public MethodType typeM(String classID, String id) {
//        return table.getMethodTypeInfo(classID, id);
//    }
//
//    public Type typeF(String classID, String id) {
//        return table.getFieldTypeInfo(classID, id);
//    }
//
//    public Type typePVF(String id) {
//        Type type;
//        type = table.getParameterTypeInfo(state.classID, state.methodID, id);
//        if (type != null)
//            return type;
//        type = table.getVariableTypeInfo(state.classID, state.methodID, id);
//        if (type != null)
//            return type;
//        return table.getFieldTypeInfo(state.classID, id);
//    }
//
//    public MethodType typeMethod(String methodID) {
//        return table.type(state.classID, methodID); //no matching var/param; search methods in current class
//
//    }
//
//    public Type typeClass(String classID) {
//        return getFullClassType(classID); //no matching var/param; search methods in current class
//
//    }
//
//    public Map<Symbol, ClassType> classes() {
//        return table.classes();
//    }
//}
