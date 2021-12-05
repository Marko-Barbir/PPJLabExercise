package hr.fer.ppj.maniacs414.lab2;

import hr.fer.ppj.maniacs414.lab2.analizator.ENKA;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
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

        HashMap<String, List<List<String>>> grammar = new HashMap<>();
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
                        if(!isEmpty && production.stream().noneMatch((symbol) -> !emptyNonterminal.contains(symbol))) {
                            changed = true;
                            isEmpty = true;
                            emptyNonterminal.add(entry.getKey());
                        }
                    }
                }
            }
        }

        System.out.println(emptyNonterminal);

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

        //this is probably a bunch of bullshit
        changed = true;
        while (changed) {
            changed = false;

            for (String state : enka.getStates()) {
                if(state.equals("q0")) continue;

                left = getLeft(state);
                List<String> right = getRight(state);
                Set<String> context = getStateContext(state);
                String nextSymbol = (right.indexOf(".") < right.size() - 1) ? right.get(right.indexOf(".")) : null;
                if(nextSymbol == null) {
                } else if (nonterminal.contains(nextSymbol)) {
                    List<String> nextStates = new ArrayList<>();
                    for(List<String> production : grammar.get(nextSymbol)) {
                        Set<String> newContext = new HashSet<>();
                    }
//                    if(!enka.getEpsilonTransition(state).contains())
                } else if (terminal.contains(nextSymbol)) {
                    int dotIndex = right.indexOf(".");
                    String nextState = left + " -> " + String.join(" ", right.subList(0, dotIndex)) + right.get(dotIndex+1) +
                            ((dotIndex < right.size()-1) ? String.join(" ", right.subList(dotIndex+2, right.size())) : "");
                    if(!enka.getTransition(state, nextSymbol).contains(nextState)) {
                        changed = true;
                        enka.addTransition(state, nextSymbol, nextState);
                    }
                }
            }
        }

    }

    public static Set<String> getStateContext(String state) {
        String context = state.substring(state.indexOf('{'), state.indexOf('}') + 1);
        if(context.isBlank()) {
            return new HashSet<>();
        }
        return Set.of(context.split(" "));
    }

    public static List<String> getRight(String state) {
        return List.of(state.substring(state.indexOf('>') + 1).strip().split(" "));
    }

    public static String getLeft(String state) {
        return state.substring(0, state.indexOf('-')).strip();
    }
}
