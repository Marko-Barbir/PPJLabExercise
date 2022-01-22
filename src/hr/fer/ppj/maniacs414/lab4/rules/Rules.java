package hr.fer.ppj.maniacs414.lab4.rules;

import hr.fer.ppj.maniacs414.lab4.parser.Node;
import hr.fer.ppj.maniacs414.lab4.parser.NonterminalNode;
import hr.fer.ppj.maniacs414.lab4.parser.TerminalNode;
import hr.fer.ppj.maniacs414.lab4.table.FunctionTable;
import hr.fer.ppj.maniacs414.lab4.table.VariableTable;
import hr.fer.ppj.maniacs414.lab4.types.*;

import java.math.BigInteger;
import java.util.*;

public class Rules {
    private static FunctionTable.FunctionEntry currentFunction = null;
    public static Map<String, String> memoryEntries= new HashMap<>();
    public static List<String> globalCode = new ArrayList<>();
    private static Stack<Integer> loopStack = new Stack<>();
    private static int initx = 0;
    private static int ifx = 0;
    private static int loopx = 0;
    private static int unneg = 0;
    private static int comp = 0;
    private static int sc = 0;

    public static void check(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        if(node.name.equals("slozena_naredba")){
            if(node.children.size() == 3){
                slozena_naredba1(node, variableTable, functionTable);
            }
            else {
                slozena_naredba2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("lista_naredbi")){
            if(node.children.size() == 1){
                lista_naredbi1(node, variableTable, functionTable);
            }
            else {
                lista_naredbi2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("naredba")){
            naredba(node, variableTable, functionTable);
        }

        else if(node.name.equals("izraz_naredba")){
            if(node.children.size() == 1){
                izraz_naredba1(node, variableTable, functionTable);
            }
            else {
                izraz_naredba2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("naredba_grananja")){
            if(node.children.size() == 5){
                naredba_grananja1(node, variableTable, functionTable);
            }
            else {
                naredba_grananja2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("naredba_petlje")){
            if(node.children.size() == 5){
                naredba_petlje1(node, variableTable, functionTable);
            }
            else if (node.children.size() == 6){
                naredba_petlje2(node, variableTable, functionTable);
            }
            else {
                naredba_petlje3(node, variableTable, functionTable);
            }
        }

            else if(node.name.equals("naredba_skoka")){
            if(node.children.size() == 3){
                naredba_skoka3(node, variableTable, functionTable);
            }
            else if (((TerminalNode)node.children.get(0)).token.equals("KR_RETURN")){
                naredba_skoka2(node, variableTable, functionTable);
            }
            else {
                naredba_skoka1(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("prijevodna_jedinica")){
            if(node.children.size() == 1){
                prijevodna_jedinica1(node, variableTable, functionTable);
            }
            else {
                prijevodna_jedinica2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("vanjska_deklaracija")){
            vanjska_deklaracija(node, variableTable, functionTable);
        }

        else if(node.name.equals("definicija_funkcije")){
            if(node.children.get(3) instanceof TerminalNode){
                definicija_funkcije1(node, variableTable, functionTable);
            }
            else {
                definicija_funkcije2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("lista_parametara")){
            if(node.children.size() == 1){
                lista_parametara1(node, variableTable, functionTable);
            }
            else {
                lista_parametara2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("deklaracija_parametra")){
            if(node.children.size() == 2){
                deklaracija_parametra1(node, variableTable, functionTable);
            }
            else {
                deklaracija_parametra2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("lista_deklaracija")){
            if(node.children.size() == 1){
                lista_deklaracija1(node, variableTable, functionTable);
            }
            else {
                lista_deklaracija2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("deklaracija")){
            deklaracija(node, variableTable, functionTable);
        }

        else if(node.name.equals("lista_init_deklaratora")){
            if(node.children.size() == 1){
                lista_init_deklaratora1(node, variableTable, functionTable);
            }
            else {
                lista_init_deklaratora2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("init_deklarator")){
            if(node.children.size() == 1){
                init_deklarator1(node, variableTable, functionTable);
            }
            else {
                init_deklarator2(node, variableTable, functionTable);
            }
        }

        else if(node.name.equals("izravni_deklarator")){
            if(node.children.size() == 1){
                izravni_deklarator1(node, variableTable, functionTable);
            }
            else if (node.children.get(2) instanceof NonterminalNode){
                izravni_deklarator4(node, variableTable, functionTable);
            }
            else if (((TerminalNode)node.children.get(2)).token.equals("BROJ")){
                izravni_deklarator2(node, variableTable, functionTable);
            }
            else {
                izravni_deklarator3(node, variableTable, functionTable);
            }
        }
        else if(node.name.equals("primarni_izraz")) {
            if(node.children.size() == 1) {
                if(((TerminalNode) node.children.get(0)).token.equals("IDN")) primarni_izraz1(node, variableTable, functionTable);
                else if(((TerminalNode) node.children.get(0)).token.equals("BROJ")) primarni_izraz2(node, variableTable, functionTable);
                else if(((TerminalNode) node.children.get(0)).token.equals("ZNAK")) primarni_izraz3(node, variableTable, functionTable);
                else if(((TerminalNode) node.children.get(0)).token.equals("NIZ_ZNAKOVA")) primarni_izraz4(node, variableTable, functionTable);
            }
            else if(node.children.size() == 3) primarni_izraz5(node, variableTable, functionTable);
        }
        else if(node.name.equals("postfiks_izraz")) {
            postfiks_izraz(node, variableTable, functionTable);
        }
        else if(node.name.equals("lista_argumenata")) {
            if(node.children.size()==1) lista_argumenata1(node, variableTable, functionTable);
            else if(node.children.size()==3) lista_argumenata2(node, variableTable, functionTable);
        }
        else if(node.name.equals("unarni_izraz")) {
            if(node.children.size()==1) unarni_izraz1(node, variableTable, functionTable);
            else if(node.children.size()==2) {
                if(((NonterminalNode) node.children.get(1)).name.equals("unarni_izraz")) {
                    unarni_izraz2(node, variableTable, functionTable);
                } else {
                    unarni_izraz3(node, variableTable, functionTable);
                }
            }
        }
        else if(node.name.equals("cast_izraz")) {
            if(node.children.size()==1) cast_izraz1(node, variableTable, functionTable);
            else if(node.children.size()==4) cast_izraz2(node, variableTable, functionTable);
        }
        else if (node.name.equals("ime_tipa")) {
            if(node.children.size() == 1) ime_tipa1(node, variableTable, functionTable);
            else if(node.children.size() == 2) ime_tipa2(node, variableTable, functionTable);
        }
        else if (node.name.equals("specifikator_tipa")) specifikator_tipa(node, variableTable, functionTable);
        else if (node.name.equals("multiplikativni_izraz")
                || node.name.equals("aditivni_izraz")
                || node.name.equals("odnosni_izraz")
                || node.name.equals("jednakosni_izraz")
                || node.name.equals("bin_i_izraz")
                || node.name.equals("bin_xili_izraz")
                || node.name.equals("bin_ili_izraz")
                || node.name.equals("log_i_izraz")
                || node.name.equals("log_ili_izraz")) {
            if(node.children.size()==1) op_izraz1(node, variableTable, functionTable);
            else if(node.children.size()==3) op_izraz2(node, variableTable, functionTable);
        }
        else if(node.name.equals("izraz_pridruzivanja")) {
            if(node.children.size()==1) izraz_pridruzivanja1(node, variableTable, functionTable);
            else if(node.children.size()==3) izraz_pridruzivanja2(node, variableTable, functionTable);
        }
        else if(node.name.equals("izraz")) {
            if(node.children.size()==1) izraz1(node, variableTable, functionTable);
            else if(node.children.size()==3) izraz2(node, variableTable, functionTable);
        }
        else if(node.name.equals("lista_izraza_pridruzivanja")) {
            if(node.children.size()==1) lista_izraza_pridruzivanja1(node, variableTable, functionTable);
            else if(node.children.size()==3) lista_izraza_pridruzivanja2(node, variableTable, functionTable);
        }
        else if (node.name.equals("inicijalizator")) {
            if(node.children.size()==1) inicijalizator1(node, variableTable, functionTable);
            else if(node.children.size()==3) inicijalizator2(node, variableTable, functionTable);
        }
    }

    private static void error(NonterminalNode node){
        System.out.println(node.getProduction());
        System.exit(1);
    }

    private static void checkImplicitCast(NonterminalNode node, int childIndex, Type type){
        if(!((Type)node.children.get(childIndex).props.get("tip")).implicitCastsInto(type)) {
            error(node);
        }
    }
    private static void checkIfInside(String name, NonterminalNode node){
        for(NonterminalNode n = (NonterminalNode) node.parent; n != null; n = (NonterminalNode) n.parent) {
            if (n.name.equals(name)) return;
        }
        error(node);
    }

    private static void checkIfInsideFunctionVoid(NonterminalNode node){
        NonterminalNode funcNode = null;
        for(NonterminalNode n = (NonterminalNode) node.parent; n != null; n = (NonterminalNode) n.parent){
            if(n.name.equals("definicija_funkcije")){
                funcNode = n;
                break;
            }
        }
        //mozda greska zbog 2. uvjeta?
        if(funcNode == null || !funcNode.children.get(0).props.get("tip").equals(new VoidType())){
            error(node);
        }
    }

    private static void checkIfInsideFunctionNonVoid(Type type, NonterminalNode node){
        NonterminalNode funcNode = null;
        for(NonterminalNode n = (NonterminalNode) node.parent; n != null; n = (NonterminalNode) n.parent){
            if(n.name.equals("definicija_funkcije")){
                funcNode = n;
                break;
            }
        }
        //mozda greska zbog 2. uvjeta?
        if(funcNode == null || !type.implicitCastsInto((Type)funcNode.children.get(0).props.get("tip"))){
            error(node);
        }
    }

    private static void checkIfFunctionAlreadyDefined(String name, FunctionTable table, NonterminalNode node){
        while(table != null){
            if(table.isAlreadyDeclared(name) && table.getFunction(name).isDefined()){
                error(node);
            }
            table = table.parentTable;
        }
    }

    private static void initDeclaratorHelper(NonterminalNode node, Type other) {
        Integer declElemCount = (Integer)node.children.get(0).props.get("br-elem");
        Integer initElemCount = (Integer)node.children.get(2).props.get("br-elem");
        List<Type> initTypes = (List<Type>)node.children.get(2).props.get("tipovi");
        if (initElemCount > declElemCount) {
            error(node);
        }
        for (Type type : initTypes){
            if(!type.implicitCastsInto(other)){
                error(node);
            }
        }
    }

    private static void slozena_naredba1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        VariableTable newVariableTable = new VariableTable(variableTable);
        FunctionTable newFunctionTable = new FunctionTable(functionTable);

        if(node.props.containsKey("tipovi")){
            List<Type> paramTypes = (List<Type>) node.props.get("tipovi");
            List<String> names = (List<String>) node.props.get("imena");

            for (int i = 0; i < paramTypes.size(); i++){
                newVariableTable.variables.put((String)names.get(i), new VariableTable.VariableEntry(paramTypes.get(i), i * 4));
            }
        }

        check((NonterminalNode) node.children.get(1), newVariableTable, newFunctionTable);
    }

    private static void slozena_naredba2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        VariableTable newVariableTable = new VariableTable(variableTable);
        FunctionTable newFunctionTable = new FunctionTable(functionTable);

        if(node.props.containsKey("tipovi")){
            List<Type> paramTypes = (List<Type>) node.props.get("tipovi");
            List<String> names = (List<String>) node.props.get("imena");

            for (int i = 0; i < paramTypes.size(); i++){
                newVariableTable.variables.put((String)names.get(i), new VariableTable.VariableEntry((Type) paramTypes.get(i), i * 4));
            }
        }

        check((NonterminalNode) node.children.get(1), newVariableTable, newFunctionTable);
        check((NonterminalNode) node.children.get(2), newVariableTable, newFunctionTable);
    }

    private static void lista_naredbi1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
    }

    private static void lista_naredbi2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        check((NonterminalNode) node.children.get(1), variableTable, functionTable);
    }

    private static void naredba(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
    }

    private static void izraz_naredba1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        node.addProp("tip", new IntType());
        if(node.props.containsKey("forCond")) {
            currentFunction.generatedCode.add("\tMOVE 1, R0");
            currentFunction.generatedCode.add("\tPUSH R0");
            currentFunction.stackSize--;
        }
    }

    private static void izraz_naredba2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        node.addProp("tip", node.children.get(0).props.get("tip"));
        if(!node.props.containsKey("forCond")) {
            currentFunction.generatedCode.add("\tPOP R0");
            currentFunction.stackSize--;
        }
    }
    private static void naredba_grananja1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        checkImplicitCast(node, 2, new IntType());
        currentFunction.generatedCode.add("\tPOP R0");
        currentFunction.stackSize--;
        currentFunction.generatedCode.add("\tCMP R0, 0");
        int x = ifx++;
        currentFunction.generatedCode.add("\tJR_EQ ENDIF" + x);
        check((NonterminalNode) node.children.get(4), variableTable, functionTable);
        currentFunction.generatedCode.add("ENDIF"+x);
    }

    private static void naredba_grananja2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        checkImplicitCast(node, 2, new IntType());
        currentFunction.generatedCode.add("\tPOP R0");
        currentFunction.stackSize--;
        currentFunction.generatedCode.add("\tCMP R0, 0");
        int x = ifx++;
        currentFunction.generatedCode.add("\tJR_EQ ELSE" + x);
        check((NonterminalNode) node.children.get(4), variableTable, functionTable);
        currentFunction.generatedCode.add("\tJR ENDIF" + x);
        currentFunction.generatedCode.add("ELSE" + x);
        check((NonterminalNode) node.children.get(6), variableTable, functionTable);
        currentFunction.generatedCode.add("ENDIF" + x);
    }

    private static void naredba_petlje1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        int x = loopx++;
        currentFunction.generatedCode.add("LOOPSTART"+x);
        loopStack.push(x);
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        currentFunction.generatedCode.add("\tPOP R0");
        currentFunction.stackSize--;
        currentFunction.generatedCode.add("\tCMP R0, 0");
        currentFunction.generatedCode.add("\tJR_EQ LOOPEND"+x);
        checkImplicitCast(node, 2, new IntType());
        check((NonterminalNode) node.children.get(4), variableTable, functionTable);
        currentFunction.generatedCode.add("\tJR LOOPSTART"+x);
        currentFunction.generatedCode.add("LOOPEND"+x);
        loopStack.pop();
    }

    private static void naredba_petlje2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        int x = loopx++;
        currentFunction.generatedCode.add("LOOPSTART"+x);
        loopStack.push(x);
        node.children.get(3).addProp("forCond", true);
        check((NonterminalNode) node.children.get(3), variableTable, functionTable);
        checkImplicitCast(node, 3, new IntType());
        currentFunction.generatedCode.add("\tPOP R0");
        currentFunction.stackSize--;
        currentFunction.generatedCode.add("\tCMP R0,0");
        currentFunction.generatedCode.add("\tJR_EQ LOOPEND"+x);
        check((NonterminalNode) node.children.get(5), variableTable, functionTable);
        currentFunction.generatedCode.add("\tJR LOOPSTART"+x);
        currentFunction.generatedCode.add("LOOPEND"+x);
    }

    private static void naredba_petlje3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        int x = loopx++;
        currentFunction.generatedCode.add("LOOPSTART"+x);
        loopStack.push(x);
        node.children.get(3).addProp("forCond", true);
        check((NonterminalNode) node.children.get(3), variableTable, functionTable);
        checkImplicitCast(node, 3, new IntType());
        currentFunction.generatedCode.add("\tPOP R0");
        currentFunction.stackSize--;
        currentFunction.generatedCode.add("\tCMP R0,0");
        currentFunction.generatedCode.add("\tJR_EQ LOOPEND"+x);
        check((NonterminalNode) node.children.get(4), variableTable, functionTable);
        check((NonterminalNode) node.children.get(6), variableTable, functionTable);
        currentFunction.generatedCode.add("\tJR LOOPSTART"+x);
        currentFunction.generatedCode.add("LOOPEND"+x);
    }

    private static void naredba_skoka1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        checkIfInside("naredba_petlje", node);
        int x = loopStack.peek();
        switch (((TerminalNode)node.children.get(0)).token) {
            case "KR_CONTINUE" -> currentFunction.generatedCode.add("\t JR LOOPSTART" + x);
            case "KR_BREAK" -> currentFunction.generatedCode.add("\t JR LOOPEND" + x);
            default -> error(node);
        }
    }

