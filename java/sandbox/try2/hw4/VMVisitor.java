package sandbox.try2.hw4;

import cs132.vapor.ast.*;

public class VMVisitor extends VInstr.VisitorPR<VFunction, String, RuntimeException> {
    public VMVisitor() {
    }

    public String visit(VFunction var1, VAssign var2) throws RuntimeException {
        return null;
    }

    public String visit(VFunction var1, VCall var2) throws RuntimeException {
        return null;
    }

    public String visit(VFunction var1, VBuiltIn var2) throws RuntimeException {
        return null;
    }


    public String visit(VFunction var1, VMemWrite var2) throws RuntimeException {
        return null;
    }

    public String visit(VFunction var1, VMemRead var2) throws RuntimeException {
        return null;
    }

    public String visit(VFunction var1, VBranch var2) throws RuntimeException {
        return null;
    }

    public String visit(VFunction var1, VGoto var2) throws RuntimeException {
        return null;
    }

    public String visit(VFunction var1, VReturn var2) throws RuntimeException {
        return null;
    }
}

