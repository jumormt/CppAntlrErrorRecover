package models;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;

public class APITreeBean {
    ParseTree APITree = null; // 该api所在行的tree
    ParseTree topTree = null; // 该api所属函数的tree
    ArrayList<String> paramList = new ArrayList<>(); // 该api的形参
    String APIName = null;
    String topName = null;

    public APITreeBean(){

    }

    public ParseTree getAPITree() {
        return APITree;
    }

    public ParseTree getTopTree() {
        return topTree;
    }

    public ArrayList<String> getParamList() {
        return paramList;
    }

    public String getAPIName() {
        return APIName;
    }

    public String getTopName() {
        return topName;
    }

    public void setAPITree(ParseTree APITree) {
        this.APITree = APITree;
    }

    public void setParamList(ArrayList<String> paramList) {
        this.paramList = paramList;
    }

    public void setTopTree(ParseTree topTree) {
        this.topTree = topTree;
    }

    public void setAPIName(String APIName) {
        this.APIName = APIName;
    }

    public void setTopName(String TopName){
        this.topName = TopName;
    }
}
