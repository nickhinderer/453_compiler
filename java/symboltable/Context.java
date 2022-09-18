package symboltable;

public class Context {
//    public boolean variable;
//    public boolean parameter;
//    public boolean field;

    //    public boolean _class;
    public boolean method;
    public boolean variable;
    public boolean parameter;
    public boolean field;
    public String classID;
    public String methodID;

    public Context() {
        /*variable = false; parameter = false; field = false; _class = false;*/
        method = false;
        variable = false;
        parameter = false;
        field = false;
    }
    //public final static SymbolTable symbolTable;
}