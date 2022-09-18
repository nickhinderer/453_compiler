package translate.vaporm;

import java.util.*;

public class RegisterAllocation {
    static Active active;
    static Registers registers;
    static List<Interval> liveIntervals;
    static Set<Interval> deadIntervals;
    static Set<Spill> spills;
    static int time, R = 16;

    public static void linearScan(Graph CFG) {
        active = new Active();
        registers = new Registers();
        liveIntervals = new LinkedList<>();
        deadIntervals = new HashSet<>();
        spills = new HashSet<>();
        sortLiveIntervals(CFG);
        for (time = -1; time < CFG.blocks.size() - 1; time++) {
            for (Interval interval : liveIntervals) {
                if (interval.start == time) {
                    expireOldIntervals(interval.start);
                    if (active.length == R) {
                        spillAtInterval(interval);
                    } else {
                        registers.add(interval);
                        active.insert(interval);
                    }
                }
            }
            CFG.blocks.get(time + 1).record = registers.record(time);
            refreshLiveIntervals();
            CFG.blocks.get(time + 1).consumeRecord();
        }
        CFG.spills = spills;
        Spill.reset();
    }

    private static void refreshLiveIntervals() {
        liveIntervals.removeAll(deadIntervals);
    }

    static void sortLiveIntervals(Graph CFG) {
        for (var entry : CFG.intervals.entrySet()) {
            Interval interval = entry.getValue();
            int i;
            for (i = 0; i < liveIntervals.size(); i++)
                if (interval.start < liveIntervals.get(i).start)
                    break;
            liveIntervals.add(i, interval);
        }
    }

    static void expireOldIntervals(int time) {
        for (int index = 0; index < active.length; index++) {
            Interval j = active.get(index);
            if (j != null) {
                if (j.end >= time) return;
                active.expire(index);
                registers.remove(j);
                deadIntervals.add(j);
            }
        }
    }

    static void spillAtInterval(Interval i) {
        Interval spill = active.get(active.length - 1);
        if (spill.end > i.end) {
//            spill.variable.spilled = time;
            registers.remove(spill);
            registers.add(i);
            Spill spillPoint = new Spill(time, spill.variable);
            spills.add(spillPoint);
            active.expire(spill);
            deadIntervals.add(spill);
            active.insert(i);
        } else {
            Spill spillPoint = new Spill(time, i.variable);
            deadIntervals.add(i);
            spills.add(spillPoint);
        }
    }

    public static void spillEverywhere(Graph CFG) {
        registers = new Registers();
        for (var entry : CFG.intervals.entrySet()) {
            CFG.spills.add(new Spill(-1, entry.getKey()));
        }
        for (int i = 0; i < CFG.blocks.size(); ++i) {
            CFG.blocks.get(i).record = registers.record(i - 1);
            CFG.blocks.get(i).consumeRecord();
        }
    }
}
