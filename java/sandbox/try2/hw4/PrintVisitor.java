//package sandbox.try2.hw4;
//
//import cs132.vapor.ast.*;
//
//class PrintVisitor extends VInstr.VisitorR<String, RuntimeException> {
//    public PrintVisitor() {
//    }
//
//    @Override
//    public String visit(VCall vCall) {
//        return String.format("call %s", vCall.addr);
//    }
//
//    @Override
//    public String visit(VAssign vAssign) {
//        return String.format("%s = %s", vAssign.dest.toString(), vAssign.source.toString());
//    }
//
//    @Override
//    public String visit(VBuiltIn vBuiltIn) {
//        if (vBuiltIn.op == VBuiltIn.Op.Error)
//            return String.format("Error(%s)", vBuiltIn.args[0]);
//        String start = "";
//        if (vBuiltIn.op == VBuiltIn.Op.PrintIntS) {
//            start = String.format("PrintIntS(", vBuiltIn.args[0]);
//        } else
//            start = String.format("%s = %s(", vBuiltIn.dest.toString(), vBuiltIn.op.name);
//        for (int i = 0; i < vBuiltIn.args.length; ++i) {
//            start += vBuiltIn.args[i].toString() + " ";
//        }
//        start += ')';
//        return start;
//    }
//
//    @Override
//    public String visit(VMemWrite vMemWrite) {
//        String memory;
//        if (vMemWrite.dest instanceof VMemRef.Stack)
//            memory = String.format("%s[%d]", ((VMemRef.Stack) vMemWrite.dest).region.toString().toLowerCase(), ((VMemRef.Stack) vMemWrite.dest).index);
//        else {
//            memory = String.format("[%s+%d]", ((VMemRef.Global) vMemWrite.dest).base, ((VMemRef.Global) vMemWrite.dest).byteOffset);
//        }
//        return String.format("%s = %s", memory, vMemWrite.source.toString());
//    }
//
//    @Override
//    public String visit(VMemRead vMemRead) {
//        String memory;
//        if (vMemRead.source instanceof VMemRef.Stack)
//            memory = String.format("%s[%d]", ((VMemRef.Stack) vMemRead.source).region.toString().toLowerCase(), ((VMemRef.Stack) vMemRead.source).index);
//        else {
//            memory = String.format("[%s+%d]", ((VMemRef.Global) vMemRead.source).base, ((VMemRef.Global) vMemRead.source).byteOffset);
//        }
//        return String.format("%s = %s", vMemRead.dest.toString(), memory);
//    }
//
//    @Override
//    public String visit(VBranch vBranch) {
//        String _if = vBranch.positive ? "if" : "if0";
//        return String.format("%s %s goto %s", _if, vBranch.value.toString(), vBranch.target.toString());
//    }
//
//    @Override
//    public String visit(VGoto vGoto) {
//        return String.format("goto %s", vGoto.target.toString());
//    }
//
//    @Override
//    public String visit(VReturn vReturn) {
//        return null;
//    }
//}