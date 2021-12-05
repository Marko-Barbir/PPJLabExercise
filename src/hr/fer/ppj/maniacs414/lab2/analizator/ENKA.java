package hr.fer.ppj.maniacs414.lab2.analizator;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class ENKA implements Serializable {
    private static final long serialVersionUID = 2L;
    private HashMap<String, HashMap<String, HashSet<String>>> transitions;
    private HashMap<String, HashSet<String>> epsilonTransitions;
    private HashSet<String> acceptableStates, states;
    private int stateCount;

    public ENKA(){
        transitions = new HashMap<>();
        epsilonTransitions = new HashMap<>();
        acceptableStates = new HashSet<>();
        states = new HashSet<>();
        stateCount = 1;
    }

    public int getStateCount() {
        return stateCount;
    }

    public void addTransition(String a, String c, String b) {
        if (!(states.contains(a) && states.contains(b))) {
           addState(a);
           addState(b);
        }
        if(!transitions.containsKey(a)){
            transitions.put(a, new HashMap<>());
        }
        if(!transitions.get(a).containsKey(c)) {
            transitions.get(a).put(c, new HashSet<>());
        }
        transitions.get(a).get(c).add(b);
    }

    public void addEpsilonTransition(String a, String b) {
        if (!(states.contains(a) && states.contains(b))) {
            addState(a);
            addState(b);
        }
        if(!epsilonTransitions.containsKey(a)) {
            epsilonTransitions.put(a, new HashSet<>());
        }
        epsilonTransitions.get(a).add(b);
    }


    public HashSet<String> getTransition(String a, String c){
        if (!states.contains(a)) throw new IllegalArgumentException("Automata state doesn't exist");
        if (transitions.containsKey(a) && transitions.get(a).containsKey(c)) {
            return transitions.get(a).get(c);
        }
        return new HashSet<>();
    }

    public HashSet<String> getTransition(Set<String> a, String c){
        HashSet<String> result = new HashSet<>();
        for (String s : a) {
            HashSet<String> nextStates = getTransition(s, c);
            result.addAll(nextStates);
        }
        return result;
    }

    public HashSet<String> getEpsilonTransition(String a) {
        if (!states.contains(a)) throw new IllegalArgumentException("Automata state doesn't exist");
        if(epsilonTransitions.containsKey(a)) {
            return epsilonTransitions.get(a);
        }
        return new HashSet<>();
    }

    public boolean addAcceptableState(String state) {
        return acceptableStates.add(state);
    }

    public boolean isAcceptableState(String state) {
        return acceptableStates.contains(state);
    }

    public boolean addState(String state) {
        if(states.add(state)) {
            stateCount++;
            return true;
        }
        return false;
    }

    public boolean isState(String state) {
        return states.contains(state);
    }

    public HashSet<String> getStates() {
        return states;
    }
}
