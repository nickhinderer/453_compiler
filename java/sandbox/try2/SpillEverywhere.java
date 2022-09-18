//package vapor.vaporm.hw4;
//
//import cs132.util.ProblemException;
//import cs132.vapor.ast.*;
//import cs132.vapor.parser.VaporParser;
//
//import java.io.*;
//import java.util.*;
//
//import static vapor.vaporm.hw4.SpillEverywhere.*;
//
//public class SpillEverywhere {
//    public static VaporProgram parseVapor(InputStream in, PrintStream err)
//            throws IOException {
//        VBuiltIn.Op[] ops = {
//                VBuiltIn.Op.Add, VBuiltIn.Op.Sub, VBuiltIn.Op.MulS, VBuiltIn.Op.Eq, VBuiltIn.Op.Lt, VBuiltIn.Op.LtS,
//                VBuiltIn.Op.PrintIntS, VBuiltIn.Op.HeapAllocZ, VBuiltIn.Op.Error,
//        };
//        boolean allowLocals = true;
//        String[] registers = null;
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
////        try {
////            in = new FileInputStream("tests/translate/vapor/TreeVisitor.vapor");
////        } catch (FileNotFoundException e) {
////            throw new RuntimeException(e);
////        }
//        in = System.in;
//        VaporProgram p;
//        try {
//            p = parseVapor(in, null);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        assert p != null;
//        //new function
//        for (VDataSegment segment : p.dataSegments) {
//            System.out.println("const " + segment.ident);
//            for (VOperand.Static label : segment.values) {
//                VLabelRef l = (VLabelRef) label;
//                System.out.println(':' + l.ident);
//            }
//            System.out.println();
//        }
//        ArrayList<InstructionsWrapper> functions = new ArrayList<>();
//        for (VFunction F : p.functions) {
//            InstructionsWrapper newFunctionInstructions = new InstructionsWrapper();
//            //step 1
//            Instruction signature = createSignature(F);
//            newFunctionInstructions.add(signature);
//            List<Instruction> calls = transmuteCalls(F);
//            if (calls != null)
//                calls.forEach(newFunctionInstructions::add);
//            Instruction ret = transmuteReturn(F);
//            newFunctionInstructions.add(ret);
//            List<Instruction> statements = transmuteStatements(F);
//            statements.forEach(newFunctionInstructions::add);
////            instructions.add(Arrays.asList(signature)); //Arrays.asList() List.of()
//            int offset = F.sourcePos.line;
//            for (VCodeLabel label : F.labels) {
//                int line = label.sourcePos.line;
//                Instruction instruction = new Instruction(line, label.ident + ':');
//                newFunctionInstructions.add(instruction);
//            }
//            functions.add(newFunctionInstructions);
//
//        }
//        for (InstructionsWrapper function : functions) {
//            function.print();
//        }
//    }
//
//    private static List<Instruction> transmuteStatements(VFunction F) {
//        List<Instruction> statements = new ArrayList<>();
//        SEV v = new SEV();
//        for (VInstr vaporInstruction : F.body) {
//            int originalLine = vaporInstruction.sourcePos.line;
//            Instruction instruction = new Instruction(originalLine);
//            List<String> transmutation = vaporInstruction.accept(F, v);
//            if (transmutation != null)
//                transmutation.forEach(instruction::add);
//            statements.add(instruction);
//        }
//        return statements;
//    }
//
//    static Instruction transmuteReturn(VFunction F) {
//        VReturn ret = (VReturn) F.body[F.body.length - 1];
//        Instruction instruction = new Instruction(ret.sourcePos.line);
//        if (ret.value == null) {
//            instruction.add("ret");
//            return instruction;
//        } else if (isLiteral(ret.value)) { //can't return label reference (unless you could return functions), so must be int literal
//            VLitInt literal = (VLitInt) ret.value;
//            instruction.add(String.format("$v0 = %d", literal.value));
//            instruction.add("ret");
//            return instruction;
//        } else {
//            String restore = restore(F, ret.value, 0);
//            instruction.add(restore);
//            instruction.add("$v0 = $t0");
//            instruction.add("ret");
//            return instruction;
//        }
//    }
//
//    static List<String> backup(VFunction F, VOperand variable) {
//
//        return null;
//    }
//
//    static void restore(VFunction F, String variable) {
//
//    }
//
//    //get register method?
//
//    //    private static int reg() {
////        return i % 2 + 1;
////    }
//    static List<Instruction> transmuteCalls(VFunction F) {
//        List<Instruction> calls = new ArrayList<>();
//        InstructionTypeVisitor v = new InstructionTypeVisitor();
//        for (VInstr instruction : F.body) {
//            int type = instruction.accept(v);
//            if (type == 1) {
//                int original;
//                List<String> lines = new ArrayList<>();
//                {
//                    VCall call = (VCall) instruction;
//                    original = call.sourcePos.line;
//                    int register = 0;
//                    int i = 0;
//                    for (VOperand arg : call.args) {
//                        String restore = restore(F, arg, 0);
//                        lines.add(restore);
//                        lines.add(String.format("out[%d] = $t0", i));
//                        i++;
//                    }
//                    {
//                        //handle label case later
//                        if (call.addr instanceof VAddr.Label) {
//                            VAddr.Label<VFunction> addr = (VAddr.Label<VFunction>) call.addr;
//                            VLabelRef<VFunction> label = addr.label;
//                            lines.add(String.format("call :%s", label.ident));
//                        } else {
//                            VAddr.Var<VFunction> address = (VAddr.Var<VFunction>) call.addr;
//                            VVarRef.Local variable = (VVarRef.Local) address.var;
//                            String restore = restore(F, variable.ident, 0);
//                            lines.add(restore);
//                            lines.add("call $t0");
//                        }
//                        lines.add("$t0 = $v0");
//                    }
//
//                    if (call.dest != null) {
//                        String backup = backup(F, call.dest.ident, 0);
//                        lines.add(backup);
//                    }
////                    VAddr<VAddr.Var> addr = (VAddr<VAddr.Var>) call.addr;
//                }
//                Instruction call = new Instruction(original);
//                for (String line : lines)
//                    call.add(line);
//                calls.add(call);
//            }
//        }
//        return calls;
//    }
//
//    static String backup(VFunction F, String id, int register) {
//        //don't even bother checking if it is static, i couldn't be
//        //just think, do you ever see "1 = a + 2" in programming? no
////        VVarRef.Local local = (VVarRef.Local) variable; //must be local, can't be register b/c its vapor not vapor-m. local: param or var, find out now
////        String id = local.ident;
//        //test if it is local variable in the function. if it is not local, it must be a parameter
//        String backup;
//        if (isParam(F, id)) {
//            int index = paramIndex(F, id);
//            backup = String.format("in[%d] = $t%d", index, register);
//        } else {
//            int index = Arrays.asList(F.vars).indexOf(id);
//            backup = String.format("local[%d] = $t%d", index, register);
//        }
//        return backup;
//    }
//
//    static String restore(VFunction F, VOperand variable, int register) {
//        String restore;
//        int i;
//        if (isLiteralOrLabel(variable)) {
//            /*
//             * program has type checked, but that doesn't matter because hw4 is independent
//             * of hw2 and the assignment says "take a valid vapor program and convert it to vapor-m
//             * so technically you'd want to cover that case if it is valid in vapor, but in this
//             * case it is not because you can't pass string literals or labels as arguments in vapor
//             *
//             * VLitInt literal = (VLitInt) variable;
//             * return String.format("$t%d = %d", register, literal.value);
//             *
//             */
//            /*
//            * correction: this is a general function that should work in any context
//            * and while the above statement is true for function calls, it is not true for
//            * assignments and memory writes. the old fuction before writing this is above and
//            * i will correct it below |
//            *                         v
//            */
//            if (isLiteral(variable)) {
//                VLitInt literal = (VLitInt) variable;
//                return String.format("$t%d = %d", register, literal.value);
//                //tbh you should get rid of this and do the checking for static before you
//                //even call this
//                //instead you should make a method called isLiteralOrLabel and callthat first
//                //before calling restore
//            } else {
//                //VLabelRef label = (VLabelRef) variable;
//                return null;
//            }
//        }
//        //otherwise it is variable or param, check for which below...
//        VVarRef.Local local = (VVarRef.Local) variable; //must be local, can't be register b/c its vapor not vapor-m. local: param or var, find out now
//        String id = local.ident;
//        //test if it is local variable in the function. if it is not local, it must be a parameter
//        if (isParam(F, id)) {
//            int index = paramIndex(F, id);
//            restore = String.format("$t%d = in[%d]", register, index);
//        } else {
//            int index = Arrays.asList(F.vars).indexOf(id);
//            restore = String.format("$t%d = local[%d]", register, index);
//        }
//        return restore;
//    }
//
//    static String restore(VFunction F, String id, int register) {
//        String restore;
//        int i;
//        //test if it is local variable in the function. if it is not local, it must be a parameter
//        if (isParam(F, id)) {
//            int index = paramIndex(F, id);
//            restore = String.format("$t%d = in[%d]", register, index);
//        } else {
//            int index = Arrays.asList(F.vars).indexOf(id);
//            restore = String.format("$t%d = local[%d]", register, index);
//        }
//        return restore;
//    }
//
//    static int paramIndex(VFunction F, String param) {
//        for (int i = 0; i < F.params.length; i++)
//            if (param.equals(F.params[i].ident))
//                return i;
//        return -1;
//    }
//
//    static boolean isParam(VFunction F, String variable) {
//        ArrayList<String> params = new ArrayList<>();
//        for (VVarRef.Local param : F.params) {
//            params.add(param.ident);
//        }
//        return params.contains(variable);
//    }
//
//    //check whether it is a local variable. if not, then it must be a parameter
//    static boolean isVariable(VFunction F, String variable) {
//        return Arrays.asList(F.vars).contains(variable);
//    }
//
//    static Instruction createSignature(VFunction F) {
//        int in = F.params.length, local = F.vars.length, out;
//        InstructionTypeVisitor v = new InstructionTypeVisitor();
//        int maximum = 0;
//        for (VInstr instruction : F.body) {
//            int type = instruction.accept(v);
//            if (type == 1) {
//                VCall call = (VCall) instruction;
//                int argSize = call.args.length;
//                //noinspection ManualMinMaxCalculation
//                maximum = argSize > maximum ? argSize : maximum;
//            }
//        }
//        out = maximum;
//        return new Instruction(0, String.format("func %s [in %d, out %d, local %d]", F.ident, in, out, local));
//    }
//
//    static boolean isLiteralOrLabel(VOperand operand) {
//        if (! (operand instanceof VOperand.Static))
//            return false;
//        return isLabel(operand) || isLiteral(operand);
//    }
//
//    static boolean isLiteral(VOperand operand) {
//        return operand instanceof VLitInt;
//    }
//
//    static boolean isLabel(VOperand operand) {
//        return operand instanceof VLabelRef;
//    }
//}
//
////class Register {
////    String register;
////    String variable;
////}
//
//class InstructionsWrapper {
//    List<Instruction> instructions;
//
//    InstructionsWrapper() {
//        instructions = new ArrayList<>();
//    }
//
//    void add(Instruction instruction) {
//        if (instruction == null)
//            return;
//        if (instruction.lines.isEmpty())
//            return;
//        int index = instruction.original, size = instructions.size(); //index to insert
//        boolean added = false;
////        for (int i = size-1; i >= 0; i--)
//        for (int i = 0; i < size; i++)
//            if (index <= instructions.get(i).original) {
//                instructions.add(i, instruction);
//                added = true;
//                break;
//            }
//        if (!added)
//            instructions.add(instruction);
//    }
//
//    public void print() {
//        for (Instruction instruction : instructions)
//            for(String line : instruction.lines)
//                System.out.println(line);
//        System.out.print("\n\n\n\n\n\n\n\n");
//    }
//}
//
//
///*
// *
// * EX:
// * t.1 = 2           line: 1
// * t.2 = 3           line: 2
// *    |
// *    v
// * $t0 = 2           original: 1
// * local[1] = $t0    original: 1    |    lines: {"$t0 = 2", "local[1] = $t0"}, orig: 1
// * $t0 = 3           original: 2
// * local[2] = $t0    original: 2
// *
// * InstructionsWrapper : {
// *   instructions : [
// *     {
// *        lines : ["$t0 = 2", "local[1] = $t0"],
// *        original : 1
// *     },
// *     {
// *       lines : ["$t0 = 3", "local[2] = $t0"],
// *      original : 2
// *     }
// *   ]
// * }
// *
// *
// *
// */
//
//class Instruction {
//    List<String> lines; //each line is an invididual instruction itself, they just all represent the origial instruction in vapor hence why the're gruoped together in a single instruction even though the "instruction" itself is composed of individual instructions, it is jsut  supossed to represent the grouping in the original vapr program
//    // instruction number of the instruction in the original
//    // function from which this instruction ijs derived
//    // (spill everywhere takes each instruction in its own
//    // context, doesn't care about other ones, so that means
//    // if you insert instructions they all have to be grouped
//    // with the original)
//    int original;
//
//    Instruction(int original, String line) {
//        this.original = original;
//        lines = new ArrayList<>(Arrays.asList(line));
//    }
//
//    Instruction(int original) {
//        this.original = original;
//        lines = new ArrayList<>();
//    }
//
//    void add(String line) {
//        lines.add(line);
//    }
//}
//
////class Line {
////    Line(int original, String instruction) {
////        this.instruction = instruction;
////    }
////
////
////    String instruction;
////}
//class SEV extends VInstr.VisitorPR<VFunction, List<String>, RuntimeException> {
//    public SEV() {
//
//    }
//
//    public List<String> visit(VFunction F, VAssign assign) {
//        List<String> instructions = new ArrayList<>();
//        VOperand source = assign.source;
//        String value;
//        if (isLiteralOrLabel(source)) {
//            if (source instanceof VLabelRef) {
//                VLabelRef label = (VLabelRef) source;
//                value = ':' + label.ident;
//            } else {
//                VLitInt literal = (VLitInt) source;
//                value = Integer.toString(literal.value);
//            }
//        } else {
//            String restore = restore(F, source, 1);
//            instructions.add(restore);
//            value = "$t1";
//        }
//        String assignStatement = String.format("$t0 = %s", value);
//        instructions.add(assignStatement);
//
//        VVarRef.Local dest = (VVarRef.Local) assign.dest; //remember: this is vapor, so it can't be a register
//        String id = dest.ident;
//        String backup = backup(F, id, 0);
//        instructions.add(backup);
//        return instructions;
//    }
//
//    public List<String> visit(VFunction F, VCall call) {
//        return null;
//    }
//
//    public List<String> visit(VFunction F, VBuiltIn builtIn) {
//        List<String> instructions = new ArrayList<>();
//        if (builtIn.op.name.equals("Error")) {
//            VLitStr str = (VLitStr) builtIn.args[0];
//            String error = String.format("Error(\"%s\")", str.value);
//            instructions.add(error);
//            return instructions;
//        }
//        int register = 1;
//        Map<Integer, String> registers = new HashMap<>();
//        //backup
//        for (VOperand arg : builtIn.args) {
//            if (!isLiteralOrLabel(arg)) {
//                VVarRef.Local variable = (VVarRef.Local) arg;
//                String restore = restore(F, arg, register++);
//                instructions.add(restore);
//            }
//        }
//        StringBuilder builtInStatement;
//        if (builtIn.dest != null)
//            builtInStatement = new StringBuilder("$t0 = " + builtIn.op.name + '(');
//        else
//            builtInStatement = new StringBuilder(builtIn.op.name + '(');
//        register = 1;
//        for (VOperand arg : builtIn.args) {
//            if (!isLiteralOrLabel(arg)) {
//                builtInStatement.append(String.format(" $t%d ", register++));
//            } else {
//                if (isLiteral(arg)) {
//                    VLitInt literal = (VLitInt) arg;
//                    builtInStatement.append(Integer.toString(literal.value)).append(' ');
//                } else {
//                    //something went wrong, you shouldn't pass labels to builtins
//                    return null;
//                }
//            }
//        }
//        builtInStatement.append(')');
//        instructions.add(builtInStatement.toString());
//        if (builtIn.dest != null) {
//            VVarRef.Local variable = (VVarRef.Local) builtIn.dest;
//            String id = variable.ident;
//            String backup = backup(F, id, 0);
//            instructions.add(backup);
//        }
////        VOperand source = buil.source;
//        String value;
//        return instructions;
//    }
//
//    public List<String> visit(VFunction F, VMemWrite memWrite) {
//        List<String> instructions = new ArrayList<>();
//        VMemRef.Global dest = (VMemRef.Global) memWrite.dest; //couldn't be 'stack' which is only other option
//        String destValue;
//        if (dest.base instanceof VAddr.Label) {
//            VAddr.Label base = (VAddr.Label) dest.base;
//            destValue = ':' + base.label.ident;
//        } else if (dest.base instanceof VAddr.Var) {
//            VAddr.Var base = (VAddr.Var) dest.base;
//            VVarRef variable = base.var;
//            String restore = restore(F, variable, 0);
//            instructions.add(restore);
//            destValue = "$t0";
//        } else {
//            return null;
//        }
//        String sourceValue;
//        if (!isLiteralOrLabel(memWrite.source)) {
//            VVarRef.Local variable = (VVarRef.Local) memWrite.source;
//            String restore = restore(F, variable, 1);
//            instructions.add(restore);
//            sourceValue = "$t1";
//        } else {
//            if (isLabel(memWrite.source)) {
//                VLabelRef label = (VLabelRef) memWrite.source;
//                sourceValue = ':' + label.ident;
//            } else if (isLiteral(memWrite.source)) {
//                VLitInt literal = (VLitInt) memWrite.source;
//                sourceValue = String.valueOf(literal.value);
//            } else {
//                return null;
//            }
//        }
//        String memWriteStatement = String.format("[%s+%d] = %s", destValue, dest.byteOffset, sourceValue);
//        instructions.add(memWriteStatement);
//        return instructions;
//
//        //remember: you are translating vapor, which doesn't have memory references other
//        //than global ( e.g. [t.0+4] as opposed to local[1] ) so don't even bother checking
//        //it is safe to assume/pretend local/stack memory references don't exist in vapor
//
//
//    }
//
//    public List<String> visit(VFunction F, VMemRead memRead) {
//        List<String> instructions = new ArrayList<>();
//
////        String destValue;
////        if (!isLiteralOrLabel(memRead.source)) {
////            VVarRef.Local variable = (VVarRef.Local) memRead.source;
////            String restore = restore(F, variable, 1);
////            instructions.add(restore);
////            sourceValue = "$t1";
////        } else {
////            if (isLabel(memRead.source)) {
////                VLabelRef label = (VLabelRef) memRead.source;
////                sourceValue = ':' + label.ident;
////            } else if (isLiteral(memRead.source)) {
////                VLitInt literal = (VLitInt) memRead.source;
////                sourceValue = String.valueOf(literal.value);
////            } else {
////                return null;
////            }
////        }
//
//
//
//
//        VMemRef.Global source = (VMemRef.Global) memRead.source; //couldn't be 'stack' which is only other option
//        String sourceValue;
//        if (source.base instanceof VAddr.Label) {
//            VAddr.Label base = (VAddr.Label) source.base;
//            sourceValue = ':' + base.label.ident;
//        } else if (source.base instanceof VAddr.Var) {
//            VAddr.Var base = (VAddr.Var) source.base;
//            VVarRef variable = base.var;
//            String restore = restore(F, variable, 1);
//            instructions.add(restore);
//            sourceValue = "$t1";
//        } else {
//            return null;
//        }
//
//        String memReadStatement = String.format("$t0 = [%s+%d]", sourceValue, source.byteOffset);
//        instructions.add(memReadStatement);
//        //dest couldn't be a label, and it couldn't be another mem ref, only a var
//        VVarRef.Local variable = (VVarRef.Local) memRead.dest;
//        String backup = backup(F, variable.ident, 0);
//        instructions.add(backup);
//        return instructions;
//    }
//
//    public List<String> visit(VFunction F, VBranch branch) {
//        List<String> instructions = new ArrayList<>();
//        StringBuilder ifStatement;
//        if (branch.positive)
//            ifStatement = new StringBuilder("if ");
//        else
//            ifStatement = new StringBuilder("if0 ");
//        if (isLiteral(branch.value)) {
//            VLitInt literal = (VLitInt) branch.value;
//            ifStatement.append(literal.value).append(" goto :");
//        } else {
//            String restore = restore(F, branch.value, 0);
//            instructions.add(restore);
//            ifStatement.append("$t0 goto :");
//        }
//        VLabelRef label = branch.target;
//        ifStatement.append(label.ident);
//        instructions.add(ifStatement.toString());
//        return instructions;
//    }
//
//    public List<String> visit(VFunction F, VGoto _goto) {
//        if (_goto.target instanceof VAddr.Var)
//            return null;
//        VAddr.Label target = (VAddr.Label) _goto.target;
//        VLabelRef label = target.label;
//        String gotoStatement = "goto :" + label.ident;
//        return Arrays.asList(gotoStatement);
//    }
//
//    public List<String> visit(VFunction F, VReturn ret) {
//        return null;
//    }
//}
//
//class InstructionTypeVisitor extends VInstr.VisitorR<Integer, RuntimeException> {
//    public InstructionTypeVisitor() {
//    }
//
//    public Integer visit(VCall var1) {
//        return 1;
//    }
//
//    public Integer visit(VAssign var1) {
//        return 2;
//    }
//
//    public Integer visit(VBuiltIn var1) {
//        return 3;
//    }
//
//    public Integer visit(VMemWrite var1) {
//        return 4;
//    }
//
//    public Integer visit(VMemRead var1) {
//        return 5;
//    }
//
//    public Integer visit(VBranch var1) {
//        return 6;
//    }
//
//    public Integer visit(VGoto var1) {
//        return 7;
//    }
//
//    public Integer visit(VReturn var1) {
//        return 8;
//    }
//}