package table;

import types.Type;

import java.util.HashMap;

public class VariableTable {
    public static class VariableEntry {
        public Type type;
        public int stackIndex;

        public VariableEntry(Type type, int stackIndex) {
            this.type = type;
            this.stackIndex = stackIndex;
        }
    }
    public VariableTable parentTable;

    public HashMap<String, VariableEntry> variables;

    public VariableTable(VariableTable parentTable) {
        this.parentTable = parentTable;
        this.variables = new HashMap<>();
    }

    public boolean isAlreadyDeclared(String name) {
        return variables.containsKey(name);
    }

    public Type getType(String name) {
        if(variables.containsKey(name)) return variables.get(name).type;
        if(parentTable!=null) {
            return parentTable.getType(name);
        }
        return null;
    }

    public VariableTable getGlobalScope(){
        VariableTable table = this;
        while(table.parentTable != null){
            table = table.parentTable;
        }

        return table;
    }
}
