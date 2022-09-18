package translate.vaporm;

import cs132.util.ProblemException;
import cs132.vapor.ast.*;
import cs132.vapor.parser.VaporParser;

import java.io.*;
import java.util.*;

public class V2VM {
    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
        VBuiltIn.Op[] ops = {VBuiltIn.Op.Add, VBuiltIn.Op.Sub, VBuiltIn.Op.MulS, VBuiltIn.Op.Eq, VBuiltIn.Op.Lt, VBuiltIn.Op.LtS, VBuiltIn.Op.PrintIntS, VBuiltIn.Op.HeapAllocZ, VBuiltIn.Op.Error,};
        boolean allowLocals = true;
        String[] registers = {};
        boolean allowStack = false;
        VaporProgram program;
        try {
            program = VaporParser.run(new InputStreamReader(in), 1, 1, java.util.Arrays.asList(ops), allowLocals, registers, allowStack);
        } catch (ProblemException ex) {
            err.println(ex.getMessage());
            return null;
        }
        return program;
    }

    public static void main(String[] args) {
        InputStream in;
        VaporProgram p;
        try {
//            in = new FileInputStream("tests/translate/vapor/BubbleSort.vapor");
            in = System.in;
            p = parseVapor(in, null);
//        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        assert p != null;
        List<Graph> functions = Graph.createGraphs(p);
        functions.forEach(RegisterAllocation::linearScan);
//        functions.forEach(RegisterAllocation::spillEverywhere);
        functions.forEach(Graph::aggregateVariables);
        for (Graph function : functions) {
            function.blocks.forEach((block) -> {
                block.expand();
                block.spill();
                block.createSpillMap();
                block.swap();
                block.addLines();
//                System.out.println(block.printDebug());
//                System.out.print(block);
            });
        }
        VaporPrinter.print(p, functions);
//        VaporPrinter.file(p, functions, "target/output/out.s", false);
//        VaporPrinter.file(p, functions, "target/output/out.s", true);
//        test();
    }


    public static void test() {
        final String[] files = {
                "BinaryTree.opt.vapor",
                "LinearSearch.opt.vapor",
                "QuickSort.opt.vapor",
                "BinaryTree.vapor",
                "LinearSearch.vapor",
                "QuickSort.vapor",
                "BubbleSort.opt.vapor",
                "LinkedList.opt.vapor",
                "ShortCircuit.opt.vapor",
                "BubbleSort.vapor",
                "LinkedList.vapor",
                "ShortCircuit.vapor",
                "Factorial.opt.vapor",
                "MoreThan4.opt.vapor",
                "TreeVisitor.opt.vapor",
                "Factorial.vapor",
                "MoreThan4.vapor",
                "TreeVisitor.vapor"
        };

        for (String file : files) {
            VaporProgram p;
            InputStream in;
            try {
                String dir = "tests/translate/vapor/";
                in = new FileInputStream(dir + file);
//            in = System.in;
                p = parseVapor(in, null);
//        } catch (FileNotFoundException e) {
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
            assert p != null;
            List<Graph> functions = Graph.createGraphs(p);
            functions.forEach(RegisterAllocation::linearScan);
//        functions.forEach(RegisterAllocation::spillEverywhere);
            functions.forEach(Graph::aggregateVariables);
            for (Graph function : functions) {
                function.blocks.forEach((block) -> {
                    block.expand();
                    block.spill();
                    block.createSpillMap();
                    block.swap();
                    block.addLines();
//                System.out.println(block.printDebug());
//                System.out.print(block);
                });
            }
            VaporPrinter.file(p, functions, "target/output/" + file + 'm', false);
        }
    }
}
