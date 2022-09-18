//package sandbox;
//
//import cs132.util.ProblemException;
//import cs132.vapor.ast.*;
//import cs132.vapor.parser.VaporParser;
//import cs132.vapor.ast.VBuiltIn.Op;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//
//public class PG {
//
//
//    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
//        VBuiltIn.Op[] ops = {VBuiltIn.Op.Add, VBuiltIn.Op.Sub, VBuiltIn.Op.MulS, VBuiltIn.Op.Eq, VBuiltIn.Op.Lt, VBuiltIn.Op.LtS, VBuiltIn.Op.PrintIntS, VBuiltIn.Op.HeapAllocZ, VBuiltIn.Op.Error,};
//        boolean allowLocals = true;
////        String[] registers = {"v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8",};
//        String[] registers = {};
//        boolean allowStack = false;
//
//        VaporProgram program;
//        try {
//            program = VaporParser.run(new InputStreamReader(in), 1, 1, java.util.Arrays.asList(ops), allowLocals, registers, allowStack);
//        } catch (ProblemException ex) {
//            err.println(ex.getMessage());
//            return null;
//        }
//
//        return program;
//    }
//
//    public static void main(String[] args) {
//        InputStream in = null;
//        try {
////            in = System.in;
//            in = new FileInputStream("tests/translate/vapor/LinkedList.vapor");
////        } catch (Exception e) {
//        } catch (FileNotFoundException e) {
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
//
//        }
//        assert p != null;
//        {
//            PG pg = new PG();
//            List<Graph> functions = pg.createGraph(p);
//            System.out.println();
//            for (Graph function : functions) {
//                new LS().linearScan(function);
//                new LS().printSpillsAndRegisterMap(function);
//            }
////            LinearScan.LS.linearScan(functions.get(17));
////            LinearScan.LS.printSpillsAndRegisterMap(functions.get(17));
//            (new LS()).linearScan(functions.get(5));
//            new LS().printSpillsAndRegisterMap(functions.get(5));
//        }
//    }
//
//    public List<Graph> createGraph(VaporProgram p) {
//        List<Graph> graphs = new LinkedList<>();
//        DUVisitor duv = new DUVisitor();
//        for (VFunction F : p.functions) {
//            Graph g = new Graph();
//            g.function = F;
//            Node parameters = new Node(0);
//            parameters.params = new ArrayList<>();
//            for (VVarRef.Local param : F.params) parameters.addParameter(Variable.variable(param.toString()));
//            g.node(parameters);
//            for (int i = 0; i < F.body.length; i++) {
//                VInstr instr = F.body[i];
//                Node node = instr.accept(i + 1, duv);
//                g.node(node);
//            }
//
//            for (Node node : g.nodes) {
//                if (node.num == 0) for (Variable param : node.params) param.firstDef = 0;
//                if (node.def == null && node.use.isEmpty()) continue;
//                if (node.def != null) if (node.def.firstDef == -1) node.def.firstDef = node.num;
//                if (!node.use.isEmpty()) for (Variable use : node.use) use.lastUse = node.num;
//            }
//            g.collectVariables();
//            g.createIntervals();
//            graphs.add(g);
//            Variable.reset();
//        }
//        return graphs;
//    }
//}
//
//
//class DUVisitor extends VInstr.VisitorPR<Integer, Node, RuntimeException> {
//    public DUVisitor() {
//    }
//
//    public Node visit(Integer num, VAssign assign) {
//        Node node = new Node(num);
//        node.instruction = assign;
//
//        if (!isIntLiteral(assign.source)) {
//            Variable use = Variable.variable(assign.source.toString());
//            Variable.Interval useInterval = new Variable.Interval(use);
//            node.addUse(use);
//        }
//
//        Variable def = Variable.variable(assign.dest.toString());
//        def.interval(num);
//        node.addDef(def);
//
//        return node;
//    }
//
//    public Node visit(Integer num, VCall call) {
//        Node node = new Node(num);
//        node.instruction = call;
//        VVarRef.Local dest = call.dest;
//        if (dest != null) node.addDef(Variable.variable(dest.toString()));
//        VAddr<VFunction> addr = call.addr;
//        if (!(addr instanceof VAddr.Label)) node.addUse(Variable.variable(addr.toString()));
//        for (VOperand arg : call.args) if (!isIntLiteral(arg)) node.addUse(Variable.variable(arg.toString())); //can just check litint, because if it is static, then only other choice is label ref, otherwise it is not static, otherwise it iis vvarref (can't be memref, and within varref it can't be register bc its vapor not vaporm)
//        //ask if we have to translate all of vapor-m
//        return node;
//    }
//
//    public Node visit(Integer num, VBuiltIn builtIn) {
//        Node node = new Node(num);
//        node.instruction = builtIn;
//
//        //shortcut: yy
//        Op op = builtIn.op;
//        switch (op.name) {
//            case "Error":
//                return node;
//            case "HeapAllocZ": {
//                VVarRef dest = builtIn.dest;
//                if (dest != null) node.addDef(Variable.variable(dest.toString()));
//                VOperand arg = builtIn.args[0];
//                if (isIntLiteral(arg)) return node;
//                else node.addUse(Variable.variable(arg.toString()));
//                return node;
//            }
//            case "PrintIntS": {
//                VOperand arg = builtIn.args[0];
//                if (isIntLiteral(arg)) return node;
//                else node.addUse(Variable.variable(arg.toString()));
//                return node;
//            }
//            case "Add":
//            case "Sub":
//            case "MulS":
//            case "Eq":
//            case "Lt":
//            case "LtS": {
//                VVarRef dest = builtIn.dest;
//                if (dest != null) node.addDef(Variable.variable(dest.toString()));
//                VOperand arg1 = builtIn.args[0];
//                if (isIntLiteral(arg1)) return node;
//                else node.addUse(Variable.variable(arg1.toString()));
//                VOperand arg2 = builtIn.args[1];
//                if (isIntLiteral(arg2)) return node;
//                else node.addUse(Variable.variable(arg2.toString()));
//                return node;
//            }
//        }
//        return new Node(num);
//    }
//
//    public Node visit(Integer num, VMemWrite write) {
//        Node node = new Node(num);
//        node.instruction = write;
//        //can't be 'stack' because that is only in vaporm
//        VMemRef.Global dest = (VMemRef.Global) write.dest;
//        node.addUse(Variable.variable(dest.base.toString()));
//        VOperand source = write.source;
//        if (isIntLiteral(source))
//            return node; //not going to be static (lit int, lit string, label) wait it could be a lebel
//        if (source instanceof VLabelRef) return node;
//        node.addUse(Variable.variable(source.toString()));
//        return node;
//    }
//
//    public Node visit(Integer num, VMemRead read) {
//        Node node = new Node(num);
//        node.instruction = read;
//        VMemRef.Global source = (VMemRef.Global) read.source;
//        node.addUse(Variable.variable(source.base.toString()));
//        VOperand dest = read.dest;
////        if (!(dest instanceof ))
//        //its not going to be a label ref
//        //ask teacher, so you say the hw is not using the previous homework input
//        //does that mean we have to cover everything in vapor even if it is not in MJ? If so, why are we not doing computed gotos and where specifically
//        // does it say what and what not we have to cover.    because we are not doing
//        //computed
//        node.addDef(Variable.variable(dest.toString()));
//        return node;
//    }
//
//    public Node visit(Integer num, VBranch branch) {
//        Node node = new Node(num);
//        VOperand value = branch.value;
//        if (isIntLiteral(value)) return node;
//        node.addUse(Variable.variable(value.toString()));
//        return node;
//    }
//
//    public Node visit(Integer num, VGoto _goto) {
//        //no computed goto, couldn't be Var
////        if (_goto.target instanceof VAddr.Var)
////            node.addUse(new Variable(_goto.target.toString()));
//        Node node = new Node(num);
//        node.instruction = _goto;
//        return node;
//    }
//
//    public Node visit(Integer num, VReturn ret) {
//        Node node = new Node(num);
//        node.instruction = ret;
//        VOperand value = ret.value;
//        if (value == null) return node;
//        if (isIntLiteral(value)) return node;
//        //not a label (can't return functions), not a register (vapor, not vaporm), not any other type of static (lit int, label ref, and kind of strings but they are different)
//        //so its going to be a VVarRef and a local one (only other option is register)
//        node.addUse(Variable.variable(value.toString()));
//        return node;
//    }
//
//    static boolean isIntLiteral(VOperand op) {
//        return op instanceof VLitInt;
//    }
//
//}