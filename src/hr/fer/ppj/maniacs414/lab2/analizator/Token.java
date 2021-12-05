package hr.fer.ppj.maniacs414.lab2.analizator;

public class Token {
    private String type;
    private int lineNumber;
    private String content;

    public Token(String type, int lineNumber, String content) {
        this.type = type;
        this.lineNumber = lineNumber;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return type + " " + lineNumber + " " + content;
    }
}
