package translate.vaporm;

import cs132.util.SourcePos;
import cs132.vapor.ast.*;

import java.util.*;

public class Node {
    Graph function;
    VFunction originalFunction;
    public int line;
    public int number;
    VInstr originalInstruction;
    Set<Node> predecessors;
    Set<Node> successors;
    Set<String> liveIn;
    Set<String> liveOut;
    boolean expanded;
    List<VInstr> expansion;
    List<String> lines;
    Record record;
    Set<String> spilledVariables;
    Set<String> mappedVariables;
    Set<String> variables;
    public Set<String> locals;
    public Set<String> parameters;
    String definedVariable;
    Set<String> usedVariables;
    List<Register> availableRegisters;
    Map<String, Register> spillMap;

    public Node(Graph function, VFunction originalFunction, int number) {
        this.function = function;
        this.originalFunction = originalFunction;
        this.number = number;
        this.predecessors = new HashSet<>();
        this.successors = new HashSet<>();
        this.liveIn = new HashSet<>();
        this.liveOut = new HashSet<>();
        this.lines = new LinkedList<>();
        this.expanded = false;
        this.spilledVariables = new HashSet<>();
        this.mappedVariables = new HashSet<>();
        this.variables = new HashSet<>();
        this.locals = new HashSet<>();
        this.parameters = new HashSet<>();
        this.usedVariables = new HashSet<>();
        this.spillMap = new HashMap<>();
    }

    public Node(Graph function, VFunction originalFunction, VInstr originalInstruction, int number) {
        this.function = function;
        this.originalFunction = originalFunction;
        this.number = number;
        this.predecessors = new HashSet<>();
        this.successors = new HashSet<>();
        this.liveIn = new HashSet<>();
        this.liveOut = new HashSet<>();
        this.originalInstruction = originalInstruction;
        this.lines = new LinkedList<>();
        this.expanded = false;
        this.spilledVariables = new HashSet<>();
        this.mappedVariables = new HashSet<>();
        this.variables = new HashSet<>();
        this.usedVariables = new HashSet<>();
        this.spillMap = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder lines = new StringBuilder();
        if (number == -1) {
            for (int i = 0; i < this.lines.size(); ++i) {
                if (i == 0) {
                    lines.append(this.lines.get(0)).append('\n');
                } else {
                    lines.append('\t').append(this.lines.get(i)).append('\n');
                }
            }
        } else {
            for (String line : this.lines) {
                if (line != null)
                    if (!line.isBlank())
                        lines.append('\t').append(line).append('\n');
            }
        }
        return lines.toString();
    }

    public void addLines() {
        PrintVisitor pv = new PrintVisitor();
        if (number == -1) {
            createSignature();
            insertCalleeBackups();
            insertArgumentRetrieval();
        } else if (originalInstruction instanceof VCall) {
            VInstr instruction;
            insertCallerBackups();
            for (int i = 0; i < expansion.size(); ++i) {
                instruction = expansion.get(i);
                if (!(instruction instanceof VCall)) {
                    lines.add(instruction.accept(pv));
                } else {
                    lines.add(instruction.accept(pv));
                    insertCallerRestores();
                    i += 1;
                    if (i != expansion.size()) {
                        lines.add(expansion.get(i).accept(pv));
                    }
                }
            }
        } else if (originalInstruction instanceof VReturn) {
            if (expanded) {
                for (VInstr instruction : expansion) {
                    if (instruction instanceof VReturn) {
                        insertCalleeRestores();
                        lines.add("ret");
                    } else {
                        lines.add(instruction.accept(pv));
                    }
                }
            } else {
                insertCalleeRestores();
                lines.add("ret");
            }
        } else {
            if (expanded) {
                for (VInstr instruction : expansion) {
                    lines.add(instruction.accept(pv));
                }
            } else {
                lines.add(originalInstruction.accept(pv));
            }
        }
    }

