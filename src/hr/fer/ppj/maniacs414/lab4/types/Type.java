package hr.fer.ppj.maniacs414.lab4.types;

public interface Type {
    default boolean implicitCastsInto(Type other){
        return this.equals(other);
    }
    default boolean explicitCastsInto(Type other) {
        return this.equals(other);
    }
    String getTypeName();
    default boolean isDefined() {
        return false;
    }
}
