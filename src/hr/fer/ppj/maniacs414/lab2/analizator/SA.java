package hr.fer.ppj.maniacs414.lab2.analizator;

import hr.fer.ppj.maniacs414.lab1.analizator.ENKA;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class SA {
    private List<Token> inputTokens;
    private int index;
    private Stack<Object> mainStack;
    private Stack<Node> nodeStack;

    private Map<Integer, Map<String, Integer>> newStateTable;
    private Map<Integer, Map<String, String>> actionTable;
    private Set<String> syncCharacters;

    public SA() {
        this.inputTokens = new ArrayList<>();
        this.index = 0;
        this.mainStack = new Stack<>();
        this.nodeStack = new Stack<>();

        try {
            FileInputStream fileInputStream1
                    = new FileInputStream("src/hr/fer/ppj/lab2/maniacs414/analizator/newState.txt");
            ObjectInputStream objectInputStream1
                    = new ObjectInputStream(fileInputStream1);
            this.newStateTable = (Map<Integer, Map<String, Integer>>) objectInputStream1.readObject();

            FileInputStream fileInputStream2
                    = new FileInputStream("src/hr/fer/ppj/lab2/maniacs414/analizator/action.txt");
            ObjectInputStream objectInputStream2
                    = new ObjectInputStream(fileInputStream2);
            this.actionTable = (Map<Integer, Map<String, String>>) objectInputStream2.readObject();

            FileInputStream fileInputStream3
                    = new FileInputStream("src/hr/fer/ppj/lab2/maniacs414/analizator/syncCharacters.txt");
            ObjectInputStream objectInputStream3
                    = new ObjectInputStream(fileInputStream3);
            this.syncCharacters = (Set<String>) objectInputStream3.readObject();

            objectInputStream1.close();
            objectInputStream2.close();
            objectInputStream3.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Scanner inScanner = new Scanner(System.in);
        while (inScanner.hasNextLine()) {
            String line = inScanner.nextLine();
            String[] chunks;

            if (!line.equals("")){
                chunks = line.split(" ", 3);
                inputTokens.add(new Token(chunks[0], Integer.parseInt(chunks[1]), chunks[2]));
            }
        }
        inputTokens.add(new Token("|", -1, ""));

        parseInput();
        printTree(nodeStack.pop(), 0);
    }

    private void parseInput() {
        mainStack.push(0);
        while (index < inputTokens.size()) {
            if(!(mainStack.peek() instanceof Integer currentState)) {
                throw new IllegalStateException("Expected integer on top of the main stack, found: " + mainStack.peek());
            }
            String action = actionTable.get(currentState).get(inputTokens.get(index).getType());
            if (action == null) {
                throw new IllegalStateException("No ActionTable entry for the given input");
            }

            if (action.equals("pri")){
                System.out.println("Expression parsed successfully.");
                break;
            }
            else if (action.startsWith("p")){
                performMove(action);
            }
            else if (action.startsWith("r")){
                boolean success = performReduction(action);
                if (!success) {
                    performRecovery(currentState);
                }
            }
            else if (action.startsWith("o")){
                performRecovery(currentState);
            }
            else {
                throw new IllegalStateException("Invalid action notation");
            }
        }
    }

    private void performRecovery(Integer currentState) {
        Set<String> expectedCharacters = actionTable.get(currentState).keySet().stream()
                .filter(k -> !actionTable.get(currentState).get(k).startsWith("o")).collect(Collectors.toSet());
        Token token = inputTokens.get(index);

        System.out.printf("Syntax error: line %d, expected %s, received %s %s\n",
                token.getLineNumber(), String.join(" ", expectedCharacters), token.getType(), token.getContent());

        while (index < inputTokens.size() && !syncCharacters.contains(inputTokens.get(index).getType())) {
            index++;
        }

        Integer state = currentState;
        while (!mainStack.isEmpty() && actionTable.get(state).get(inputTokens.get(index).getType()).startsWith("o")) {
            if (mainStack.peek() instanceof Integer) {
                mainStack.pop();
            }
            mainStack.pop();
            nodeStack.pop();
            state = (Integer) mainStack.peek();
        }
    }

    private void performMove(String action) {
        String[] actionChunks = action.split(" ");
        mainStack.push(inputTokens.get(index).getType());
        mainStack.push(Integer.valueOf(actionChunks[1]));
        nodeStack.push(new Node(inputTokens.get(index).toString()));
        index++;
    }

    private boolean performReduction(String action) {
        String[] actionChunks = action.split(" ");
        String nonterminalSymbol = actionChunks[1];
        int removeCount = Integer.parseInt(actionChunks[2]);

        for (int i = 0; i < 2 * removeCount; i++) {
            mainStack.pop();
        }
        Integer oldState = (Integer) mainStack.peek();
        Integer newState = newStateTable.get(oldState).get(nonterminalSymbol);

        if (newState < 0) {
            return false;
        }

        mainStack.push(nonterminalSymbol);
        mainStack.push(newState);

        Node parent = new Node(nonterminalSymbol);
        if (removeCount == 0) {
            parent.addChild(new Node("$"));
        }

        for (int i = 0; i < removeCount; i++) {
            parent.addChild(nodeStack.pop());
        }
        nodeStack.push(parent);

        return true;
    }

    private void printTree(Node root, int level) {
        System.out.print(" ".repeat(level));
        System.out.println(root.getValue());
        for (Node child : root.getChildren()) {
            printTree(child, level + 1);
        }
    }

    public static void main(String[] args) {
        SA sa = new SA();
    }

}
