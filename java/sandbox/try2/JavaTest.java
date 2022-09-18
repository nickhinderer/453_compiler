package sandbox.try2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

class C {
    int a = 1;
}

class D extends C {
    boolean a = false;
}

public class JavaTest {
    static int c = 1;

    C JavaTest() {

        return new D();
    }

    static boolean and(boolean b1, boolean b2) {
        if (b1 && b2) {
            return true;
        } else {
            return false;
        }
    }

    static boolean ifand(boolean b1, boolean b2) {
        if (b1) {
            if (b2) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        int c = 2;
        int JavaTest = 1;
        System.out.println(c);
        System.out.println(JavaTest);
        System.out.println(new D().a);
        String arrayAlloc = "func AllocArray(size)\n\tbytes = MulS(size 4)\n\tbytes = Add(bytes 4)\n\tv = HeapAllocZ(bytes)\n\t[v] = size\n\tret v";
        {
            int a;
//         int c;
//      int c;
        }
        System.out.println(arrayAlloc);
        String pe1 = "t0 = [this+8]";
        pe1 = "a ";
        String[] some = pe1.split(" ", 2);
        System.out.println(Arrays.toString(some));
        System.out.println(some[1].length());
        System.out.println(some[1].isEmpty());
        System.out.println(some[1].isBlank());

        boolean b1 = true, b2 = true;
        Random r = new Random(42);
        for (int i = 0; i < 100; i++) {
            int s = r.nextInt();
            b1 = s % 2 != 0;
            s = r.nextInt();
            b2 = s % 2 != 0;
            if (and(b1, b2) != ifand(b1, b2))
                System.out.println("\n\n\n\n");
        }
        ArrayList a = new ArrayList<>(Arrays.asList(1, 2, 3, 5));
//        a.add(5, 4);
//        System.out.println(a.toString());

        HashMap<Integer, Character> m1 = new HashMap();

        m1.put(1, 'a');
        HashMap<Integer, Character> m2 = new HashMap(m1);
        m1.put(2, 'b');
        m2.put(3,'c');
    }


}






















