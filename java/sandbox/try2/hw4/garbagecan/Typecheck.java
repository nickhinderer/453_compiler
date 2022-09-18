//package sandbox.try2.hw4.garbagecan;
//
//import parser.MiniJavaParser;
//import symboltable.SymbolTableVisitor;
//import syntaxtree.Node;
//import typecheck.TypeCheckException;
//import typecheck.TypeCheckVisitor;
//import visitor.PrettyPrinter;
//
//public class Typecheck {
//
//	public static void main(String args[]){
//		Node root = null;
//		try {
//			root = new MiniJavaParser(System.in).Goal();
//
//			PrettyPrinter<Void,String> pp = new PrettyPrinter<Void, String>();
////			root.accept(pp, "");
//
////			SymbolTableVisit ts = new SymbolTableVisit();
////			root.accept(ts, 0);
////			SymbolTableVisitPass2 ts1 = new SymbolTableVisitPass2(ts.table);
////			root.accept(ts1, 0);
////			SymbolTableVisitPass3 ts2 = new SymbolTableVisitPass3(ts1.table);
////			root.accept(ts2, 0);
////			SymbolTable symtab = ts2.table.getTable();
////			System.out.println("Program typechecks");
//
////			SymbolTableVisitCopy ts = new SymbolTableVisitCopy();
////			root.accept(ts, 0);
////			SymbolTableVisitPass2copy ts1 = new SymbolTableVisitPass2copy(ts.table);
////			root.accept(ts1, 0);
////			SymbolTableVisitPass3copy ts2 = new SymbolTableVisitPass3copy(ts1.table);
////			root.accept(ts2, 0);
////			SymbolTable symtab = ts2.table.getSymbolTable();
//			SymbolTableVisitor sv = new SymbolTableVisitor();
//			root.accept(sv, null);
//			TypeCheckVisitor tcv = new TypeCheckVisitor();
//			var a = root.accept(tcv, sv.getSymbolTable());
//			if (a == null)
//				throw new TypeCheckException();
//			//SymbolTable symbolTable = sv.getSymbolTable();
//			//VaporVisitor v = new VaporVisitor(sv.getSymbolTable());
////			VaporVisitor v = new VaporVisitor(sv.getSymbolTable());
////			root.accept(v, null);
////			v.
//			System.out.println("Program type checked successfully");
//		} catch (Exception e) {
//
////			e.printStackTrace();
////			if (e.getMessage() != null)
////				System.err.println(e.getMessage());
//			System.out.println("Type error");
//		}
//	}
//	//NOTE!! SPLIT UP INITIALIZE AND THAT STRING INTO A NEW CLASS THAT CALLS VAPOR VISITOR<, YOU NEED TO SEPARATE THOSE. T
//	//tomorrow go lie about that apartment and get it.reherase it 20 times, sound natural, kill it
//
//	//and bood job separating symbol table from type checker, now you can make hw3 separate from hw2
//}
//
