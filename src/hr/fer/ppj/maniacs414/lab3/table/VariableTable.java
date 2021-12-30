package hr.fer.ppj.maniacs414.lab3.table;

import hr.fer.ppj.maniacs414.lab3.types.Type;

import java.util.Hashtable;

public class VariableTable {
    Hashtable<String, Type> variables;
    VariableTable parentTable;

    public VariableTable(VariableTable parentTable) {
        this.parentTable = parentTable;
        variables = new Hashtable<>();
    }

    public boolean isAlreadyDefined(String name) {
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
