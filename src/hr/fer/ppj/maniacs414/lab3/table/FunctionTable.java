package hr.fer.ppj.maniacs414.lab3.table;

import hr.fer.ppj.maniacs414.lab3.types.FunctionType;
import hr.fer.ppj.maniacs414.lab3.types.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionTable {
    public HashMap<String, FunctionType> functions;
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
        if(functions.containsKey(name)) return functions.get(name);
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
