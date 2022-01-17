package hr.fer.ppj.maniacs414.lab4.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NonterminalNode extends Node {
    public String name;
    public List<Node> children;

    public NonterminalNode(Node parent, String name) {
        this.parent = parent;
        this.name = name;
        this.children = new ArrayList<>();
        this.props = new HashMap<>();
    }

    public NonterminalNode(String name) {
        this(null, name);
    }

    public boolean addChild(Node child){
        return children.add(child);
    }

    public static void printTree(Node root, int level){
        System.out.print(" ".repeat(level));
        System.out.println(root);

        if(root instanceof NonterminalNode) {
            NonterminalNode ntNode = (NonterminalNode) root;
            for (Node child : ntNode.children){
                printTree(child, level + 1);
            }
        }
    }

    @Override
    public String toString() {
        return "<" + name + ">";
    }

    public String getProduction(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.toString());
        sb.append(" ::=");
        for(Node child : children){
            sb.append(" " + child);
        }

        return sb.toString();
    }
}
