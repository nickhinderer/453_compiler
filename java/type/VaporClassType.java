package type;

import symboltable.Symbol;
import symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VaporClassType {
    private Symbol name;
    private String vmt;
    private int size;
    private List<VaporMethodType> methods;
    private Map<Symbol, Integer> fieldOffsets;

    public VaporClassType(ClassType classType, boolean isMain) {
        classType.getMethodType("main").vapor = new VaporMethodType(classType.classID());
        classType.vapor = this;
    }

    public static void create(Symbol symbol, ClassType classType, SymbolTable symbolTable) {
        if (classType.isMain()) new VaporClassType(classType, true);
        else new VaporClassType(classType, symbolTable);
    }

    public String vmt() {
        return vmt;
    }

    public int allocSize() {
        return size;
    }

    public int getMethodOffset(String method) {
        int index = 0;
        for (VaporMethodType m : methods) {
            if (m.methodID.equals(method))
                return 4 * index;
            index++;
        }
        return 4 * methods.indexOf(method);
    }

    public VaporClassType(ClassType classType, SymbolTable symbolTable) {
        methods = new ArrayList<>();
        fieldOffsets = new HashMap<>();
        int fieldsCount = classType.getFieldCount();
        this.size = 4 * fieldsCount + 4;
        Map<Symbol, MethodType> classMethods = classType.getMethods();
        int offset = 0;
        for (Symbol id : classType.getMethodsOrder()) {
            VaporMethodType method = new VaporMethodType(classMethods.get(id).classID, id.toString(), classMethods.get(id));
            methods.add(method);
            classType.getMethodType(id.toString()).vapor = method;
        }
        classType.vapor = this;
        offset = 1;
        for (Symbol id : classType.getFieldsOrder()) {
            fieldOffsets.put(id, offset++);
        }
        vmt = createVmt(classType.classID());
    }

    private String createVmt(String className) {
        StringBuilder vmt = new StringBuilder("const vmt_" + className + '\n');
        for (VaporMethodType method : methods)
            vmt.append("\t:").append(method.getName()).append('\n');
        return vmt.toString();
    }

    public int methodOffset(String id) {
        return -1;
    }

    public int fieldOffset(String id) {
        return fieldOffsets.get(Symbol.symbol(id));
    }
}