    private static void naredba_skoka2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        checkIfInsideFunctionVoid(node);
    }

    private static void naredba_skoka3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(1), variableTable, functionTable);
        checkIfInsideFunctionNonVoid((Type) node.children.get(1).props.get("tip"), node);
        currentFunction.generatedCode.add("\tPOP R6");
        currentFunction.stackSize--;
    }

    private static void prijevodna_jedinica1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
    }

    private static void prijevodna_jedinica2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        check((NonterminalNode) node.children.get(1), variableTable, functionTable);
    }

    private static void vanjska_deklaracija(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
    }

    private static void definicija_funkcije1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        if (node.children.get(0).props.get("tip").equals(new CharType(true)) || node.children.get(0).props.get("tip").equals(new IntType(true))){
            error(node);
        }
        String functionName = ((TerminalNode) node.children.get(1)).value;
        Type returnType = (Type)node.children.get(0).props.get("tip");
        checkIfFunctionAlreadyDefined(functionName, functionTable, node);
        FunctionTable globalScope = functionTable.getGlobalScope();
        FunctionType newFunction = new FunctionType(returnType, new VoidType());
        if(isAlreadyDeclared(functionName, variableTable.getGlobalScope(), globalScope) && !globalScope.getFunction(functionName).equals(newFunction)){
            error(node);
        }
        newFunction.isDefined = true;
        currentFunction = new FunctionTable.FunctionEntry(newFunction);
        functionTable.functions.put(functionName, currentFunction);
        check((NonterminalNode) node.children.get(5), new VariableTable(variableTable), new FunctionTable(functionTable));
        currentFunction = null;
    }

    private static void definicija_funkcije2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        if (node.children.get(0).props.get("tip").equals(new CharType(true)) || node.children.get(0).props.get("tip").equals(new IntType(true))){
            error(node);
        }
        String functionName = ((TerminalNode) node.children.get(1)).value;
        Type returnType = (Type)node.children.get(0).props.get("tip");
        checkIfFunctionAlreadyDefined(functionName, functionTable, node);
        check((NonterminalNode) node.children.get(3), variableTable, functionTable);
        FunctionTable globalScope = functionTable.getGlobalScope();
        List<Type> paramTypes = (List<Type>) node.children.get(3).props.get("tipovi");
        List<String> names = (List<String>) node.children.get(3).props.get("imena");
        FunctionType newFunction = new FunctionType(returnType, paramTypes);
        if(isAlreadyDeclared(functionName, variableTable.getGlobalScope(), globalScope) && !globalScope.getFunction(functionName).equals(newFunction)){
            error(node);
        }
        newFunction.isDefined = true;
        currentFunction = new FunctionTable.FunctionEntry(newFunction);
        functionTable.functions.put(functionName, currentFunction);

        node.children.get(5).addProp("tipovi", paramTypes);
        node.children.get(5).addProp("imena", names);
        check((NonterminalNode) node.children.get(5), variableTable, functionTable);
        currentFunction = null;
    }

    private static void lista_parametara1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        node.addProp("tipovi", new ArrayList<>(List.of((Type)node.children.get(0).props.get("tip"))));
        node.addProp("imena", new ArrayList<>(List.of((String)node.children.get(0).props.get("ime"))));
    }

    private static void lista_parametara2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        NonterminalNode lista_parametara = (NonterminalNode) node.children.get(0);
        NonterminalNode deklaracija_parametra = (NonterminalNode) node.children.get(2);
        check(lista_parametara, variableTable, functionTable);
        check(deklaracija_parametra, variableTable, functionTable);
        List<Type> tipovi = new ArrayList<>((List<Type>) lista_parametara.props.get("tipovi"));
        List<String> imena = new ArrayList<>((List<String>) lista_parametara.props.get("imena"));
        if(imena.contains((String)node.children.get(2).props.get("ime"))) {
            error(node);
        }
        tipovi.add((Type) deklaracija_parametra.props.get("tip"));
        imena.add((String) deklaracija_parametra.props.get("ime"));
        node.props.put("tipovi", tipovi);
        node.props.put("imena", imena);
    }

    private static void deklaracija_parametra1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        Type type = (Type)node.children.get(0).props.get("tip");
        if(type.equals(new VoidType())){
            error(node);
        }
        node.addProp("tip", type);
        node.addProp("ime", ((TerminalNode) node.children.get(1)).value);
    }

    private static void deklaracija_parametra2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        Type type = (Type)node.children.get(0).props.get("tip");
        if(type.equals(new VoidType())){
            error(node);
        }
        node.addProp("tip", new ArrayType(type));
        node.addProp("ime", ((TerminalNode) node.children.get(1)).value);
    }

    private static void lista_deklaracija1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
    }

    private static void lista_deklaracija2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        check((NonterminalNode) node.children.get(1), variableTable, functionTable);
    }

    private static void deklaracija(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        node.children.get(1).addProp("ntip", (Type)node.children.get(0).props.get("tip"));
        check((NonterminalNode) node.children.get(1), variableTable, functionTable);
    }

    private static void lista_init_deklaratora1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        node.children.get(0).addProp("ntip", (Type)node.props.get("ntip"));
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
    }

    private static void lista_init_deklaratora2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        node.children.get(0).addProp("ntip", (Type)node.props.get("ntip"));
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        node.children.get(2).addProp("ntip", (Type)node.props.get("ntip"));
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
    }

    private static void init_deklarator1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        node.children.get(0).addProp("ntip", (Type)node.props.get("ntip"));
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        Type type = (Type)node.children.get(0).props.get("tip");
        if(type.equals(new IntType(true)) || type.equals(new CharType(true)) || type.equals(new ArrayType(new IntType(true))) || type.equals(new ArrayType(new CharType(true)))){
            error(node);
        }
        addFrisc("\tPOP R0");
        if(currentFunction != null) currentFunction.stackSize--;
    }

    private static void init_deklarator2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        node.children.get(0).addProp("ntip", (Type)node.props.get("ntip"));
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        addFrisc("\tPOP R3");
        if(currentFunction != null) currentFunction.stackSize--;
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        Object br_elem = node.children.get(2).props.get("br-elem");
        addFrisc("\tMOVE %D " + (br_elem == null ? 1 : (int)br_elem) + ",R2");
        addFrisc("INIT" + initx + "\t POP R0");
        addFrisc("\tSTORE R0, (R3)");
        addFrisc("\tADD R3, 4 R3");
        addFrisc("\tSUB R2,1,R2");
        addFrisc("\tCMP R2, 0");
        addFrisc("\tJR_NE INIT" + initx);
        currentFunction.stackSize+=(br_elem == null ? 1 : (int)br_elem);
        Type declType = (Type)node.children.get(0).props.get("tip");
        Type initType = (Type)node.children.get(2).props.get("tip");
        Type intType = new IntType();
        Type charType = new CharType();
        Type constIntType = new IntType(true);
        Type constCharType = new CharType(true);
        if(declType.equals(intType) || declType.equals(constIntType)){
            if (!initType.implicitCastsInto(intType)) {
                error(node);
            }
        }
        else if(declType.equals(charType) || declType.equals(constCharType)){
            if (!initType.implicitCastsInto(charType)) {
                error(node);
            }
        }
        else if(declType.equals(new ArrayType(intType)) || declType.equals(new ArrayType(constIntType))){
            initDeclaratorHelper(node, intType);
        }
        else if(declType.equals(new ArrayType(charType)) || declType.equals(new ArrayType(constCharType))){
            initDeclaratorHelper(node, charType);
        }
        else{
            error(node);
        }

        TerminalNode IDN = (TerminalNode) (((NonterminalNode) (node.children.get(0))).children.get(0));
        Node i = node.children.get(2);
        while (i instanceof NonterminalNode) {
            NonterminalNode nonterminalNode = (NonterminalNode) i;
            i = nonterminalNode.children.get(0);
        }
        TerminalNode BROJ = (TerminalNode) i;
    }

