package hr.fer.ppj.maniacs414.lab3.rules;

import hr.fer.ppj.maniacs414.lab3.parser.Node;
import hr.fer.ppj.maniacs414.lab3.parser.NonterminalNode;
import hr.fer.ppj.maniacs414.lab3.parser.TerminalNode;
import hr.fer.ppj.maniacs414.lab3.table.FunctionTable;
import hr.fer.ppj.maniacs414.lab3.table.VariableTable;
import hr.fer.ppj.maniacs414.lab3.types.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Rules {
    public static void check(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        if(node.name.equals("blabla")){
            //poziv funkcije
        }

        else if(node.name.equals("blabla2")){
            //poziv funkcije
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
    }

    private static void example_example1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        return;
    }
    private static void primarni_izraz1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable){
        TerminalNode IDN = (TerminalNode) node.children.get(0);
        Type variable = variableTable.getType((String) IDN.token);
        if(variable == null) error(node);
        node.props.put("tip", variable);
        node.props.put("l-izraz", !isConstantNumerical(variable));
    }

    private static void primarni_izraz2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        TerminalNode BROJ = (TerminalNode) node.children.get(0);
        if(new BigInteger(BROJ.value).compareTo(BigInteger.valueOf(2147483647)) > 0 ||
            new BigInteger(BROJ.value).compareTo(BigInteger.valueOf(-2147483648)) < 0) {
            error(node);
        }
        node.props.put("tip", new IntType());
        node.props.put("l-izraz", false);
    }

    private static void primarni_izraz3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        TerminalNode ZNAK = (TerminalNode) node.children.get(0);
        if(!isChar(ZNAK.value)) {
            error(node);
        }
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
            node.props.put("tip", primarni_izraz.props.get("tip"));
            node.props.put("l-izraz", primarni_izraz.props.get("l-izraz"));
        } else if(node.children.size() == 4) {
            if(((TerminalNode) node.children.get(1)).token.equals("L_UGL_ZAGRADA")) {
                NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
                NonterminalNode izraz = (NonterminalNode) node.children.get(0);
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
                node.props.put("tip", arrt.elementType);
                node.props.put("l-izraz", !isConstantNumerical(arrt.elementType));
            }
            else {
                NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
                NonterminalNode lista_argumenata = (NonterminalNode) node.children.get(0);
                check(postfiks_izraz, variableTable, functionTable);
                check(lista_argumenata, variableTable, functionTable);
                if(!(postfiks_izraz.props.get("tip") instanceof FunctionType funkcija)) {
                    error(node);
                    throw new IllegalArgumentException();
                }
                List<Type> tipovi = (List<Type>) lista_argumenata.props.get("tipovi");
                if(tipovi.size() != funkcija.paramTypes.size()) {
                    error(node);
                }
                for(int i=0; i<tipovi.size(); i++) {
                    if(!tipovi.get(i).implicitCastsInto(funkcija.paramTypes.get(i))) {
                        error(node);
                    }
                }
                node.props.put("tip", funkcija.returnType);
                node.props.put("l-izraz", false);
            }
        } else if(node.children.size() == 3) {
            NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
            check(postfiks_izraz, variableTable, functionTable);
            if(!(postfiks_izraz.props.get("tip") instanceof FunctionType funkcija)) {
                error(node);
                throw new IllegalArgumentException();
            }
            node.props.put("tip", funkcija.returnType);
            node.props.put("l-izraz", false);
        } else if(node.children.size() == 2) {
            NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
            check(postfiks_izraz, variableTable, functionTable);
            if(!((boolean) postfiks_izraz.props.get("l-izraz")) ||
                !((Type) postfiks_izraz.props.get("tip")).implicitCastsInto(new IntType())) {
                error(node);
            }
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
        check(postfiks_izraz, variableTable, functionTable);
        node.addProp("tip", postfiks_izraz.props.get("tip"));
        node.addProp("l-izraz", postfiks_izraz.props.get("l-izraz"));
    }

    private static void unarni_izraz2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode unarni_izraz = (NonterminalNode) node.children.get(1);
        check(unarni_izraz, variableTable, functionTable);
        if(!((boolean) unarni_izraz.props.get("l-izraz")) ||
                ((Type) unarni_izraz.props.get("tip")).implicitCastsInto(new IntType())) {
            error(node);
        }
        node.addProp("tip", new IntType());
        node.addProp("l-izraz", false);
    }

    private static void unarni_izraz3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode unarni_operator= (NonterminalNode) node.children.get(0);
        NonterminalNode cast_izraz = (NonterminalNode) node.children.get(1);
        check(cast_izraz, variableTable, functionTable);
        if(!((Type) cast_izraz.props.get("tip")).implicitCastsInto(new IntType())) {
            error(node);
        }
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
        ((Type) cast_izraz.props.get("tip")).explicitCastsInto((Type) ime_tipa.props.get("tip"));
        node.addProp("tip", ime_tipa.props.get("tip"));
        node.addProp("l-izraz", 0);
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
        if(tip instanceof CharType charType) {
            charType.isConst = true;
            node.addProp("tip", charType);
        } else if(tip instanceof IntType intType) {
            intType.isConst = true;
            node.addProp("tip", intType);
        } else {
            error(node);
        }
    }

    private static void specifikator_tipa(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        node.props.put("tip", switch (((TerminalNode) node.children.get(0)).token) {
            case "KR_VOID" -> new VoidType();
            case "KR_CHAR" -> new CharType();
            case "KR_INT" -> new IntType();
        });
    }

    private static void op_izraz1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode op = (NonterminalNode) node.children.get(0);
        check(op, variableTable, functionTable);
        node.addProp("tip", op.props.get("tip"));
        node.addProp("l-izraz", op.props.get("l-izraz"));
    }

    private static void op_izraz2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode op1 = (NonterminalNode) node.children.get(0);
        NonterminalNode op2 = (NonterminalNode) node.children.get(2);
        check(op1, variableTable, functionTable);
        if(!((Type) op1.props.get("tip")).implicitCastsInto(new IntType())) {
            error(node);
        }
        check(op2, variableTable, functionTable);
        if(!((Type) op2.props.get("tip")).implicitCastsInto(new IntType())) {
            error(node);
        }
        node.addProp("tip", new IntType());
        node.addProp("l-izraz", false);
    }

    private static void izraz_pridruzivanja1(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode log_ili_izraz = (NonterminalNode) node.children.get(0);
        check(log_ili_izraz, variableTable, functionTable);
        node.addProp("tip", log_ili_izraz.props.get("tip"));
        node.addProp("l-izraz", log_ili_izraz.props.get("l-izraz"));
    }

    private static void izraz_pridruzivanja2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        NonterminalNode postfiks_izraz = (NonterminalNode) node.children.get(0);
        NonterminalNode izraz_pridruzivanja = (NonterminalNode) node.children.get(2);
        check(postfiks_izraz, variableTable, functionTable);
        if((boolean) postfiks_izraz.props.get("l-izraz")) {
            error(node);
        }
        if(!((Type) izraz_pridruzivanja.props.get("tip")).implicitCastsInto((Type) postfiks_izraz.props.get("tip"))) {
            error(node);
        }
        check(izraz_pridruzivanja, variableTable, functionTable);
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
        check(lista_izraza_pridruzivanja, variableTable, functionTable);
        check(izraz_pridruzivanja, variableTable, functionTable);
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
                && ((TerminalNode) i.children.get(0)).token.equals("NIZ_ZNAKOvA")) {
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
        if(variableTable.isAlreadyDeclared(IDN.value)) {
            error(node);
        }
        variableTable.variables.put(IDN.value, ntip);
        node.addProp("tip", ntip);
    }

    private static void izravni_deklarator2(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        Type ntip = (Type) node.props.get("ntip");
        if(ntip.equals(new VoidType())) {
            error(node);
        }
        TerminalNode IDN = (TerminalNode) node.children.get(0);
        TerminalNode BROJ = (TerminalNode) node.children.get(2);
        int br_elem= Integer.parseInt(BROJ.value);
        if(variableTable.isAlreadyDeclared(IDN.value)) {
            error(node);
        }
        if (br_elem <= 0 || br_elem > 1024) {
            error(node);
        }
        variableTable.variables.put(IDN.value, new ArrayType(ntip));
        node.addProp("tip", new ArrayType(ntip));
        node.addProp("br-elem", br_elem);
    }

    private static void izravni_deklarator3(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        Type ntip = (Type) node.props.get("ntip");
        TerminalNode IDN = (TerminalNode) node.children.get(0);
        FunctionType funkcija = new FunctionType(ntip, new VoidType());
        if(functionTable.isAlreadyDeclared(IDN.value)) {
            if(!functionTable.functions.get(IDN.value).equals(funkcija)) {
                error(node);
            }
        } else {
            functionTable.functions.put(IDN.value, funkcija);
        }
        node.addProp("tip", funkcija);
    }

    private static void izravni_deklarator4(NonterminalNode node, VariableTable variableTable, FunctionTable functionTable) {
        Type ntip = (Type) node.props.get("ntip");
        TerminalNode IDN = (TerminalNode) node.children.get(0);
        NonterminalNode lista_parametara = (NonterminalNode) node.children.get(2);
        check(lista_parametara, variableTable, functionTable);
        FunctionType funkcija = new FunctionType(ntip, ((List<Type>) lista_parametara.props.get("tipovi")).toArray(new Type[0]));
        if(functionTable.isAlreadyDeclared(IDN.value)) {
            if(!functionTable.functions.get(IDN.value).equals(funkcija)) {
                error(node);
            }
        } else {
            functionTable.functions.put(IDN.value, funkcija);
        }
        node.addProp("tip", funkcija);
    }

    private static void error(Node node) {
        System.out.println(node);
        System.exit(0);
    }

    private static boolean isChar(String text) {
        return text.length() == 1 ||
                text.equals("\\t") ||
                text.equals("\\n") ||
                text.equals("\\0") ||
                text.equals("\\'") ||
                text.equals("\\\"") ||
                text.equals("\\\\");
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

    private static boolean isConstantNumerical(Type type) {
        if(type instanceof CharType chart) {
            return chart.isConst;
        } else if(type instanceof IntType intt) {
            return intt.isConst;
        }
        return false;
    }
}
