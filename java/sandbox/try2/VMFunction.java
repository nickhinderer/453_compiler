//package vapor.vaporm.hw4;
//
//import cs132.util.SourcePos;
//import cs132.vapor.ast.*;
//import cs132.vapor.ast.VMemRef.Stack.Region;
//
//import java.lang.reflect.Array;
//import java.util.*;
//
//public class VMFunction {
//    int in, out, local;
//    List<String> lines;
//
//    public VMFunction(Graph function) {
//        this.lines = new ArrayList<>();
//        this.computeStackSize(function);
//        lines.add(String.format("func %s [in %d, out %d, local %d]", function.original.ident, in, out, local));
//        insertCalleeBackups();
//        insertArgumentRetrieval(function);
//        //do function arg backup
//        //do spill insertions (backups, restores, v1 t8 v0)
//        //do
////        List<VInstr> instructions = new ArrayList<>();
//
//
////        insertStatements(); //which calls the ones below and then prints them
//
//        expandCallAndReturnInstructions(function.nodes);
//        expandSpills(function);
////        swapTemporariesRegisters(function);
//
//
//        insertInstructions(function);
//
////        insertStatements(); //all but the final ret statement
////        insertCalleeRestores();
////        insertReturn();
//
//
////        for (Node node : function.nodes) {
////            VInstr instruction = node.instruction;
////            if (instruction instanceof VCall) {
////                VCall call = (VCall) instruction;
////                node.expansion = new Call(call).call;
////                node.expanded = true;
////            } else if (node.instruction instanceof VReturn) {
////                VReturn vReturn = (VReturn) node.instruction;
////                if (vReturn.value != null) {
////                    node.expansion = expandReturn(vReturn);
////                    node.expanded = true;
////                }
////            }
//////                instructions.addAll(new Call(call).call);
//////            } else {
//////                instructions.add(instruction);
//////            }
////        }
//
//
////
////
////
////
////        //do ret and all the processing except for backup/restore and var name swap
////        List<VInstr> instructions = new ArrayList<>();
////
////        for (Spill spill : function.spills) { //Spill is a record of a spill
////            Node node = spill.backupPoint;
////            for (int i = node.num; i < function.nodes.size(); ++i) {
////                node = function.nodes.get(i);
////
//////                List<VInstr> expansion = spill(node, spill);
////
////                node.expansion = spill(node, spill);
////
////
////                //BUG!!!!!, only set to expanded if it really was (compare size of instructions before and after :/   )
////                node.expanded = true;
////            }
////
////
//////            node.expansion = spill(node, spilled);
//////
//////
//////
//////            if (node.expanded) {
//////                for (VInstr instruction : node.expansion) {
//////
//////                }
//////            } else {
//////                VInstr instruction = node.instruction;
//////
//////            }
//////            for (int i = node.num + 1; i < function.nodes.size(); ++i) {
//////                Node nextNode = function.nodes.get(i);
//////                if (node.expanded) {
//////                    for (VInstr instruction : nextNode.expansion) {
//////
//////                    }
//////                } else {
//////                    VInstr instruction = node.instruction;
//////
//////                }
//////            }
////        }
////
//
//
////        for (Node node : function.nodes) {
////            if (node.expanded) {
////                restore();
////                backup();
////                for (VInstr instruction : node.expansion) {
////                    instructions.add(instruction);
////                }
////            } else {
////                instructions.add(node.instruction);
////            }
////        }
//        //add backup/restores
////        for (VInstr instruction : instructions) {
////
////        }
////        for ()
//
//        System.out.println();
//        //swap variable names
//
//        //then print it
//        System.out.println();
//        //insert backup/restores (just temp names and
//        //ACTUALLY WAIT DO THIS FIRST BEFORE NEXT LINE: go insert spill backup and restore instructions in the right place (local spot is spill spot + 16) using v1 and t8 (v0 only for rets)
//        //actually then the next step is only to swap local names for register names since it is already done and also deal with spills and use v1 for those instead when no map exists
//        //translate instructions, create new big list of instructions, then go swap out register names using the records
//        //then create and use an instruction printer visitor on the new list of instructions and add them to lines
//
//
//        //so
//        //
//        insertCalleeRestores();
//
//        lines.add("ret");
//        //and also hand returns and add it to lines, then done bam
//        //labels and rets!!!
//        //print visitor, do the same for hw3 and do it good and redo sym_tab and redefine api and simplify
//        //and study for final, and 415 weed appt job notes follow all, 345 415 sunday 415 project today
//        //also, if it is spilled and it is a param, don't allocate space for it, it already has space, but
//        //still consider it in the pool of variables (like you are now, don't worry you don't have to change
//        //anything in linear scan, only the above step when calculating in/out and when doing backups and restores
//        //then at that time you just have to remember to do in instead of local)
//        //oh and also add the code for getting arguments from in if they are not spilled as params and putting them into the right
//        //registers, and if you do that and then spill a param later, like you said above, you put it back into in rather than local and allocating two spots, spilled params go back into
//        //in whether they were spilled from the start (in which case you simply do nothing and jsut pull from in when needed the same a a normal spill/restore but using in rather than local)
//        //work out examples and prove it to yourself for all the above
//        //just write out examples and what you need to do then tranlate that o code
//        //and since there can only be three operands in the case you need to use spilled, v0 v1 and t8 will suffice
//        //and then during calls if you have 8 spilled you need at once, you really split that into 8 instructoins and
//        //use 1 at a time
//        //if the spilled variable is used, just restore before, defined, backup after, both, restore before backup after
//
//
//        //just go instruction by instruction, one at a time
//        //do the spills first,
//
//        //so calls first
//        //spill register priority : v1, t8, v0 (last resort, v0 only used for defs)
//        //and remember, there are so many optimizations you can make like cuttig out intermediates and not saving
//        //unsed defs and arg passing using a4 and sharing space in local if it is not needed, but that adds complexity
//        //and areas you could be wrong, your first attetmpt should be for correctness, then go back and add those things
//        //and same for hw2 and hw3, do those first and correct, and good debugging
//
//        for (String line : lines) {
//            System.out.println(line);
//        }
//
//
//    }
//
//    void insertArgumentRetrieval(Graph function) {
//        Set<String> params = new HashSet<>();
//        List<String> order = new ArrayList<>();
//        if (function.nodes.get(0).params != null) {
//            for (Variable param : function.nodes.get(0).params) {
//                params.add(param.name);
//                order.add(param.name);
//            }
//
//            for (Spill spill : function.spills)
//                if (spill.backupPoint.num == 0)
//                    params.remove(spill.variable.name);
//
//            Record firstRecord = function.nodes.get(1).record;
//
//            if (firstRecord != null)
//                for (var entry : firstRecord.registers.entrySet()) {
//                    if (params.contains(entry.getKey()))
//                        lines.add(String.format("%s = in[%d]", firstRecord.registers.get(entry.getKey()).toString(), order.indexOf(entry.getKey())));
//                }
//
//
//            for (String param : params) {
////            function.
////            lines.add(String.format("%s = in[%d]", ));
//            }
//            Node firstNode = function.nodes.get(0);
//            Record startingParameters = function.nodes.get(0).record;
////            startingParameters.
////                    function.spills;
////            Set<String> params = new HashSet<>();
////            Record record = function.node(0).record;
////            for (VVarRef.Local param : function.original.params)
////                params.add(param.toString());
////            for (Spill spill : function.spills) {
////                if (spill.backupPoint.num != 0)
////            }
//        }
//
//
////        function.original.params;
////        function.
//    }
//
//
//    //retreive vars from in/out too (like it assigned to a param, beginning of function after signature after backing up callee saved, it sbhould be $sx = in[x]
//    //basically it is just seeing if they are not spilled at zero, if so, then before the first instruction, but
//    // after the callee backups and signature, retreive the args from in[]
//    //that and also make sure to swap v0 v1 and t8 in backups and restores in the actual instruction
//    //and add the returns last and then you're good
//    //and swap register names too
//
//
//    //okay
//    //go walk dogs
//    //soopers
//    //food
//    //come back finish hw4
//    //415
//
//    //hw2 hw3
//
//    //453 study
//
//
//    //then 415 and hw2 and hw3
//
//
//    void swapTemporariesRegisters(Graph function) {
//
//    }
//
//    void insertInstructions(Graph function) {
//        PrintVisitor pv = new PrintVisitor();
//        for (Node node : function.nodes) {
//            if (node.expanded) {
//                for (VInstr instruction : node.expansion) {
//                    if (instruction != null)
//                        lines.add(instruction.accept(pv));
//                }
//            } else {
//                if (node.instruction != null)
//                    lines.add(node.instruction.accept(pv));
//            }
//        }
//    }
//
//    void expandSpills(Graph function) {
//        for (Spill spill : function.spills) { //Spill is a record of a spill
//            Node node = spill.backupPoint;
//            for (int i = node.num; i < function.nodes.size(); ++i) {
//                node = function.nodes.get(i);
//                node.expansion = spill(node, spill);
//                node.expanded = true;
//            }
//        }
//    }
//
//    void expandCallAndReturnInstructions(List<Node> nodes) {
//        for (Node node : nodes) {
//            VInstr instruction = node.instruction;
//            if (instruction instanceof VCall) {
//                VCall call = (VCall) instruction;
//                node.expansion = new Call(call).expand();
//                node.expanded = true;
//            } else if (node.instruction instanceof VReturn) {
//                VReturn vReturn = (VReturn) node.instruction;
//                if (vReturn.value != null) {
//                    node.expansion = expandReturn(vReturn);
//                    node.expanded = true;
//                }
//            }
////                instructions.addAll(new Call(call).call);
////            } else {
////                instructions.add(instruction);
////            }
//            //mouse wheel click is paste
//        }
//    }
//
////    List<VInstr> spiill(Node node, Spill spill) {
////        List<VInstr> instructions = new ArrayList<>();
////        if (node.use.contains(spill.variable)) {
////            instructions.add(restore(spill, node.used$v0));
////            String register = node.used$v0 ? "v1" : "v0";
////            if (node.expanded) {
////                List<VInstr> expansionSwapped = new ArrayList<>();
////                for (VInstr instruction : node.expansion)
////                    expansionSwapped.add(swap(instruction, spill.variable.name, register));
////                instructions.addAll(node.expansion);
////            } else {
////                node.instruction = swap(node.instruction, spill.variable.name, register);
////                instructions.add(node.instruction);
////            }
////            if (!node.used$v0) node.used$v0 = true;
////        } else {
////            if (node.expanded) instructions.addAll(node.expansion);
////            else instructions.add(node.instruction);
////        }
////        if (node.def == spill.variable) instructions.add(backup(spill));
////        return instructions;
////    }
//
////    String register = node.used$v0 ? "v1" : "v0";
//
//    List<VInstr> spill(Node node, Spill spill) {
//        List<VInstr> instructions = new ArrayList<>();
//        if (node.use.contains(spill.variable)) {
//            if (node.expanded) {
//                VInstr restore = restore(spill, node.used$v0);
//                VariableUsageVisitor vuv = new VariableUsageVisitor();
//                for (VInstr instruction : node.expansion) {
//                    if (instruction != null) {
//                        List<String> variablesUsed = instruction.accept(vuv);
//                        if (variablesUsed != null)
//                            if (variablesUsed.contains(spill.variable.name))
//                                instructions.add(restore);
//                        instructions.add(instruction);
//                    }
//                }
//
//                System.out.println();
//                if (!node.used$v0) node.used$v0 = true;
//            } else {
//                instructions.add(restore(spill, node.used$v0));
//                instructions.add(node.instruction);
//                if (!node.used$v0) node.used$v0 = true;
//            }
//
//        } else {
//            if (node.expanded) instructions.addAll(node.expansion);
//            else instructions.add(node.instruction);
//        }
//        if (node.def == spill.variable) instructions.add(backup(spill));
//        return instructions;
//    }
//
////    VInstr swap(VInstr instruction, String temporary, String register) {
////
////    }
//
//    String swap(String temporary, String register, String input) {
//        register = "\\$" + register;
//        return input.replaceAll(temporary, register);
//    }
//
//    //        if (node.use.contains(spill.variable))
////            instructions.add(restore(spill));
//    VMemRead restore(Spill spill, boolean used$v0) {
//        String name = used$v0 ? "v1" : "v0";
//        SourcePos pos = new SourcePos(-1, -1);
//        VVarRef.Register register = new VVarRef.Register(pos, name, -1);
//        VMemRef memory = new VMemRef.Stack(pos, spill.region, spill.location);
//        VMemRead restore = new VMemRead(pos, register, memory);
//        return restore;
//    }
//
//    VMemWrite backup(Spill spill) {
//        VMemWrite backup;
//        SourcePos pos = new SourcePos(-1, -1);
//        VVarRef.Register register = new VVarRef.Register(pos, "t8", -1);
//        VMemRef memory = new VMemRef.Stack(pos, spill.region, spill.location);
//        backup = new VMemWrite(pos, memory, register);
//        //use $t8
//        //shortcuts
//        return backup;
//    }
//
//    List<VInstr> expandReturn(VReturn vReturn) {
//        List<VInstr> ret = new ArrayList<>();
//        SourcePos sourcePos = vReturn.sourcePos;
//        VVarRef.Register returnRegister = new VVarRef.Register(sourcePos, "v0", -1);
//        VAssign returnAssignment = new VAssign(sourcePos, returnRegister, vReturn.value);
//        ret.add(returnAssignment);
//        VReturn returnStatement = new VReturn(sourcePos, null);
//        ret.add(returnStatement);
//        return ret;
//    }
//
//    void insertCalleeBackups() {
//        for (int i = 0; i < 8; ++i) {
//            lines.add(String.format("local[%d] = $s%d", i, i));
//        }
//    }
//
//    void insertCalleeRestores() {
//        for (int i = 0; i < 8; ++i) {
//            lines.add(String.format("$s%d = local[%d]", i, i));
//        }
//    }
//
//    void computeStackSize(Graph function) {
//        this.in = function.original.params.length;
//        this.out = computeOut(function.original);
//        this.local = 16 + spillSize(function); //function.spills.size(); // conservative/defensive strategy: backup all callee/caller saved (not optimal by any means, but correct). so local = 8 + 8 + # spills
//    }
//
//    int spillSize(Graph function) {
////        int local = 0;
//        List<String> params = new ArrayList<>();
//        Set<String> locals = new HashSet<>();
//        for (VVarRef.Local param : function.original.params)
//            params.add(param.ident);
//        for (String var : function.original.vars)
//            if (!params.contains(var))
//                locals.add(var);
//        int localIndex = 0;
//        for (Spill spill : function.spills) {
//            String id = spill.variable.name;
//            if (params.contains(id)) {
//                spill.region = Region.In;
//                spill.location = params.indexOf(id);
//            } else {
//                spill.region = Region.Local;
//                spill.location = localIndex++;
//            }
//        }
//        return localIndex;
////        for (VVarRef.Local param : function.original.params) {
////            if (!allVariables.contains(param.ident)) {
////                locals++;
////            }
////        }
//    }
//
//    static int computeOut(VFunction F) {
//        int out = 0;
//        for (VInstr call : F.body) {
//            if (call instanceof VCall)
//                out = Math.max(out, ((VCall) call).args.length);
//        }
//        return out;
//    }
//
//    @Override
//    public String toString() {
//        //print the function as vapor-m (do same for hw3 and make it better)
//        return null;
//    }
//
//    //this and visitors, maybe use visitors in here,
//}
//
//class SwapVisitor extends VInstr.VisitorPR<RTPair, VInstr, RuntimeException> {
//    public SwapVisitor() {
//    }
//
//    VOperand swapOperand(VOperand vOperand, String register) {
//        if (vOperand instanceof VOperand.Static)
//            return vOperand;
//        if (vOperand instanceof VLitStr)
//            return vOperand;
//        if (vOperand instanceof VVarRef)
//            return swapVarRef((VVarRef) vOperand, register);
//        return null;
//    }
//
//    VVarRef swapVarRef(VVarRef vVarRef, String register) {
//        if (vVarRef instanceof VVarRef.Register)
//            return vVarRef;
//        VVarRef.Register varRef = new VVarRef.Register(vVarRef.sourcePos, register, -1);
//        return varRef;
//    }
//
//    @Override
//    public VInstr visit(RTPair map, VAssign vAssign) {
//        if (!map.temporary.equals(vAssign.dest.toString()) && !map.temporary.equals(vAssign.source.toString()))
//            return vAssign;
//        VOperand source;
//        if (map.temporary.equals(vAssign.source.toString()))
//            source = swapOperand(vAssign.source, map.register.toString());
//        else
//            source = vAssign.source;
//        VVarRef dest;
//        if (!(vAssign.dest instanceof VVarRef.Register)) {
//            if (map.temporary.equals(vAssign.dest.toString()))
//                dest = swapVarRef(vAssign.dest, map.register.toString());
//            else
//                dest = vAssign.dest;
//        } else {
//            dest = vAssign.dest;
//        }
//        return new VAssign(vAssign.sourcePos, dest, source);
//    }
//
//    @Override
//    public VInstr visit(RTPair map, VCall vCall) {
//        if (((VAddr.Var) vCall.addr).var instanceof VVarRef.Register)
//            return vCall;
//        if (map.temporary.equals(((VAddr.Var) vCall.addr).var.toString()))
//            return vCall;
//        VVarRef ref = swapVarRef(((VAddr.Var) vCall.addr).var, map.register.toString());
//        VAddr.Var<VFunction> addr = new VAddr.Var(ref);
//        return new VCall(vCall.sourcePos, addr, vCall.args, vCall.dest);
//    }
//
//    @Override
//    public VInstr visit(RTPair map, VBuiltIn vBuiltIn) {
//
//        return null;
//    }
//
//    @Override
//    public VInstr visit(RTPair map, VMemWrite vMemWrite) {
//        return null;
//    }
//
//    @Override
//    public VInstr visit(RTPair map, VMemRead vMemRead) {
//        return null;
//    }
//
//    @Override
//    public VInstr visit(RTPair map, VBranch vBranch) {
//        return null;
//    }
//
//    @Override
//    public VInstr visit(RTPair map, VGoto vGoto) {
//        return null;
//    }
//
//    @Override
//    public VInstr visit(RTPair map, VReturn vReturn) {
//        return null;
//    }
//
//}
//
//class RTPair {
//    Register register;
//    String temporary;
//}
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
//        return "builtin";
//    }
//
//    @Override
//    public String visit(VMemWrite vMemWrite) {
//        String memory;
//        if (vMemWrite.dest instanceof VMemRef.Stack)
//            memory = String.format("%s[%d]", ((VMemRef.Stack) vMemWrite.dest).region.toString(), ((VMemRef.Stack) vMemWrite.dest).index);
//        else
//            memory = String.format("[%s+%d]", ((VMemRef.Global) vMemWrite.dest).base, ((VMemRef.Global) vMemWrite.dest).byteOffset);
//        return String.format("%s = %s", memory, vMemWrite.source.toString());
//    }
//
//    @Override
//    public String visit(VMemRead vMemRead) {
//        String memory;
//        if (vMemRead.source instanceof VMemRef.Stack)
//            memory = String.format("%s[%d]", ((VMemRef.Stack) vMemRead.source).region.toString(), ((VMemRef.Stack) vMemRead.source).index);
//        else
//            memory = String.format("[%s+%d]", ((VMemRef.Global) vMemRead.source).base, ((VMemRef.Global) vMemRead.source).byteOffset);
//        return String.format("%s = %s", vMemRead.dest.toString(), memory);
//    }
//
//    @Override
//    public String visit(VBranch vBranch) {
//        String _if = vBranch.positive ? "if" : "if0";
//        return String.format("%s %s goto :%s", _if, vBranch.value.toString(), vBranch.target.toString());
//    }
//
//    @Override
//    public String visit(VGoto vGoto) {
//        return String.format("goto :%s", vGoto.target.toString());
//    }
//
//    @Override
//    public String visit(VReturn vReturn) {
//        return null;
//    }
//
//}
//
//class VariableUsageVisitor extends VInstr.VisitorR<List<String>, RuntimeException> {
//    public VariableUsageVisitor() {
//    }
//
//    @Override
//    public List<String> visit(VCall vCall) {
//        return new ArrayList<>(Collections.singleton(vCall.addr.toString()));
//    }
//
//    @Override
//    public List<String> visit(VAssign vAssign) {
//        return new ArrayList<>(Collections.singleton(vAssign.source.toString()));
//    }
//
//    @Override
//    public List<String> visit(VBuiltIn vBuiltIn) {
//        ArrayList<String> args = new ArrayList<>();
//        for (VOperand arg : vBuiltIn.args) {
//            if (arg instanceof VVarRef.Local)
//                args.add(arg.toString());
//        }
//        return args;
//    }
//
//    @Override
//    public List<String> visit(VMemWrite vMemWrite) {
//        ArrayList<String> variables = new ArrayList<>();
//        if (vMemWrite.source instanceof VVarRef.Local)
//            variables.add(vMemWrite.source.toString());
//        if (vMemWrite.dest instanceof VMemRef.Global)
//            variables.add(((VMemRef.Global) vMemWrite.dest).base.toString());
//        return variables;
//    }
//
//    @Override
//    public List<String> visit(VMemRead vMemRead) {
//        ArrayList<String> variables = new ArrayList<>();
//        if (vMemRead.dest instanceof VVarRef.Local)
//            variables.add(vMemRead.dest.toString());
//        if (vMemRead.source instanceof VMemRef.Global)
//            variables.add(((VMemRef.Global) vMemRead.source).base.toString());
//        return variables;
//    }
//
//    @Override
//    public List<String> visit(VBranch vBranch) {
//        return new ArrayList<>(Collections.singleton(vBranch.value.toString()));
//    }
//
//    @Override
//    public List<String> visit(VGoto vGoto) {
//        return null;
//    }
//
//    @Override
//    public List<String> visit(VReturn vReturn) {
//        if (vReturn.value != null)
//            if (vReturn.value instanceof VVarRef.Local)
//                return new ArrayList<String>(Collections.singleton(vReturn.value.toString()));
//        return null;
//    }
//
//}
////class Instruction {
////    VInstr original;
////    List<String> lines;
////
////}
//
//class Call {
//    VCall original;
//    List<String> lines;
//    List<VInstr> call;
//
//    Call(VCall vCall) {
//        original = vCall;
//        lines = new ArrayList<>();
//        call = new ArrayList<>();
//        System.out.println();
//    }
//
//    List<VInstr> expand() {
//        call.addAll(insertCallerBackups(original.sourcePos));
//        call.addAll(passArguments(original));
//        call.add(new VCall(original.sourcePos, original.addr, new VOperand[]{}, null));
//        lines.add(String.format("call %s", original.addr.toString()));
//        if (original.dest != null) lines.add(String.format("%s = $v0", original.dest.ident));
//        if (original.dest != null)
//            call.add(new VAssign(original.sourcePos, original.dest, new VVarRef.Register(original.sourcePos, "v0", -1)));
//        call.addAll(insertCallerRestores(original.sourcePos));
//        return call;
//    }
//
//
//    List<VInstr> passArguments(VCall vCall) {
//        for (int i = 0; i < vCall.args.length; ++i)
//            lines.add(String.format("in[%d] = %s", i, vCall.args[i].toString()));
//        List<VInstr> pass = new ArrayList<>();
//        SourcePos pos = vCall.sourcePos;
//        for (int i = 0; i < vCall.args.length; ++i) {
//            VMemRef.Stack in = new VMemRef.Stack(pos, Region.Out, i);
//            VMemWrite arg = new VMemWrite(pos, in, vCall.args[i]);
//            pass.add(arg);
//        }
//        return pass;
//    }
//
//    List<VInstr> insertCallerBackups(SourcePos sourcePos) {
//        List<VInstr> backups = new ArrayList<>();
//        for (int i = 0; i < 8; ++i) {
//            VVarRef.Register register = new VVarRef.Register(sourcePos, "t" + i, -1);
//            VMemRef.Stack memory = new VMemRef.Stack(sourcePos, Region.Local, i + 8);
//            VMemWrite backup = new VMemWrite(sourcePos, memory, register);
//            backups.add(backup);
//            lines.add(String.format("local[%d] = $t%d", i + 8, i));
//        }
//        return backups;
//    }
//
//    private List<VInstr> insertCallerRestores(SourcePos sourcePos) {
//        List<VInstr> restores = new ArrayList<>();
//        for (int i = 0; i < 8; ++i) {
//            VVarRef.Register register = new VVarRef.Register(sourcePos, "t" + i, -1);
//            VMemRef.Stack memory = new VMemRef.Stack(sourcePos, Region.Local, i + 8);
//            VMemRead restore = new VMemRead(sourcePos, register, memory);
//            restores.add(restore);
//            lines.add(String.format("$t%d = local[%d]", i, i + 8));
//        }
//        return restores;
//    }
//
//    static boolean isIntLiteral(VOperand vOperand) {
//        return vOperand instanceof VLitInt;
//    }
//}