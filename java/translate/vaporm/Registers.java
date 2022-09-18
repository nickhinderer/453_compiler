package translate.vaporm;


import java.util.*;

public class Registers {
    HashMap<String, Register> registerMap;
    List<Register> free;
    List<Register> used;

    Registers() {
        registerMap = new HashMap<>();
        free = new ArrayList<>(Arrays.asList(Register.$s0, Register.$s1, Register.$s2, Register.$s3, Register.$s4, Register.$s5, Register.$s6, Register.$s7, Register.$t0, Register.$t1, Register.$t2, Register.$t3, Register.$t4, Register.$t5, Register.$t6, Register.$t7));
        used = new ArrayList<>();
    }

    void add(Interval i) {
        Register r = free.remove(0);
        used.add(r);
        registerMap.put(i.variable, r);
    }

    void remove(Interval i) {
        Register r = registerMap.remove(i.variable);
        free.add(r);
        used.remove(r);
    }

    Record record(int time) {
        Record record = new Record();
        record.node = time;
        record.registerMap.putAll(registerMap);
        record.free.addAll(free);
        return record;
    }
}
