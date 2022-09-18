//package vapor.hw4;
//
//import cs132.vapor.ast.*;
//
//public class InstructionVisitor extends VInstr.VisitorR<Integer, RuntimeException> {
//    public InstructionVisitor() {
//    }
//
//    public Integer visit(VAssign var1) throws RuntimeException {
//        return 0;
//    }
//
//    public Integer visit(VCall var1) throws RuntimeException {
//        return 1;
//    }
//
//    public Integer visit(VBuiltIn var1) throws RuntimeException {
//        return 2;
//    }
//
//    public Integer visit(VMemWrite var1) throws RuntimeException {
//        return 3;
//    }
//
//    public Integer visit(VMemRead var1) throws RuntimeException {
//        return 4;
//    }
//
//    public Integer visit(VBranch var1) throws RuntimeException {
//        return 5;
//    }
//
//    public Integer visit(VGoto var1) throws RuntimeException {
//        return 6;
//    }
//
//    public Integer visit(VReturn var1) throws RuntimeException {
//
//        return 7;
//    }
//}
