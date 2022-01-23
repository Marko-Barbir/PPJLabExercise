package table;

import types.FunctionType;
import types.VoidType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionTable {
    public static class FunctionEntry {
        public FunctionType type;
        public int stackSize;
        public List<String> generatedCode;

        public FunctionEntry(FunctionType type) {
            this.type = type;
            this.stackSize = type.paramTypes.contains(new VoidType()) ? 1 : type.paramTypes.size()+1;
            this.generatedCode = new ArrayList<>();
        }
    }

    public HashMap<String, FunctionEntry> functions;
    public FunctionTable parentTable;
    public List<FunctionTable> children;

    public FunctionTable(FunctionTable parentTable) {
        this.parentTable = parentTable;
        this.functions = new HashMap<>();
        this.children = new ArrayList<>();
        if(parentTable != null){
            parentTable.children.add(this);
        }
    }

    public boolean isAlreadyDeclared(String name) {
        return functions.containsKey(name);
    }

    public FunctionType getFunction(String name) {
        if(functions.containsKey(name)) return functions.get(name).type;
        if(parentTable!=null) {
            return parentTable.getFunction(name);
        }
        return null;
    }

    public FunctionTable getGlobalScope(){
        FunctionTable table = this;
        while(table.parentTable != null){
            table = table.parentTable;
        }

        return table;
    }
}
