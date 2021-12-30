package hr.fer.ppj.maniacs414.lab3.parser;

public class TerminalNode extends Node {
    public String token;
    public int line;
    public String value;

    public TerminalNode(Node parent, String token, int line, String value) {
        this.parent = parent;
        this.token = token;
        this.line = line;
        this.value = value;
    }

    public TerminalNode(String token, int line, String value) {
        this(null, token, line, value);
    }

    @Override
    public String toString() {
        return token + " " + line + " " + value;
    }
}
