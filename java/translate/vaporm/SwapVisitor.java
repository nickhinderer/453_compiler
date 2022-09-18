package translate.vaporm;

import cs132.util.SourcePos;
import cs132.vapor.ast.*;

import java.util.ArrayList;

public class SwapVisitor extends VInstr.VisitorPR<RTPair, VInstr, RuntimeException> {
    public SwapVisitor() {
    }

    VOperand swapOperand(VOperand vOperand, String temporary, Register register) {
        if (vOperand instanceof VOperand.Static) return vOperand;
        if (vOperand instanceof VLitStr) return vOperand;
        if (vOperand instanceof VVarRef) return swapVarRef((VVarRef) vOperand, temporary, register);
        return null;
    }

    VVarRef swapVarRef(VVarRef vVarRef, String temporary, Register register) {
        if (vVarRef == null) return null;
        if (vVarRef instanceof VVarRef.Register) return vVarRef;
        if (!temporary.equals(vVarRef.toString())) return vVarRef;
        return new VVarRef.Register(vVarRef.sourcePos, register.toString().substring(1), -1);
    }

    @Override
    public VInstr visit(RTPair map, VAssign vAssign) {
        if (!map.temporary.equals(vAssign.dest.toString()) && !map.temporary.equals(vAssign.source.toString()))
            return vAssign;
        VOperand source;
        if (map.temporary.equals(vAssign.source.toString()))
            source = swapOperand(vAssign.source, map.temporary, map.register);
        else source = vAssign.source;
        VVarRef dest;
        if (!(vAssign.dest instanceof VVarRef.Register)) {
            if (map.temporary.equals(vAssign.dest.toString()))
                dest = swapVarRef(vAssign.dest, map.temporary, map.register);
            else dest = vAssign.dest;
        } else {
            dest = vAssign.dest;
        }
        return new VAssign(vAssign.sourcePos, dest, source);
    }

    @Override
    public VInstr visit(RTPair map, VCall vCall) {
        if (!(vCall.addr instanceof VAddr.Var)) return vCall;
        if (((VAddr.Var) vCall.addr).var instanceof VVarRef.Register) return vCall;
        VVarRef ref = swapVarRef(((VAddr.Var) vCall.addr).var, map.temporary, map.register);
        VAddr.Var<VFunction> addr = new VAddr.Var(ref);
        return new VCall(vCall.sourcePos, addr, vCall.args, vCall.dest);
    }

    @Override
    public VInstr visit(RTPair map, VBuiltIn vBuiltIn) {
        ArrayList<VOperand> args = new ArrayList<>();
        for (VOperand arg : vBuiltIn.args) {
            args.add(swapOperand(arg, map.temporary, map.register));
        }
        return new VBuiltIn(vBuiltIn.sourcePos, vBuiltIn.op, args.toArray(vBuiltIn.args), swapVarRef(vBuiltIn.dest, map.temporary, map.register));
    }

    @Override
    public VInstr visit(RTPair map, VMemWrite vMemWrite) {
        SourcePos pos = vMemWrite.sourcePos;
        VMemRef memRef;
        if (vMemWrite.dest instanceof VMemRef.Global) {
            VMemRef.Global g = (VMemRef.Global) vMemWrite.dest;
            if (g.base instanceof VAddr.Var) {
                VAddr.Var v = (VAddr.Var) g.base;
                VAddr.Var nv = new VAddr.Var(swapVarRef(v.var, map.temporary, map.register));
                memRef = new VMemRef.Global(vMemWrite.dest.sourcePos, nv, g.byteOffset);
            } else {
                memRef = vMemWrite.dest;
            }
        } else
            memRef = vMemWrite.dest;
        VOperand vo = swapOperand(vMemWrite.source, map.temporary, map.register);
        return new VMemWrite(vMemWrite.sourcePos, memRef, swapOperand(vMemWrite.source, map.temporary, map.register));
    }

    @Override
    public VInstr visit(RTPair map, VMemRead vMemRead) {
        SourcePos pos = vMemRead.sourcePos;
        VMemRef memRef;
        if (vMemRead.source instanceof VMemRef.Global) {
            VMemRef.Global g = (VMemRef.Global) vMemRead.source;
            if (g.base instanceof VAddr.Var) {
                VAddr.Var v = (VAddr.Var) g.base;
                VAddr.Var nv = new VAddr.Var(swapVarRef(v.var, map.temporary, map.register));
                memRef = new VMemRef.Global(vMemRead.source.sourcePos, nv, g.byteOffset);
            } else {
                memRef = vMemRead.source;
            }
        } else
            memRef = vMemRead.source;
        VOperand vo = swapOperand(vMemRead.dest, map.temporary, map.register);
        return new VMemRead(vMemRead.sourcePos, swapVarRef(vMemRead.dest, map.temporary, map.register), memRef);
    }

    @Override
    public VInstr visit(RTPair map, VBranch vBranch) {
        VOperand value = swapOperand(vBranch.value, map.temporary, map.register);
        return new VBranch(vBranch.sourcePos, vBranch.positive, value, vBranch.target);
    }

    @Override
    public VInstr visit(RTPair map, VGoto vGoto) {
        return vGoto;
    }

    @Override
    public VInstr visit(RTPair map, VReturn vReturn) {
        return vReturn;
    }
}


