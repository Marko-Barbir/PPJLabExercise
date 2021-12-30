package hr.fer.ppj.maniacs414.lab3.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NonterminalNode extends Node{
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

    public Object addProp(String name, Object prop){
       return props.put(name, prop);
    }

    public boolean addChild(Node child){
        return children.add(child);
    }

    public boolean check(){
        return false;
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
}
