package hr.fer.ppj.maniacs414.lab4;

import hr.fer.ppj.maniacs414.lab4.parser.NonterminalNode;
import hr.fer.ppj.maniacs414.lab4.parser.Parser;
import hr.fer.ppj.maniacs414.lab4.rules.Rules;
import hr.fer.ppj.maniacs414.lab4.table.FunctionTable;
import hr.fer.ppj.maniacs414.lab4.table.VariableTable;
import hr.fer.ppj.maniacs414.lab4.types.FunctionType;
import hr.fer.ppj.maniacs414.lab4.types.IntType;
import hr.fer.ppj.maniacs414.lab4.types.VoidType;

import java.util.HashMap;
import java.util.Map;

public class GeneratorKoda {
    public static void main(String[] args) {
        NonterminalNode root = Parser.parseInput(System.in);
        VariableTable variableTable = new VariableTable(null);
        FunctionTable functionTable = new FunctionTable(null);
        Rules.check(root, variableTable, functionTable);

        FunctionType mainFunc = functionTable.getFunction("main");
        if(mainFunc == null || !mainFunc.equals(new FunctionType(new IntType(), new VoidType()))){
            System.out.println("main");
            return;
        }

        Map<String, Map<FunctionType, Boolean>> allFunctions = new HashMap<>();
        markDefinedFunctions(functionTable, allFunctions);
        for(Map<FunctionType, Boolean> map : allFunctions.values()){
            if(map.values().contains(false)){
                System.out.println("funkcija");
                return;
            }
        }
    }

    private static void markDefinedFunctions(FunctionTable table, Map<String, Map<FunctionType, Boolean>> allFunctions){
        for (String name : table.functions.keySet()){
            FunctionType type = table.functions.get(name);
            if(!allFunctions.containsKey(name)){
                Map<FunctionType, Boolean> newMap = new HashMap<>();
                newMap.put(type, type.isDefined());
                allFunctions.put(name, newMap);
            }

            if(!allFunctions.get(name).containsKey(type)){
                allFunctions.get(name).put(type, type.isDefined());
            }

            if(type.isDefined()){
                allFunctions.get(name).put(type, type.isDefined());
            }
        }

        for(FunctionTable child : table.children){
            markDefinedFunctions(child, allFunctions);
        }
    }
}
