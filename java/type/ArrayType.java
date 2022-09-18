package type;

import type.enums.TYPE;

public class ArrayType extends Type {
    public int length;
    public ArrayType() {
        this.type = TYPE.ARRAY;
        this.length = -1;
    }

    @Override
    public boolean equals(Type other) {
        return other.type == TYPE.ARRAY;
    }
}
