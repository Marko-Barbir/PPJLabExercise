package hr.fer.ppj.maniacs414.lab3.types;

public interface Type {
    boolean implicitCastsInto(Type other);
    boolean explicitCastsInto(Type other);
}
