import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;

public class CalleeBean {
    private ParseTree tree = null;
    private String name = null;
    private ArrayList<String> paramList = null;

    public CalleeBean() {

    }

    public ParseTree getTree() {
        return tree;
    }

    public void setTree(ParseTree tree) {
        this.tree = tree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getParamList() {
        return paramList;
    }

    public void setParamList(ArrayList<String> paramList) {
        this.paramList = paramList;
    }
}