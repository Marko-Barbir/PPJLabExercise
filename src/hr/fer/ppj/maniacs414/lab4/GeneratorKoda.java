package hr.fer.ppj.maniacs414.lab4;

import hr.fer.ppj.maniacs414.lab4.parser.NonterminalNode;
import hr.fer.ppj.maniacs414.lab4.parser.Parser;
import hr.fer.ppj.maniacs414.lab4.rules.Rules;
import hr.fer.ppj.maniacs414.lab4.table.FunctionTable;
import hr.fer.ppj.maniacs414.lab4.table.VariableTable;
import hr.fer.ppj.maniacs414.lab4.types.FunctionType;
import hr.fer.ppj.maniacs414.lab4.types.IntType;
import hr.fer.ppj.maniacs414.lab4.types.VoidType;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class GeneratorKoda {
    public static void main(String[] args) {
        PrintStream p;
        try {
            p = new PrintStream(new BufferedOutputStream(new FileOutputStream("a.frisc")));
            System.setOut(p);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
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

        generateCode(variableTable, functionTable);
        p.flush();
        p.close();
    }

    private static void generateCode(VariableTable variableTable, FunctionTable functionTable) {
        System.out.println("""
                \tMOVE 40000, R7"""
                );
        Rules.globalCode.forEach(System.out::println);
        System.out.println("""
                \tCALL F_MAIN
                \tHALT
                
                MUL
                \tLOAD R0,(R7+8)
                \tLOAD R1, (R7+4)
                \tPUSH R2
                \tPUSH R3
                \tMOVE 0,R3
                \tMOVE 0,R2
                \tCMP R0, 0
                \tJR_NN FPMUL
                \tADD R2,1,R2
                \tSUB R3,R0,R0
                FPMUL CMP R1,0
                \tJR_NN SPMUL
                \tADD R2,1,R2
                \tSUB R3,R1,R1
                SPMUL MOVE 0,R6
                STEPMUL CMP R1,0
                \tJR_EQ ENDMUL
                \tADD R6, R0, R6
                \tSUB R1,1,R1
                \tJR STEPMUL
                ENDMUL
                \tCMP R2,1
                \tJR_NE ENDMULFUNC
                \tSUB R3,R6,R6
                ENDMULFUNC
                \tPOP R3
                \tPOP R2
                \tRET
                
                DIV
                \tLOAD R0,(R7+8)
                \tLOAD R1, (R7+4)
                \tPUSH R2
                \tPUSH R3
                \tMOVE 0,R3
                \tMOVE 0,R2
                \tCMP R0, 0
                \tJR_NN FPDIV
                \tADD R2,1,R2
                \tSUB R3,R0,R0
                FPDIV CMP R1,0
                \tJR_NN SPDIV
                \tADD R2,1,R2
                \tSUB R3,R1,R1
                SPDIV MOVE 0,R6
                STARTDIV SUB R0,R1,R0
                \tJR_N ENDDIV
                \tADD R6,1,R6
                \tJR STARTDIV
                ENDDIV
                \tCMP R2,1
                \tJR_NE ENDDIVFUNC
                \tSUB R3,R6,R6
                ENDDIVFUNC
                \tPOP R3
                \tPOP R2
                \tRET
                
                MOD
                \tLOAD R6,(R7+8)
                \tLOAD R1, (R7+4)
                \tPUSH R2
                \tPUSH R3
                \tMOVE 0,R3
                \tCMP R0, 0
                \tJR_NN FPMOD
                \tSUB R3,R6,R6
                FPMOD CMP R1,0
                \tJR_NN SPMOD
                \tSUB R3,R1,R1
                SPMOD
                STARTMOD SUB R6,R1,R6
                \tJR_N ENDMOD
                \tJR STARTMOD
                ENDMOD ADD R6, R1, R6
                \tPOP R3
                \tPOP R2
                \tRET
                """);

        for(Map.Entry<String, FunctionTable.FunctionEntry> entry : functionTable.functions.entrySet()) {
            System.out.printf("F_%s%n", entry.getKey().toUpperCase());
            entry.getValue().generatedCode.forEach(System.out::println);
            System.out.println();
        }

        for (Map.Entry<String, String> entry : Rules.memoryEntries.entrySet()) {
            System.out.printf("%s DW %s\n", entry.getKey(), entry.getValue());
        }
    }

    private static void markDefinedFunctions(FunctionTable table, Map<String, Map<FunctionType, Boolean>> allFunctions){
        for (String name : table.functions.keySet()){
            FunctionType type = table.functions.get(name).type;
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
