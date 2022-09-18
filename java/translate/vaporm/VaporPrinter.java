package translate.vaporm;

import cs132.vapor.ast.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class VaporPrinter {
    public static void print(VaporProgram p, List<Graph> functions) {
        for (VDataSegment segment : p.dataSegments) {
            System.out.println("const " + segment.ident);
            for (VOperand.Static label : segment.values) {
                System.out.println('\t' + label.toString());
            }
            System.out.print('\n');
        }
        for (Graph function : functions) {
            Deque<VCodeLabel> labels = new LinkedList<>(Arrays.asList(function.function.labels));
            int j;
            for (int i = -1; i < function.blocks.size() - 1; ++i) {
                j = i;
                Node node = function.blocks.get(i + 1);
                if (i != -1)
                    if (!labels.isEmpty()) {
                        if (labels.peek().ident.equals("while3_top"))
                            System.out.println();
                        if (labels.peek() != null)
                            while (labels.peek().instrIndex == j) {
                                System.out.println(labels.pop().ident + ':');
                                if (labels.isEmpty())
                                    break;
                            }
                    }
                System.out.print(node);
            }
            if (!labels.isEmpty())
                while (!labels.isEmpty())
                    System.out.println(labels.pop().ident + ':');
            System.out.print('\n');
        }
    }

    public static void file(VaporProgram p, List<Graph> functions, String filename, boolean debug) {
        FileWriter file, debugFile = null;
        try {
            file = new FileWriter(filename);
            if (debug)
                debugFile = new FileWriter(filename + ".debug");
            for (VDataSegment segment : p.dataSegments) {
                file.write("const " + segment.ident + '\n');
                for (VOperand.Static label : segment.values) {
                    file.write('\t' + label.toString() + '\n');
                }
                file.write('\n');
            }
            for (Graph function : functions) {
                Deque<VCodeLabel> labels = new LinkedList<>(Arrays.asList(function.function.labels));
                int j;
                for (int i = -1; i < function.blocks.size() - 1; ++i) {
                    j = i;
                    Node node = function.blocks.get(i + 1);
                    if (i != -1)
                        if (!labels.isEmpty()) {
                            if (labels.peek() != null)
                                while (labels.peek().instrIndex == j) {
                                    file.write(labels.pop().ident + ":\n");
                                    if (labels.isEmpty())
                                        break;
                                }
                        }
                    file.write(node.toString());
                    if (debug) {
                        debugFile.write(node.printDebug());
                    }
                }
                if (!labels.isEmpty())
                    while (!labels.isEmpty())
                        file.write(labels.pop().ident + ":\n");
                file.write('\n');
            }
            file.close();
            if (debugFile != null)
                debugFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
