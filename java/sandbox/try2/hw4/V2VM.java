//package sandbox.try2.hw4;
//
//import cs132.util.ProblemException;
//import cs132.vapor.ast.*;
//import cs132.vapor.parser.VaporParser;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.PrintStream;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//public class V2VM {
//    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
//        VBuiltIn.Op[] ops = {VBuiltIn.Op.Add, VBuiltIn.Op.Sub, VBuiltIn.Op.MulS, VBuiltIn.Op.Eq, VBuiltIn.Op.Lt, VBuiltIn.Op.LtS, VBuiltIn.Op.PrintIntS, VBuiltIn.Op.HeapAllocZ, VBuiltIn.Op.Error,};
//        boolean allowLocals = true;
//        String[] registers = {};
//        boolean allowStack = false;
//        VaporProgram program;
//        try {
//            program = VaporParser.run(new InputStreamReader(in), 1, 1, java.util.Arrays.asList(ops), allowLocals, registers, allowStack);
//        } catch (ProblemException ex) {
//            err.println(ex.getMessage());
//            return null;
//        }
//        return program;
//    }
//
//    public static void main(String[] args) {
//        InputStream in = null;
//        try {
////            in = new FileInputStream("tests/translate/vapor/BubbleSort.vapor");
//            in = System.in;
////        } catch (FileNotFoundException e) {
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        VaporProgram p;
//        try {
//            p = parseVapor(in, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//            System.out.println(e);
//            throw new RuntimeException(e);
//        }
//        assert p != null;
//        {
//            for (VDataSegment segment : p.dataSegments) {
//                System.out.println("const " + segment.ident);
//                for (VOperand.Static value : segment.values)
//                    System.out.println('\t' + value.toString());
//            }
//            List<Graph> functions = createGraphs(p);
//            for (Graph function : functions) {
//                new LinearScan().linearScan(function);
////                LinearScan.printSpillsAndRegisterMap(function);
//                VMFunction functionVM = new VMFunction(function);
//                System.out.println();
//            }
//        }
//    }
//
//    public static List<Graph> createGraphs(VaporProgram p) {
//        List<Graph> graphs = new LinkedList<>();
//        NodeVisitor nv = new NodeVisitor();
//        for (VFunction F : p.functions) {
//            Graph functionCFG = new Graph();
//            functionCFG.original = F;
//            Node parameters = new Node(0);
//            parameters.params = new ArrayList<>();
//            for (VVarRef.Local param : F.params) {
//                parameters.addParameter(Variable.variable(param.toString()));
//            }
//            functionCFG.addNode(parameters);
//            for (int i = 0; i < F.body.length; i++) {
//                VInstr instr = F.body[i];
//                Node node = instr.accept(i + 1, nv);
//                functionCFG.addNode(node);
//            }
//            for (Node node : functionCFG.nodes) {
//                if (node.num == 0)
//                    for (Variable param : node.params)
//                        param.firstDef = 0;
//                if (node.def == null && node.use.isEmpty())
//                    continue;
//                if (node.def != null)
//                    if (node.def.firstDef == -1) {
//                    node.def.firstDef = node.num;
//                    node.def.lastUse = node.num;
//                }
//                if (!node.use.isEmpty())
//                    for (Variable use : node.use)
//                        use.lastUse = node.num;
//            }
//            functionCFG.collectVariables();
//            functionCFG.createIntervals();
//            graphs.add(functionCFG);
//            Variable.reset();
//        }
//        return graphs;
//    }
//}
