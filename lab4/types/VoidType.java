package types;

public class VoidType implements Type {
    @Override
    public String getTypeName() {
        return "void";
    }

    @Override
    public String toString() {
        return getTypeName();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VoidType;
    }


}
