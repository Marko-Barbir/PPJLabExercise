package hr.fer.ppj.maniacs414.lab3.types;

public class IntType implements Type {
    public boolean isConst;

    public IntType(boolean isConst) {
        this.isConst = isConst;
    }

    public IntType() {
        isConst = false;
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
}
