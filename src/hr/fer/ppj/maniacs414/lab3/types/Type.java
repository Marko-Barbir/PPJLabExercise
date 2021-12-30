package hr.fer.ppj.maniacs414.lab3.types;

public interface Type {
    default boolean implicitCastsInto(Type other){
        return false;
    }
    default boolean explicitCastsInto(Type other) {
        return false;
    }
    String getTypeName();
}
