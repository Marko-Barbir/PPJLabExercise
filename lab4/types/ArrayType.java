package types;

import java.util.Objects;

public class ArrayType implements Type {
    public Type elementType;
    public boolean isDefined;

    public ArrayType (Type elementType) {
        this.elementType = elementType;
        isDefined = false;
    }

    public ArrayType(Type elementType, boolean isDefined) {
        this.elementType = elementType;
        this.isDefined = isDefined;
    }

    @Override
    public boolean implicitCastsInto(Type other) {
        if(this.equals(other)) return true;
        if(!(other instanceof ArrayType)) {
            return false;
        }
        ArrayType arrayType = (ArrayType) other;
        if(elementType instanceof CharType) {
            CharType charType = (CharType) elementType;
            if(!charType.isConst) {
                if(arrayType.elementType instanceof CharType) {
                    CharType charType1 = (CharType) arrayType.elementType;
                    return charType1.isConst;
                }
            }
        } else if(elementType instanceof IntType) {
            IntType intType = (IntType) elementType;
            if(!intType.isConst) {
                if(arrayType.elementType instanceof IntType) {
                    IntType intType1 = (IntType) arrayType.elementType;
                    return intType1.isConst;
                }
            }
        }
        return false;
    }

    @Override
    public boolean explicitCastsInto(Type other) {
        return Type.super.explicitCastsInto(other);
    }

    @Override
    public String getTypeName() {
        return "niz("+elementType.getTypeName()+")";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ArrayType)) return false;
        ArrayType other = (ArrayType) obj;
        return elementType.equals(other.elementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementType);
    }

    @Override
    public String toString() {
        return getTypeName();
    }

    @Override
    public boolean isDefined() {
        return isDefined;
    }
}
