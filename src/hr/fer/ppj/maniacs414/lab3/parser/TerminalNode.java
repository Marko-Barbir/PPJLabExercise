package hr.fer.ppj.maniacs414.lab3.parser;

import java.util.HashMap;

public class TerminalNode extends Node {
    public String token;
    public int line;
    public String value;

    public TerminalNode(Node parent, String token, int line, String value) {
        this.parent = parent;
        this.token = token;
        this.line = line;
        this.value = value;
        this.props = new HashMap<>();
    }

    public TerminalNode(String token, int line, String value) {
        this(null, token, line, value);
    }

    @Override
    public String toString() {
        return String.format("%s(%d,%s)", token, line, value);
    }
}
