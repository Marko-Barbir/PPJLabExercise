import parser.NonterminalNode;
import parser.Parser;
import rules.Rules;
import table.FunctionTable;
import table.VariableTable;
import types.FunctionType;
import types.IntType;
import types.VoidType;

import java.util.HashMap;
import java.util.Map;

public class SemantickiAnalizator {
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
