import parser.NonterminalNode;
import parser.Parser;
import rules.Rules;
import table.FunctionTable;
import table.VariableTable;
import types.FunctionType;
import types.IntType;
import types.VoidType;

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
        System.out.println("\tMOVE 40000, R7"
                );
        Rules.globalCode.forEach(System.out::println);
        System.out.println("\tCALL F_MAIN\n" +
                           "\tHALT\n" +
                           "\n" +
                           "MUL\n" +
                           "\tLOAD R0,(R7+8)\n" +
                           "\tLOAD R1, (R7+4)\n" +
                           "\tPUSH R2\n" +
                           "\tPUSH R3\n" +
                           "\tMOVE 0,R3\n" +
                           "\tMOVE 0,R2\n" +
                           "\tCMP R0, 0\n" +
                           "\tJR_NN FPMUL\n" +
                           "\tADD R2,1,R2\n" +
                           "\tSUB R3,R0,R0\n" +
                           "FPMUL CMP R1,0\n" +
                           "\tJR_NN SPMUL\n" +
                           "\tADD R2,1,R2\n" +
                           "\tSUB R3,R1,R1\n" +
                           "SPMUL MOVE 0,R6\n" +
                           "STEPMUL CMP R1,0\n" +
                           "\tJR_EQ ENDMUL\n" +
                           "\tADD R6, R0, R6\n" +
                           "\tSUB R1,1,R1\n" +
                           "\tJR STEPMUL\n" +
                           "ENDMUL\n" +
                           "\tCMP R2,1\n" +
                           "\tJR_NE ENDMULFUNC\n" +
                           "\tSUB R3,R6,R6\n" +
                           "ENDMULFUNC\n" +
                           "\tPOP R3\n" +
                           "\tPOP R2\n" +
                           "\tRET\n" +
                           "\n" +
                           "DIV\n" +
                           "\tLOAD R0,(R7+8)\n" +
                           "\tLOAD R1, (R7+4)\n" +
                           "\tPUSH R2\n" +
                           "\tPUSH R3\n" +
                           "\tMOVE 0,R3\n" +
                           "\tMOVE 0,R2\n" +
                           "\tCMP R0, 0\n" +
                           "\tJR_NN FPDIV\n" +
                           "\tADD R2,1,R2\n" +
                           "\tSUB R3,R0,R0\n" +
                           "FPDIV CMP R1,0\n" +
                           "\tJR_NN SPDIV\n" +
                           "\tADD R2,1,R2\n" +
                           "\tSUB R3,R1,R1\n" +
                           "SPDIV MOVE 0,R6\n" +
                           "STARTDIV SUB R0,R1,R0\n" +
                           "\tJR_N ENDDIV\n" +
                           "\tADD R6,1,R6\n" +
                           "\tJR STARTDIV\n" +
                           "ENDDIV\n" +
                           "\tCMP R2,1\n" +
                           "\tJR_NE ENDDIVFUNC\n" +
                           "\tSUB R3,R6,R6\n" +
                           "ENDDIVFUNC\n" +
                           "\tPOP R3\n" +
                           "\tPOP R2\n" +
                           "\tRET\n" +
                           "\n" +
                           "MOD\n" +
                           "\tLOAD R6,(R7+8)\n" +
                           "\tLOAD R1, (R7+4)\n" +
                           "\tPUSH R2\n" +
                           "\tPUSH R3\n" +
                           "\tMOVE 0,R3\n" +
                           "\tCMP R0, 0\n" +
                           "\tJR_NN FPMOD\n" +
                           "\tSUB R3,R6,R6\n" +
                           "FPMOD CMP R1,0\n" +
                           "\tJR_NN SPMOD\n" +
                           "\tSUB R3,R1,R1\n" +
                           "SPMOD\n" +
                           "STARTMOD SUB R6,R1,R6\n" +
                           "\tJR_N ENDMOD\n" +
                           "\tJR STARTMOD\n" +
                           "ENDMOD ADD R6, R1, R6\n" +
                           "\tPOP R3\n" +
                           "\tPOP R2\n" +
                           "\tRET\n");

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
