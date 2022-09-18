package sandbox.try2;

import java.util.regex.Pattern;

public class JTest {

    static String em(String input) {
//        StringBuilder sb = new StringBuilder("^\\s*");
        StringBuilder sb = new StringBuilder("\\s");
//        StringBuilder sb = new StringBuilder("\\s*");

        for (Character c : input.toCharArray()) {
            if (c == '.')
                sb.append("\\.");
            else if (c == '$')
                sb.append("\\$");
            else
                sb.append(c);
        }
//        sb.append('$');
        sb.append("\\s");

        return sb.toString();
    }

    public static double power(int x, int y) {
        int counter;
        if (y < 0)
            counter = -y;
        else
            counter = y;
        double result = 1.0;
        for (; counter >= 0; --counter) {
            result *= x;
        }
        if (y < 0)
            result = 1.0 / result;
        return result;
    }


    public static void main(String[] args) {


//        String source = "g t.0  ";
//        String match = em("t.0");
//
//        System.out.println(match);
//        match = "(^|\\s+)t\\.0($|\\s+)";
//
//        System.out.println(source.contains(match));
//        System.out.println();
//        System.out.println(source.matches(match));

//        System.out.println("INSERT INTO eid_enrollments VALUES \n");
//        for (int i = 0; i < 26; i++) {
//            System.out.printf("(8000000%d, 'CS', '370', 'FA19'),\n", i);
//        }

        System.out.println(power(3, -2));
        System.out.println(power(3, 3));
        System.out.println(power(1, 3));
        System.out.println(power(1, -2));
        System.out.println(power(3, 0));

    }
}
