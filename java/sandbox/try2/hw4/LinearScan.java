//package sandbox.try2.hw4;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class LinearScan {
//    Graph CFG;
//    Active active;
//    List<Variable.Interval> liveIntervals;
//    Registers registers;
//    List<Spill> spills;
//
//    int time, R = 16;
//
//    public void linearScan(Graph CFG) {
//        spills = new ArrayList<>();
//        registers = new Registers();
//        liveIntervals = new ArrayList<>();
//        active = new Active();
//        this.CFG = CFG;
//        sortLiveIntervals(CFG);
//        for (time = 0; time < CFG.nodes.size(); time++) {
//            for (Variable.Interval i : liveIntervals)
//                if (i.start == time) {
//                    expireOldIntervals(i.start);
//                    if (active.length == R) {
//                        spillAtInterval(i);
//                    } else {
//                        registers.add(i);
//                        active.insert(i);
//                    }
//                } else {
////                    expireOldIntervals(time);
//                }
//            CFG.nodes.get(time).record = registers.record();
////            CFG.nodes.get(time).record.
//        }
//        CFG.spills = spills;
//        Spill.reset();
//    }
//
//
//    void expireOldIntervals(int time) {
//        for (int index = 0; index < active.length; index++) {
//            Variable.Interval j = active.get(index);
//            if (j != null) {
//                if (j.end >= time) return;
//                active.expire(index);
//                registers.remove(j);
//            }
//        }
//    }
//
//    void spillAtInterval(Variable.Interval i) {
//        Variable.Interval spill = active.get(active.length - 1);
//
//        if (spill.end > i.end) {
//            spill.variable.spilled = time;
//            registers.remove(spill);
//            registers.add(i);
//            Spill spillPoint = new Spill(spill, CFG.addNode(time), spill.variable.lastUse);
//            spills.add(spillPoint);
//            active.expire(spill);
//            active.insert(i);
//        } else {
//            Spill spillPoint = new Spill(i, CFG.addNode(time), -1);
//            spills.add(spillPoint);
//        }
//    }
//
//    void sortLiveIntervals(Graph CFG) {
//        for (Variable.Interval interval : CFG.intervals) {
//            int i;
//            for (i = 0; i < liveIntervals.size(); i++)
//                if (interval.start < liveIntervals.get(i).start)
//                    break;
//            liveIntervals.add(i, interval);
//        }
//    }
//
//    static void printSpillsAndRegisterMap(Graph CFG) {
//        System.out.printf("-- %s --\n\n", CFG.original.ident);
//        for (Spill spill : CFG.spills) System.out.print(spill);
//        for (Node node : CFG.nodes) System.out.print(node);
//    }
//}
//
//
