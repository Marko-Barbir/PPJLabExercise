package hr.fer.ppj.maniacs414.lab4.types;

public class CharType implements Type {
    public boolean isConst, isDefined;

    public CharType(boolean isConst) {
        this.isConst = isConst;
    }

    public CharType(boolean isConst, boolean isDefined) {
        this.isConst = isConst;
        this.isDefined = isDefined;
    }

    public CharType() {
        isConst = false;
        isDefined = false;
    }

    @Override
    public boolean implicitCastsInto(Type other) {
        return other instanceof CharType || other instanceof IntType;
    }

    @Override
    public boolean explicitCastsInto(Type other) {
        return other instanceof CharType || other instanceof IntType;
    }

    @Override
    public String getTypeName() {
        if(isConst) {
            return "const(char)";
        } else {
            return "char";
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
        if(!(obj instanceof CharType other)) return false;
        return other.isConst == this.isConst;
    }
}
