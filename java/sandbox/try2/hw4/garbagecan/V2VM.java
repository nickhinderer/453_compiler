package sandbox.try2.hw4.garbagecan;

import cs132.util.ProblemException;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.parser.VaporParser;
import translate.vaporm.Graph;
import translate.vaporm.RegisterAllocation;
import translate.vaporm.VaporPrinter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class V2VM {
    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
        VBuiltIn.Op[] ops = {VBuiltIn.Op.Add, VBuiltIn.Op.Sub, VBuiltIn.Op.MulS, VBuiltIn.Op.Eq, VBuiltIn.Op.Lt, VBuiltIn.Op.LtS, VBuiltIn.Op.PrintIntS, VBuiltIn.Op.HeapAllocZ, VBuiltIn.Op.Error,};
        boolean allowLocals = true;
        String[] registers = {};
        boolean allowStack = false;
        VaporProgram program;
        try {
            program = VaporParser.run(new InputStreamReader(in), 1, 1, Arrays.asList(ops), allowLocals, registers, allowStack);
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
//        functions.forEach(RegisterAllocation::linearScan);
        functions.forEach(RegisterAllocation::spillEverywhere);
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
    }


}
