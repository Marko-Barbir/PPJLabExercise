package hr.fer.ppj.lab1.maniacs414;

import hr.fer.ppj.lab1.maniacs414.analizator.Action;
import hr.fer.ppj.lab1.maniacs414.analizator.ENKA;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
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
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        String line = null;
        HashMap<String, String> regexMap = new HashMap<>();
        while (scanner.hasNextLine() && (line=scanner.nextLine()).startsWith("{")) {
            String[] split = line.split(" ", 2);
            String name = split[0], regex = split[1];
            for(String key: regexMap.keySet()) {
                regex = regex.replace(key, regexMap.get(key));
            }
            regexMap.put(name, regex);
        }
        String[] split = new String[0];
        if (line != null) {
            split = line.split(" ");
        }
        ArrayList<String> states = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(split, 1, split.length)));
        if(scanner.hasNextLine()) {
            line = scanner.nextLine();
        }else {
            line = null;
        }
        if (line != null) {
            split = line.split(" ");
        }
        ArrayList<String> tokenNames = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(split, 1, split.length)));
        HashMap<Integer, ENKA> automata = new HashMap<>();
        for (int i = 0; i < states.size(); i++) {
            automata.put(i, new ENKA());
        }


        while (scanner.hasNextLine() && (line = scanner.nextLine()).startsWith("<")) {
            int indexOfClose = line.indexOf('>');
            String stateName = line.substring(1, indexOfClose);
            String regex = line.substring(indexOfClose+1);
            for(String key: regexMap.keySet()) {
                regex = regex.replace(key, regexMap.get(key));
            }
            ENKA automaton = automata.get(states.indexOf(stateName));
            StatePair result = convert(regex, automaton);
            automaton.addEpsilonTransition(0, result.leftState);
            automaton.addAcceptableState(result.rightState);

            scanner.nextLine();
            if(!(tokenNames.contains(line=scanner.nextLine()) || line.equals("-"))) {
                throw new IllegalArgumentException("Invalid token name.");
            }
            Action action = new Action(line);
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).startsWith("}")) {
                if (line.startsWith("NOVI_REDAK")){
                    action.newLine = true;
                } else if (line.startsWith("UDJI_U_STANJE")) {
                    action.newState = states.indexOf(line.split(" ", 2)[1]);
                } else if (line.startsWith("VRATI_SE")) {
                    action.goBack = Integer.parseInt(line.split(" ", 2)[1]);
                }
            }
            automaton.addAction(result.rightState, action);
        }

        FileOutputStream fOut = new FileOutputStream("src/hr/fer/ppj/lab1/maniacs414/analizator/enkamap.txt");
        ObjectOutputStream oOut = new ObjectOutputStream(fOut);
        oOut.writeObject(automata);
        oOut.flush();
        oOut.close();
    }

    public static boolean isOperator(String text, int i) {
        int num = 0;
        while (i - 1 >= 0 && text.charAt(i-1) == '\\') {
            num = num + 1;
            i = i - 1;
        }
        return num % 2 == 0;
    }

    private static StatePair convert(String expression, ENKA automaton) {
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
