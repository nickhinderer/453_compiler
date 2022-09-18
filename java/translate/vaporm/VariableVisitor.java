package translate.vaporm;

import cs132.vapor.ast.*;

import java.util.HashSet;
import java.util.Set;

public class VariableVisitor extends VInstr.VisitorR<Set<String>, RuntimeException> {
    public VariableVisitor() {
    }

    public Set<String> visit(VAssign assign) {
        Set<String> variables = new HashSet<>();
        if (!isIntLiteral(assign.source)) {
            variables.add(assign.source.toString());
        }
        variables.add(assign.dest.toString());
        return variables;
    }

    public Set<String> visit(VCall call) {
        Set<String> variables = new HashSet<>();
        VVarRef.Local dest = call.dest;
        if (dest != null) {
            variables.add(dest.toString());
        }
        VAddr<VFunction> addr = call.addr;
        if (!(addr instanceof VAddr.Label))
            variables.add(addr.toString());
        for (VOperand arg : call.args)
            if (!isIntLiteral(arg))
                variables.add(arg.toString());
        return variables;
    }

    public Set<String> visit(VBuiltIn builtIn) {
        Set<String> variables = new HashSet<>();
        VBuiltIn.Op op = builtIn.op;
        switch (op.name) {
            case "Error":
                return variables;
            case "HeapAllocZ": {
                VVarRef dest = builtIn.dest;
                if (dest != null)
                    variables.add(dest.toString());
                VOperand arg = builtIn.args[0];
                if (isIntLiteral(arg))
                    return variables;
                else variables.add(arg.toString());
                return variables;
            }
            case "PrintIntS": {
                VOperand arg = builtIn.args[0];
                if (isIntLiteral(arg))
                    return variables;
                else variables.add(arg.toString());
                return variables;
            }
            case "Add":
            case "Sub":
            case "MulS":
            case "Eq":
            case "Lt":
            case "LtS": {
                VVarRef dest = builtIn.dest;
                if (dest != null) {
                    variables.add(dest.toString());
                }
                VOperand arg1 = builtIn.args[0];
                if (!isIntLiteral(arg1)) variables.add(arg1.toString());
                VOperand arg2 = builtIn.args[1];
                if (isIntLiteral(arg2)) return variables;
                else variables.add(arg2.toString());
                return variables;
            }
        }
        return variables;
    }

    public Set<String> visit(VMemWrite write) {
        Set<String> variables = new HashSet<>();
        VMemRef.Global dest = (VMemRef.Global) write.dest;
        variables.add(dest.base.toString());
        VOperand source = write.source;
        if (isIntLiteral(source)) return variables;
        if (source instanceof VLabelRef) return variables;
        variables.add(source.toString());
        return variables;
    }

    public Set<String> visit(VMemRead read) {
        Set<String> variables = new HashSet<>();
        VMemRef.Global source = (VMemRef.Global) read.source;
        variables.add(source.base.toString());
        VOperand dest = read.dest;
        variables.add(dest.toString());
        variables.add(dest.toString());
        return variables;
    }

    public Set<String> visit(VBranch branch) {
        Set<String> variables = new HashSet<>();
        VOperand value = branch.value;
        if (isIntLiteral(value)) return variables;
        variables.add(value.toString());
        return variables;
    }

    public Set<String> visit(VGoto _goto) {
        return new HashSet<>();
    }

    public Set<String> visit(VReturn ret) {
        Set<String> variables = new HashSet<>();
        VOperand value = ret.value;
        if (value == null) return variables;
        if (isIntLiteral(value)) return variables;
        variables.add(value.toString());
        return variables;
    }

    static boolean isIntLiteral(VOperand op) {
        return op instanceof VLitInt;
    }
}
