package hr.fer.ppj.maniacs414.lab2;

import hr.fer.ppj.maniacs414.lab2.analizator.DKA;
import hr.fer.ppj.maniacs414.lab2.analizator.ENKA;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GSA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        String line = null;

        String startState = null;
        LinkedHashSet<String> nonterminal = null;
        LinkedHashSet<String> terminal = null;
        LinkedHashSet<String> sync = null;
        try {
            nonterminal = new LinkedHashSet<>(List.of(scanner.nextLine().split(" ", 2)[1].split(" ")));
            startState = nonterminal.stream().findFirst().get();

            terminal = new LinkedHashSet<>(List.of(scanner.nextLine().split(" ", 2)[1].split(" ")));
            sync = new LinkedHashSet<>(List.of(scanner.nextLine().split(" ", 2)[1].split(" ")));
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("Invalid input syntax.");
            System.exit(-1);
        }

        LinkedHashMap<String, List<List<String>>> grammar = new LinkedHashMap<>();
        String left = null;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if(!line.startsWith(" ")) {
                left = line.strip();
            } else {
                line = line.strip();
                List<String> right;
                if(line.equals("$")) {
                    right = new ArrayList<>();
                } else {
                    right = List.of(line.split(" "));
                }
                if(!grammar.containsKey(left)) {
                    grammar.put(left, new ArrayList<>());
                }
                grammar.get(left).add(right);
            }
        }

        Set<String> emptyNonterminal = new HashSet<>();

        boolean changed = true;

        while (changed){
            changed = false;

            for (var entry : grammar.entrySet()) {
                if(!emptyNonterminal.contains(entry.getKey())) {
                    boolean isEmpty = false;
                    for (var production : entry.getValue()) {
                        if(!isEmpty && emptyNonterminal.containsAll(production)) {
                            changed = true;
                            isEmpty = true;
                            emptyNonterminal.add(entry.getKey());
                        }
                    }
                }
            }
        }

        HashMap<String, HashMap<String, Boolean>> zapocinjeZnakom = new HashMap<>();
        for (Map.Entry<String, List<List<String>>> entry : grammar.entrySet()) {
            zapocinjeZnakom.put(entry.getKey(), new HashMap<>());
            for(var value : entry.getValue()) {
                if(!value.isEmpty()) {
                    String firstSymbol = value.get(0);
                    zapocinjeZnakom.get(entry.getKey()).put(firstSymbol, true);
                }
            }
        }

        for(String row : Stream.concat(nonterminal.stream(), terminal.stream()).collect(Collectors.toSet())) {
            zapocinjeZnakom.computeIfAbsent(row, (x) -> new HashMap<>());
            zapocinjeZnakom.get(row).put(row, true);
        }
        changed = true;
        while (changed) {
            changed = false;

            for(String x : Stream.concat(nonterminal.stream(), terminal.stream()).collect(Collectors.toSet())) {
                for(String y : Stream.concat(nonterminal.stream(), terminal.stream()).collect(Collectors.toSet())) {
                    for(String z : Stream.concat(nonterminal.stream(), terminal.stream()).collect(Collectors.toSet())) {
                        if(zapocinjeZnakom.getOrDefault(x, new HashMap<>()).getOrDefault(y, false) &&
                            zapocinjeZnakom.getOrDefault(y, new HashMap<>()).getOrDefault(z, false)) {
                            if(!changed) {
                                changed = !(zapocinjeZnakom.getOrDefault(x, new HashMap<>()).getOrDefault(z, false));
                            }
                            zapocinjeZnakom.computeIfAbsent(x, (s) -> new HashMap<>());
                            zapocinjeZnakom.get(x).put(z, true);
                        }
                    }
                }
            }
        }

        HashMap<String, Set<String>> zapocinje = new HashMap<>();
        for(String symbol : nonterminal) {
            zapocinje.put(symbol, new HashSet<>());
            for (String term : terminal) {
                if(zapocinjeZnakom.getOrDefault(symbol, new HashMap<>()).getOrDefault(term, false)) {
                    zapocinje.get(symbol).add(term);
                }
            }
        }

        ENKA enka = new ENKA();
        enka.addEpsilonTransition("q0", "S' -> . " + startState + " {|}");

        //this is probably a bunch of bullshit, nvm it works
        changed = true;
        while (changed) {
            changed = false;

            for (String state : new HashSet<>(enka.getStates())) {
                if(state.equals("q0")) continue;

                left = getLeft(state);
                List<String> right = getRight(state);
                TreeSet<String> context = getStateContext(state);
                int dotIndex = right.indexOf(".");
                String nextSymbol = (dotIndex < right.size() - 1) ? right.get(dotIndex+1) : null;
                List<String> beta = (dotIndex < right.size() - 2) ? right.subList(dotIndex+2, right.size()) : new ArrayList<>();
                if(nextSymbol == null)
                    continue;
                else if (nonterminal.contains(nextSymbol)) {
                    List<String> nextStates = new ArrayList<>();
                    for(List<String> production : grammar.get(nextSymbol)) {
                        TreeSet<String> newContext = new TreeSet<>();
                        boolean betaIsEmpty = true;
                        for (var symbol : beta) {
                            if (nonterminal.contains(symbol)) {
                                newContext.addAll(zapocinje.get(symbol));
                                if(!emptyNonterminal.contains(symbol)) {
                                    betaIsEmpty = false;
                                    break;
                                }
                            } else if (terminal.contains(symbol)) {
                                newContext.add(symbol);
                                betaIsEmpty = false;
                                break;
                            } else throw new IllegalArgumentException("Symbol " + symbol + " for context not in grammar.");
                        }
                        if (betaIsEmpty) {
                            newContext.addAll(context);
                        }
                        String nextState = nextSymbol + " -> . " + String.join(" ", production) +
                                " {" + String.join(",", newContext) + "}";
                        nextStates.add(nextState);
                    }
                    for (String nextState : nextStates) {
                        if(!enka.getEpsilonTransition(state).contains(nextState)) {
                            changed = true;
                            enka.addEpsilonTransition(state, nextState);
                        }
                    }
                }
                String nextState = left + " -> " + String.join(" ", right.subList(0, dotIndex)) +
                        " " +  nextSymbol + " . " + String.join(" ", beta) + " {" +
                        String.join(",", context) + "}";
                if(!enka.getTransition(state, nextSymbol).contains(nextState)) {
                    changed = true;
                    enka.addTransition(state, nextSymbol, nextState);
                }
            }
        }

        DKA dka = new DKA();

        Integer curState = dka.addState();
        dka.addStateToLRItemLink(curState, epsilonClosure(Set.of("q0"), enka));
        Stack<Integer> statesForProcess = new Stack<>();
        statesForProcess.push(curState);
        while (!statesForProcess.isEmpty()) {
            curState = statesForProcess.pop();

            TreeSet<String> enkaStates = dka.getStateLRItems(curState);

            for (String symbol : Stream.concat(nonterminal.stream(), terminal.stream()).collect(Collectors.toSet())) {
                TreeSet<String> nextStates = new TreeSet<>();
                for (String state : enkaStates) {
                    if(!enka.getTransition(state, symbol).isEmpty()) {
                        nextStates.addAll(epsilonClosure(enka.getTransition(state, symbol), enka));
                    }
                }
                if(!nextStates.isEmpty()) {
                    if(!dka.getStateToLRItemMap().containsValue(nextStates)) {
                        int newState = dka.addState();
                        dka.addTransition(curState, symbol, newState);
                        dka.addStateToLRItemLink(newState, nextStates);
                        statesForProcess.push(newState);
                    } else {
                        dka.addTransition(curState, symbol,
                                dka.getStateToLRItemMap()
                                        .entrySet()
                                        .stream()
                                        .filter(entry -> nextStates.equals(entry.getValue()))
                                        .map(Map.Entry::getKey)
                                        .findFirst().orElse(null));
                    }
                }
            }
        }

        HashMap<Integer, HashMap<String, String>> action = new HashMap<>();
        HashMap<Integer, HashMap<String, Integer>> newState = new HashMap<>();

        for(int i = 0; i< dka.getStateCount();i++) {
            action.put(i, new HashMap<>());
            newState.put(i, new HashMap<>());
        }

        for (int state = 0; state<dka.getStateCount(); state++) {
            TreeSet<String> LRItems = dka.getStateLRItems(state);
            if(LRItems.stream().anyMatch(x -> !x.equals("q0") && isComplete(x))) {
                for (var entry : grammar.entrySet()) {
                    left = entry.getKey();
                    for (var production : entry.getValue()) {
                        for(var complete : LRItems.stream().filter(x -> !x.equals("q0") && isComplete(x)).collect(Collectors.toList())) {
                            if(productionMatchesLRItem(left, production, complete)) {
                                for (var input : getStateContext(complete)) {
                                    String finalLeft = left;
                                    action.get(state).computeIfAbsent(input, (x) -> "r " + finalLeft + " " + production.size());
                                }
                            }
                        }
                    }
                }
            }
            for (var entry : dka.getTransitions().entrySet()) {
                for(var transition : entry.getValue().entrySet()) {
                    if(nonterminal.contains(transition.getKey())) {
                        newState.get(entry.getKey()).put(transition.getKey(), transition.getValue());
                    } else if (terminal.contains(transition.getKey())) {
                        action.get(entry.getKey()).put(transition.getKey(), "p " + transition.getValue());
                    }
                }
            }
            String finalStartState = startState;
            String string = "S' ->  " + finalStartState + " .  {|}";
            int finishState = dka.getStateToLRItemMap().entrySet().stream()
                    .filter(entry -> entry.getValue().contains(string)).mapToInt(Map.Entry::getKey)
                    .findFirst().orElse(0);
            action.get(finishState).put("|", "pri");
        }
        for (int i = 0; i<dka.getStateCount(); i++) {
            for (String symbol : Stream.concat(terminal.stream(), Stream.of("|")).collect(Collectors.toSet())) {
                action.get(i).putIfAbsent(symbol, "o");
            }
            for (String symbol : nonterminal){
                newState.get(i).putIfAbsent(symbol, -1);
            }
        }

        try {
            FileOutputStream fileOut1 =
                    new FileOutputStream("src/hr/fer/ppj/lab2/maniacs414/analizator/newState.txt");
            ObjectOutputStream out1 = new ObjectOutputStream(fileOut1);
            out1.writeObject(newState);

            FileOutputStream fileOut2 =
                    new FileOutputStream("src/hr/fer/ppj/lab2/maniacs414/analizator/action.txt");
            ObjectOutputStream out2 = new ObjectOutputStream(fileOut2);
            out2.writeObject(action);

            FileOutputStream fileOut3 =
                    new FileOutputStream("src/hr/fer/ppj/lab2/maniacs414/analizator/syncCharacters.txt");
            ObjectOutputStream out3 = new ObjectOutputStream(fileOut3);
            out3.writeObject(sync);


            out1.close();
            out2.close();
            out3.close();
            fileOut1.close();
            fileOut2.close();
            fileOut3.close();
        } catch (IOException i) {
            i.printStackTrace();
        }

        System.out.println(action);
        System.out.println(newState);
        System.out.println();
    }

    public static TreeSet<String> getStateContext(String state) {
        String context = state.substring(state.indexOf('{')+1, state.indexOf('}'));
        if(context.isBlank()) {
            return new TreeSet<>();
        }
        return new TreeSet<>(Arrays.asList(context.split(",")));
    }

    public static List<String> getRight(String state) {
        return List.of(state.substring(state.indexOf("->") + 2, state.indexOf("{")).strip().split(" "));
    }

    public static String getLeft(String state) {
        return state.substring(0, state.indexOf('-')).strip();
    }

    private static TreeSet<String> epsilonClosure(Set<String> states, ENKA enka){
        TreeSet<String> result = new TreeSet<>();
        Stack<String> stack = new Stack<>();
        for (String state : states){
            result.add(state);
            stack.push(state);
        }
        return useAllEpsilonTransitions(result, stack, enka);
    }

    private static TreeSet<String> useAllEpsilonTransitions(TreeSet<String> result, Stack<String> stack, ENKA enka) {
        while (!stack.isEmpty()){
            String t = stack.pop();
            TreeSet<String> epsilonTransitionStates = new TreeSet<>(enka.getEpsilonTransition(t));
            for (String i : epsilonTransitionStates){
                if (!result.contains(i)){
                    result.add(i);
                    stack.push(i);
                }
            }
        }

        return result;
    }

    private static boolean isComplete(String item) {
        List<String> right = getRight(item);
        return right.get(right.size()-1).equals(".");
    }

    private static boolean productionMatchesLRItem(String left, List<String> right, String LRItem) {
        return (left.equals(getLeft(LRItem)) &&
                right.equals(getRight(LRItem).stream().filter(symbol -> !symbol.equals(".")).collect(Collectors.toList())));
    }
}
