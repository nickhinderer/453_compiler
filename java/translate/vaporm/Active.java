package translate.vaporm;

import java.util.ArrayList;
import java.util.Arrays;

public class Active {
    Interval[] active;
    int length;

    Active() {
        active = new Interval[1];
        length = 0;
    }

    public void expire(Interval interval) {
        int index = 0;
        for (Interval i : active) {
            if (interval.variable.equals(i.variable)) break;
            index++;
        }
        expire(index);
    }

    public void expire(int index) {
        active[index] = null;
        ArrayList<Interval> unexpired = new ArrayList<>();
        for (Interval j : active) {
            if (j != null) {
                unexpired.add(j);
            }
        }
        active = new Interval[Math.max(1, unexpired.size())];
        active = unexpired.toArray(active);
        length = active.length;
    }

    void insert(Interval interval) {
        if (length == 0 || active[0] == null) {
            active[0] = interval;
            length = 1;
            return;
        }
        active = Arrays.copyOf(active, active.length + 1);
        int end = interval.end, index;
        for (index = 0; index < length; index++)
            if (end < active[index].end)
                break;
        length++;
        if (length == 2) {
            if (index == 1) {
                active[index] = interval;
            } else {
                active[1] = active[0];
                active[0] = interval;
            }
            return;
        }
        System.arraycopy(active, index, active, index + 1, active.length - index - 1);
        active[index] = interval;
    }

    public Interval get(int index) {
        return active[index];
    }
}
