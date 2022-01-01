package hr.fer.ppj.maniacs414.lab3.table;

import hr.fer.ppj.maniacs414.lab3.types.FunctionType;
import hr.fer.ppj.maniacs414.lab3.types.Type;

import java.util.HashMap;

public class FunctionTable {
    public HashMap<String, FunctionType> functions;

    public FunctionTable() {
        functions = new HashMap<>();
    }

    public boolean isAlreadyDeclared(String name) {
        return functions.containsKey(name);
    }
}
