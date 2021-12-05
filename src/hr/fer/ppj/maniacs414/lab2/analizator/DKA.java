package hr.fer.ppj.maniacs414.lab2.analizator;

import java.io.Serializable;
import java.util.*;

public class DKA implements Serializable {
    private static final long serialVersionUID = 3L;
    private HashMap<Integer, HashMap<String, Integer>> transitions;
    private HashSet<Integer> acceptableStates;
    private HashMap<Integer, TreeSet<String>> stateToLRItemMap;
    private int stateCount;

    public DKA(){
        transitions = new HashMap<>();
        acceptableStates = new HashSet<>();
        stateToLRItemMap = new HashMap<>();
        stateCount = 0;
    }

    public int getStateCount() {
        return stateCount;
    }

    public void addTransition(Integer a, String c, Integer b) {
        if (a >= stateCount ||
                b >= stateCount) {
            throw new IllegalArgumentException("No such state");
        }
        if(!transitions.containsKey(a)){
            transitions.put(a, new HashMap<>());
        }
        if(transitions.get(a).containsKey(c)) {
            throw new IllegalStateException("Not deterministic!");
        }
        transitions.get(a).put(c, b);
    }



    public Integer getTransition(Integer a, String c){
        if (a>=stateCount) throw new IllegalArgumentException("Automata state doesn't exist");
        return transitions.getOrDefault(a, new HashMap<>()).getOrDefault(c, null);
    }

    public boolean addAcceptableState(Integer state) {
        return acceptableStates.add(state);
    }

    public boolean isAcceptableState(Integer state) {
        return acceptableStates.contains(state);
    }

    public int addState() {
        return stateCount++;
    }

    public HashMap<Integer, TreeSet<String>> getStateToLRItemMap() {
        return new HashMap<>(stateToLRItemMap);
    }

    public void addStateToLRItemLink(Integer state, TreeSet<String> LRItem) {
        stateToLRItemMap.put(state, LRItem);
    }

    public TreeSet<String> getStateLRItems(Integer state) {
        return new TreeSet<>(stateToLRItemMap.getOrDefault(state, new TreeSet<>()));
    }
}
