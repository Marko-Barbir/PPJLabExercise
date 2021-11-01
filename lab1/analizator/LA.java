import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class LA {
    private char[] text;
    private HashMap<Integer, ENKA> enkaMap;
    private int start;
    private int end;
    private int last;
    private int regexIndex;
    private int state;
    private int lineNumber;

    @SuppressWarnings("unchecked")
    public LA() {
        Scanner inScanner = new Scanner(System.in).useDelimiter("\\Z");
        text = inScanner.next().toCharArray();
        inScanner.close();

        try {
            FileInputStream fileInputStream
                    = new FileInputStream("enkamap.txt");
            ObjectInputStream objectInputStream
                    = new ObjectInputStream(fileInputStream);
            this.enkaMap = (HashMap<Integer, ENKA>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        this.start = 0;
        this.end = 0;
        this.last = 0;
        this.regexIndex = 0;
        this.state = 0;
        this.lineNumber = 1;
    }

   public void generateTokens(){
        String token = generateToken();
        while (!token.equals("EOF")){
            if(token.equals("")) {
                token = generateToken();
                continue;
            }

            System.out.println(token);
            token = generateToken();
        }
   }

    private String generateToken(){
        if (end >= text.length){
            return "EOF";
        }
        String token = "";
        TreeSet<Integer> R = epsilonClosure(0);
        TreeSet<Integer> P = new TreeSet<>(enkaMap.get(this.state).acceptableStates);
        P.retainAll(R);
        while(!R.isEmpty() && end < text.length){
            char a;
            if (!P.isEmpty()) {
                regexIndex = P.first();
                last = end - 1;
            }
            a = text[end++];
            TreeSet<Integer> Q = new TreeSet<>(R);
            R = epsilonClosure(enkaMap.get(this.state).getTransition(Q, a));
            P = new TreeSet<>(enkaMap.get(this.state).acceptableStates);
            P.retainAll(R);

            if (end == text.length && !P.isEmpty()){
                regexIndex = P.first();
                last = end - 1;
            }
        }

        if (regexIndex == 0){
            System.err.println(text[start]);
            start++;
            end = start;
        }
        else {
            Action action = enkaMap.get(this.state).actionMap.get(regexIndex);
            if (action.goBack >= 0){
                last = start + action.goBack - 1;
            }
            if (!action.tokenName.equals("-")){
                token = action.tokenName + " " + lineNumber + " " + new String(text, start, last-start+1);
            }
            if (action.newLine){
                lineNumber++;
            }
            if(action.newState != null){
                this.state = action.newState;
            }

            regexIndex = 0;
            end = start = last + 1;
        }


        return token;
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
            TreeSet<Integer> epsilonTransitionStates = enkaMap.get(this.state).getEpsilonTransition(t);
            for (Integer i : epsilonTransitionStates){
                if (!result.contains(i)){
                    result.add(i);
                    stack.push(i);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        LA la = new LA();
        la.generateTokens();
    }

}
