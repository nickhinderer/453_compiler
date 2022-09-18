//package vapor.vapor.hw3;//import TypeCheck.*;
//import symboltable.SymbolTableVisitor;
//import parser.MiniJavaParser;
//import syntaxtree.*;
//import typecheck.TypeCheckException;
//import typecheck.TypeCheckVisitor;
//import visitor.*;
//import vapor.vapor.VaporVisitor;
//
//public class J2V {
//
//    public static void main(String args[]){
//        Node root = null;
//        try {
//            root = new MiniJavaParser(System.in).Goal();
//            PrettyPrinter<Void,String> pp = new PrettyPrinter<Void, String>();
//            SymbolTableVisitor sv = new SymbolTableVisitor();
//            root.accept(sv, null);
//            TypeCheckVisitor tcv = new TypeCheckVisitor();
//            var a = root.accept(tcv, sv.getSymbolTable());
//            if (a == null)
//                throw new TypeCheckException();
//            VaporVisitor v = new VaporVisitor(sv.getSymbolTable());
//            root.accept(v, null);
//        } catch (Exception e) {
//
////			e.printStackTrace();
//            if (e.getMessage() != null)
//                System.err.println(e.getMessage());
//        }
//    }
//}
//
