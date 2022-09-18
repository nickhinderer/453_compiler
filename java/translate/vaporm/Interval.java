package translate.vaporm;

public class Interval {
    public int start;
    public int end;
    public String variable;

    public Interval(int nodeIndex, String variable) {
        this.start = nodeIndex;
        this.end = nodeIndex;
        this.variable = variable;
    }
}
