//package sandbox.try2.hw4;
//
//import cs132.vapor.ast.*;
//
//class DUVisitor extends VInstr.VisitorPR<Variable, Integer, RuntimeException> {
//    public DUVisitor() {
//    }
//
//    public Integer visit(Variable v, VAssign assign) {
//        int bit = 0;
//        if (v.name.equals(assign.dest.toString()))
//            bit += 1;
//        if (v.name.equals(assign.source.toString()))
//            bit += 2;
//        return bit;
//    }
//
//    public Integer visit(Variable v, VCall call) {
//        int bit = 0;
//        VVarRef.Local dest = call.dest;
//        if (dest != null)
//            if (v.name.equals(call.dest.toString()))
//                bit += 1;
//        VAddr<VFunction> addr = call.addr;
//        if (!(addr instanceof VAddr.Label))
//            if (v.name.equals(addr.toString()))
//                bit += 2;
//        for (VOperand arg : call.args)
//            if (v.name.equals(arg.toString()))
//                if (bit == 1 || bit == 0)
//                    bit += 2;
//        return bit;
//    }
//
//    public Integer visit(Variable v, VBuiltIn builtIn) {
//        int bit = 0;
//        VBuiltIn.Op op = builtIn.op;
//        switch (op.name) {
//            case "Error":
//                return 0;
//            case "HeapAllocZ": {
//                VVarRef dest = builtIn.dest;
//                if (dest != null)
//                    if (v.name.equals(builtIn.dest.toString()))
//                        bit += 1;
//                VOperand arg = builtIn.args[0];
//                if (v.name.equals(arg.toString()))
//                    bit += 2;
//                return bit;
//            }
//            case "PrintIntS": {
//                VOperand arg = builtIn.args[0];
//                if (v.name.equals(arg.toString()))
//                    bit += 2;
//                return bit;
//            }
//            case "Add":
//            case "Sub":
//            case "MulS":
//            case "Eq":
//            case "Lt":
//            case "LtS": {
//                VVarRef dest = builtIn.dest;
//                if (dest != null)
//                    if (v.name.equals(dest.toString()))
//                        bit += 1;
//                VOperand arg1 = builtIn.args[0];
//                if (v.name.equals(arg1.toString()))
//                    bit += 2;
//                VOperand arg2 = builtIn.args[1];
//                if (v.name.equals(arg2.toString()))
//                    if (bit == 0 || bit == 1)
//                        bit += 2;
//                return bit;
//            }
//        }
//        return 0;
//    }
//
//    public Integer visit(Variable v, VMemWrite write) {
//        int bit = 0;
//        if (!(write.dest instanceof VMemRef.Stack)) {
//            VMemRef.Global dest = (VMemRef.Global) write.dest;
//            if (v.name.equals(dest.base.toString()))
//                bit += 2;
//        }
//        VOperand source = write.source;
//        if (source instanceof VLabelRef) return bit;
//        if (v.name.equals(source.toString()))
//            if (bit == 0)
//                bit += 2;
//        return bit;
//    }
//
//    public Integer visit(Variable v, VMemRead read) {
//        int bit = 0;
//        if (!(read.source instanceof VMemRef.Stack)) {
//            VMemRef.Global source = (VMemRef.Global) read.source;
//            if (v.name.equals(source.base.toString()))
//                bit += 2;
//        }
//        VOperand dest = read.dest;
//        if (v.name.equals(dest.toString()))
//            bit += 1;
//        return bit;
//    }
//
//    public Integer visit(Variable v, VBranch branch) {
//        int bit = 0;
//        VOperand value = branch.value;
//        if (v.name.equals(value.toString()))
//            bit += 2;
//        return bit;
//    }
//
//    public Integer visit(Variable v, VGoto _goto) {
//        return 0;
//    }
//
//    public Integer visit(Variable v, VReturn ret) {
//        int bit = 0;
//        VOperand value = ret.value;
//        if (value == null) return 0;
//        if (v.name.equals(value.toString()))
//            bit += 2;
//        return bit;
//    }
//}
