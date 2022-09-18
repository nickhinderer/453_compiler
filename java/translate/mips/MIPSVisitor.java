package translate.mips;

import cs132.vapor.ast.*;

import java.util.regex.Pattern;

public class MIPSVisitor extends VInstr.VisitorR<String, RuntimeException> {
    public MIPSVisitor() {
    }

    private static boolean isInt(String s) {
        final Pattern pattern = Pattern.compile("^-?\\d+$");
        return pattern.matcher(s).matches();
    }

    private static boolean isLabel(String s) {
        return s.charAt(0) == ':';
    }

    public String visit(VCall call) {
        String addr = call.addr.toString();
        if (isLabel(addr)) return String.format("  jal %s", addr.substring(1));
        else return String.format("  jalr %s", addr);
    }

    public String visit(VMemWrite write) {
        String source = write.source.toString();//this is so much easier than how you were doing it in both iterations of spill everywhere, you retard
        boolean literal = false;
        if (isInt(source)) {
            if (Integer.parseInt(source) == 0) source = "$0";
            else literal = true;
        }
        if (write.dest instanceof VMemRef.Stack) {
            VMemRef.Stack stackMemory = (VMemRef.Stack) write.dest;
            int index = 4 * stackMemory.index;
            VMemRef.Stack.Region region = stackMemory.region;
            switch (region) {
                case Local:
                    if (literal) return String.format("  li $t8 %s\n  sw $t8 -%d($fp)", source, index + 12);
                    else return String.format("  sw %s -%d($fp)", source, index + 12);
                case In:
                    if (literal) return String.format("  li $t8 %s\n  sw $t8 %d($fp)", source, index);
                    else return String.format("  sw %s %d($fp)", source, index);
                case Out:
                    if (literal)
                        return String.format("  li $t8 %s\n  sw $t8 %d($sp)", source, index); //shouldn't really ever hit this case
//                    return String.format("li $t8 %s\n  sw $t8 -%d($sp)", source, index + 12 + F.stack.local * 4); //shouldn't really ever hit this case //yoooo this is the complex way he said to avoid, and it aint even that bad
                    else {
                        if (source.charAt(0) == ':')
                            return String.format("  la $t8 %s\n  sw $t8 %d($sp)", source.substring(1), index); //shouldn't really ever hit this case
                        else
                            return String.format("  sw %s %d($sp)", source, index); //shouldn't really ever hit this case
                    }
//                    return String.format("lw %s -%d($sp)", source, index + 12 + F.stack.local * 4); //shouldn't really ever hit this case
            }
        } else {
            VMemRef.Global heapMemory = (VMemRef.Global) write.dest;
            int byteOffset = heapMemory.byteOffset;
            String dest = heapMemory.base.toString();
            if (isLabel(source))
                return String.format("  la $t8 %s\n  sw $t8 %d(%s)", source.substring(1), byteOffset, dest);
            else if (literal) return String.format("  li $t8 %s\n  sw $t8 %d(%s)", source, byteOffset, dest);
            else return String.format("  sw %s %d(%s)", source, byteOffset, dest);

        }
        return null;
    }

    public String visit(VMemRead read) {
        String dest = read.dest.toString();
//        int offset = read.
        if (read.source instanceof VMemRef.Stack) {
            VMemRef.Stack stackMemory = (VMemRef.Stack) read.source;
            int index = 4 * stackMemory.index;
            VMemRef.Stack.Region region = stackMemory.region;
            switch (region) {
                case Local:
                    return String.format("  lw %s -%d($fp)", dest, index + 12); //index + 8
                case In:
                    return String.format("  lw %s %d($fp)", dest, index);
                case Out:
                    return String.format("  lw %s -%d($sp)", dest, index); //shouldn't really ever hit this case
//                    return null;
            }
        } else {
            VMemRef.Global heapMemory = (VMemRef.Global) read.source;
            int byteOffset = heapMemory.byteOffset;
            String source = heapMemory.base.toString();

            if (isLabel(dest)) return String.format("  la %s %s", source, dest);
            else return String.format("  lw %s %d(%s)", dest, byteOffset, source);
        }
        return null;
    }

    public String visit(VReturn var1) {
        return null;
    }

    private String builtInArithmetic(VBuiltIn builtIn, String operation) {
        String arg1 = builtIn.args[0].toString(), arg2 = builtIn.args[1].toString();
        String dest = builtIn.dest.toString();
        if (isInt(arg1)) return String.format("  li $t8 %s\n  %s %s $t8 %s", arg1, operation, dest, arg2);
        else return String.format("  %s %s %s %s", operation, dest, arg1, arg2);
    }

    private String builtInComparison(VBuiltIn builtIn, String operation) {
        String arg1 = builtIn.args[0].toString(), arg2 = builtIn.args[1].toString();
        String dest = builtIn.dest.toString();
        if (isInt(arg1)) return String.format("  li $t8 %s\n  %s %s $t8 %s", arg1, operation, dest, arg2);
        else return String.format("  %s %s %s %s", operation, dest, arg1, arg2);
    }

    public String visit(VAssign assign) {
        String source = assign.source.toString();
        String dest = assign.dest.toString();
        if (isInt(source)) return String.format("  li %s %s", dest, source);
        else if (isLabel(source)) return String.format("  la %s %s", dest, source.substring(1));
        else return String.format("  move %s %s", dest, source);
    }

    public String visit(VBuiltIn builtIn) {
        switch (builtIn.op.name) {
            case "Add": {
                return builtInArithmetic(builtIn, "addu");
            }
            case "Sub": {
                return builtInArithmetic(builtIn, "subu");
            }
            case "MulS": {
                return builtInArithmetic(builtIn, "mul");
            }
            case "Eq": {
                return builtInComparison(builtIn, "seq"); //this has been tested, it works on your MIPS interpreter
            }
            case "Lt": {
                String arg2 = builtIn.args[1].toString();
                if (isInt(arg2)) return builtInComparison(builtIn, "sltiu");
                else return builtInComparison(builtIn, "sltu");
            }
            case "LtS": {
                String arg2 = builtIn.args[1].toString();
                if (isInt(arg2)) return builtInComparison(builtIn, "slti");
                else return builtInComparison(builtIn, "slt");
            }
            case "Error": {
                String errorMessage = builtIn.args[0].toString(), label = errorMessage.contains("null pointer") ? "_str0" : "_str1";
                return String.format("  la $a0 %s\n  j _error", label);
            }
            case "HeapAllocZ": {
                String allocSize = builtIn.args[0].toString();
                String dest = builtIn.dest.toString();
                if (isInt(allocSize))
                    return String.format("  li $a0 %s\n  jal _heapAlloc\n  move %s $v0", allocSize, dest);
                else return String.format("  move $a0 %s\n  jal _heapAlloc\n  move %s $v0", allocSize, dest);
            }
            case "PrintIntS": {
                String integer = builtIn.args[0].toString();
                if (isInt(integer)) return String.format("  li $a0 %s\n  jal _print", integer);
                else return String.format("  move $a0 %s\n  jal _print", integer);
            }
        }
        return null;
    }

    public String visit(VBranch branch) {
        String value = branch.value.toString(), label = branch.target.toString().substring(1);
        if (branch.positive) return String.format("  %s %s %s", "bnez", value, label);
        else return String.format("  %s %s %s", "beqz", value, label);
    }

    public String visit(VGoto _goto) {
        String label = _goto.target.toString().substring(1);
        return String.format("  j %s", label);
    }
}