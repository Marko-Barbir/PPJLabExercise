package hr.fer.ppj.lab1.analizator;

import com.sun.source.util.Trees;
import hr.fer.ppj.lab1.ENKA;

import java.util.*;

public class LA {
    private String text;
    private Map<Integer, ENKA> dummyENKA = newDummyENKA();
    private int start;
    private int end;
    private int last;
    private int regexIndex;
    private int state;

    public LA(){
        Scanner inScanner = new Scanner(System.in).useDelimiter("\\Z");
        text = inScanner.next();
        inScanner.close();

        this.start = 1;
        this.end = 0;
        this.last = 1;
        this.regexIndex = 0;
        this.state = 0;
    }

    public void generateTokens(){
        //TODO
    }

    private TreeSet<Integer> epsilonClosure(Integer state){
        TreeSet<Integer> result = new TreeSet<>();
        Stack<Integer> stack = new Stack<>();
        result.add(state);
        stack.push(state);

        return useAllEpsilonTransitions(result, stack);
    }

    private TreeSet<Integer> epsilonClosure(TreeSet<Integer> states){
        TreeSet<Integer> result = new TreeSet<>();
        Stack<Integer> stack = new Stack<>();
        for (Integer state : states){
            result.add(state);
            stack.push(state);
        }
        return useAllEpsilonTransitions(result, stack);
    }

    private TreeSet<Integer> useAllEpsilonTransitions(TreeSet<Integer> result, Stack<Integer> stack) {
        while (!stack.isEmpty()){
            Integer t = stack.pop();
            TreeSet<Integer> epsilonTransitionStates = dummyENKA.get(this.state).getEpsilonTransition(t);
            for (Integer i : epsilonTransitionStates){
                if (!result.contains(i)){
                    result.add(i);
                    stack.push(i);
                }
            }
        }

        return result;
    }

    private Map<Integer, ENKA> newDummyENKA(){
        ENKA dummyENKA = new ENKA();
        int a = dummyENKA.newState();
        int b = dummyENKA.newState();
        int c = dummyENKA.newState();
        int d = dummyENKA.newState();
        int y = dummyENKA.newState();
        dummyENKA.addTransition(a, '1', b);
        dummyENKA.addTransition(c, '2', d);
        dummyENKA.addEpsilonTransition(0, a);
        dummyENKA.addEpsilonTransition(0, c);
        dummyENKA.addEpsilonTransition(d, y);
        dummyENKA.addEpsilonTransition(b, y);
        Map<Integer , ENKA> stateMap = new HashMap<>();
        stateMap.put(0, dummyENKA);
        return stateMap;
    }

    public static void main(String[] args) {
        LA la = new LA();
        la.generateTokens();
    }

}
