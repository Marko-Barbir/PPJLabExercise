package hr.fer.ppj.maniacs414.lab3.types;

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
        return (elementType instanceof CharType && !((CharType) elementType).isConst) ||
                elementType instanceof IntType && !((IntType) elementType).isConst;
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
        if(!(obj instanceof ArrayType other)) return false;
        return elementType.equals(other.elementType);
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
