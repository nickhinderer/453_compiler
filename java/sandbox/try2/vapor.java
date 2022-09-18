//package vapor.hw4;
//
//import cs132.vapor.ast.*;
//
//import cs132.util.*;
////import cs132.vapor.*;
//import cs132.vapor.parser.*;
////import cs132.vapor.ast.*;
//import cs132.vapor.ast.VBuiltIn.Op;
////import cs132.util.ProblemException;
////import cs132.vapor.parser.VaporParser;
////import vapor.parser.ast.VaporParser;
////import cs132.vapor.ast.VaporProgram;
////import cs132.vapor.ast.VBuiltIn.Op;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicReference;
//
//enum REGISTER {
//    s0, s1, s2
//}
//
//public class vapor {
//    public static VaporProgram parseVapor(InputStream in, PrintStream err)
//            throws IOException {
//        Op[] ops = {
//                Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
//                Op.PrintIntS, Op.HeapAllocZ, Op.Error,
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
//        try {
//            in = new FileInputStream("TreeVisitor.vapor");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        VaporProgram p;
//        try {
//            p = parseVapor(in, null);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        assert p != null;
////        System.out.println(p.functions[0].vars[0]);
//        Map<String, REGISTER> registerMap;
//        Map<String, Integer> spillMap;
//        for (VFunction F : p.functions) {
//            Graph cf = new Graph(F); //1 make cfg for each function
//            Map<String, Interval> intervals = cf.computeIntervals();  //2 compute live intervals
//            registerMap = new HashMap<>();
//            spillMap = new HashMap<>();
//            computeRegistersSpills(intervals, registerMap, spillMap); //3
////            regalloc(F, registerMap, spillMap); //4
////            backupCalleeSavedRegisters(F); //5
//            transmuteCalls(p); //6
//            transmuteArguments(p); //7
//            transmuteSignatures(p); //8
//        }
//
//        //spill everywhere
//        for (VFunction F : p.functions) {
//            spillMap = new HashMap<>();
//            String[] vars = F.vars;
//            int localSize = vars.length;
//            computeMap(F, spillMap);
//            ArrayList<VInstr> a = regallocSpillEverywhere(F, spillMap);
////            VFunction[] new_functions = (VFunction[]) a.toArray();
////            VaporProgram new_program = new VaporProgram(true, null, )
//            updateRegisters(a, new ArrayList<>(List.of(vars)), spillMap, F);
////            F.body = a.toArray();
//            System.out.println("\n\n\nNEXT FUNCTION\n\n\n");
//        }
////        for (VFunction F : p.functions)
////            updateRegisters(F);
////        ArrayList<String> vars = new ArrayList<>(List.of(F.vars));
//
////        System.out.println();
//
//
//        //spill everywhere
//// 1. compute size of local
///* 2. create map from temp variable to memory (local) array
//   3. insert backup and restore (lhs/rhs)
//   4. change register names (s0, s1, s2)
//   5. update method signatures, argument passing, and method calls (a0, v0, in/out/local, etc.)
//* */
//        System.out.println();
//    }
//
//    //spill everywhere: dest: t0  src: t1, t2
//    private static ArrayList<VInstr> regallocSpillEverywhere(VFunction F, Map<String, Integer> spillMap) {
//        InstructionVisitor v = new InstructionVisitor();
//        ArrayList<VInstr> instructions = new ArrayList<>();
//        ArrayList<String> vars = new ArrayList<>(List.of(F.vars));
//        ArrayList<String> params = new ArrayList<>();
//        for (VVarRef.Local param : F.params) {
//            params.add(param.toString());
//        }
//        int startPos = F.body[0].sourcePos.line;
//        String dest, source;
//        VMemWrite backup;
//        VMemRead restore;
//        for (VInstr instr : F.body) {
//            dest = null;
//            source = null;
//            backup = null;
//            restore = null;
//            switch (instr.accept(v)) {
//                case 0: //VAssign
//                {
//                    VAssign assign = (VAssign) instr;
//                    dest = assign.dest.toString();
//                    source = assign.source.toString();//, /*backup = null,*/;// restore = null;
//                    if (vars.contains(source)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
//                        restore = new VMemRead(pos, (VVarRef) assign.source, spill);
//                        instructions.add(restore);
//                        System.out.println(source + " = local[" + spillMap.get(source) + "]");
//                    }
//                    instructions.add(assign);
//                    System.out.println(dest + " = " + source);
//                    if (vars.contains(dest)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
//                        backup = new VMemWrite(pos, spill, assign.dest);
//                        instructions.add(backup);
//                        System.out.println("local[" + spillMap.get(dest) + "] = " + dest);
//                    }
//                    System.out.println();
//                }
//                break;
//                case 1: {
//                    VCall call = (VCall) instr;
//                    dest = call.dest.toString();
//                    source = call.addr.toString();
//                    if (vars.contains(source)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
//                        VAddr.Var<VFunction> addr = (VAddr.Var<VFunction>) call.addr;
//                        restore = new VMemRead(pos, addr.var, spill);
//                        instructions.add(restore);
//                        System.out.println(source + " = local[" + spillMap.get(source) + "]");
//                    }
//                    String funcargs = "";
//                    boolean first = true;
//                    for (VOperand o : call.args) {
//                        if (first) {
//                            funcargs += o.toString();
//                            first = false;
//                            continue;
//                        }
//                        funcargs += ' ' + o.toString();
//                    }
//                    instructions.add(call);
//                    System.out.println(dest + " = call " + source + "(" + funcargs + ")");
//                    if (vars.contains(dest)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
//                        backup = new VMemWrite(pos, spill, call.dest);
//                        instructions.add(backup);
//                        System.out.println("local[" + spillMap.get(dest) + "] = " + dest);
//                    }
//                    System.out.println();
//                }
//                break;
//                case 2: {
//                    VBuiltIn builtin = (VBuiltIn) instr;
//                    if (builtin.op == Op.Error) {
//
//                        instructions.add(builtin);
//                        break;
//                    }
//                    if (builtin.op == Op.PrintIntS) {
//                        String[] args = new String[builtin.args.length];
//                        int i = 0;
//                        for (VOperand operand : builtin.args) {
//                            args[i] = operand.toString();
//                            i++;
//                        }
//                        i = 0;
//                        for (String _source : args) {
//                            if (vars.contains(_source)) {
//                                SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                                VMemRef memRef;
//                                if (params.contains(_source))
//                                    memRef = new VMemRef.Stack(pos, VMemRef.Stack.Region.In, spillMap.get(_source));
//                                else
//                                    memRef = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(_source));
//                                restore = new VMemRead(pos, (VVarRef) builtin.args[i], memRef);
//                                instructions.add(restore);
//                                if (params.contains(_source))
//                                    System.out.println(_source + " = in[" + spillMap.get(_source) + "]");
//                                else
//                                    System.out.println(_source + " = local[" + spillMap.get(_source) + "]");
//                            }
//                            i++;
//                        }
//                        instructions.add(builtin);
//                        System.out.println("PrintIntS(" + args[0] + ")");
//                        System.out.println();
//                        break;
//                    }
////                    if (builtin.op == Op.HeapAllocZ) {
//////
////                        instructions.add(builtin);
//////                        break;
////                    }
//                    dest = builtin.dest.toString();
//                    String[] args = new String[builtin.args.length];
//                    int i = 0;
//                    for (VOperand operand : builtin.args) {
//                        args[i] = operand.toString();
//                        i++;
//                    }
//                    i = 0;
//                    for (String _source : args) {
//                        if (vars.contains(_source)) {
//                            SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                            VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(_source));
//                            restore = new VMemRead(pos, (VVarRef) builtin.args[i], spill);
//                            instructions.add(restore);
//                            System.out.println(_source + " = local[" + spillMap.get(_source) + "]");
//                        }
//                        i++;
//                    }
//                    instructions.add(builtin);
//                    String builtinArgs = "";
//                    boolean first = true;
//                    for (VOperand operand : builtin.args) {
//                        if (first) {
//                            builtinArgs += operand.toString();
//                            first = false;
//                            continue;
//                        }
//                        builtinArgs += ' ' + operand.toString();
//                    }
//                    System.out.println(dest + " = " + builtin.op.name + "(" + builtinArgs + ")");
//                    if (vars.contains(dest)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
//                        backup = new VMemWrite(pos, spill, builtin.dest);
//                        instructions.add(backup);
//                        System.out.println("local[" + spillMap.get(dest) + "] = " + dest);
//                    }
//                    System.out.println();
////                    instructions.add(instr);
//                }
//                break;
//                case 3: {
//                    VMemWrite write = (VMemWrite) instr;
//                    VMemRef.Global ref = (VMemRef.Global) write.dest;
//                    dest = ref.base.toString();
//                    source = write.source.toString();
//                    if (vars.contains(source)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
//                        restore = new VMemRead(pos, (VVarRef) write.source, spill);
//                        instructions.add(restore);
//                        System.out.println(source + " = local[" + spillMap.get(source) + "]");
//                    }
//                    if (vars.contains(dest)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
//                        VAddr.Var<VDataSegment> base = (VAddr.Var<VDataSegment>) ref.base;
//                        restore = new VMemRead(pos, base.var, spill);
//                        instructions.add(restore);
//                        System.out.println(dest + " = local[" + spillMap.get(dest) + "]");
//                    }
//                    instructions.add(write);
//                    System.out.println("[" + dest + "+" + ref.byteOffset + "] = " + source);
//
//                    System.out.println();
//                }
//                break;
//                case 4: {
//                    VMemRead read = (VMemRead) instr;
//                    VMemRef.Global ref = (VMemRef.Global) read.source;
//                    dest = read.dest.toString();
//                    source = ref.base.toString();
//                    if (vars.contains(source)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
//                        VAddr.Var<VDataSegment> base = (VAddr.Var<VDataSegment>) ref.base;
//                        restore = new VMemRead(pos, base.var, spill);
//                        instructions.add(restore);
//                        System.out.println(source + " = local[" + spillMap.get(source) + "]");
//                    }
//                    instructions.add(read);
//                    System.out.println(dest + " = [" + source + "+" + ref.byteOffset + "]");
//                    if (vars.contains(dest)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
//                        backup = new VMemWrite(pos, spill, read.dest);
//                        instructions.add(backup);
//                        System.out.println("local[" + spillMap.get(dest) + "] = " + dest);
//                    }
//                    System.out.println();
//                }
//                break;
//                case 5: {
//                    VBranch branch = (VBranch) instr;
//                    source = branch.value.toString();
//                    //no computed goto, so don't worry about dest (same for goto obviously)
//                    if (vars.contains(source)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef memRef = null;// = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
//                        if (params.contains(source))
//                            memRef = new VMemRef.Stack(pos, VMemRef.Stack.Region.In, spillMap.get(source));
//                        else
//                            memRef = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
//
//                        restore = new VMemRead(pos, (VVarRef) branch.value, memRef);
//                        instructions.add(restore);
//                        if (params.contains(source)) {
//
//                            memRef = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
//                            pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                            backup = new VMemWrite(pos, memRef, branch.value);
//                            instructions.add(backup);
//                        }
//                        if (params.contains(source)) {
//                            System.out.println(source + " = in[" + spillMap.get(source) + "]");
////                            System.out.println(source + "foo");
//                            System.out.println("local[" + spillMap.get(source) + "] = " + source);
//                            params.remove(source);
//                        }
//                        else
//                            System.out.println(source + " = local[" + spillMap.get(source) + "]");
//                    }
//                    instructions.add(branch);
//                    String _if;
//                    if (branch.positive)
//                        _if = "if";
//                    else
//                        _if = "if0";
//                    System.out.println(_if + " " + source + " goto " + branch.target.toString());
//                    System.out.println();
//                }
//                break;
//                case 6: {
//                    VGoto _goto = (VGoto) instr;
//                    instructions.add(instr);
//                    System.out.println("goto " + _goto.target.toString());
//                    System.out.println();
//                }
//                break;
//                case 7: {
//                    VReturn _return = (VReturn) instr;
////                    value = _return.value.toString();
//                    if (_return.value != null) {
//                        source = _return.value.toString();
//                        if (vars.contains(source)) {
//                            SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                            VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
//                            restore = new VMemRead(pos, (VVarRef) _return.value, spill);
//                            instructions.add(restore);
//                            System.out.println(source + " = local[" + spillMap.get(source) + "]");
//                        }
//                        instructions.add(instr);
//                        System.out.println("ret " + source);
//                        System.out.println();
//                    } else {
//                        instructions.add(instr);
//                        System.out.println("ret");
//                        System.out.println();
//                    }
//                }
//                break;
//            }
//        }
//        return instructions;
//    }
//
//    private static void updateRegisters(ArrayList<VInstr> instructions, ArrayList<String> vars, Map<String, Integer> spillMap, VFunction F) {
//        InstructionVisitor v = new InstructionVisitor();
////        ArrayList<String> vars = new ArrayList<>(List.of(F.vars));
//
//        int index = 0;
//        Map<String, String> temporaryTemporaryRegisterMap = new HashMap<>(); //temporary as in it is reset after usage, not as in temporary variable in vapor
//        for (VInstr instr : instructions) {
//            switch (instr.accept(v)) {
//                case 0: {
//                    VAssign assign = (VAssign) instr;
////                    VVarRef dest = new VVarRef(assign.sourcePos, );
////                    VAssign _new = new VAssign(assign.sourcePos, );
//                    String dest = assign.dest.toString();
//                    if (vars.contains(dest)) {
//                        temporaryTemporaryRegisterMap.put(dest, "$t0");
//                        System.out.print("$t0 = ");
//                    } else {
//                        System.out.print(dest + " = ");
//                    }
//                    String source = assign.source.toString();
//                    if (vars.contains(source)) {
////                        System.out.println("source = " + source);
//                        String register = temporaryTemporaryRegisterMap.get(source);
//                        System.out.println(register);
//                        temporaryTemporaryRegisterMap.remove(source);
//                    } else {
//                        System.out.println(source);
//                    }
//                }
//                break;
//                case 1: {
//                    VCall call = (VCall) instr;
//                    String dest = call.dest.toString();
//                    int outIndex = 0;
//                    for (VOperand operand : call.args) {
//                        if (vars.contains(operand.toString())) {
//                            boolean reg1Free = !temporaryTemporaryRegisterMap.containsValue("$t1");
//                            if (reg1Free)
//                                temporaryTemporaryRegisterMap.put(dest, "$t1");
//                            else
//                                temporaryTemporaryRegisterMap.put(dest, "$t2");
//                            System.out.println(temporaryTemporaryRegisterMap.get(dest) + " = local[" + spillMap.get(operand.toString()) + "]");
//                            System.out.println("out[" + outIndex + "] = " + temporaryTemporaryRegisterMap.get(dest));
//                            System.out.println();
//                            temporaryTemporaryRegisterMap.remove(dest);
//                        } else { //is a parameter to the function F itself
////                            System.out.println("hi");
////                            VOperand[] args = call.args;
////                            int inIndex = 0;
////                            for (VOperand arg : args) {
////                                if (dest.equals(operand.toString())) {
//                            if (List.of(F.params).contains(operand.toString())) {
//                                int paramIndex = List.of(F.params).indexOf(operand.toString());
//                                System.out.println("$v0 = in[" + paramIndex + "]");
//                                System.out.println("out[" + outIndex + "] = $v0");
//                            } else {
//                                System.out.println("out[" + outIndex + "] = " + operand);
//                            }
////                                }
////                            }
//                        }
//                        outIndex++;
//                    }
////                        System.out.println("source = " + source);
////                        System.out.println("call $t1");
//                    if (vars.contains(dest)) {
//                        temporaryTemporaryRegisterMap.put(dest, "$t0");
//                        System.out.print("call ");
//
//                    }
//                    String addr = call.addr.toString();
//                    if (vars.contains(addr)) {
//                        System.out.println(temporaryTemporaryRegisterMap.get(addr));
//                        System.out.println("$t0 = $v0");
//                        temporaryTemporaryRegisterMap.remove(addr);
//                    }
//                    System.out.println("");
//
////                    String dest = call.dest.toString();
////                    if (vars.contains(dest)) {
////                        System.out.println("$t0 = $v0");
////                    } else {
////                        System.out.println(dest + " = $v0");
////                    }
//
//
////                    boolean f = true;
////                    for (VOperand operand : call.args) {
////                        if (f) {
////                            System.out.print(operand.toString());
////                            f = false;
////                        } else {
////                            System.out.print(' ' + operand.toString());
////                        }
////                    }
////                    System.out.println(")");
//                }
//                break;
//                case 2: {
//                    VBuiltIn builtin = (VBuiltIn) instr;
////                    temporaryTemporaryRegisterMap.put("t.0", "$t0");
//                    if (builtin.dest != null) {
//                        String dest = builtin.dest.toString();
//                        if (vars.contains(dest)) {
//                            temporaryTemporaryRegisterMap.put(dest, "$t0");
//                            System.out.print("$t0 = " + builtin.op.name + "(");
//                        } else {
//                            System.out.print(dest + " = " + builtin.op.name + "(");
//                        }
//                    } else {
//                        System.out.print(builtin.op.name + "(");
//                    }
//                    ArrayList<String> sources = new ArrayList<>();
//                    for (VOperand operand : builtin.args) {
//                        sources.add(operand.toString());
//                    }
//                    for (String source : sources) {
//                        if (vars.contains(source)) {
//                            String register = temporaryTemporaryRegisterMap.get(source);
//                            System.out.print(register + ' ');
//                            temporaryTemporaryRegisterMap.remove(source);
//                        } else {
//                            System.out.print(source + ' ');
//                        }
//                    }
//                    System.out.println(")");
//                }
//                break;
//                case 3: {
//
//                    VMemWrite write = (VMemWrite) instr;
//                    if (write.dest instanceof VMemRef.Global) {
//                        VMemRef.Global ref = (VMemRef.Global) write.dest;
//                        String dest = ref.base.toString();
//                        if (vars.contains(dest)) {
//                            System.out.print("[" + temporaryTemporaryRegisterMap.get(dest) + "+" + ref.byteOffset + "] = ");
//                            temporaryTemporaryRegisterMap.remove(dest);
//                        } else {
//                            System.out.print(dest + ' ');
//                        }
//                        String source = write.source.toString();
//                        if (vars.contains(source)) {
////                        System.out.println("source = " + source);
//                            System.out.println(temporaryTemporaryRegisterMap.get(source));
//                            temporaryTemporaryRegisterMap.remove(source);
//                        } else {
//                            System.out.println(source);
//                        }
//                        ArrayList<String> remove = new ArrayList();
////                        temporaryTemporaryRegisterMap.forEach((key, value) -> {
////                            if (value.equals("$t0"))
////                                remove.add(key);
////                        });
////                        temporaryTemporaryRegisterMap.remove(remove.get(0));
//                    }
//                    if (write.dest instanceof VMemRef.Stack) {
//                        VMemRef.Stack ref = (VMemRef.Stack) write.dest;
//                        String dest = "local[" + ref.index + "] = $t0";
//                        System.out.println(dest);
//                        System.out.println();
//
//                    }
//
//                }
//                break;
//                case 4: {
//                    VMemRead read = (VMemRead) instr;
//                    if (read.source instanceof VMemRef.Global) {
//                        String dest = "$t0";
//                        System.out.print("$t0 = ");
//                        VMemRef.Global ref = (VMemRef.Global) read.source;
//                        String source = ref.base.toString();
//                        if (vars.contains(source)) {
//                            System.out.println("[" + temporaryTemporaryRegisterMap.get(source) + "+" + ref.byteOffset + "]");
//                            temporaryTemporaryRegisterMap.remove(source);
//                        }
//                    }
//                    if (read.source instanceof VMemRef.Stack) {
//                        String dest = read.dest.toString();
//                        if (vars.contains(dest)) {
//                            boolean reg1Free = !temporaryTemporaryRegisterMap.containsValue("$t1");
//                            if (reg1Free)
//                                temporaryTemporaryRegisterMap.put(dest, "$t1");
//                            else
//                                temporaryTemporaryRegisterMap.put(dest, "$t2");
//                            System.out.print(temporaryTemporaryRegisterMap.get(dest) + " = ");
//                        } else {
//                            System.out.print(dest + " = ");
//                        }
//                        VMemRef.Stack ref = (VMemRef.Stack) read.source;
//                        String source = null;
//                        if (ref.region == VMemRef.Stack.Region.Local)
//                            source = "local[" + ref.index + "]";
//                        if (ref.region == VMemRef.Stack.Region.In)
//                            source = "in[" + ref.index + "]";
//                        if (ref.region == VMemRef.Stack.Region.Out)
//                            source = "out[" + ref.index + "]";
//                        System.out.println(source);
//                    }
//                }
//                break;
//                case 5: {
//                    VBranch branch = (VBranch) instr;
//                    String dest = branch.value.toString();
//                    String _if;
//                    if (branch.positive)
//                        _if = "if";
//                    else
//                        _if = "if0";
//                    if (vars.contains(dest)) {
//                        System.out.println(_if + " " + temporaryTemporaryRegisterMap.get(dest) + " goto " + branch.target.toString());
//                        temporaryTemporaryRegisterMap.remove(dest);
//                    } else {
//                        System.out.println(_if + " " + dest + " goto " + branch.target.toString());
//                    }
//                }
//                break;
//                case 6: {
//                    VGoto _goto = (VGoto) instr;
//                    System.out.println("goto " + _goto.target.toString());
//                }
//                break;
//                case 7: {
//                    VReturn _return = (VReturn) instr;
//                    if (_return.value != null) {
//                        if (vars.contains(_return.value.toString())) {
////                            boolean reg1Free = !temporaryTemporaryRegisterMap.containsValue("$t1");
////                            if (reg1Free)
////                                temporaryTemporaryRegisterMap.put(_return.value.toString(), "$t1");
////                            else
////                                temporaryTemporaryRegisterMap.put(_return.value.toString(), "$t2");
//                            System.out.println("$v0 = " + temporaryTemporaryRegisterMap.get(_return.value.toString()));
//                            System.out.println("ret");
//                        } else { //literal
//                            System.out.println("$v0 = " + _return.value);
//                            System.out.println("ret");
//                        }
//                    } else {
//                        System.out.println("ret");
//                    }
//                }
//                    break;
//            }
//        }
//        index++;
//    }
//
//    private static void computeMap(VFunction F, Map<String, Integer> spillMap) {
//        int i = 0;
//        for (String var : F.vars) {
//            spillMap.put(var, i);
//            i++;
//        }
//    }
//
//    ///////////////
//
//    private static void computeRegistersSpills
//            (Map<String, Interval> intervals, Map<String, REGISTER> registerMap, Map<String, Integer> spillMap) {
//
//    }
//
//    private static void regalloc(VaporProgram
//                                         program, Map<String, REGISTER> registerMap, Map<String, Integer> spillMap) {
//
//    }
//
//    private static void backupCalleeSavedRegisters(VaporProgram program) {
//
//    }
//
//    private static void transmuteCalls(VaporProgram program) {
//
//    }
//
//    private static void transmuteArguments(VaporProgram program) {
//
//    }
//
//    private static void transmuteSignatures(VaporProgram program) {
//
//    }
//}