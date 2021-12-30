package hr.fer.ppj.maniacs414.lab3.table;

import hr.fer.ppj.maniacs414.lab3.types.Type;

import java.util.HashMap;

public class VariableTable {
    HashMap<String, Type> variables;
    VariableTable parentTable;

    public VariableTable(VariableTable parentTable) {
        this.parentTable = parentTable;
        this.variables = new HashMap<>();
    }

    public boolean isAlreadyDeclared(String name) {
        return variables.containsKey(name);
    }

    public Type getType(String name) {
        if(variables.containsKey(name)) return variables.get(name);
        if(parentTable!=null) {
            return parentTable.getType(name);
        }
        return null;
    }
}
