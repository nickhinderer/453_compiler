package translate.vaporm;

import java.util.*;

public class Record {
    public int node;
    public Map<String, Register> registerMap;
    public Set<Register> freeRegisters;
    public List<Register> free;

    public Record() {
        this.registerMap = new HashMap<>();
        this.freeRegisters = new HashSet<>();
        this.free = new LinkedList<>();

    }
}
