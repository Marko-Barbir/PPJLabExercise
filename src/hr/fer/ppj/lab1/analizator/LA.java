package hr.fer.ppj.lab1.analizator;

import hr.fer.ppj.lab1.Action;
import hr.fer.ppj.lab1.ENKA;

import java.util.*;

public class LA {
    private char[] text;
    private Map<Integer, ENKA> dummyENKA = newDummyENKA();
    private int start;
    private int end;
    private int last;
    private int regexIndex;
    private int state;
    private int lineNumber;

    public LA(){
        Scanner inScanner = new Scanner(System.in).useDelimiter("\\Z");
        text = inScanner.next().toCharArray();
        inScanner.close();

        this.start = 1;
        this.end = 0;
        this.last = 1;
        this.regexIndex = 0;
        this.state = 0;
        this.lineNumber = 1;
    }

    public void generateTokens(){
        TreeSet<Integer> R = epsilonClosure(0);
        while(end < text.length){
            TreeSet<Integer> P = new TreeSet<>(dummyENKA.get(this.state).acceptableStates);
            P.retainAll(R);
            if (P.isEmpty() && !R.isEmpty()){
                Character a = text[end++];
                TreeSet<Integer> Q = new TreeSet<>(R);
                R = epsilonClosure(dummyENKA.get(this.state).getTransition(Q, a));
            }
            else if (!P.isEmpty()){
                regexIndex = P.first();
                last = end - 1;
                Character a = text[end++];
                TreeSet<Integer> Q = new TreeSet<>(R);
                R = epsilonClosure(dummyENKA.get(this.state).getTransition(Q, a));
            }
            else if (R.isEmpty()){
                if (regexIndex == 0){
                    System.err.println(text[start]);
                    start++;
                    end = start;
                    R = epsilonClosure(0);
                }
                else {
                    Action action = dummyENKA.get(this.state).actionMap.get(regexIndex);
                    if (action.goBack >= 0){
                        last = start + action.goBack - 1;
                    }
                    if (!action.tokenName.equals("-")){
                        System.out.println(action.tokenName + " " + lineNumber + " " + new String(text, start, last-start+1));
                    }
                    if (action.newLine){
                        lineNumber++;
                    }
                    if(action.newState != null){
                        this.state = action.newState;
                    }

                    regexIndex = 0;
                    end = start = last + 1;
                    R = epsilonClosure(0);
                }
            }
        }
    }

    private TreeSet<Integer> epsilonClosure(Integer state){
        TreeSet<Integer> result = new TreeSet<>();
        Stack<Integer> stack = new Stack<>();
        result.add(state);
        stack.push(state);

        return useAllEpsilonTransitions(result, stack);
    }

    private TreeSet<Integer> epsilonClosure(Set<Integer> states){
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
