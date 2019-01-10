import org.antlr.v4.runtime.tree.ParseTree;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CallerBean {
    private ParseTree callerTree = null;
    private ParseTree calleeTree = null;
    private String callerName = null;
    private String calleeName = null;
    private ArrayList<String> callerParamList = null;
    private ArrayList<String> calleeParamList = null;

    public ParseTree getCallerTree() {
        return callerTree;
    }

    public void setCallerTree(ParseTree callerTree) {
        this.callerTree = callerTree;
    }

    public ParseTree getCalleeTree() {
        return calleeTree;
    }

    public void setCalleeTree(ParseTree calleeTree) {
        this.calleeTree = calleeTree;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCalleeName() {
        return calleeName;
    }

    public void setCalleeName(String calleeName) {
        this.calleeName = calleeName;
    }

    public ArrayList<String> getCallerParamList() {
        return callerParamList;
    }

    public void setCallerParamList(ArrayList<String> callerParamList) {
        this.callerParamList = callerParamList;
    }

    public ArrayList<String> getCalleeParamList() {
        return calleeParamList;
    }

    public void setCalleeParamList(ArrayList<String> calleeParamList) {
        this.calleeParamList = calleeParamList;
    }
}
