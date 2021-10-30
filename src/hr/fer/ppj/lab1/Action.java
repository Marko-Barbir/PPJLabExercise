package hr.fer.ppj.lab1;

import java.io.Serializable;

public class Action implements Serializable {
    public String tokenName;
    public boolean newLine;
    public Integer newState;
    public int goBack;

    public Action(String tokenName){
        this.tokenName = tokenName;
        this.newLine = false;
        this.newState = null;
        this.goBack = -1;
    }

    public Action(String tokenName, boolean newLine, Integer newState, int goBack) {
        this.tokenName = tokenName;
        this.newLine = newLine;
        this.newState = newState;
        this.goBack = goBack;
    }
}
