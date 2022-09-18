package translate.vapor;//import TypeCheck.*;
import symboltable.SymbolTableVisitor;
import parser.MiniJavaParser;
import syntaxtree.Node;

public class J2V {

    public static void main(String[] args){
        Node root;
        try {
            root = new MiniJavaParser(System.in).Goal();
            SymbolTableVisitor sv = new SymbolTableVisitor();
            root.accept(sv, null);
            VaporVisitor v = new VaporVisitor(sv.getSymbolTable());
            root.accept(v, null);
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.err.println(e.getMessage());
        }
    }
}

