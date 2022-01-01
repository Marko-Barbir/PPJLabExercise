package parser;

import hr.fer.ppj.maniacs414.lab3.types.CharType;
import hr.fer.ppj.maniacs414.lab3.types.IntType;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static NonterminalNode parseInput(InputStream is){
        Scanner sc = new Scanner(is);
        List<Node> parents = new ArrayList<>();
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            Node node = extractNode(line);
            int spaces = line.length() - line.stripLeading().length();

            if(node instanceof NonterminalNode){
                if(parents.size() - 1 < spaces){
                    parents.add(node);
                } else {
                    parents.set(spaces, node);
                }
            }

            if(spaces > 0) {
                node.parent = parents.get(spaces - 1);
                ((NonterminalNode)parents.get(spaces - 1)).addChild(node);
            }

        }

        return (NonterminalNode)parents.get(0);
    }

    private static Node extractNode(String text){
        text = text.strip();
        Pattern terminalPattern = Pattern.compile("(\\S+) (\\S+) (\\S+)");
        Pattern nonterminalPattern = Pattern.compile("<(\\S+)>");

        Matcher terminalMatcher = terminalPattern.matcher(text);
        Matcher nonterminalMatcher = nonterminalPattern.matcher(text);

        if(terminalMatcher.matches()){
            TerminalNode terminalNode = new TerminalNode(terminalMatcher.group(1), Integer.parseInt(terminalMatcher.group(2)), terminalMatcher.group(3));
            try {
                Integer.parseInt(terminalNode.value);
                terminalNode.addProp("tip", new IntType(true));
            } catch (NumberFormatException e) {
                terminalNode.addProp("tip", new CharType(true));
            }

            return terminalNode;
        }

        if(nonterminalMatcher.matches()){
            return new NonterminalNode(nonterminalMatcher.group(1));
        }

        throw new IllegalArgumentException("Cannot extract node from " + text);
    }

    public static void main(String[] args) {
        NonterminalNode root = parseInput(System.in);

        NonterminalNode.printTree(root , 0);
    }
}
