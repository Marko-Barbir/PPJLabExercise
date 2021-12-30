package hr.fer.ppj.maniacs414.lab3.types;

public class ArrayType implements Type {
    public Type elementType;
    public ArrayType (Type elementType) {
        this.elementType = elementType;
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
}
