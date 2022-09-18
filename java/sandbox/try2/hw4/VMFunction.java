//package sandbox.try2.hw4;
//
//import cs132.util.SourcePos;
//import cs132.vapor.ast.*;
//import cs132.vapor.ast.VMemRef.Stack.Region;
//
//import java.util.*;
//
//import static translate.vaporm.hw4.Register.*;
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
//        cumulateSpills(function);
//        insertSpills(function);
//        allocateRegistersToTemporaries(function);
//        addInstructions(function);
//        addReturn(function);
//        for (String line : lines) {
//            if (line != null && !line.matches("^null$"))
//                System.out.println(line);
//        }
//    }
//
//    private void allocateRegistersToTemporaries(Graph function) {
//        for (Node node : function.nodes) {
//            if (node.num != 0) {
//                Record record = node.record;
//                swap(node, record.registers);
//                if (node.instruction instanceof VCall) {
//                    Map<String, Register> microMap = new HashMap<>();
//                    for (Variable spilledVariable : node.spilled) {
//                        microMap.put(spilledVariable.name, $t8);
//                    }
//                    swap(node, microMap);
//                } else {
//                    Map<String, Register> miniMap = new HashMap<>();
//                    Set<Register> registers = new HashSet<>() {{
//                        add($t0);
//                        add($v0);
//                        add($v1);
//                    }};
//                    for (Variable spilledVariable : node.spilled) {
//                        Register register = registers.iterator().next();
//                        registers.remove(register);
//                        miniMap.put(spilledVariable.name, register);
//                    }
//                    swap(node, miniMap);
//                }
//            }
//        }
//    }
//
//    void swap(Node node, Map<String, Register> trMap) {
//        SwapVisitor sv = new SwapVisitor();
//        for (var entry : trMap.entrySet()) {
//            RTPair tr = new RTPair(entry.getKey(), entry.getValue());
//            if (node.expanded) {
//                for (int i = 0; i < node.expansion.size(); ++i) {
//                    VInstr instruction = node.expansion.remove(i);
//                    instruction = instruction.accept(tr, sv);
//                    node.expansion.add(i, instruction);
//                }
//            } else {
//                node.instruction = node.instruction.accept(tr, sv);
//            }
//        }
//    }
//
//    private void cumulateSpills(Graph function) {
//        Set<Variable> spilledVariables = new HashSet<>();
//        for (Spill spill : function.spills) {
//            spilledVariables.add(spill.variable);
//            spill.variable.spilled = spill.backupPoint.num;
//        }
//        for (int i = 1; i < function.nodes.size(); ++i) {
//            Node node = function.nodes.get(i);
//            Set<Variable> spilled = new HashSet<>(node.variables);
//            for (var entry : node.record.registers.entrySet()) {
//                for (Variable variable : node.variables) {
//                    if (entry.getKey().equals(variable.name))
//                        spilled.remove(variable);
//                }
//            }
//            node.spilled = spilled;
//        }
//        DUVisitor duv = new DUVisitor();
//        for (Variable spilledVariable : spilledVariables) {
//            Set<Node> defs = new HashSet<>();
//            Set<Node> uses = new HashSet<>();
//            for (Node node : function.nodes) {
//                if (node.num != 0) {
//                    if (spilledVariable.name.equals("t.0"))
//                        System.out.println();
//                    int du = node.instruction.accept(spilledVariable, duv);
//                    switch (du) {
//                        case 1:
//                            defs.add(node);
//                            node.def = spilledVariable;
//                            break;
//                        case 2:
//                            uses.add(node);
//                            break;
//                        case 3:
//                            defs.add(node);
//                            uses.add(node);
//                            break;
//                    }
//                }
//            }
//            spilledVariable.defined = defs;
//            spilledVariable.used = uses;
//        }
//    }
//
//    void addReturn(Graph function) {
//        Node returnNode = function.nodes.get(function.nodes.size() - 1);
//        PrintVisitor pv = new PrintVisitor();
//        if (returnNode.expanded) {
//            SwapVisitor sv = new SwapVisitor();
//            for (VInstr instruction : returnNode.expansion) {
//                String line = instruction.accept(pv);
//                if (returnNode.record != null) {
//                    if (returnNode.record.registers != null) {
//                        for (var entry : returnNode.record.registers.entrySet()) {
//                            RTPair miniMap = new RTPair(entry.getKey(), entry.getValue());
//                            instruction = instruction.accept(miniMap, sv);
//                        }
//                        line = instruction.accept(pv);
//                    }
//                }
//                if (line != null)
//                    lines.add(line);
//            }
//        }
//        addCalleeRestores();
//        lines.add("ret");
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
//            for (Spill spill : function.spills)
//                if (spill.backupPoint.num == 0) params.remove(spill.variable.name);
//            Record firstRecord = function.nodes.get(0).record;
//            if (firstRecord != null) for (var entry : firstRecord.registers.entrySet()) {
//                if (params.contains(entry.getKey()))
//                    lines.add(String.format("%s = in[%d]", firstRecord.registers.get(entry.getKey()).toString(), order.indexOf(entry.getKey())));
//            }
//        }
//    }
//
//    void addInstructions(Graph function) {
//        SwapVisitor sv = new SwapVisitor();
//        PrintVisitor pv = new PrintVisitor();
//        Deque<VCodeLabel> q = new LinkedList();
//        for (VCodeLabel l : function.original.labels)
//            q.addLast(l);
//        int j;
//        for (int i = 0; i < function.nodes.size() - 1; ++i) {
//            j = i - 1;
//            Node node = function.nodes.get(i);
//            if (i != 0) {
//                if (!q.isEmpty()) {
//                    while (q.peek().instrIndex == j) {
//                        lines.add(q.pop().ident + ':');
//                        if (q.isEmpty())
//                            break;
//                    }
//                }
//            }
//            if (node.expanded) {
//                for (VInstr instruction : node.expansion) {
//                    if (instruction != null) {
//                        if (node.record != null) {
//                            if (node.record.registers != null) {
//                                for (var entry : node.record.registers.entrySet()) {
//                                    RTPair miniMap = new RTPair(entry.getKey(), entry.getValue());
//                                    if (instruction != null)
//                                        instruction = instruction.accept(miniMap, sv);
//                                }
//                            }
//                        }
//                        String line = instruction.accept(pv);
//                        lines.add(line);
//                    }
//                }
//            } else {
//                if (node.instruction != null) {
//                    VInstr copy = node.instruction;
//                    if (node.record != null)
//                        if (node.record.registers != null)
//                            for (var entry : node.record.registers.entrySet()) {
//                                RTPair miniMap = new RTPair(entry.getKey(), entry.getValue());
//                                copy = copy.accept(miniMap, sv);
//                            }
//                    String line = copy.accept(pv);
//                    lines.add(line);
//                }
//            }
//        }
//        if (!q.isEmpty())
//            while (!q.isEmpty())
//                lines.add(q.pop().ident + ':');
//    }
//
//    void insertSpills(Graph function) {
//        for (Spill spill : function.spills) {
//            VInstr backup = backup(spill, spill.variable.name);
//            VInstr restore = restore(spill, spill.variable.name);
//            Variable spilledVariable = spill.variable;
//            for (Node defNode : spilledVariable.defined) {
//                List<VInstr> expansion = new ArrayList<>();
//                if (defNode.expanded) {
//                    DUVisitor duVisitor2 = new DUVisitor();
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
//            for (Node useNode : spilledVariable.used) {
//                List<VInstr> expansion = new ArrayList<>();
//                if (useNode.num >= spilledVariable.spilled) {
//                    if (useNode.expanded) {
//                        DUVisitor duVisitor2 = new DUVisitor();
//                        for (VInstr instruction : useNode.expansion) {
//                            if (instruction.sourcePos.line != -69) {
//                                int bit = instruction.accept(spilledVariable, duVisitor2);
//                                if (bit == 2 || bit == 3) {
//                                    expansion.add(restore);
//                                }
//                            }
//                            expansion.add(instruction);
//                        }
//                        useNode.expansion = expansion;
//                    } else {
//                        expansion.add(restore);
//                        expansion.add(useNode.instruction);
//                        useNode.expansion = expansion;
//                        useNode.expanded = true;
//                    }
//                }
//            }
//        }
//    }
//
//    void expandCallNodes(List<Node> nodes) {
//        for (Node node : nodes) {
//            VInstr instruction = node.instruction;
//            if (instruction instanceof VCall) {
//                VCall call = (VCall) instruction;
//                node.expansion = new Call(call).expand();
//                node.expanded = true;
//            }
//        }
//    }
//
//    VMemRead restore(Spill spill, String name) {
//        SourcePos pos = new SourcePos(-69, -69);
//        VVarRef.Local temporary = new VVarRef.Local(pos, name, -1);
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
//        SourcePos pos = new SourcePos(-69, -69);
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
//        Node node = function.nodes.get(function.nodes.size() - 1);
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
//        this.local = 16 + spillSize(function);
//
//    }
//
//    public int spillSize(Graph function) {
//        List<String> params = new ArrayList<>();
//        for (VVarRef.Local param : function.original.params)
//            params.add(param.ident);
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
//        StringBuilder sb = new StringBuilder();
//        for (String line : lines) {
//        }
//        return null;
//    }
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
//        if (original.dest != null)
//            call.add(new VAssign(original.sourcePos, original.dest, new VVarRef.Register(original.sourcePos, "v0", -1)));
//        call.addAll(addCallerRestores(original.sourcePos));
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
//            VVarRef.Register register = new VVarRef.Register(sourcePos, "t" + i, -69);
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
//            VVarRef.Register register = new VVarRef.Register(sourcePos, "t" + i, -69);
//            VMemRef.Stack memory = new VMemRef.Stack(sourcePos, Region.Local, i + 8);
//            VMemRead restore = new VMemRead(sourcePos, register, memory);
//            restores.add(restore);
//            lines.add(String.format("$t%d = local[%d]", i, i + 8));
//        }
//        return restores;
//    }
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
