//package sandbox.try2;
//
//package vapor.vaporm.hw4;
//
//import cs132.util.SourcePos;
//import cs132.vapor.ast.*;
//import cs132.vapor.ast.VMemRef.Stack.Region;
//import vapor.vaporm.hw4.*;
//import vapor.vaporm.hw4.Node;
//
//import java.util.*;
//
//import static vapor.vaporm.hw4.Register.$v0;
//import static vapor.vaporm.hw4.Register.*;
//
//public class VMFunction {
//    int in, out, local;
//    List<String> lines;
//
//    public VMFunction(Graph function) {
//        this.lines = new ArrayList<>();
//        this.computeStackSize(function);
//        lines.add(String.format("func %s [in %d, out %d, local %d]", function.original.ident, in, out, local));
//        addCalleeBackups();
//        addArgumentRetrieval(function);
//        expandCallNodes(function.nodes);
//        expandReturnNode(function);
//
//
//
//
//        cumulateSpills(function); //collect spill data and other set data
//        insertSpills(function); //insert backups and restores using temporary names
//        allocateRegistersToTemporaries(function); //swap temporaries with their registers
//
//
//
//        addInstructions(function);
//        addReturn(function);
////        lines.add("ret");
//
////        boolean b = false;
////        int i = 0;
//        for (String line : lines) {
////            if (i == 596)
////                System.out.println();
////            for (String v : function.original.vars) {
////
////                if (Pattern.compile("(^|\\s+)"+v+"($|\\s+)").matcher(line).find())
////                    b = true;
////            }
////            if (!b)
//            if (line != null && !line.matches("^null$"))
//                System.out.println(line);
////            i++;
//        }
//
//
//    }
//
//
//    //pass 1: visit all nodes and use register map and call swap on all instructoins with each entry in the register map for the record of that node
//    //pass 2: visit non-call nodes and create temporary miniMap using v0, v1, and t8, repeat step 1 using this temporary record on the remaining variables
//    //pass 3: visit all call nodes and call swap on each instruction using only t8
//    private void allocateRegistersToTemporaries(Graph function) {
//    }
//
//    private void cumulateSpills(Graph function) {
//
//        //set of all variables spilled
//        //their first defs, their last uses,
//        //the nodes they are used in, the nodes they are defined in
//        //the nodes
//
//        //then just insert a backup after each def, a restore before each use
//        //and use $v0 $v1 and $t8 after the spill point
//
//        //make a list of variables spilledVariables that belongs to each graph
//        //make each variable have the above data
//        //then just go variable by variable
//        //and for each variable
//        //visit each node it is in
//        //then visit each instruction in the node expansion
//        //and for each instruction insert a backup if it is used
//        //and a restore if it is defined
//
//
//        //dude, you wasted so much time today tyring to do the above by:
//
//        //1. looking at it with a microscope (and that is almost literal, you wasted so much time debugging being so confused (although you did learn how to go back in the stack trace and which line called it and ide features and also strategies like instead of jumping to breakpoints to get to a specific spot, put an if clause with a condition that only the bug could reach, then just insert a empty print statemtn then just simply jump to that breakpoint and you can instanly see the state)
//        //2. coding in a wack-a-mole style (dealing with problems as they come, and coming up with ad hoc solutions without any consideration to an overall plan or going of any sort of plan, not to mention, this leads to bad code which couases more problems which then only makes it worse until you have monolithic spaghetti code that you don't understand and has tons of bugs and is unreadable, instead, plan it out before, and come up with totally  clera names and functions and roles and design the system beforehand with tons of foreseight and make sure there is not refactoring that can be deone, make it perfect the first time, never go ad hoc, someimtes functions will come to you as you code, but you shouldn't be defining functions and really anything else as you go, it is done beforehand)
//        //3. getting flustered and panicking which again only cycles and makes the probelm worse (a self-fulfilling prophecy)
//        //4. starting to code without complete knowledge of both the problem and solution (again, you can play around with code initially too in the planning stage like you do in your sandbox, then extract methods from there (because it is hard do come up with functions and methods when just htinkingn about it, you do kinda need to code a bit at least for you, it helps you think, and htis is okay and a good thing, but keep that style of ad hoc coding in the development stage when you are desiging the algorithm and working out problems by hand, once you start there shouldn't be any though and if there is you know the solution you just have to think of it for a second or two and then it comes to you because you konw the solution, if this doesn't happen, it means you need to go back to the planning stage and work out more examples by hand and come up with a clear design plan and then only after that once you are frothing to code because it seems so easy now, only then start coding, and use coding skills when doing it and design patterns)
//
//        //see, this is what you were talking about, you worked out a small example
//        //stepped back, then began thinking about it and writing out examples (little tiny ones)
//        //and now you are doing series of heuristics and pseudocode and learning the problem
//
//
//        //just do this then your job is super easy, just a couple of visitors, and then
//        //just know the solution before you start coding, not the other way around like you did today and wasted 12+ hours just to come to this
//        //seriously though, work out a couple exapmles, and then know EXACTLY how you want to do it
//
//
//        //so in this function collect the above information (and add that set to the Graph class and add the info to the Variable class)
//        //then in the next function (expand spills but is really insert spills but insert in your program means insert into the lines list, and you use expand because you are visiting the expansions and expanding them further, but keep in mind what you're doing, modularity!!!, non complex functions that do one easy to understand thing with an easy to understand name and variable names even if they are 10 words, that often is even better, remember, nothing you do syntax-wise is going to make your code more efficient (one line ifs, unclear syntactic "shortcuts" which really jsut make code harder to understand even if it takes one less line, and variable names like b and f that you have in mind as you are coding it (this variable b represents this ) but then in a week when you come back to it it makes zero sense and thanks to it having a dumbass name like 'b' it is impossible to infer
//        //in the next function, loop through that set of variables
//        //for each spilled variable
//        //visit each node it was either used or defined in
//        //if it is before the spill point, use the register assigned in the map and only add backup after defs
//        //if it is in a node at (actually, maybe just after, just go look in the debugger and write out an example) or after the spill point, use $v1 $v0 $t8 and both backup and restore
//
//
//        //also fix records and maps
//        Set<Variable> spilledVariables = new HashSet<>();
//        for (Spill spill : function.spills) {
//            spilledVariables.add(spill.variable);
//        }
//        //find all defs and uses
//        vapor.vaporm.hw4.DUVisitor2 duv = new vapor.vaporm.hw4.DUVisitor2();
//        for (Variable spilledVariable : spilledVariables) {
//            Set<vapor.vaporm.hw4.Node> defs = new HashSet<>();
//            Set<vapor.vaporm.hw4.Node> uses = new HashSet<>();
//            for (vapor.vaporm.hw4.Node node : function.nodes) {
////                if (node.expanded) {
//
////                } else {
//                if (node.num != 0)
//                    node.instruction.accept(spilledVariable, duv);
////                }
//            }
//            spilledVariable.defined = defs;
//            spilledVariable.used = uses;
//        }
//    }
//
//    String swapVarReg(String line, Map<String, Register> map, Graph function) {
//        if (line == null)
//            return "";
//
//        if (line.equals("t.3 = $v0"))
//            System.out.println();
//        for (var entry : map.entrySet()) {
//            line = line.replace(entry.getKey(), entry.getValue().toString());
//        }
//        for (Variable v : function.variables) {
//            if (line.contains(v.name))
//                return "";
//        }
//
//        return line;
//        //BUG (think vars named in and out and HeapAllocZ etc)
//    }
//
//    void addReturn(Graph function) {
//        vapor.vaporm.hw4.Node returnNode = function.nodes.get(function.nodes.size() - 1);
//        vapor.vaporm.hw4.PrintVisitor pv = new vapor.vaporm.hw4.PrintVisitor();
//        if (returnNode.expanded) {
//            vapor.vaporm.hw4.SwapVisitor sv = new vapor.vaporm.hw4.SwapVisitor();
//            for (VInstr instruction : returnNode.expansion) {
//                String line = instruction.accept(pv);
//                if (returnNode.record != null) {
//                    if (returnNode.record.registers != null) {
//                        for (var entry : returnNode.record.registers.entrySet()) {
//                            vapor.vaporm.hw4.RTPair miniMap = new vapor.vaporm.hw4.RTPair(entry.getKey(), entry.getValue());
//                            instruction = instruction.accept(miniMap, sv);
//                        }
//                        /*String */
//                        line = instruction.accept(pv);
////                    String line = swapVarReg(line, node.record.registers, function);
////                        line = swapVarReg(line, returnNode.record.registers, function); //or you could change this function to take that and loop through map and call swap visitor a bunch of times and then print visitor and return that
//                    }
////                    lines.add(line);
//                }
//                if (line != null)
//                    lines.add(line);
//            }
//            addCalleeRestores();
//            lines.add("ret");
//        } else {
//            addCalleeRestores();
//            lines.add("ret");
//        }
//    }
//
//    void addArgumentRetrieval(Graph function) {
//        Set<String> params = new HashSet<>();
//        List<String> order = new ArrayList<>();
//        if (function.nodes.get(0).params != null) {
//            for (Variable param : function.nodes.get(0).params) {
//                params.add(param.name);
//                order.add(param.name);
//            }
//
//            for (Spill spill : function.spills)
//                if (spill.backupPoint.num == 0) params.remove(spill.variable.name);
//
//            Record firstRecord = function.nodes.get(0).record;
//
//            if (firstRecord != null) for (var entry : firstRecord.registers.entrySet()) {
//                if (params.contains(entry.getKey()))
//                    lines.add(String.format("%s = in[%d]", firstRecord.registers.get(entry.getKey()).toString(), order.indexOf(entry.getKey())));
//            }
//
//
//            for (String param : params) {
//            }
//            vapor.vaporm.hw4.Node firstNode = function.nodes.get(0);
//            Record startingParameters = function.nodes.get(0).record;
//        }
//    }
//
//    void addInstructions(Graph function) {
//        vapor.vaporm.hw4.SwapVisitor sv = new vapor.vaporm.hw4.SwapVisitor();
//        vapor.vaporm.hw4.PrintVisitor pv = new vapor.vaporm.hw4.PrintVisitor();
//        Deque<VCodeLabel> q = new LinkedList();
//        for (VCodeLabel l : function.original.labels)
//            q.addLast(l);
//
//        int j;
//        for (int i = 0; i < function.nodes.size() - 1; ++i) {
//            j = i - 1;
//            vapor.vaporm.hw4.Node node = function.nodes.get(i);
//            if (i != 0) {
//                int linePos = node.instruction.sourcePos.line;
//                if (!q.isEmpty()) {
//                    while (q.peek().instrIndex == j) {
//                        lines.add(q.pop().ident + ':');
//
////                        j++;
//                        if (q.isEmpty())
//                            break;
//                    }
//                }
//            }
//            if (node.expanded) {
//                for (VInstr instruction : node.expansion) {
//                    if (instruction != null) {
//
//                        if (node.record != null) {
//                            if (node.record.registers != null) {
//                                for (var entry : node.record.registers.entrySet()) {
//                                    vapor.vaporm.hw4.RTPair miniMap = new vapor.vaporm.hw4.RTPair(entry.getKey(), entry.getValue());
//                                    if (instruction != null)
//                                        instruction = instruction.accept(miniMap, sv);
//                                }
//                            }
//                        }
//                        if (instruction == null)
//                            System.out.println();
//                        String line = instruction.accept(pv);
////                                line = swapVarReg(line, node.record.registers, function);
//                        lines.add(line);
//                    }
//                }
//            } else {
//                if (node.instruction != null) {
//                    VInstr copy = node.instruction;
//                    if (node.record != null)
//                        if (node.record.registers != null)
//                            for (var entry : node.record.registers.entrySet()) {
//                                vapor.vaporm.hw4.RTPair miniMap = new vapor.vaporm.hw4.RTPair(entry.getKey(), entry.getValue());
//                                copy = copy.accept(miniMap, sv);
//                            }
//                    String line = copy.accept(pv);
////                    String line = swapVarReg(line, node.record.registers, function);
//                    lines.add(line);
//                }
//            }
//        }
//        if (!q.isEmpty())
//            while (!q.isEmpty())
//                lines.add(q.pop().ident + ':');
//    }
//
//
//
//    //for each spilled variable:
//    //  create backup and restore using the temporary name (worry about v0 v1 t8 later in the next step)
//    //  for each node the variable was defined
//    //      insert a backup instruction after the instruction(s) the spilled variabled was defined in
//    //  for each node the variable was used
//    //      if the node is at or past the spill point
//    //          insert a restore before the instruction(s) the variable was used in (du visitor)
//    void insertSpills(Graph function) {
////        for (Variable spilledVariable : function.spilledVariables) {
////            VInstr backup = backup();
////        }
//
//        for (Spill spill : function.spills) {
//            VInstr backup = backup(spill, spill.variable.name);
//            VInstr restore = restore(spill, spill.variable.name);
//            Variable spilledVariable = spill.variable;
//            for (vapor.vaporm.hw4.Node defNode : spilledVariable.defined) {
//                List<VInstr> expansion = new ArrayList<>();
//                if (defNode.expanded) {
//                    vapor.vaporm.hw4.DUVisitor2 duVisitor2 = new vapor.vaporm.hw4.DUVisitor2();
//                    for (VInstr instruction : defNode.expansion) {
//                        expansion.add(instruction);
//                        int bit = instruction.accept(spilledVariable, duVisitor2);
//                        if (bit == 1 || bit == 3) {
//                            expansion.add(backup);
//                        }
//                    }
//                    defNode.expansion = expansion;
//                } else {
//                    expansion.add(defNode.instruction);
//                    expansion.add(backup);
//                    defNode.expansion = expansion;
//                    defNode.expanded = true;
//                }
//            }
//            for (vapor.vaporm.hw4.Node useNode : spilledVariable.used) {
//                List<VInstr> expansion = new ArrayList<>();
//                if (useNode.expanded) {
//                    vapor.vaporm.hw4.DUVisitor2 duVisitor2 = new vapor.vaporm.hw4.DUVisitor2();
//                    for (VInstr instruction : useNode.expansion) {
//                        int bit = instruction.accept(spilledVariable, duVisitor2);
//                        if (bit == 2 || bit == 3) {
//                            expansion.add(restore);
//                        }
//                        expansion.add(instruction);
//                    }
//                    useNode.expansion = expansion;
//                } else {
//                    expansion.add(restore);
//                    expansion.add(useNode.instruction);
//                    useNode.expansion = expansion;
//                    useNode.expanded = true;
//                }
//            }
//        }
//        /*for (Spill spill : function.spills) { //Spill is a record of a spill
//            Node node = spill.backupPoint;
//            //or start from zero and add a backup to that memory spot after every use (convservative)
//            for (int i = 0; i < node.num; ++i) {
//                Node n = function.nodes.get(i);
//                if (n.def == spill.variable) {
//                    if (!n.expanded) {
//                        n.expansion = new ArrayList<>();
//                        n.expanded = true;
//                    }
//                    VMemRef.Stack memRef = new VMemRef.Stack(n.instruction.sourcePos, spill.region, spill.location + 16);
//                    VOperand operand = new VVarRef.Register(n.instruction.sourcePos, n.record.registers.get(spill.variable.name).toString().substring(1), -1);
//                    VInstr backup = new VMemWrite(n.instruction.sourcePos, memRef, operand);
//                    n.expansion.add(n.instruction);
//                    n.expansion.add(backup);
//                    break;
//                }
//
//            }
//            for (int i = node.num; i < function.nodes.size(); ++i) {
//                node = function.nodes.get(i);
//                node.expansion = spill(node, spill);
//                node.expanded = true;
//            }
//        }*/
//    }
//
//    void expandCallNodes(List<vapor.vaporm.hw4.Node> nodes) {
//        for (vapor.vaporm.hw4.Node node : nodes) {
//            VInstr instruction = node.instruction;
//            if (instruction instanceof VCall) {
//                VCall call = (VCall) instruction;
//                node.expansion = new vapor.vaporm.hw4.Call(call).expand();
//                node.expanded = true;
//            } /*else if (node.instruction instanceof VReturn) {
//                VReturn vReturn = (VReturn) node.instruction;
//                if (vReturn.value != null) {
//                    node.expansion = expandReturn(vReturn);
//                    node.expanded = true;
//                }
//            }        */              /*  if (node.record != null)*/
//            /* if (node.record.registers != null)*/
//        }
//    }
//
//
////    List<VInstr> addBackup(Node node, Spill spill, List<VInstr> instructions) {
////
////
////    }
//
//    boolean isDefinedInInstruction(Variable variable, VInstr instruction) {
//        if (instruction instanceof VAssign) {
//            VAssign a = (VAssign) instruction;
//            return variable.name.equals(a.dest.toString());
//        } else if (instruction instanceof VMemRead) {
//            VMemRead r = (VMemRead) instruction;
//            return variable.name.equals(r.dest.toString());
//        } else if (instruction instanceof VBuiltIn) {
//            VBuiltIn b = (VBuiltIn) instruction;
//            if (b.op == VBuiltIn.Op.Error || b.op == VBuiltIn.Op.PrintIntS)
//                return false;
//            return variable.name.equals(b.dest.toString());
//        }
//        return false;
//    }
//
//    List<VInstr> spill(vapor.vaporm.hw4.Node node, Spill spill) {
//
//        vapor.vaporm.hw4.PrintVisitor pv = new vapor.vaporm.hw4.PrintVisitor();
//        List<VInstr> instructions = new ArrayList<>();
//        vapor.vaporm.hw4.SwapVisitor sv = new vapor.vaporm.hw4.SwapVisitor();
//        if (spill.variable.name.equals("num_aux"))
//            System.out.println();
//        if (node.use.contains(spill.variable)) {
//
//
////            if (node.def != spill.variable) {
//            String register = node.used$v1 ? "v0" : "v1";
//            Register r = node.used$v1 ? $v0 : $v1;
//            vapor.vaporm.hw4.RTPair miniMap = new vapor.vaporm.hw4.RTPair(spill.variable.name, r);
//
//            if (node.expanded) {
//                VInstr restore = restore(spill, register);
//                VInstr backup = backup(spill, register); //t8?
//                vapor.vaporm.hw4.VariableUsageVisitor vuv = new vapor.vaporm.hw4.VariableUsageVisitor();
//                for (VInstr instruction : node.expansion) {
//                    if (instruction != null) {
//                        List<String> variablesUsed = instruction.accept(vuv);
//                        if (variablesUsed != null) {
//                            if (variablesUsed.contains(spill.variable.name)) {
//                                instructions.add(restore);
//                                instructions.add(instruction.accept(miniMap, sv));
////                                String r = restore.accept(pv);
////                                lines.add(r);
////                                String i = instruction.accept(pv).replace(spill.variable.name, '$' + register);
////                                lines.add(i);
//                                //won't work for variables names out/in/local/[]/vapor reserved words
//                            } else {
//                                instructions.add(instruction.accept(miniMap, sv));
//
////                                lines.add(instruction.accept(pv));
//                            }
//                        }
//                        if (node.def == spill.variable) {
//                            if (isDefinedInInstruction(spill.variable, instruction)) {
////                                instructions.add(backup(spill, register));
//                                instructions.add(backup);
//
//                            }
//                        }
//                    }
//                }
//            } else {
//                instructions.add(restore(spill, register));
//                instructions.add(node.instruction.accept(miniMap, sv));
//                if (node.def == spill.variable) {
//                    if (isDefinedInInstruction(spill.variable, node.instruction)) {
//                        instructions.add(backup(spill, register));
//                    }
//                }
//            }
//
////            } else {
////                System.out.println();
////                //also check for backup here incase both used and defined
////                //in which case use the same register
////                //do same thing as above but add this check at the end of the restore loop and the else clause if not expanded
////
////                if (node.def == spill.variable) {
////                    if (instruction instanceof VAssign) {
////
////                    } else if (instruction instanceof VMemRead) {
////
////                    } else if (instruction instanceof VBuiltIn) {
////                        VBuiltIn b = (VBuiltIn) instruction;
////                        if (b.op != VBuiltIn.Op.Error && b.op != VBuiltIn.Op.PrintIntS)
////                            System.out.println();
////                    }
////                }
////
////            }
//
//            node.used$v1 = true;
//        } else if (node.def == spill.variable) {
//            vapor.vaporm.hw4.RTPair miniMap = new vapor.vaporm.hw4.RTPair(spill.variable.name, $t8);
//            if (node.expanded)
//                node.expansion.forEach((instruction) -> {
//                    if (instruction != null) instructions.add(instruction.accept(miniMap, sv));
//                });
//            else
//                instructions.add(node.instruction.accept(miniMap, sv));
//            instructions.add(backup(spill, "t8"));
//        } else {
//            if (node.expanded)
//                instructions.addAll(node.expansion);
//            else
//                instructions.add(node.instruction);
//        }
//        return instructions;
//    }
//
//    String swap(String temporary, String register, String input) {
//        register = "\\$" + register;
//        return input.replaceAll(temporary, register);
//    }
//
//    VMemRead restore(Spill spill, String name) {
//        SourcePos pos = new SourcePos(-1, -1);
////        VVarRef.Register register = new VVarRef.Register(pos, name, -1);
//        VVarRef.Local temporary = new VVarRef.Local(pos, name, -1);
//
//        VMemRef memory;
//        if (spill.region == Region.Local)
//            memory = new VMemRef.Stack(pos, spill.region, spill.location + 16);
//        else
//            memory = new VMemRef.Stack(pos, spill.region, spill.location);
//        VMemRead restore = new VMemRead(pos, temporary, memory);
//        return restore;
//    }
//
//    VMemWrite backup(Spill spill, String name) {
//        VMemWrite backup;
//        SourcePos pos = new SourcePos(-1, -1);
////        VVarRef.Register r = new VVarRef.Register(pos, register, -1);
//        VVarRef.Local temporary = new VVarRef.Local(pos, name, -1);
//        VMemRef memory;
//        if (spill.region == Region.Local)
//            memory = new VMemRef.Stack(pos, spill.region, spill.location + 16);
//        else
//            memory = new VMemRef.Stack(pos, spill.region, spill.location);
//        backup = new VMemWrite(pos, memory, temporary);
//        return backup;
//    }
//
//    void expandReturnNode(Graph function) {
//        vapor.vaporm.hw4.Node node = function.nodes.get(function.nodes.size() - 1);
//        VReturn vReturn = (VReturn) node.instruction;
//        if (vReturn.value == null)
//            return;
//        List<VInstr> instructions = new ArrayList<>();
//        VAssign vAssign = new VAssign(vReturn.sourcePos, new VVarRef.Register(vReturn.sourcePos, "v0", -1), vReturn.value);
//        VReturn ret = new VReturn(vReturn.sourcePos, null);
//        instructions.add(vAssign);
//        instructions.add(ret);
//        node.expanded = true;
//        node.expansion = instructions;
////        SourcePos sourcePos = vReturn.sourcePos;
////        VVarRef.Register returnRegister = new VVarRef.Register(sourcePos, "v0", -1);
////        VAssign returnAssignment = new VAssign(sourcePos, returnRegister, vReturn.value);
////        ret.add(returnAssignment);
////        VReturn returnStatement = new VReturn(sourcePos, null);
////        ret.add(returnStatement);
////        return ret;
//    }
//
//    void addCalleeBackups() {
//        for (int i = 0; i < 8; ++i) {
//            lines.add(String.format("local[%d] = $s%d", i, i));
//        }
//    }
//
//    void addCalleeRestores() {
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
//            if (!params.contains(var)) locals.add(var);
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
//            if (call instanceof VCall) out = Math.max(out, ((VCall) call).args.length);
//        }
//        return out;
//    }
//
//    @Override
//    public String toString() {
//        //print the function as vapor-m (do same for hw3 and make it better)
//        StringBuilder sb = new StringBuilder();
//        for (String line : lines) {
//
//        }
//        return null;
//    }
//
//    String createExactRegex(String input) {
//        StringBuilder sb = new StringBuilder("^\\s");
//        for (Character c : input.toCharArray()) {
//
//        }
//        return null;
//    }
//
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
//            if (((VMemRef.Global) vMemWrite.dest).byteOffset > 0)
//                memory = String.format("[%s+%d]", ((VMemRef.Global) vMemWrite.dest).base, ((VMemRef.Global) vMemWrite.dest).byteOffset);
//            else
//                memory = String.format("[%s]", ((VMemRef.Global) vMemWrite.dest).base);
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
//            if (((VMemRef.Global) vMemRead.source).byteOffset > 0)
//                memory = String.format("[%s+%d]", ((VMemRef.Global) vMemRead.source).base, ((VMemRef.Global) vMemRead.source).byteOffset);
//            else
//                memory = String.format("[%s]", ((VMemRef.Global) vMemRead.source).base);
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
//            if (arg instanceof VVarRef.Local) args.add(arg.toString());
//        }
//        return args;
//    }
//
//    @Override
//    public List<String> visit(VMemWrite vMemWrite) {
//        ArrayList<String> variables = new ArrayList<>();
//        if (vMemWrite.source instanceof VVarRef.Local) variables.add(vMemWrite.source.toString());
//        if (vMemWrite.dest instanceof VMemRef.Global) variables.add(((VMemRef.Global) vMemWrite.dest).base.toString());
//        return variables;
//    }
//
//    @Override
//    public List<String> visit(VMemRead vMemRead) {
//        ArrayList<String> variables = new ArrayList<>();
//        if (vMemRead.dest instanceof VVarRef.Local) variables.add(vMemRead.dest.toString());
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
//        if (vReturn.value != null) if (vReturn.value instanceof VVarRef.Local)
//            return new ArrayList<String>(Collections.singleton(vReturn.value.toString()));
//        return null;
//    }
//
//}
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
//        call.addAll(addCallerBackups(original.sourcePos));
//        call.addAll(passArguments(original));
//        call.add(new VCall(original.sourcePos, original.addr, new VOperand[]{}, null));
////        lines.add(String.format("call %s", original.addr.toString()));
////        if (original.dest != null) lines.add(String.format("%s = $v0", original.dest.ident));
//        call.addAll(addCallerRestores(original.sourcePos));
//        if (original.dest != null)
//            call.add(new VAssign(original.sourcePos, original.dest, new VVarRef.Register(original.sourcePos, "v0", -1)));
//        return call;
//    }
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
//    List<VInstr> addCallerBackups(SourcePos sourcePos) {
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
//    private List<VInstr> addCallerRestores(SourcePos sourcePos) {
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
//}
//
//
//class SwapVisitor extends VInstr.VisitorPR<vapor.vaporm.hw4.RTPair, VInstr, RuntimeException> {
//    public SwapVisitor() {
//    }
//
//    VOperand swapOperand(VOperand vOperand, String temporary, Register register) {
//        if (vOperand instanceof VOperand.Static) return vOperand;
//        if (vOperand instanceof VLitStr) return vOperand;
//        if (vOperand instanceof VVarRef) return swapVarRef((VVarRef) vOperand, temporary, register);
//        return null;
//    }
//
//    VVarRef swapVarRef(VVarRef vVarRef, String temporary, Register register) {
//        if (vVarRef == null) return null;
//        if (vVarRef instanceof VVarRef.Register) return vVarRef;
//        if (!temporary.equals(vVarRef.toString())) return vVarRef;
//        VVarRef.Register varRef = new VVarRef.Register(vVarRef.sourcePos, register.toString().substring(1), -1);
//        return varRef;
//    }
//
//    @Override
//    public VInstr visit(vapor.vaporm.hw4.RTPair map, VAssign vAssign) {
//        if (!map.temporary.equals(vAssign.dest.toString()) && !map.temporary.equals(vAssign.source.toString()))
//            return vAssign;
//        VOperand source;
//        if (map.temporary.equals(vAssign.source.toString()))
//            source = swapOperand(vAssign.source, map.temporary, map.register);
//        else source = vAssign.source;
//        VVarRef dest;
//        if (!(vAssign.dest instanceof VVarRef.Register)) {
//            if (map.temporary.equals(vAssign.dest.toString()))
//                dest = swapVarRef(vAssign.dest, map.temporary, map.register);
//            else dest = vAssign.dest;
//        } else {
//            dest = vAssign.dest;
//        }
//        return new VAssign(vAssign.sourcePos, dest, source);
//    }
//
//    @Override
//    public VInstr visit(vapor.vaporm.hw4.RTPair map, VCall vCall) {
//        if (!(vCall.addr instanceof VAddr.Var)) return vCall;
//        if (((VAddr.Var) vCall.addr).var instanceof VVarRef.Register) return vCall;
////        if (map.temporary.equals(((VAddr.Var) vCall.addr).var.toString())) return vCall;
//        VVarRef ref = swapVarRef(((VAddr.Var) vCall.addr).var, map.temporary, map.register);
//        VAddr.Var<VFunction> addr = new VAddr.Var(ref);
//        return new VCall(vCall.sourcePos, addr, vCall.args, vCall.dest);
//    }
//
//    @Override
//    public VInstr visit(vapor.vaporm.hw4.RTPair map, VBuiltIn vBuiltIn) {
//        ArrayList<VOperand> args = new ArrayList<>();
//        for (VOperand arg : vBuiltIn.args) {
//            args.add(swapOperand(arg, map.temporary, map.register));
//        }
//
//        VBuiltIn vb = new VBuiltIn(vBuiltIn.sourcePos, vBuiltIn.op, args.toArray(vBuiltIn.args), swapVarRef(vBuiltIn.dest, map.temporary, map.register));
//        return vb;
//    }
//
//    @Override
//    public VInstr visit(vapor.vaporm.hw4.RTPair map, VMemWrite vMemWrite) {
//        SourcePos pos = vMemWrite.sourcePos;
//        VMemRef memRef;
//        if (vMemWrite.dest instanceof VMemRef.Global) {
//            VMemRef.Global g = (VMemRef.Global) vMemWrite.dest;
//            if (g.base instanceof VAddr.Var) {
//                VAddr.Var v = (VAddr.Var) g.base;
//                VAddr.Var nv = new VAddr.Var(swapVarRef(v.var, map.temporary, map.register));
//                memRef = new VMemRef.Global(vMemWrite.dest.sourcePos, nv, g.byteOffset);
//            } else {
//                memRef = vMemWrite.dest;
//            }
//        } else
////        if (vMemWrite.dest instanceof VMemRef.Stack)
//            memRef = vMemWrite.dest;
//        VOperand vo = swapOperand(vMemWrite.source, map.temporary, map.register);
//        VMemWrite vMemWrite2 = new VMemWrite(vMemWrite.sourcePos, memRef, swapOperand(vMemWrite.source, map.temporary, map.register));
//        return vMemWrite2;
//    }
//
//    @Override
//    public VInstr visit(vapor.vaporm.hw4.RTPair map, VMemRead vMemRead) {
//        SourcePos pos = vMemRead.sourcePos;
//        VMemRef memRef;
//        if (vMemRead.source instanceof VMemRef.Global) {
//            VMemRef.Global g = (VMemRef.Global) vMemRead.source;
//            if (g.base instanceof VAddr.Var) {
//                VAddr.Var v = (VAddr.Var) g.base;
//                VAddr.Var nv = new VAddr.Var(swapVarRef(v.var, map.temporary, map.register));
//                memRef = new VMemRef.Global(vMemRead.source.sourcePos, nv, g.byteOffset);
//            } else {
//                memRef = vMemRead.source;
//            }
//        } else
////        if (vMemRead.dest instanceof VMemRef.Stack)
//            memRef = vMemRead.source;
//        VOperand vo = swapOperand(vMemRead.dest, map.temporary, map.register);
//        VMemRead vMemRead2 = new VMemRead(vMemRead.sourcePos, swapVarRef(vMemRead.dest, map.temporary, map.register), memRef);
//        return vMemRead2;
//    }
//
//    @Override
//    public VInstr visit(vapor.vaporm.hw4.RTPair map, VBranch vBranch) {
//        VBranch branch;
//        VOperand value = swapOperand(vBranch.value, map.temporary, map.register);
//        branch = new VBranch(vBranch.sourcePos, vBranch.positive, value, vBranch.target);
//        return branch;
//    }
//
//    @Override
//    public VInstr visit(vapor.vaporm.hw4.RTPair map, VGoto vGoto) {
//        return vGoto;
//    }
//
//    @Override
//    public VInstr visit(vapor.vaporm.hw4.RTPair map, VReturn vReturn) {
//        return vReturn;
//    }
//
//}
//
//class RTPair {
//    Register register;
//    String temporary;
//
//    public RTPair(String name, Register register) {
//        this.register = register;
//        this.temporary = name;
//    }
//}
//
//
//class DUVisitor2 extends VInstr.VisitorPR<Variable, Integer, RuntimeException> {
//    public DUVisitor2() {
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
//
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
//
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
//        VMemRef.Global dest = (VMemRef.Global) write.dest;
//        if (v.name.equals(dest.toString()))
//            bit += 2;
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
//        VMemRef.Global source = (VMemRef.Global) read.source;
//        if (v.name.equals(source.toString()))
//            bit += 2;
//        VOperand dest = read.dest;
//        if (v.name.equals(dest.toString()))
//            if (bit == 0)
//                bit += 2;
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
//
//}
