package hr.fer.ppj.lab1.maniacs414.analizator;

import java.io.Serializable;

public class Action implements Serializable {
    public String tokenName;
    public boolean newLine;
    public String newState;
    public int goBack;

    public Action(String tokenName){
        this.tokenName = tokenName;
        this.newLine = false;
        this.newState = null;
        this.goBack = -1;
    }

    public Action(String tokenName, boolean newLine, String newState, int goBack) {
        this.tokenName = tokenName;
        this.newLine = newLine;
        this.newState = newState;
        this.goBack = goBack;
    }
}
