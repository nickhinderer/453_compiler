//package sandbox.try2.hw4;
//
//import cs132.vapor.ast.VFunction;
//import cs132.vapor.ast.VInstr;
//import cs132.vapor.ast.VMemRef;
//
//import java.util.*;
//
//public class Graph {
//    List<Node> nodes;
//    Set<Variable> variables;
//    List<Variable.Interval> intervals;
//    public VFunction original;
//    List<Spill> spills;
//    Set<Variable> spilledVariables;
//
//    public Graph() {
//        nodes = new ArrayList<>();
//        variables = new HashSet<>();
//        intervals = new ArrayList<>();
//        spills = new ArrayList<>();
//        spilledVariables = new HashSet<>();
//    }
//
//    void variable(Variable v) {
//        variables.add(v);
//        intervals.add(v.interval);
//    }
//
//    public void addNode(Node n) {
//        nodes.add(n);
//    }
//
//    Node addNode(int time) {
//        for (Node node : nodes) {
//            if (node.num == time) return node;
//        }
//        return null;
//    }
//
//    void collectVariables() {
//        for (Node n : nodes) {
//            this.variables.addAll(n.variables);
//        }
//    }
//
//    void createIntervals() {
//        for (Variable v : variables) {
//            Variable.Interval i = v.interval(true);
//            intervals.add(i);
//        }
//    }
//}
//
//class Active {
//    Variable.Interval[] active;
//    int length;
//
//    Active() {
//        active = new Variable.Interval[1];
//        length = 0;
//    }
//
//    public void expire(Variable.Interval interval) {
//        int index = 0;
//        for (Variable.Interval i : active) {
//            if (interval.variable.name.equals(i.variable.name)) break;
//            index++;
//        }
//        expire(index);
//    }
//
//    public void expire(int index) {
//        active[index] = null;
//        ArrayList<Variable.Interval> unexpired = new ArrayList<>();
//        for (Variable.Interval j : active) {
//            if (j != null) {
//                unexpired.add(j);
//            }
//        }
//        active = new Variable.Interval[Math.max(1, unexpired.size())];
//        active = unexpired.toArray(active);
//        length = active.length;
//    }
//
//    void insert(Variable.Interval interval) {
//        if (length == 0 || active[0] == null) {
//            active[0] = interval;
//            length = 1;
//            return;
//        }
//        active = Arrays.copyOf(active, active.length + 1);
//        int end = interval.end, index;
//        for (index = 0; index < length; index++)
//            if (end < active[index].end)
//                break;
//        length++;
//        if (length == 2) {
//            if (index == 1) {
//                active[index] = interval;
//            } else {
//                active[1] = active[0];
//                active[0] = interval;
//            }
//            return;
//        }
//        System.arraycopy(active, index, active, index + 1, active.length - index - 1);
//        active[index] = interval;
//    }
//
//    public Variable.Interval get(int index) {
//        return active[index];
//    }
//}
//
//class Registers {
//    HashMap<Variable, Register> registerMap;
//    List<Register> free;
//    List<Register> used;
//
//    Registers() {
//        registerMap = new HashMap<>();
//        free = new ArrayList<>(Arrays.asList(Register.$s0, Register.$s1, Register.$s2, Register.$s3, Register.$s4, Register.$s5, Register.$s6, Register.$s7, Register.$t0, Register.$t1, Register.$t2, Register.$t3, Register.$t4, Register.$t5, Register.$t6, Register.$t7));
//        used = new ArrayList<>();
//    }
//
//    void add(Variable.Interval i) {
//        Register r = free.remove(0);
//        used.add(r);
//        registerMap.put(i.variable, r);
//    }
//
//    void remove(Variable.Interval i) {
//        Register r = registerMap.remove(i.variable);
//        free.add(r);
//        used.remove(r);
//    }
//
//    Record record() {
//        Record record = new Record();
//        registerMap.forEach((variable, register) -> {
//            record.registers.put(String.valueOf(variable.name), register);
//        });
//        record.free.addAll(free);
//        return record;
//    }
//}
//
//class Record {
//    Map<String, Register> registers;
//    List<Register> free;
//    Spill spill;
//
//    Record() {
//        registers = new HashMap<>();
//        free = new LinkedList<>();
//        spill = null;
//    }
//}
//
//class Spill {
//    Node backupPoint;
//    Variable variable;
//    static int spills = 0;
//    int location;
//    VMemRef.Stack.Region region;
//    int use;
//
//    public Spill(Variable.Interval i, Node backupPoint, int lastUse) {
//        this.backupPoint = backupPoint;
//        this.variable = i.variable;
//        this.location = spills++;
//        this.use = lastUse;
//    }
//
//    public static void reset() {
//        spills = 0;
//    }
//
//    @Override
//    public String toString() {
//        return String.format("Spilled Variable: \033[;34m%s\033[0m at node/instruction \033[;31m%d\033[0m at local[\033[;32m%d\033[0m]\n\n", this.variable.name, this.backupPoint.num, this.location);
//    }
//}
//
//class Node {
//    int num;
//    Variable def;
//    public List<Variable> params;
//    List<Variable> use;
//    List<Variable> variables;
//    Record record;
//    VInstr instruction;
//    boolean expanded;
//    List<VInstr> expansion;
//    boolean used$v1;
//    Set<Variable> spilled;
//
//    public Node(int num) {
//        this.num = num;
//        this.use = new ArrayList<>();
//        this.variables = new ArrayList<>();
//        this.spilled = new HashSet<>();
//        expanded = false;
//        used$v1 = false;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder output = new StringBuilder(String.format("node %d\n", this.num));
//        this.record.registers.forEach((k, v) -> {
//            if (v != null) output.append(String.format("Variable: \033[;34m%s\033[0m -> \033[;31m%s\033[0m\n", k, v));
//        });
//        output.append("Free Registers: [");
//        for (int i = 0; i < this.record.free.size(); i++) {
//            Register r = this.record.free.get(i);
//            if (i == this.record.free.size() - 1) output.append(String.format("\033[;32m%s\033[0m", r));
//            else output.append(String.format("\033[;32m%s\033[0m, ", r));
//        }
//        output.append("]\n\n");
//        return output.toString();
//    }
//
//    public void addParameter(Variable variable) {
//        this.params.add(variable);
//        this.variables.add(variable);
//    }
//
//    void addUse(Variable variable) {
//        this.use.add(variable);
//        this.variables.add(variable);
//    }
//
//    void addDef(Variable variable) {
//        this.def = variable;
//        this.variables.add(variable);
//    }
//}
//
//class Variable {
//    String name;
//    Interval interval;
//    int firstDef;
//    int lastUse;
//    int spilled;
//    Set<Node> used;
//    Set<Node> defined;
//    static Dictionary<String, Variable> dict = new Hashtable<>();
//
//    public static Variable variable(String id) {
//        String u = id.intern();
//        Variable variable = dict.get(u);
//        if (variable == null) {
//            variable = new Variable(u);
//            dict.put(u, variable);
//        }
//        return variable;
//    }
//
//    public static void reset() {
//        dict = new Hashtable<>();
//    }
//
//    private Variable(String name) {
//        this.name = name;
//        firstDef = -1;
//        lastUse = -1;
//    }
//
//    static class Interval {
//        Variable variable;
//        int start;
//        int end;
//
//        Interval(Variable variable) {
//            this.variable = variable;
//            start = -1;
//            end = -1;
//        }
//
//        Interval(Variable variable, int start) {
//            this.variable = variable;
//            this.start = start;
//            this.end = -1;
//        }
//
//        public Interval(Variable variable, int firstDef, int lastUse) {
//            this.variable = variable;
//            this.start = firstDef;
//            this.end = lastUse;
//        }
//    }
//
//    void interval(int start, int end) {
//        this.interval = new Interval(this, start, end);
//        interval.start = start;
//        interval.end = end;
//    }
//
//    void interval(int start) {
//        this.interval = new Interval(this, start);
//    }
//
//    void interval() {
//        this.interval = new Interval(this, firstDef, lastUse);
//    }
//
//    Interval interval(boolean differentiate) {
//        this.interval = new Interval(this, firstDef, lastUse);
//        return interval;
//    }
//}
