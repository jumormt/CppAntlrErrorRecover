package models;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;

public class QueueItemBean {
    private ParseTree currentPointTree = null;
    private ParseTree topTree = null;
    private String traceVarName = null;
    private String topTreeName = null;
    private ArrayList<Integer> previousLines = new ArrayList<>();

    public String getTopTreeName() {
        return topTreeName;
    }

    public void setTopTreeName(String topTreeName) {
        this.topTreeName = topTreeName;
    }

    public ParseTree getTopTree() {
        return topTree;
    }

    public void setTopTree(ParseTree topTree) {
        this.topTree = topTree;
    }

    public String getTraceVarName() {
        return traceVarName;
    }

    public void setTraceVarName(String traceVarName) {
        this.traceVarName = traceVarName;
    }

    public ParseTree getCurrentPointTree() {
        return currentPointTree;
    }

    public void setCurrentPointTree(ParseTree currentPointTree) {
        this.currentPointTree = currentPointTree;
    }

    public ArrayList<Integer> getPreviousLines() {
        return previousLines;
    }

    public void setPreviousLines(ArrayList<Integer> previousLines) {
        this.previousLines = previousLines;
    }
}