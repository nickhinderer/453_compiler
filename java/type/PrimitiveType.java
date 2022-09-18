package type;

import type.enums.TYPE;

public class PrimitiveType extends Type {
    public String subType;

    public PrimitiveType(String subType) {
        this.subType = subType;
        this.type = TYPE.PRIMITIVE;
    }

    public boolean equals(Type other) {
        if (other.type != this.type)
            return false;
        return this.subType.equals(((PrimitiveType) other).subType);
    }
}