    public void expand() {
        if (this.number == -1) {
            return;
        }
        boolean isCall = this.originalInstruction instanceof VCall;
        boolean isReturn = this.originalInstruction instanceof VReturn;
        if (!isCall && !isReturn) return;
        if (isCall) {
            expandCall();
        }
        if (isReturn) {
            expandReturn();
        }
    }

    private void expandCall() {
        VCall vCall = (VCall) originalInstruction;
        expansion = new ArrayList<>();
        expanded = true;
        expansion.addAll(passArguments());
        expansion.add(new VCall(vCall.sourcePos, vCall.addr, null, null));
        VVarRef.Register returnRegister = new VVarRef.Register(vCall.sourcePos, "v0", -1);
        if (vCall.dest != null) expansion.add(new VAssign(vCall.sourcePos, vCall.dest, returnRegister));
    }

    private List<VInstr> passArguments() {
        VCall vCall = (VCall) originalInstruction;
        List<VInstr> pass = new ArrayList<>();
        SourcePos pos = vCall.sourcePos;
        for (int i = 0; i < vCall.args.length; ++i) {
            VMemRef.Stack in = new VMemRef.Stack(pos, VMemRef.Stack.Region.Out, i);
            VMemWrite arg = new VMemWrite(pos, in, vCall.args[i]);
            pass.add(arg);
        }
        return pass;
    }

    private void expandReturn() {
        VReturn vReturn = (VReturn) originalInstruction;
        if (vReturn.value == null) return;
        expansion = new ArrayList<>();
        expanded = true;
        VVarRef.Register returnRegister = new VVarRef.Register(vReturn.sourcePos, "v0", -1);
        VAssign returnAssignment = new VAssign(vReturn.sourcePos, returnRegister, vReturn.value);
        expansion.add(returnAssignment);
        expansion.add(new VReturn(vReturn.sourcePos, null));
    }

    private void insertArgumentRetrieval() {
        Set<String> mappedParameters = new HashSet<>(function.parameters);
        mappedParameters.removeAll(function.blocks.get(0).spilledVariables);
        Record firstRecord = function.blocks.get(0).record;
        for (String parameter : mappedParameters)
            lines.add(String.format("%s = in[%d]", firstRecord.registerMap.get(parameter).toString(), function.parameters.indexOf(parameter)));
    }

    public void spill() {
        if (number == -1) return;
        Set<String> functionWideSpillsInNode = new HashSet<>(function.spilledVariables);
        functionWideSpillsInNode.retainAll(variables);
        for (String variable : functionWideSpillsInNode) {
            Spill spill = null;
            for (Spill s : function.spills) {
                if (variable.equals(s.variable)) {
                    spill = s;
                    break;
                }
            }
            int firstDefNode = function.intervals.get(variable).start;
            if (number == firstDefNode) {
                if (expanded) {
                    List<VInstr> expansion = new LinkedList<>();
                    for (VInstr instruction : this.expansion) {
                        expansion.addAll(insertBackup(spill, instruction));
                    }
                    this.expansion = expansion;
                } else {
                    this.expansion = insertBackup(spill, originalInstruction);
                    this.expanded = true;
                }
            } else {
                if (expanded) {
                    List<VInstr> expansion = new LinkedList<>();
                    for (VInstr instruction : this.expansion) {
                        expansion.addAll(insertSpill(spill, instruction));
                    }
                    this.expansion = expansion;
                } else {
                    this.expansion = insertSpill(spill, originalInstruction);
                    this.expanded = true;
                }
            }
        }
    }

    private List<VInstr> insertSpill(Spill spill, VInstr instruction) {
        if (instruction.sourcePos.line == -69) return Collections.singletonList(instruction);
        List<VInstr> instructions = new LinkedList<>();
        DUVisitor duv = new DUVisitor();
        int usage = instruction.accept(spill.variable, duv);
        if (usage == 2 || usage == 3) {
            instructions.add(spill.restore());
        }
        instructions.add(instruction);
        if (usage == 1 || usage == 3) {
            instructions.add(spill.backup());
        }
        return instructions;
    }

