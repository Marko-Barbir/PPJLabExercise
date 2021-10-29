package hr.fer.ppj.lab1.maniacs414;

import hr.fer.ppj.lab1.maniacs414.analizator.ENKA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class GLA {
    private static class StatePair{
        public int leftState, rightState;

        public StatePair(int leftState, int rightState) {
            this.leftState = leftState;
            this.rightState = rightState;
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line;
        HashMap<String, String> regexMap = new HashMap<>();
        while (scanner.hasNextLine() && (line=scanner.nextLine()).startsWith("{")) {
            String[] split = line.split(" ", 1);
            String name = split[0], regex = split[1];
            for(String key: regexMap.keySet()) {
                regex = regex.replaceAll(key, regexMap.get(key));
            }
            regexMap.put(name, regex);
        }
        ArrayList<String> states = new ArrayList<>();
        while (scanner.hasNextLine() && (line=scanner.nextLine()).startsWith("%X")) {
            String[] split = line.split(" ");
            states.addAll(Arrays.asList(Arrays.copyOfRange(split, 1, split.length)));
        }
        ArrayList<String> tokenNames = new ArrayList<>();
        while (scanner.hasNextLine() && (line=scanner.nextLine()).startsWith("%L")) {
            String[] split = line.split(" ");
            states.addAll(Arrays.asList(Arrays.copyOfRange(split, 1, split.length)));
        }
        ArrayList<ENKA> automata = new ArrayList<>();
        states.forEach((state) -> automata.add(new ENKA()));
    }

    public static boolean isOperator(String text, int i) {
        int num = 0;
        while (i - 1 >= 0 && text.charAt(i-1) == '\\') {
            num = num + 1;
            i = i - 1;
        }
        return num % 2 == 0;
    }

    private StatePair convert(String expression, ENKA automaton) {
        ArrayList<String> options = new ArrayList<>();
        int parenCount = 0;
        int lastUngrouped = 0;
        for (int i = 0; i < expression.length(); i++) {
            if(expression.charAt(i) == '(' && isOperator(expression, i)){
                parenCount++;
            } else if(expression.charAt(i) == ')' && isOperator(expression, i)) {
                parenCount--;
            } else if(parenCount==0 && expression.charAt(i)=='|' && isOperator(expression, i)){
                options.add(expression.substring(lastUngrouped, i));
                lastUngrouped = i+1;
            }
        }
        if(options.size() > 0) {
            options.add(expression.substring(lastUngrouped));
        }
        int leftState = automaton.newState();
        int rightState = automaton.newState();
        if(options.size() > 0) {
            for(String option : options) {
                StatePair temp = convert(option, automaton);
                automaton.addEpsilonTransition(leftState, temp.leftState);
                automaton.addEpsilonTransition(temp.rightState, rightState);
            }
        } else {
            boolean prefixed = false;
            int lastState = leftState;
            for(int i = 0; i < expression.length(); i++) {
                int a,b;
                if(prefixed) {
                    prefixed = false;
                    char transitionChar;
                    switch (expression.charAt(i)){
                        case 't':
                            transitionChar = '\t';
                            break;
                        case 'n':
                            transitionChar = '\n';
                            break;
                        case '_':
                            transitionChar = ' ';
                            break;
                        default:
                            transitionChar = expression.charAt(i);
                    }

                    a = automaton.newState();
                    b = automaton.newState();
                    automaton.addTransition(a, transitionChar, b);
                } else {
                    if(expression.charAt(i) == '\\') {
                        prefixed = true;
                        continue;
                    }
                    if(expression.charAt(i) != '(') {
                        a = automaton.newState();
                        b = automaton.newState();
                        if(expression.charAt(i) == '$'){
                            automaton.addEpsilonTransition(a, b);
                        }else {
                            automaton.addTransition(a, expression.charAt(i), b);
                        }
                    } else {
                        int j = expression.substring(i).indexOf(')');
                        StatePair temp = convert(expression.substring(i+1, j), automaton);
                        a = temp.leftState;
                        b = temp.rightState;
                        i = j;
                    }
                }
                if(i+1<expression.length() && expression.charAt(i+1)=='*') {
                    int x = a;
                    int y = b;
                    a = automaton.newState();
                    b = automaton.newState();
                    automaton.addEpsilonTransition(a, x);
                    automaton.addEpsilonTransition(y, b);
                    automaton.addEpsilonTransition(a, b);
                    automaton.addEpsilonTransition(y, x);
                    i++;
                }

                automaton.addEpsilonTransition(lastState, a);
                lastState = b;
            }
            automaton.addEpsilonTransition(lastState, rightState);
        }
        return new StatePair(leftState, rightState);
    }
}
