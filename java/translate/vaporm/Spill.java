package translate.vaporm;

import cs132.util.SourcePos;
import cs132.vapor.ast.VMemRead;
import cs132.vapor.ast.VMemRef;
import cs132.vapor.ast.VMemWrite;
import cs132.vapor.ast.VVarRef;

public class Spill {
    public int spillPoint;
    public String variable;
    //    private static int spills = 0;
    public VMemRef.Stack.Region region;
    public int index;

    public Spill(int spillPoint, String variable) {
        this.spillPoint = spillPoint;
        this.variable = variable;
    }

    public static void reset() {
//        spills = 0;
    }

    public VMemRead restore() {
        SourcePos pos = new SourcePos(-69, -69);
        VVarRef.Local temporary = new VVarRef.Local(pos, variable, -1);
        VMemRef memory;
        if (region == VMemRef.Stack.Region.Local)
            memory = new VMemRef.Stack(pos, region, index + 16);
        else
            memory = new VMemRef.Stack(pos, region, index);
        return new VMemRead(pos, temporary, memory);
    }

    public VMemWrite backup() {
        VMemWrite backup;
        SourcePos pos = new SourcePos(-69, -69);
        VVarRef.Local temporary = new VVarRef.Local(pos, variable, -1);
        VMemRef memory;
        if (region == VMemRef.Stack.Region.Local)
            memory = new VMemRef.Stack(pos, region, index + 16);
        else
            memory = new VMemRef.Stack(pos, region, index);
        backup = new VMemWrite(pos, memory, temporary);
        return backup;
    }
}