    private List<VInstr> insertBackup(Spill spill, VInstr instruction) {
        if (instruction.sourcePos.line == -69) return Collections.singletonList(instruction);
        List<VInstr> instructions = new LinkedList<>();
        DUVisitor duv = new DUVisitor();
        if (instruction.accept(spill.variable, duv) % 2 == 1) { //0: no def/use 1: def 2: use 3: def/use  therefore -> n%2 == 1 variable was defined
            instructions.add(instruction);
            instructions.add(spill.backup());
        } else {
            instructions.add(instruction);
        }
        return instructions;
    }

    public void swap() {
        if (number == -1) return;
        Map<String, Register> combinedMap = new HashMap<>();
        combinedMap.putAll(spillMap);
        combinedMap.putAll(record.registerMap);
        SwapVisitor sv = new SwapVisitor();
        for (String variable : variables) {
            RTPair p = new RTPair(variable, combinedMap.get(variable));
            if (expanded) {
                List<VInstr> swapped = new LinkedList<>();
                for (VInstr instruction : expansion) {
                    swapped.add(instruction.accept(p, sv));
                }
                expansion = swapped;
            } else {
                List<VInstr> swapped = new ArrayList<>();
                swapped.add(originalInstruction.accept(p, sv));
                expansion = swapped;
                expanded = true;
            }
        }
    }

    private void insertCalleeBackups() {
        for (int i = 0; i < 8; ++i) {
            lines.add(String.format("local[%d] = $s%d", i, i));
        }
    }

    private void insertCalleeRestores() {
        for (int i = 0; i < 8; ++i) {
            lines.add(String.format("$s%d = local[%d]", i, i));
        }
    }

    private void insertCallerBackups() {
        for (int i = 0; i < 8; ++i) {
            lines.add(String.format("local[%d] = $t%d", i + 8, i));
        }
    }

    private void insertCallerRestores() {
        for (int i = 0; i < 8; ++i) {
            lines.add(String.format("$t%d = local[%d]", i, i + 8));
        }
    }

    private void createSignature() {
        int in = originalFunction.params.length;
        int out = computeOut();
        int local = computeLocal();
        lines.add(String.format("func %s [in %d, out %d, local %d]", originalFunction.ident, in, out, local));
    }

    private int computeLocal() {
        Set<String> spilledLocals = new HashSet<>(function.spilledVariables);
        function.parameters.forEach(spilledLocals::remove);
        return spilledLocals.size() + 16;
    }

    private int computeOut() {
        int out = 0;
        for (VInstr call : originalFunction.body) {
            if (call instanceof VCall) out = Math.max(out, ((VCall) call).args.length);
        }
        return out;
    }

    public void consumeRecord() {
        for (String variable : variables) {
            if (record.registerMap.containsKey(variable)) mappedVariables.add(variable);
            else spilledVariables.add(variable);
        }
    }

