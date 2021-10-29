package hr.fer.ppj.lab1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class ENKA implements Serializable {
    public HashMap<Integer, HashMap<Character, TreeSet<Integer>>> transitions;
    public HashMap<Integer, TreeSet<Integer>> epsilonTransitions;
    public HashMap<Integer, Action> actionMap;
    public HashSet<Integer> acceptableStates;
    int stateCount;

    public ENKA(){
        transitions = new HashMap<>();
        epsilonTransitions = new HashMap<>();
        actionMap = new HashMap<>();
        acceptableStates = new HashSet<>();
        stateCount = 1;
    }

    public int newState(){
        return (stateCount++) - 1;
    }

    public void addTransition(int a, char c, int b) {
        if(!transitions.containsKey(a)){
            transitions.put(a, new HashMap<>());
        }
        if(!transitions.get(a).containsKey(c)) {
            transitions.get(a).put(c, new TreeSet<>());
        }
        transitions.get(a).get(c).add(b);
    }

    public void addEpsilonTransition(int a, int b) {
        if(!epsilonTransitions.containsKey(a)) {
            epsilonTransitions.put(a, new TreeSet<>());
        }
        epsilonTransitions.get(a).add(b);
    }

    public void addAction (int a, Action action){
        if(actionMap.containsKey(a)) throw new IllegalArgumentException("State already has associated action.");
        actionMap.put(a, action);
    }

    public Action getAction(int state) {
        if (actionMap.containsKey(state)) {
            return actionMap.get(state);
        }
        throw new IllegalArgumentException("The state does not have an associated action.");
    }

    public TreeSet<Integer> getTransition(int a, char c){
        if (transitions.containsKey(a) && transitions.get(a).containsKey(c)) {
            return transitions.get(a).get(c);
        }
        return new TreeSet<>();
    }

    public TreeSet<Integer> getEpsilonTransition(int a) {
        if(epsilonTransitions.containsKey(a)) {
            return epsilonTransitions.get(a);
        }
        return new TreeSet<>();
    }

    public boolean addAcceptableState(int state) {
        return acceptableStates.add(state);
    }

    public boolean isAcceptableState(int state) {
        return acceptableStates.contains(state);
    }
}
