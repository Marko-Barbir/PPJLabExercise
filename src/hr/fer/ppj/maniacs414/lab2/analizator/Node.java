package hr.fer.ppj.maniacs414.lab2.analizator;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String value;
    private List<Node> children;

    public Node(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        this.children.add(0, child);
    }
}
