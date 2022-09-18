package translate.mips;

import cs132.util.ProblemException;
import cs132.vapor.ast.*;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VBuiltIn.Op;

import java.io.*;
import java.util.*;

public class VM2M {
    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
        Op[] ops = {Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS, Op.PrintIntS, Op.HeapAllocZ, Op.Error,};
        boolean allowLocals = false;
        String[] registers = {"v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8",};
        boolean allowStack = true;
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
        try {
            in = System.in;
//            in = new FileInputStream("tests/translate/vaporm/TreeVisitor.opt.vaporm");
        } catch (Exception e) {
//        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        VaporProgram p;
        try {
            p = parseVapor(in, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert p != null;
        {
            final StringBuilder dataSegment = new StringBuilder(".data\n\n");
            for (VDataSegment segment : p.dataSegments) {
                dataSegment.append(segment.ident).append(':').append('\n');
                for (VOperand.Static value : segment.values) {
                    VLabelRef label = (VLabelRef) value;
                    dataSegment.append("  ").append(label.ident).append('\n');
                }
                dataSegment.append('\n');
            }
            final String prologue = ".text\n\n  jal Main\n  li $v0 10\n  syscall\n";
            System.out.print(dataSegment);
            System.out.println(prologue);
        }
        for (VFunction F : p.functions) {
            System.out.println(F.ident + ':');
            int frameSize = 4 * (2 + F.stack.out + F.stack.local);
            String prologue = String.format("  sw $fp -8($sp)\n  move $fp $sp\n  subu $sp $sp %d\n  sw $ra -4($fp)", frameSize);
            System.out.println(prologue);
            printStatements(F);
            String epilogue = String.format("  lw $ra -4($fp)\n  lw $fp -8($fp)\n  addu $sp $sp %d\n  jr $ra", frameSize);
            System.out.println(epilogue);
            System.out.println();
        }
        {
            final String heapAlloc = "_heapAlloc:\n  li $v0 9   # syscall: sbrk\n  syscall\n  jr $ra";
            final String print = "_print:\n  li $v0 1   # syscall: print integer\n  syscall\n  la $a0 _newline\n  li $v0 4   # syscall: print string\n syscall\n jr $ra";
            final String error = "_error:\n  li $v0 4   # syscall: print string\n  syscall\n  li $v0 10  # syscall: exit\n  syscall";
            final String epilogue = ".data\n.align 0\n_newline: .asciiz \"\\n\"\n_str0: .asciiz \"null pointer\\n\"\n_str1: .asciiz \"array index out of bounds\\n\"";
            System.out.println(heapAlloc);
            System.out.println(print);
            System.out.println(error);
            System.out.println(epilogue);
        }
    }

    private static void printStatements(VFunction F) {
        MIPSVisitor v = new MIPSVisitor();
        Queue<VCodeLabel> labels = new LinkedList<>();
        Collections.addAll(labels, F.labels);
        VInstr instruction;
        int instrIndex = 0;
        for (int i = 0; i < F.body.length; i++) {
            if (!labels.isEmpty()) {
                if (labels.peek().instrIndex == instrIndex) {
                    System.out.println(labels.poll().ident + ':');
                    i--;
                    continue;
                }
            }
            instruction = F.body[i];
            String MIPS = instruction.accept(v);
            if (MIPS != null) System.out.println(MIPS);
            instrIndex++;
        }
    }
}










































































































































































