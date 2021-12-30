package hr.fer.ppj.maniacs414.lab3.types;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionType implements Type {
    public Type returnType;
    public List<Type> paramTypes;

    public FunctionType(Type returnType, Type... paramTypes) {
        this.returnType = returnType;
        this.paramTypes = List.of(paramTypes);
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
}
