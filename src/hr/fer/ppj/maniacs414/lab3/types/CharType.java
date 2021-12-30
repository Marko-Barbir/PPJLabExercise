package hr.fer.ppj.maniacs414.lab3.types;

public class CharType implements Type {
    public boolean isConst;

    public CharType(boolean isConst) {
        this.isConst = isConst;
    }

    public CharType() {
        isConst = false;
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
}
