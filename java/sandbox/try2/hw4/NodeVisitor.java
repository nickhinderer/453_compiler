//package sandbox.try2.hw4;
//
//import cs132.vapor.ast.*;
//
//public class NodeVisitor extends VInstr.VisitorPR<Integer, Node, RuntimeException> {
//    public NodeVisitor() {
//    }
//
//    public Node visit(Integer num, VAssign assign) {
//        Node node = new Node(num);
//        node.instruction = assign;
//        if (!isIntLiteral(assign.source)) {
//            Variable use = Variable.variable(assign.source.toString());
//            node.addUse(use);
//        }
//        Variable def = Variable.variable(assign.dest.toString());
//        def.interval(num);
//        node.addDef(def);
//        node.addUse(def);
//        return node;
//    }
//
//    public Node visit(Integer num, VCall call) {
//        Node node = new Node(num);
//        node.instruction = call;
//        VVarRef.Local dest = call.dest;
//        if (dest != null) {
//            node.addDef(Variable.variable(dest.toString()));
//            node.addUse(Variable.variable(dest.toString()));
//        }
//        VAddr<VFunction> addr = call.addr;
//        if (!(addr instanceof VAddr.Label)) node.addUse(Variable.variable(addr.toString()));
//        for (VOperand arg : call.args)
//            if (!isIntLiteral(arg))
//                node.addUse(Variable.variable(arg.toString()));
//        return node;
//    }
//
//    public Node visit(Integer num, VBuiltIn builtIn) {
//        Node node = new Node(num);
//        node.instruction = builtIn;
//        VBuiltIn.Op op = builtIn.op;
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
//                if (dest != null) {
//                    node.addDef(Variable.variable(dest.toString()));
//                    node.addUse(Variable.variable(dest.toString()));
//                }
//                VOperand arg1 = builtIn.args[0];
//                if (!isIntLiteral(arg1)) node.addUse(Variable.variable(arg1.toString()));
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
//        VMemRef.Global dest = (VMemRef.Global) write.dest;
//        node.addUse(Variable.variable(dest.base.toString()));
//        VOperand source = write.source;
//        if (isIntLiteral(source)) return node;
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
//        node.addDef(Variable.variable(dest.toString()));
//        node.addUse(Variable.variable(dest.toString()));
//        return node;
//    }
//
//    public Node visit(Integer num, VBranch branch) {
//        Node node = new Node(num);
//        node.instruction = branch;
//        VOperand value = branch.value;
//        if (isIntLiteral(value)) return node;
//        node.addUse(Variable.variable(value.toString()));
//        return node;
//    }
//
//    public Node visit(Integer num, VGoto _goto) {
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
//        node.addUse(Variable.variable(value.toString()));
//        return node;
//    }
//
//    static boolean isIntLiteral(VOperand op) {
//        return op instanceof VLitInt;
//    }
//}
