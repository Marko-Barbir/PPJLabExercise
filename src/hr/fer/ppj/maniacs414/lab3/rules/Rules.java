package hr.fer.ppj.maniacs414.lab3.rules;

import hr.fer.ppj.maniacs414.lab3.parser.NonterminalNode;
import hr.fer.ppj.maniacs414.lab3.parser.TerminalNode;
import hr.fer.ppj.maniacs414.lab3.table.FunctionTable;
import hr.fer.ppj.maniacs414.lab3.table.VariableTable;
import hr.fer.ppj.maniacs414.lab3.types.*;

import java.util.ArrayList;
import java.util.List;

public class Rules {
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
    }

    private static void error(NonterminalNode node){
        System.out.println(node.getProduction());
        System.exit(1);
    }

    private static void checkImplicitCast(NonterminalNode node, int childIndex, Type type){
        if(!((Type)node.children.get(childIndex).props.get("tip")).implicitCastsInto(type)){
            error(node);
        }
    }

    private static void checkIfInside(String name, NonterminalNode node){
        for(NonterminalNode n = (NonterminalNode) node.parent; n != null; n = (NonterminalNode) n.parent){
            if(n.name.equals(name)) return;
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
            table = table.parentTable;
            if(table.isAlreadyDeclared(name) && table.getFunction(name).isDefined()){
                error(node);
            }
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
        check((NonterminalNode) node.children.get(1), variableTable, functionTable);
    }

    private static void slozena_naredba2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(1), variableTable, functionTable);
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
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
    }

    private static void izraz_naredba2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        node.addProp("tip", node.children.get(0).props.get("tip"));
    }
    private static void naredba_grananja1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        checkImplicitCast(node, 2, new IntType());
        check((NonterminalNode) node.children.get(4), variableTable, functionTable);
    }

    private static void naredba_grananja2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        naredba_grananja1(node, variableTable, functionTable);
        check((NonterminalNode) node.children.get(6), variableTable, functionTable);
    }

    private static void naredba_petlje1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        naredba_grananja1(node, variableTable, functionTable);
    }

    private static void naredba_petlje2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        check((NonterminalNode) node.children.get(3), variableTable, functionTable);
        checkImplicitCast(node, 3, new IntType());
        check((NonterminalNode) node.children.get(5), variableTable, functionTable);
    }

    private static void naredba_petlje3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        check((NonterminalNode) node.children.get(3), variableTable, functionTable);
        checkImplicitCast(node, 3, new IntType());
        check((NonterminalNode) node.children.get(4), variableTable, functionTable);
        check((NonterminalNode) node.children.get(6), variableTable, functionTable);
    }

    private static void naredba_skoka1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        checkIfInside("naredba_petlje", node);
    }

    private static void naredba_skoka2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        checkIfInsideFunctionVoid(node);
    }

    private static void naredba_skoka3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(1), variableTable, functionTable);
        checkIfInsideFunctionNonVoid((Type) node.children.get(1).props.get("tip"), node);
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
        String functionName = (String)node.children.get(1).props.get("ime");
        Type returnType = (Type)node.children.get(0).props.get("tip");
        checkIfFunctionAlreadyDefined(functionName, functionTable, node);
        FunctionTable globalScope = functionTable.getGlobalScope();
        FunctionType newFunction = new FunctionType(returnType, new VoidType());
        if(globalScope.isAlreadyDeclared(functionName) && !globalScope.getFunction(functionName).equals(newFunction)){
            error(node);
        }
        newFunction.isDefined = true;
        functionTable.functions.put(functionName, newFunction);
        check((NonterminalNode) node.children.get(5), new VariableTable(variableTable), new FunctionTable(functionTable));
    }

    private static void definicija_funkcije2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        if (node.children.get(0).props.get("tip").equals(new CharType(true)) || node.children.get(0).props.get("tip").equals(new IntType(true))){
            error(node);
        }
        String functionName = (String)node.children.get(1).props.get("ime");
        Type returnType = (Type)node.children.get(0).props.get("tip");
        checkIfFunctionAlreadyDefined(functionName, functionTable, node);
        check((NonterminalNode) node.children.get(3), variableTable, functionTable);
        FunctionTable globalScope = functionTable.getGlobalScope();
        List<Type> paramTypes = (List<Type>) node.children.get(3).props.get("tipovi");
        List<String> names = (List<String>) node.children.get(3).props.get("imena");
        FunctionType newFunction = new FunctionType(returnType, paramTypes);
        if(globalScope.isAlreadyDeclared(functionName) && !globalScope.getFunction(functionName).equals(newFunction)){
            error(node);
        }
        newFunction.isDefined = true;
        functionTable.functions.put(functionName, newFunction);
        VariableTable newVariableScope = new VariableTable(variableTable);
        for (int i = 0; i < paramTypes.size(); i++){
            newVariableScope.variables.put((String)names.get(i), (Type)paramTypes);
        }
        check((NonterminalNode) node.children.get(5), newVariableScope, new FunctionTable(functionTable));
    }

    private static void lista_parametara1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        node.addProp("tipovi", new ArrayList<>(List.of((Type)node.children.get(0).props.get("tip"))));
        node.addProp("imena", new ArrayList<>(List.of((String)node.children.get(0).props.get("ime"))));
    }

    private static void lista_parametara2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
        if(((List<String>)node.children.get(0).props.get("imena")).contains((String)node.children.get(2).props.get("ime"))) {
            error(node);
        }
    }

    private static void deklaracija_parametra1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        Type type = (Type)node.children.get(0).props.get("tip");
        if(type.equals(new VoidType())){
            error(node);
        }
        node.addProp("tip", type);
        node.addProp("ime", (String)node.children.get(1).props.get("ime"));
    }

    private static void deklaracija_parametra2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        Type type = (Type)node.children.get(0).props.get("tip");
        if(type.equals(new VoidType())){
            error(node);
        }
        node.addProp("tip", new ArrayType(type));
        node.addProp("ime", (String)node.children.get(1).props.get("ime"));
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
    }

    private static void init_deklarator2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        node.children.get(0).addProp("ntip", (Type)node.props.get("ntip"));
        check((NonterminalNode) node.children.get(0), variableTable, functionTable);
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);
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
    }

    private static void izravni_deklarator1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        Type type = (Type)node.props.get("ntip");
        String name = (String)node.children.get(0).props.get("ime");
        if(type instanceof VoidType){
            error(node);
        }
        if(variableTable.isAlreadyDeclared(name)){
            error(node);
        }
        variableTable.variables.put(name, type);
        node.addProp("tip", type);
    }

    private static void izravni_deklarator2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        Type type = (Type)node.props.get("ntip");
        String name = (String)node.children.get(0).props.get("ime");
        Integer value = (Integer)node.children.get(2).props.get("vrijednost");
        if(type instanceof VoidType){
            error(node);
        }
        if(variableTable.isAlreadyDeclared(name)){
            error(node);
        }
        if(value <= 0 || value > 1024){
            error(node);
        }
        variableTable.variables.put(name, type);
        node.addProp("tip", new ArrayType(type));
        node.addProp("br-elem", value);
    }

    private static void izravni_deklarator3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        Type type = (Type)node.props.get("ntip");
        String name = (String)node.children.get(0).props.get("ime");
        FunctionType newFunction = new FunctionType(type, new VoidType());
        if(functionTable.isAlreadyDeclared(name)){
            if(!functionTable.getFunction(name).equals(newFunction)){
                error(node);
            }
        }
        else {
            functionTable.functions.put(name, newFunction);
        }
        node.addProp("tip", newFunction);
    }

    private static void izravni_deklarator4(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        check((NonterminalNode) node.children.get(2), variableTable, functionTable);

        Type returnType = (Type)node.props.get("ntip");
        String name = (String)node.children.get(0).props.get("ime");
        List<Type> paramTypes = (List<Type>)node.children.get(2).props.get("tipovi");
        FunctionType newFunction = new FunctionType(returnType, paramTypes);
        if(functionTable.isAlreadyDeclared(name)){
            if(!functionTable.getFunction(name).equals(newFunction)){
                error(node);
            }
        }
        else {
            functionTable.functions.put(name, newFunction);
        }
        node.addProp("tip", newFunction);
    }


}