    public String printDebug() {
        PrintVisitor pv = new PrintVisitor();
        StringBuilder db = new StringBuilder("\033[0m\n---------------------------------------------------------------------------------\n");
        db.append(String.format("\n\033[0m- Node \033[;36m%d\033[0m -\n", number));
        if (number == -1) {
            db = new StringBuilder(String.format("\n\033[0m********************* \033[;35mFunction: %s \033[0m*********************\n\n", function.function.ident));
            db.append("Intervals: \n\n");
            for (var entry : function.intervals.entrySet()) {
                int start;
                if (entry.getValue().start == -1) start = originalFunction.sourcePos.line;
                else start = function.blocks.get(entry.getValue().start + 1).originalInstruction.sourcePos.line;
                int end;
                if (entry.getValue().end == -1) {
                    end = originalFunction.sourcePos.line;
                } else if (entry.getValue().end != function.blocks.size() - 1)
                    end = function.blocks.get(entry.getValue().end + 1).originalInstruction.sourcePos.line;
                else end = function.blocks.get(function.blocks.size() - 1).originalInstruction.sourcePos.line;
                db.append(String.format("\033[;34m%-8s\033[0m -> \033[;32m%3d\033[0m - \033[;32m%-3d\033[0m |  %d-%d\n", entry.getValue().variable, entry.getValue().start, entry.getValue().end, start, end));
            }
            db.append("\n\nSpills:\n\n");
            for (Spill spill : function.spills) {
                db.append(String.format("node \033[;34m%d\033[0m -> \033[;31m%s\033[0m\n", spill.spillPoint, spill.variable));
            }
            int len = originalFunction.ident.length();
            db.append(String.format("\n\n\033[0m********************************%s**********************\n", "*".repeat(len)));
            db.append(String.format("\n\033[0m- Node \033[;36m%d\033[0m -\n", number));
        }
        int z;
        if (number != -1) {
            db.append(String.format("\nOriginal Instruction: \033[;32m%s\033[0m\n\n", originalInstruction.accept(pv)));
        } else {
            StringBuilder signature = new StringBuilder("func " + originalFunction.ident + "(");
            z = 0;
            for (VVarRef.Local param : originalFunction.params) {
                if (z != originalFunction.params.length - 1) signature.append(param.toString()).append(' ');
                else signature.append(param.toString()).append(')');
                ++z;
            }
            if (originalFunction.params.length == 0)
                signature.append(')');
            db.append(String.format("\nOriginal Signature: \033[;32m%s\033[0m\n\n", signature));
        }
        db.append("\033[;34m").append(this).append("\033[0m");
        db.append("\nNode Predecessors: \n");
        int formatSize = 10;
        for (Node p : predecessors) {
            if (p.number != -1) formatSize = Math.max(formatSize, p.originalInstruction.accept(pv).length());
        }
        for (Node predecessor : predecessors) {
            if (predecessor.number != -1) {
                db.append(String.format("Node: \033[;34m%d\033[0m | Instruction: \033[;34m%-" + formatSize + "s\033[0m | Line: \033[;34m%d\033[0m\n", predecessor.number, predecessor.originalInstruction.accept(pv), predecessor.line));
            } else {
                StringBuilder signature = new StringBuilder("func " + originalFunction.ident + "(");
                z = 0;
                for (VVarRef.Local param : originalFunction.params) {
                    if (z != originalFunction.params.length - 1) signature.append(param.toString()).append(' ');
                    else signature.append(param.toString()).append(')');
                    ++z;
                }
                if (originalFunction.params.length == 0)
                    signature.append(')');
                db.append(String.format("Node: \033[;34m%d\033[0m | Function: \033[;32m%s\033[0m | Line: \033[;34m%d\033[0m\n", -1, signature, predecessor.line));
            }
        }
        db.append("\nNode Successors: \n");
        formatSize = 10;
        for (Node s : successors) {
            if (s.number != -1) formatSize = Math.max(formatSize, s.originalInstruction.accept(pv).length());
        }
        for (Node successor : successors) {
            if (successor.number != -1) {
                db.append(String.format("Node: \033[;34m%d\033[0m | Instruction: \033[;32m%-" + formatSize + "s\033[0m | Line: \033[;32m%d\033[0m\n", successor.number, successor.originalInstruction.accept(pv), successor.line));
            } else {
                db.append(String.format("Node: \033[;34m%d\033[0m | Line: \033[;34m%d\033[0m\n", -1, successor.line));
            }
        }
        db.append("\nLive In: [");
        if (liveIn.isEmpty()) db.append(']');
        z = 0;
        for (String liveIn : liveIn) {
            if (z != this.liveIn.size() - 1) {
                db.append(String.format("\033[;34m%s\033[0m ", liveIn));
            } else {
                db.append(String.format("\033[;34m%s\033[0m]", liveIn));
            }
            ++z;
        }
        db.append("\nLive Out: [");
        if (liveOut.isEmpty()) db.append("]\n\n");
        z = 0;
        for (String liveOut : liveOut) {
            if (z != this.liveOut.size() - 1) {
                db.append(String.format("\033[;34m%s\033[0m ", liveOut));
            } else {
                db.append(String.format("\033[;34m%s\033[0m]\n\n", liveOut));
            }
            ++z;
        }
        if (!variables.isEmpty()) {
            db.append("Variables: [\033[;33m");
            int i = 0;
            for (String used : variables) {
                if (i != variables.size() - 1) db.append(String.format("%s ", used));
                else db.append(String.format("%s\033[0m", used));
                ++i;
            }
            db.append("\033[0m]\n");
            if (!usedVariables.isEmpty()) {
                db.append("Variables Used: [\033[;34m");
                i = 0;
                for (String used : usedVariables) {
                    if (i != usedVariables.size() - 1) db.append(String.format("%s ", used));
                    else db.append(String.format("%s\033[0m", used));
                    ++i;
                }
                db.append("\033[0m]\n");
            }
            if (definedVariable != null)
                db.append(String.format("Variable Defined -> \033[;32m%s\033[0m\n\n", definedVariable));
            else db.append("Variable Defined -> \033[;31mnull\033[0m\n\n");
            int k;
            db.append("Spilled Variables: [\033[;031m");
            k = 0;
            for (String variable : spilledVariables) {
                if (k != spilledVariables.size() - 1) db.append(String.format("%s ", variable));
                else db.append(String.format("%s", variable));
                ++k;
            }
            db.append("\033[0m]\n");
            if (number != -1) {
                db.append("Spill Map:\n\n");
                for (var entry : spillMap.entrySet()) {
                    db.append(String.format("\033[;34m%s\033[0m -> \033[;31m%s\033[0m\n", entry.getKey(), entry.getValue().toString()));
                }
                db.append("\033[0m\n");
            }
            db.append("Mapped Variables: [\033[;034m");
            k = 0;
            for (String variable : mappedVariables) {
                if (k != mappedVariables.size() - 1) db.append(String.format("%s ", variable));
                else db.append(String.format("%s", variable));
                ++k;
            }
            db.append("\033[0m]\n");
        }
        db.append("Register Map:\n\n");
        for (var entry : record.registerMap.entrySet()) {
            db.append(String.format("\033[;34m%s\033[0m -> \033[31m%s\033[0m\n", entry.getKey(), entry.getValue()));
        }
        db.append("\nFree Registers: [\033[;32m");
        int j = 0;
        for (Register r : record.free) {
            if (j != record.free.size() - 1) db.append(String.format("%s ", r.toString()));
            else db.append(String.format("%s", r.toString()));
            ++j;
        }
        db.append("\033[0m]\n\n");
        String message = "\nVariables Spilled in Node: ";
        boolean spilled = false;
        for (Spill spill : function.spills) {
            if (spill.spillPoint == number) {
                db.append(String.format("%s\033[;31m%s\033[0m ", message, spill.variable));
                spilled = true;
                message = "";
            }
        }
        if (spilled) db.append('\n');
        if (mappedVariables.size() + spilledVariables.size() != variables.size()) {
            db.append("\n\n\n\n\n\n\n\n\n\033[;31m*****ERROR*****\n\n\n\n\n\n\n\n\n");
            throw new RuntimeException("variables != spilled + mapped");
        }
        if (number == function.blocks.get(function.blocks.size() - 1).number)
            return db.append("\033[;34m\n\n").toString();
        else return db.append("\033[;34m").toString();
    }

    public void createSpillMap() {
        if (number != -1) if (originalInstruction instanceof VCall || originalInstruction instanceof VReturn) {
            Map<String, Register> microMap = new HashMap<>();
            for (String spilledVariable : spilledVariables) {
                microMap.put(spilledVariable, Register.$t8);
            }
            spillMap = microMap;
        } else {
            Map<String, Register> miniMap = new HashMap<>();
            for (String spilledVariable : spilledVariables) {
                miniMap.put(spilledVariable, availableRegisters.remove(0));
            }
            spillMap = miniMap;
        }
    }
}
