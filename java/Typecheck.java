import parser.MiniJavaParser;
import symboltable.SymbolTableVisitor;
import syntaxtree.Node;
import type.Type;
import typecheck.TypeCheckException;
import typecheck.TypeCheckVisitor;
import visitor.PrettyPrinter;

public class Typecheck {
    public static void main(String[] args) {
        Node root;
        try {
            root = new MiniJavaParser(System.in).Goal();
            SymbolTableVisitor sv = new SymbolTableVisitor();
            root.accept(sv, null);
            TypeCheckVisitor tcv = new TypeCheckVisitor();
            var a = root.accept(tcv, sv.getSymbolTable());
            if (a == null)
                throw new TypeCheckException();
//            System.out.println(sv.getSymbolTable());
            System.out.println("Program type checked successfully");
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Type error");
        }
    }
}

