package types;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FunctionType implements Type {
    public boolean isDefined;
    public Type returnType;
    public List<Type> paramTypes;

    public FunctionType(Type returnType, Type... paramTypes) {
        this.returnType = returnType;
        this.paramTypes = List.of(paramTypes);
        isDefined = false;
    }

    public FunctionType(Type returnType, List<Type> paramTypes) {
        this.returnType = returnType;
        this.paramTypes = paramTypes;
        isDefined = false;
    }

    @Override
    public String getTypeName() {
        if(paramTypes.size() == 1 && paramTypes.get(0) instanceof VoidType) {
            return "funkcija(void -> " + returnType.getTypeName() + ")";
        } else {
            return "funkcija(" + paramTypes.stream().map(Type::getTypeName).collect(Collectors.toList())
                    + " -> " + returnType.getTypeName() + ")";
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
        if(!(obj instanceof FunctionType)) return false;
        FunctionType other = (FunctionType) obj;
        return this.returnType.equals(other.returnType) &&
                this.paramTypes.equals(other.paramTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnType, paramTypes);
    }
}
