package types;

import java.util.Objects;

public class IntType implements Type {
    public boolean isConst, isDefined;

    public IntType(boolean isConst, boolean isDefined) {
        this.isConst = isConst;
        this.isDefined = isDefined;
    }

    public IntType(boolean isConst) {
        this.isConst = isConst;
        isDefined = false;
    }

    public IntType() {
        isConst = false;
        isDefined = false;
    }

    @Override
    public boolean implicitCastsInto(Type other) {
        return other instanceof IntType;
    }

    @Override
    public boolean explicitCastsInto(Type other) {
        return other instanceof CharType || other instanceof IntType;
    }

    @Override
    public String getTypeName() {
        if(isConst) {
            return "const(int)";
        } else {
            return "int";
        }
    }

    @Override
    public String toString() {
        return getTypeName();
    }

    @Override
    public boolean isDefined() {
        return isDefined;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof IntType)) return false;
        IntType other = (IntType) obj;
        return this.isConst == other.isConst;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isConst);
    }
}
