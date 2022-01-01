package hr.fer.ppj.maniacs414.lab3;

import hr.fer.ppj.maniacs414.lab3.parser.NonterminalNode;
import hr.fer.ppj.maniacs414.lab3.parser.Parser;
import hr.fer.ppj.maniacs414.lab3.rules.Rules;
import hr.fer.ppj.maniacs414.lab3.table.FunctionTable;
import hr.fer.ppj.maniacs414.lab3.table.VariableTable;
import hr.fer.ppj.maniacs414.lab3.types.FunctionType;
import hr.fer.ppj.maniacs414.lab3.types.IntType;
import hr.fer.ppj.maniacs414.lab3.types.VoidType;

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

        Map<String, Boolean> allFunctions = new HashMap<>();
        markDefinedFunctions(functionTable, allFunctions);
        if(allFunctions.values().contains(false)){
            System.out.println("function");
            return;
        }
    }

    private static void markDefinedFunctions(FunctionTable table, Map<String, Boolean> allFunctions){
        for (String name : table.functions.keySet()){
            FunctionType type = table.functions.get(name);
            if(!allFunctions.containsKey(name) || type.isDefined()){
                allFunctions.put(name, type.isDefined());
            }
        }

        for(FunctionTable child : table.children){
            markDefinedFunctions(child, allFunctions);
        }
    }
}
