package sandbox.try2;

class WhileTest {
    public static void main(String[] args) {
        int i;
        WT w;
        w = new WT();
        i = w.method();
    }
}

class WT {
    public int method() {
        int i;
        i = 0;
        while (i < 100) {
            System.out.println(i);
            i = i + 1;
        }
        return 0;
    }
}