//    private static void izravni_deklarator1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
//        Type type = (Type)node.props.get("ntip");
//        String name = (String)node.children.get(0).props.get("ime");
//        if(type instanceof VoidType){
//            error(node);
//        }
//        if(variableTable.isAlreadyDeclared(name)){
//            error(node);
//        }
//        variableTable.variables.put(name, type);
//        node.addProp("tip", type);
//    }
//
//    private static void izravni_deklarator2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
//        Type type = (Type)node.props.get("ntip");
//        String name = (String)node.children.get(0).props.get("ime");
//        Integer value = (Integer)node.children.get(2).props.get("vrijednost");
//        if(type instanceof VoidType){
//            error(node);
//        }
//        if(variableTable.isAlreadyDeclared(name)){
//            error(node);
//        }
//        if(value <= 0 || value > 1024){
//            error(node);
//        }
//        variableTable.variables.put(name, type);
//        node.addProp("tip", new ArrayType(type));
//        node.addProp("br-elem", value);
//    }
//
//    private static void izravni_deklarator3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
//        Type type = (Type)node.props.get("ntip");
//        String name = (String)node.children.get(0).props.get("ime");
//        FunctionType newFunction = new FunctionType(type, new VoidType());
//        if(functionTable.isAlreadyDeclared(name)){
//            if(!functionTable.getFunction(name).equals(newFunction)){
//                error(node);
//            }
//        }
//        else {
//            functionTable.functions.put(name, newFunction);
//        }
//        node.addProp("tip", newFunction);
//    }
//
//    private static void izravni_deklarator4(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
//        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
//
//        Type returnType = (Type)node.props.get("ntip");
//        String name = (String)node.children.get(0).props.get("ime");
//        List<Type> paramTypes = (List<Type>)node.children.get(2).props.get("tipovi");
//        FunctionType newFunction = new FunctionType(returnType, paramTypes);
//        if(functionTable.isAlreadyDeclared(name)){
//            if(!functionTable.getFunction(name).equals(newFunction)){
//                error(node);
//            }
//        }
//        else {
//            functionTable.functions.put(name, newFunction);
//        }
//        node.addProp("tip", newFunction);
//    }

    private static void primarni_izraz1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        TerminalNode IDN = (TerminalNode) node.children.get(0);
        Type variable = getType(functionTable, variableTable, IDN.value);
        if(variable == null){
            variable = functionTable.getFunction(IDN.value);
            if(variable == null) {
                error(node);
            }
        }

        Pair<Object, Boolean> entry = getEntry(functionTable, variableTable, IDN.value);
        if(entry.first instanceof VariableTable.VariableEntry) {
            VariableTable.VariableEntry variableEntry = (VariableTable.VariableEntry) entry.first;

            if(entry.second) {
                if(node.props.containsKey("pointer") || variable instanceof ArrayType){
                    addFrisc("\tMOVE V_" + IDN.value.toUpperCase() + ", R0");
                } else {
                    addFrisc("\tLOAD R0, (V_" + IDN.value.toUpperCase() + ")");
                }
            } else {
                if(node.props.containsKey("pointer") || variable instanceof ArrayType){
                    addFrisc(String.format("\tMOVE 0%X, R0", 4 * (currentFunction.stackSize-1) - variableEntry.stackIndex));
                    addFrisc("\tADD R0, R7, R0");
                } else {
                    addFrisc(String.format("\tLOAD R0, (R7+0%X)", 4 * (currentFunction.stackSize-1) - variableEntry.stackIndex));
                }
            }

            addFrisc("\tPUSH R0");
            if(currentFunction != null) currentFunction.stackSize++;
        }

        node.props.put("tip", variable);
        node.props.put("l-izraz", isNonConstantNumerical(variable));
    }

    private static void primarni_izraz2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        TerminalNode BROJ = (TerminalNode) node.children.get(0);
        if(new BigInteger(BROJ.value).compareTo(BigInteger.valueOf(2147483647)) > 0 ||
            new BigInteger(BROJ.value).compareTo(BigInteger.valueOf(-2147483648)) < 0) {
            error(node);
        }

        int numberValue = Integer.parseInt(BROJ.value);
        boolean canFit = false;

        if((numberValue >> (19) & 1) == 0) {
            if((numberValue & (0xFFF00000)) == 0) {
                canFit = true;
            }
        } else{
            if((numberValue & (0xFFF00000)) == 0xFFF00000) {
                canFit = true;
            }
        }

        if(canFit) {
            addFrisc(String.format("\tMOVE %s %s,R0", "%D", BROJ.value));
        } else {
            memoryEntries.put("C_" + BROJ.value, "%D " + BROJ.value);
            addFrisc(String.format("\tLOAD R0, (%s)", "C_" + BROJ.value));
        }
        addFrisc("\tPUSH R0");
        if(currentFunction != null) currentFunction.stackSize++;


        node.props.put("tip", new IntType());
        node.props.put("l-izraz", false);
    }

    private static void primarni_izraz3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        TerminalNode ZNAK = (TerminalNode) node.children.get(0);
        if(!isChar(ZNAK.value)) {
            error(node);
        }

        char c = ZNAK.value.charAt(0);

        addFrisc(String.format("\tMOVE %s %s, R0", "%D", (int)c));
        addFrisc("\tPUSH R0");
        if(currentFunction != null) currentFunction.stackSize++;


        node.props.put("tip", new CharType());
        node.props.put("l-izraz", false);
    }

    private static void primarni_izraz4(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        TerminalNode NIZ_ZNAKOVA = (TerminalNode) node.children.get(0);
        if(!isValidString(NIZ_ZNAKOVA.value)) error(node);
        node.props.put("tip", new ArrayType(new CharType(true)));
        node.props.put("l-izraz", false);
    }

    private static void primarni_izraz5(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode izraz = (NonterminalNode) node.children.get(1);
        check(izraz, variableTable, functionTable);
        node.props.put("tip", izraz.props.get("tip"));
        node.props.put("l-izraz", izraz.props.get("l-izraz"));
    }

    private static void postfiks_izraz(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        if(node.children.size() == 1) {
            NonterminalNode primarni_izraz = (NonterminalNode) node.children.get(0);
            check(primarni_izraz, variableTable, functionTable);
            if(node.props.containsKey("pointer")){
                primarni_izraz.addProp("pointer", true);
            }
            node.props.put("tip", primarni_izraz.props.get("tip"));
            node.props.put("l-izraz", primarni_izraz.props.get("l-izraz"));
        } else if(node.children.size() == 4) {
            if(((TerminalNode) node.children.get(1)).token.equals("L_UGL_ZAGRADA")) {
                NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
                NonterminalNode izraz = (NonterminalNode) node.children.get(2);
                check(postfiks_izraz, variableTable, functionTable);
                Type postfiks_type = (Type) postfiks_izraz.props.get("tip");
                if(!(postfiks_type instanceof ArrayType)) {
                    error(node);
                    System.exit(0);
                }
                ArrayType arrt = (ArrayType) postfiks_type;
                check(izraz, variableTable, functionTable);
                if(!(((Type) izraz.props.get("tip")).implicitCastsInto(new IntType()))) {
                    error(node);
                }


                addFrisc("\tPOP R1");
                addFrisc("\tSHL R1, 2, R1");
                addFrisc("\tPOP R0");
                addFrisc("\tADD R0, R1, R0");
                if(!node.props.containsKey("pointer")){
                    addFrisc("\tLOAD R0, (R0)");
                }
                addFrisc("\tPUSH R0");
                if(currentFunction != null) currentFunction.stackSize++;

                node.props.put("tip", arrt.elementType);
                node.props.put("l-izraz", !isConstantNumerical(arrt.elementType));
            }
            else {
                NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
                NonterminalNode lista_argumenata = (NonterminalNode) node.children.get(2);
                check(postfiks_izraz, variableTable, functionTable);
                check(lista_argumenata, variableTable, functionTable);
                if(!(postfiks_izraz.props.get("tip") instanceof FunctionType)) {
                    error(node);
                    throw new IllegalArgumentException();
                }
                FunctionType funkcija = (FunctionType) postfiks_izraz.props.get("tip");
                List<Type> tipovi = (List<Type>) lista_argumenata.props.get("tipovi");
                if(tipovi.size() != funkcija.paramTypes.size()) {
                    error(node);
                }
                for(int i=0; i<tipovi.size(); i++) {
                    if(!tipovi.get(i).implicitCastsInto(funkcija.paramTypes.get(i))) {
                        error(node);
                    }
                }

                String functionName = ((TerminalNode)((NonterminalNode)((NonterminalNode)node.children.get(0)).children.get(0)).children.get(0)).value;
                addFrisc("\tCALL F_" + functionName.toUpperCase());
                addFrisc(String.format("\tADD R7, %s %s, R7", "%D" , tipovi.size() * 4));
                if(currentFunction != null) currentFunction.stackSize -= tipovi.size();

                if(!(funkcija.returnType instanceof VoidType)) {
                    addFrisc("\tPUSH R6");
                    if(currentFunction != null) currentFunction.stackSize++;
                }

                node.props.put("tip", funkcija.returnType);
                node.props.put("l-izraz", false);
            }
        } else if(node.children.size() == 3) {
            NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
            check(postfiks_izraz, variableTable, functionTable);
            if(!(postfiks_izraz.props.get("tip") instanceof FunctionType)) {
                error(node);
                throw new IllegalArgumentException();
            }
            FunctionType funkcija = (FunctionType) postfiks_izraz.props.get("tip");
            if(!funkcija.equals(new FunctionType(funkcija.returnType, new VoidType()))) {
                error(node);
            }

            String functionName = ((TerminalNode)((NonterminalNode)((NonterminalNode)node.children.get(0)).children.get(0)).children.get(0)).value;
            addFrisc("\tCALL F_" + functionName.toUpperCase());
            if(!(funkcija.returnType instanceof VoidType)) {
                addFrisc("\tPUSH R6");
                if(currentFunction != null) currentFunction.stackSize++;
            }

            node.props.put("tip", funkcija.returnType);
            node.props.put("l-izraz", false);
        } else if(node.children.size() == 2) {
            NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
            TerminalNode op = (TerminalNode) node.children.get(1);

            postfiks_izraz.addProp("pointer", true);
            check(postfiks_izraz, variableTable, functionTable);
            if(!((boolean) postfiks_izraz.props.get("l-izraz")) ||
                !((Type) postfiks_izraz.props.get("tip")).implicitCastsInto(new IntType())) {
                error(node);
            }

            addFrisc("\tPOP R1");
            addFrisc("\tLOAD R0, (R1)");
            if(op.token.equals("OP_INC")) {
                addFrisc("\tADD R0, 1, R4");
            } else {
                addFrisc("\tSUB R0, 1, R4");
            }
            addFrisc("\tSTORE R4, (R1)");
            addFrisc("\tPUSH R0");

            node.props.put("tip", new IntType());
            node.props.put("l-izraz", false);
        }
    }

    private static void lista_argumenata1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode izraz_pridruzivanja = (NonterminalNode) node.children.get(0);
        check(izraz_pridruzivanja, variableTable, functionTable);
        List<Type> tipovi = new ArrayList<>();
        tipovi.add((Type) izraz_pridruzivanja.props.get("tip"));
        node.props.put("tipovi", tipovi);
    }

    private static void lista_argumenata2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode lista_argumenata = (NonterminalNode) node.children.get(0);
        NonterminalNode izraz_pridruzivanja = (NonterminalNode) node.children.get(2);
        check(lista_argumenata, variableTable, functionTable);
        check(izraz_pridruzivanja, variableTable, functionTable);
        List<Type> tipovi = new ArrayList<>((List<Type>) lista_argumenata.props.get("tipovi"));
        tipovi.add((Type) izraz_pridruzivanja.props.get("tip"));
        node.props.put("tipovi", tipovi);
    }

    private static void unarni_izraz1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
        if(node.props.containsKey("pointer")){
            postfiks_izraz.addProp("pointer", true);
        }
        check(postfiks_izraz, variableTable, functionTable);
        node.addProp("tip", postfiks_izraz.props.get("tip"));
        node.addProp("l-izraz", postfiks_izraz.props.get("l-izraz"));
    }

    private static void unarni_izraz2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode unarni_izraz = (NonterminalNode) node.children.get(1);
        TerminalNode op = (TerminalNode) node.children.get(0);

        unarni_izraz.addProp("pointer", true);
        check(unarni_izraz, variableTable, functionTable);
        if(!(((boolean) unarni_izraz.props.get("l-izraz")) ||
                ((Type) unarni_izraz.props.get("tip")).implicitCastsInto(new IntType()))) {
            error(node);
        }

        addFrisc("\tPOP R1");
        addFrisc("\tLOAD R0, (R1)");
        if(op.token.equals("OP_INC")) {
            addFrisc("\tADD R0, 1, R0");
        } else {
            addFrisc("\tSUB R0, 1, R0");
        }
        addFrisc("\tSTORE R0, (R1)");
        addFrisc("\tPUSH R0");

        node.addProp("tip", new IntType());
        node.addProp("l-izraz", false);
    }

    private static void unarni_izraz3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        TerminalNode unarni_operator = (TerminalNode) node.children.get(0);
        NonterminalNode cast_izraz = (NonterminalNode) node.children.get(1);
        check(cast_izraz, variableTable, functionTable);
        if(!((Type) cast_izraz.props.get("tip")).implicitCastsInto(new IntType())) {
            error(node);
        }

        addFrisc("\tPOP R0");
        switch (unarni_operator.token){
            case "MINUS" -> {
                addFrisc("\tMOVE 0, R1");
                addFrisc("\tSUB R1, R0, R0");
            }
            case "OP_TILDA" -> {
                addFrisc("\tMOVE -1, R1");
                addFrisc("\tXOR R1, R0, R0");
            }
            case "OP_NEG" -> {
                addFrisc("\tCMP R0, 0");
                addFrisc(String.format("\tJR_EQ UNNEG_%d", unneg));
                addFrisc("\tMOVE 0, R0");
                addFrisc(String.format("\tJR_EQ UNNEG_%d", unneg + 1));
                addFrisc(String.format("UNNEG_%d", unneg++));
                addFrisc("\tMOVE 1, R0");
                addFrisc(String.format("UNNEG_%d", unneg++));
            }
        }
        addFrisc("\tPUSH R0");

        node.addProp("tip", new IntType());
        node.addProp("l-izraz", false);
    }

    private static void cast_izraz1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode unarni_izraz = (NonterminalNode) node.children.get(0);
        check(unarni_izraz, variableTable, functionTable);
        node.addProp("tip", unarni_izraz.props.get("tip"));
        node.addProp("l-izraz", unarni_izraz.props.get("l-izraz"));
    }

    private static void cast_izraz2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode ime_tipa = (NonterminalNode) node.children.get(1);
        NonterminalNode cast_izraz = (NonterminalNode) node.children.get(3);
        check(ime_tipa, variableTable, functionTable);
        check(cast_izraz, variableTable, functionTable);
        if(!((Type) cast_izraz.props.get("tip")).explicitCastsInto((Type) ime_tipa.props.get("tip"))) {
            error(node);
        }
        node.addProp("tip", ime_tipa.props.get("tip"));
        node.addProp("l-izraz", false);
    }

    private static void ime_tipa1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode specifikator_tipa = (NonterminalNode) node.children.get(0);
        check(specifikator_tipa, variableTable, functionTable);
        node.addProp("tip", specifikator_tipa.props.get("tip"));
    }

    private static void ime_tipa2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode specifikator_tipa = (NonterminalNode) node.children.get(1);
        check(specifikator_tipa, variableTable, functionTable);
        Type tip = (Type) specifikator_tipa.props.get("tip");
        if(tip instanceof CharType) {
            CharType charType = (CharType) tip;
            charType.isConst = true;
            node.addProp("tip", charType);
        } else if(tip instanceof IntType) {
            IntType intType = (IntType) tip;
            intType.isConst = true;
            node.addProp("tip", intType);
        } else {
            error(node);
        }
    }

    private static void specifikator_tipa(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        switch (((TerminalNode) node.children.get(0)).token) {
            case "KR_VOID":
                node.props.put("tip", new VoidType());
                break;
            case "KR_CHAR":
                node.props.put("tip", new CharType());
                break;
            case "KR_INT":
                node.props.put("tip", new IntType());
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private static void op_izraz1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode op = (NonterminalNode) node.children.get(0);
        check(op, variableTable, functionTable);
        node.addProp("tip", op.props.get("tip"));
        node.addProp("l-izraz", op.props.get("l-izraz"));
    }

    private static void op_izraz2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode op1 = (NonterminalNode) node.children.get(0);
        TerminalNode operator = (TerminalNode) node.children.get(1);
        NonterminalNode op2 = (NonterminalNode) node.children.get(2);
        boolean logicalExp = operator.token.equals("OP_I") || operator.token.equals("OP_ILI");
        check(op1, variableTable, functionTable);
        if(!((Type) op1.props.get("tip")).implicitCastsInto(new IntType())) {
            error(node);
        }

        if(logicalExp){
            int x = sc++;
            addFrisc("\tPOP R0");
            currentFunction.stackSize--;
            addFrisc("\tCMP R0, 0");
            addFrisc(String.format("\tJR_%s SC_%d", operator.token.equals("OP_I") ? "EQ" : "NE", x));
            check(op2, variableTable, functionTable);
            addFrisc("\tPOP R0");
            currentFunction.stackSize--;
            addFrisc("SC_" + x);
            addFrisc("\tPUSH R0");
            currentFunction.stackSize++;
        }

        if(!((Type) op2.props.get("tip")).implicitCastsInto(new IntType())) {
            error(node);
        }

        if(!logicalExp){
            check(op2, variableTable, functionTable);
            addFrisc("\tPOP R1");
            addFrisc("\tPOP R0");
            switch(operator.token){
                case "PLUS" -> {
                    addFrisc("\tADD R0, R1, R6");
                }
                case "MINUS" -> {
                    addFrisc("\tSUB R0, R1, R6");
                }
                case "OP_PUTA" -> {
                    addFrisc("\tPUSH R0");
                    addFrisc("\tPUSH R1");
                    addFrisc("\tCALL MUL");
                    addFrisc("\tADD R7, 8, R7");
                }
                case "OP_DIJELI" -> {
                    addFrisc("\tPUSH R0");
                    addFrisc("\tPUSH R1");
                    addFrisc("\tCALL DIV");
                    addFrisc("\tADD R7, 8, R7");
                }
                case "OP_MOD" -> {
                    addFrisc("\tPUSH R0");
                    addFrisc("\tPUSH R1");
                    addFrisc("\tCALL MOD");
                    addFrisc("\tADD R7, 8, R7");
                }
                case "OP_LT" -> {
                    addFrisc("\tCMP R0, R1");
                    addFrisc("\tJR_SLT COMP_" + comp);
                    addFrisc("\tMOVE 0, R6");
                    addFrisc("\tJR COMP_" + (comp + 1));
                    addFrisc("COMP_" + comp++);
                    addFrisc("\tMOVE 1, R6");
                    addFrisc("COMP_" + comp++);
                }
                case "OP_GT" -> {
                    addFrisc("\tCMP R0, R1");
                    addFrisc("\tJR_SGT COMP_" + comp);
                    addFrisc("\tMOVE 0, R6");
                    addFrisc("\tJR COMP_" + (comp + 1));
                    addFrisc("COMP_" + comp++);
                    addFrisc("\tMOVE 1, R6");
                    addFrisc("COMP_" + comp++);
                }
                case "OP_LTE" -> {
                    addFrisc("\tCMP R0, R1");
                    addFrisc("\tJR_SLE COMP_" + comp);
                    addFrisc("\tMOVE 0, R6");
                    addFrisc("\tJR COMP_" + (comp + 1));
                    addFrisc("COMP_" + comp++);
                    addFrisc("\tMOVE 1, R6");
                    addFrisc("COMP_" + comp++);
                }
                case "OP_GTE" -> {
                    addFrisc("\tCMP R0, R1");
                    addFrisc("\tJR_SGE COMP_" + comp);
                    addFrisc("\tMOVE 0, R6");
                    addFrisc("\tJR COMP_" + (comp + 1));
                    addFrisc("COMP_" + comp++);
                    addFrisc("\tMOVE 1, R6");
                    addFrisc("COMP_" + comp++);
                }
                case "OP_EQ" -> {
                    addFrisc("\tCMP R0, R1");
                    addFrisc("\tJR_EQ COMP_" + comp);
                    addFrisc("\tMOVE 0, R6");
                    addFrisc("\tJR COMP_" + (comp + 1));
                    addFrisc("COMP_" + comp++);
                    addFrisc("\tMOVE 1, R6");
                    addFrisc("COMP_" + comp++);
                }
                case "OP_NEQ" -> {
                    addFrisc("\tCMP R0, R1");
                    addFrisc("\tJR_NE COMP_" + comp);
                    addFrisc("\tMOVE 0, R6");
                    addFrisc("\tJR COMP_" + (comp + 1));
                    addFrisc("COMP_" + comp++);
                    addFrisc("\tMOVE 1, R6");
                    addFrisc("COMP_" + comp++);
                }
                case "OP_BIN_I" -> {
                    addFrisc("\tAND R0, R1, R6");
                }
                case "OP_BIN_XILI" -> {
                    addFrisc("\tXOR R0, R1, R6");
                }
                case "OP_BIN_ILI" -> {
                    addFrisc("\tOR R0, R1, R6");
                }
            }
            addFrisc("\tPUSH R6");
            if(currentFunction != null) currentFunction.stackSize--;
        }


        node.addProp("tip", new IntType());
        node.addProp("l-izraz", false);
    }

    private static void izraz_pridruzivanja1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode log_ili_izraz = (NonterminalNode) node.children.get(0);
        check(log_ili_izraz, variableTable, functionTable);
        node.addProp("tip", log_ili_izraz.props.get("tip"));
        node.addProp("l-izraz", log_ili_izraz.props.get("l-izraz"));
        if(currentFunction != null) {
            currentFunction.generatedCode.add("\tPOP R0");
            currentFunction.stackSize--;
        }
    }

    private static void izraz_pridruzivanja2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
        postfiks_izraz.addProp("pointer", true);
        NonterminalNode izraz_pridruzivanja = (NonterminalNode) node.children.get(2);
        check(postfiks_izraz, variableTable, functionTable);
        if(!(boolean) postfiks_izraz.props.get("l-izraz")) {
            error(node);
        }
        check(izraz_pridruzivanja, variableTable, functionTable);
        if(!((Type) izraz_pridruzivanja.props.get("tip")).implicitCastsInto((Type) postfiks_izraz.props.get("tip"))) {
            error(node);
        }

        addFrisc("\tPOP R0");
        if(currentFunction != null) currentFunction.stackSize--;
        addFrisc("\tPOP R1");
        if(currentFunction != null) currentFunction.stackSize--;
        addFrisc("\tSTORE R0, (R1)");
        addFrisc("\tPUSH R0");
        if(currentFunction != null) currentFunction.stackSize--;

        node.addProp("tip", postfiks_izraz.props.get("tip"));
        node.addProp("l-izraz", false);
    }

    private static void izraz1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode izraz_pridruzivanja = (NonterminalNode) node.children.get(0);
        check(izraz_pridruzivanja, variableTable, functionTable);
        node.addProp("tip", izraz_pridruzivanja.props.get("tip"));
        node.addProp("l-izraz", izraz_pridruzivanja.props.get("l-izraz"));
    }

    private static void izraz2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode izraz = (NonterminalNode) node.children.get(0);
        NonterminalNode izraz_pridruzivanja = (NonterminalNode) node.children.get(2);
        check(izraz, variableTable, functionTable);
        check(izraz_pridruzivanja, variableTable, functionTable);
        node.addProp("tip", izraz_pridruzivanja.props.get("tip"));
        node.addProp("l-izraz", false);
    }

    private static void lista_izraza_pridruzivanja1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        NonterminalNode izraz_pridruzivanja = (NonterminalNode) node.children.get(0);
        check(izraz_pridruzivanja, variableTable, functionTable);
        List<Type> tipovi = new ArrayList<>();
        tipovi.add((Type) izraz_pridruzivanja.props.get("tip"));
        node.props.put("tipovi", tipovi);
        node.addProp("br-elem", 1);
    }

    private static void lista_izraza_pridruzivanja2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        NonterminalNode lista_izraza_pridruzivanja = (NonterminalNode) node.children.get(0);
        NonterminalNode izraz_pridruzivanja = (NonterminalNode) node.children.get(2);
        check(izraz_pridruzivanja, variableTable, functionTable);
        check(lista_izraza_pridruzivanja, variableTable, functionTable);
        List<Type> tipovi = new ArrayList<>((List<Type>) lista_izraza_pridruzivanja.props.get("tipovi"));
        tipovi.add((Type) izraz_pridruzivanja.props.get("tip"));
        node.props.put("tipovi", tipovi);
        node.addProp("br-elem", (int) lista_izraza_pridruzivanja.props.get("br-elem") + 1);
    }

    private static void inicijalizator1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode izraz_pridruzivanja = (NonterminalNode) node.children.get(0);
        check(izraz_pridruzivanja, variableTable, functionTable);
        NonterminalNode i = izraz_pridruzivanja;
        boolean isString = false;
        while (i.children.size()==1 && i.children.get(0) instanceof NonterminalNode) {
            i = (NonterminalNode) i.children.get(0);
        }
        if(i.children.size()==1
                && i.children.get(0) instanceof TerminalNode
                && ((TerminalNode) i.children.get(0)).token.equals("NIZ_ZNAKOVA")) {
            isString = true;
        }
        if(isString) {
            int br_elem = ((TerminalNode) i.children.get(0)).value.length();
            node.addProp("br-elem", br_elem);
            List<Type> tipovi = new ArrayList<>();
            for(int j=0; j<br_elem; j++) {
                tipovi.add(new CharType());
            }
            node.addProp("tipovi", tipovi);
        } else {
            node.addProp("tip", izraz_pridruzivanja.props.get("tip"));
        }
    }

    private static void inicijalizator2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode lista_izraza_pridruzivanja = (NonterminalNode) node.children.get(1);
        check(lista_izraza_pridruzivanja, variableTable, functionTable);
        node.addProp("br-elem", lista_izraza_pridruzivanja.props.get("br-elem"));
        node.addProp("tipovi", lista_izraza_pridruzivanja.props.get("tipovi"));
    }

    private static void izravni_deklarator1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        Type ntip = (Type) node.props.get("ntip");
        if(ntip.equals(new VoidType())) {
            error(node);
        }
        TerminalNode IDN = (TerminalNode) node.children.get(0);
        if(isAlreadyDeclared(IDN.value, variableTable, functionTable)) {
            error(node);
        }
        variableTable.variables.put(IDN.value, new VariableTable.VariableEntry(ntip, currentFunction == null ? 0 : currentFunction.stackSize * 4 ));
        //GORE IPAK NE TREBA ++ JER CE SE STACK SIZE INKREMENTIRAT KOD PUSHA
        //OVDJE UBACI FRISC KOD
        if(currentFunction == null) {
            memoryEntries.put("V_" + IDN.value.toUpperCase(), "DW 0");
            globalCode.add("\tMOVE V_" + IDN.value.toUpperCase() + ", R0");
            globalCode.add("\tPUSH R0");
        } else {
            currentFunction.generatedCode.add("\tSUB R7, 4, R7");
            currentFunction.stackSize++;
            currentFunction.generatedCode.add("\tPUSH R7");
            currentFunction.stackSize++;
        }
        node.addProp("tip", ntip);
    }

    private static boolean isAlreadyDeclared(String idn, VariableTable variableTable, FunctionTable functionTable) {
        return variableTable.isAlreadyDeclared(idn) || functionTable.isAlreadyDeclared(idn);
    }

    private static void izravni_deklarator2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        Type ntip = (Type) node.props.get("ntip");
        if(ntip.equals(new VoidType())) {
            error(node);
        }
        TerminalNode IDN = (TerminalNode) node.children.get(0);
        TerminalNode BROJ = (TerminalNode) node.children.get(2);
        int br_elem= Integer.parseInt(BROJ.value);
        if(isAlreadyDeclared(IDN.value, variableTable, functionTable)) {
            error(node);
        }
        if (br_elem <= 0 || br_elem > 1024) {
            error(node);
        }
        //OVDJE UBACI FRISC KOD, BITNO DA BI STACK INDEX U PROSLOJ LINIJI POKAZIVAO NA PRVI ELEMENT ARRAYA
        if(currentFunction == null) {
            memoryEntries.put("V_" + IDN.value.toUpperCase(), "DW 0" + ", 0".repeat(br_elem-1));
            globalCode.add("\tMOVE V_" + IDN.value.toUpperCase() + ",R0");
            globalCode.add("\tPUSH R0");
        } else {
            currentFunction.generatedCode.add("\tMOVE %D " + BROJ.value + ",R0");
            currentFunction.generatedCode.add("\tSHL R0, 2, R0");
            currentFunction.generatedCode.add("\tSUB R7, R0, R7");
            currentFunction.stackSize+=br_elem;
        }
        variableTable.variables.put(IDN.value, new VariableTable.VariableEntry(new ArrayType(ntip), currentFunction.stackSize * 4 ));
        node.addProp("tip", new ArrayType(ntip));
        node.addProp("br-elem", br_elem);
    }

    private static void izravni_deklarator3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        Type ntip = (Type) node.props.get("ntip");
        TerminalNode IDN = (TerminalNode) node.children.get(0);
        FunctionType funkcija = new FunctionType(ntip, new VoidType());
        if(isAlreadyDeclared(IDN.value, variableTable, functionTable)) {
            if(!functionTable.functions.get(IDN.value).equals(funkcija)) {
                error(node);
            }
        } else {
            functionTable.functions.put(IDN.value, new FunctionTable.FunctionEntry(funkcija));
        }
        node.addProp("tip", funkcija);
    }

    private static void izravni_deklarator4(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        Type ntip = (Type) node.props.get("ntip");
        TerminalNode IDN = (TerminalNode) node.children.get(0);
        NonterminalNode lista_parametara = (NonterminalNode) node.children.get(2);
        check(lista_parametara, variableTable, functionTable);
        FunctionType funkcija = new FunctionType(ntip, ((List<Type>) lista_parametara.props.get("tipovi")).toArray(new Type[0]));
        if(isAlreadyDeclared(IDN.value, variableTable, functionTable)) {
            if(!functionTable.functions.get(IDN.value).equals(funkcija)) {
                error(node);
            }
        } else {
            functionTable.functions.put(IDN.value, new FunctionTable.FunctionEntry(funkcija));
        }
        node.addProp("tip", funkcija);
    }

    private static boolean isChar(String text) {
        return text.length() == 3 ||
                text.equals("'\\t'") ||
                text.equals("'\\n'") ||
                text.equals("'\\0'") ||
                text.equals("'\\''") ||
                text.equals("'\\\"'") ||
                text.equals("'\\\\'");
    }

    private static boolean isValidString(String string) {
        for(int i = 0; i<string.length(); i++){
            if(string.charAt(i) == '\\') {
                if((i==string.length()-1) || !(string.charAt(i+1)=='t' ||
                        string.charAt(i+1)=='n' ||
                        string.charAt(i+1)=='0' ||
                        string.charAt(i+1)=='\'' ||
                        string.charAt(i+1)=='"' ||
                        string.charAt(i+1)=='\\' )) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isNonConstantNumerical(Type type) {
        if(type instanceof CharType) {
            CharType chart = (CharType) type;
            return !chart.isConst;
        } else if(type instanceof IntType) {
            IntType intt = (IntType) type;
            return !intt.isConst;
        }
        return false;
    }

    private static boolean isConstantNumerical(Type type) {
        if(type instanceof CharType) {
            CharType chart = (CharType) type;
            return chart.isConst;
        } else if(type instanceof IntType) {
            IntType intt = (IntType) type;
            return intt.isConst;
        }
        return false;
    }

    private static Type getType(FunctionTable functionTable, VariableTable variableTable, String idn){
        while (variableTable != null) {
            if(variableTable.variables.containsKey(idn)) return variableTable.variables.get(idn).type;
            if(functionTable.functions.containsKey(idn)) return functionTable.functions.get(idn).type;
            variableTable = variableTable.parentTable;
            functionTable = functionTable.parentTable;
        }
        return null;
    }

    private static Pair<Object, Boolean> getEntry(FunctionTable functionTable, VariableTable variableTable, String idn){
        while (variableTable != null) {
            if(variableTable.variables.containsKey(idn)) return new Pair<>(variableTable.variables.get(idn), variableTable.parentTable == null);
            if(functionTable.functions.containsKey(idn)) return new Pair<>(functionTable.functions.get(idn), functionTable.parentTable == null);
            variableTable = variableTable.parentTable;
            functionTable = functionTable.parentTable;
        }
        return null;
    }

    private static void addFrisc(String code) {
        if(currentFunction == null) {
            globalCode.add(code);
        } else {
            currentFunction.generatedCode.add(code);
        }
    }

    private static class Pair<T,U> {
        public T first;
        public U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }
}
