package translate.vaporm;

import cs132.vapor.ast.*;

import java.util.*;

public class Graph {
    public VFunction function;
    public List<Node> blocks;
    public Set<Spill> spills;
    public Set<String> variables;
    public Set<String> locals;
    public List<String> parameters;
    public Set<String> spilledVariables;
    public Map<String, Interval> intervals;

    public Graph(VFunction function) {
        this.function = function;
        this.blocks = new ArrayList<>();
        this.spills = new HashSet<>();
        this.variables = new HashSet<>();
        this.locals = new HashSet<>();
        this.parameters = new ArrayList<>();
        this.spilledVariables = new HashSet<>();
        this.intervals = new HashMap<>();
    }

    public void aggregateVariables() {
        for (VVarRef.Local local : function.params) {
            variables.add(local.ident);
            parameters.add(local.ident);
        }
        for (String variable : function.vars) {
            variables.add(variable);
            if (!parameters.contains(variable))
                locals.add(variable);
        }
        for (Spill spill : spills) {
            spilledVariables.add(spill.variable);
        }
        int localIndex = 0;
        for (Spill spill : spills) {
            String id = spill.variable;
            if (parameters.contains(id)) {
                spill.region = VMemRef.Stack.Region.In;
                spill.index = parameters.indexOf(id);
            } else {
                spill.region = VMemRef.Stack.Region.Local;
                spill.index = localIndex++;
            }
        }
    }

    public static List<Graph> createGraphs(VaporProgram p) {
        List<Register> availableRegisters = new ArrayList<>() {{
            add(Register.$a0);
            add(Register.$a1);
            add(Register.$a2);
            add(Register.$a3);
        }};
        List<Graph> functions = new LinkedList<>();
        for (VFunction F : p.functions) {
            Graph function = new Graph(F);
            Node parameterNode = new Node(function, F, -1);
            parameterNode.line = F.sourcePos.line;
            for (VVarRef.Local parameter : F.params) {
                parameterNode.parameters.add(parameter.ident);
                parameterNode.variables.add(parameter.ident);
            }
            function.blocks.add(parameterNode);
            Map<String, Interval> intervals = new HashMap<>();
            for (VVarRef.Local local : F.params) {
                intervals.put(local.ident, new Interval(-1, local.ident));
            }
            VariableVisitor vv = new VariableVisitor();
            DUVisitor duv = new DUVisitor();
            Node node;
            int nodeIndex = 0;
            Node predecessor = parameterNode;
            for (VInstr instruction : F.body) {
                node = new Node(function, F, nodeIndex);
                node.originalInstruction = instruction;
                node.line = instruction.sourcePos.line;
                node.availableRegisters = new ArrayList<>(availableRegisters);
                node.variables = instruction.accept(vv);
                node.parameters.addAll(parameterNode.parameters);
                node.parameters.retainAll(node.variables);
                node.locals.addAll(node.variables);
                node.locals.removeAll(node.parameters);
                Set<String> usedVariables = new HashSet<>();
                String definedVariable = null;
                for (String variable : node.variables) {
                    int du = instruction.accept(variable, duv);
                    switch (du) {
                        case 0:
                            break;
                        case 1:
                            definedVariable = variable;
                            break;
                        case 2:
                            usedVariables.add(variable);
                            break;
                        case 3:
                            definedVariable = variable;
                            usedVariables.add(variable);
                            break;
                    }
                }
                node.definedVariable = definedVariable;
                node.usedVariables.addAll(usedVariables);
                if (definedVariable != null)
                    if (!intervals.containsKey(definedVariable)) {
                        intervals.put(definedVariable, new Interval(nodeIndex, definedVariable));
                    } else {
                        intervals.get(definedVariable).end = nodeIndex;
                    }
                final int indexCopy = nodeIndex;
                node.usedVariables.forEach((variable) -> {
                    if (!intervals.containsKey(variable)) {
                        intervals.put(variable, new Interval(indexCopy, variable));
                    } else {
                        intervals.get(variable).end = indexCopy;
                    }
                });
                function.blocks.add(node);
                if (nodeIndex == 0) {
                    node.predecessors.add(predecessor);
                    predecessor.successors.add(node);
                    predecessor = node;
                    nodeIndex += 1;
                    continue;
                }
                boolean isPreviousGoTo = predecessor.originalInstruction instanceof VGoto;
                if (!isPreviousGoTo) {
                    node.predecessors.add(predecessor);
                    predecessor.successors.add(node);
                }
                predecessor = node;
                nodeIndex += 1;
            }
            function.intervals = intervals;
            functions.add(function);
        }
        for (Graph function : functions) {
            for (Node predecessor : function.blocks) {
                if (predecessor.number == -1)
                    continue;
                Node successor = null;
                VCodeLabel label = null;
                if (predecessor.originalInstruction instanceof VGoto) {
                    VGoto vGoto = (VGoto) predecessor.originalInstruction;
                    VAddr.Label<VCodeLabel> goToLabel = (VAddr.Label<VCodeLabel>) vGoto.target;
                    label = goToLabel.label.getTarget();
                } else if (predecessor.originalInstruction instanceof VBranch) {
                    VBranch vBranch = (VBranch) predecessor.originalInstruction;
                    VLabelRef<VCodeLabel> vBranchLabel = vBranch.target;
                    label = vBranchLabel.getTarget();
                }
                if (label != null) {
                    int target = label.instrIndex;
                    for (Node other : function.blocks) {
                        if (other.number == target) {
                            successor = other;
                            break;
                        }
                    }
                    assert successor != null;
                    predecessor.successors.add(successor);
                    successor.predecessors.add(predecessor);
                }
            }
        }
        for (Graph function : functions) {
            for (Node block : function.blocks) {
                block.liveIn.addAll(block.usedVariables);
                for (Node successor : block.successors) {
                    block.liveOut.addAll(successor.liveIn);
                }
                Set<String> liveOutNotDefInBlock = new HashSet<>(block.liveOut);
                liveOutNotDefInBlock.remove(block.definedVariable);
                block.liveIn.addAll(liveOutNotDefInBlock);
            }
            boolean changed = true;
            while (changed) {
                changed = false;
                for (Node block : function.blocks) {
                    for (Node successor : block.successors) {
                        if (block.liveOut.containsAll(successor.liveIn)) {
                            continue;
                        } else {
                            changed = true;
                            block.liveOut.addAll(successor.liveIn);
                        }
                    }
                    Set<String> liveOutNotDefInBlock = new HashSet<>(block.liveOut);
                    liveOutNotDefInBlock.remove(block.definedVariable);
                    if (block.liveIn.containsAll(liveOutNotDefInBlock)) {
                        continue;
                    } else {
                        block.liveIn.addAll(liveOutNotDefInBlock);
                        changed = true;
                    }
                }
            }
        }
        for (Graph function : functions) {
            int last;
            for (var entry : function.intervals.entrySet()) {
                last = entry.getValue().end;
                for (Node block : function.blocks) {
                    if (block.liveIn.contains(entry.getKey()) || block.liveOut.contains(entry.getKey()))
                        last = Math.max(last, block.number);
                }
                entry.getValue().end = last;
            }
        }
        return functions;
    }
}
