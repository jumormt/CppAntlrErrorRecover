package Listeners;
import models.*;
import antlrfiles.*;
import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.*;
import utils.*;

public class GetErrorApiParam extends CPP14BaseListener {
    private ArrayList<String> paramList = new ArrayList<String>();
    private String apiName;
    private boolean isReachApi = false;
    private boolean isFirstReachLeft = false;

    public boolean isEnd() {
        return isEnd;
    }

    private boolean isEnd = false;
    private Stack<String> bracketsStack = new Stack<String>();
    private final Map<String, String> bsMap = new HashMap<String, String>(){
        {
            put(")", "(");
            put("}", "{");
            put("]", "[");
        }
    };


    public ArrayList<String> getParamList() {
        return paramList;
    }

    public GetErrorApiParam(String apiName) {
        this.apiName = apiName;
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        if (isEnd) return;

        if (isFirstReachLeft) {
            if (bracketsStack.empty()) {
                isEnd = true;
                return;
            }

            if (node.getText().equals("(")) {
                bracketsStack.push("(");
                return;
            }

            if (node.getText().equals(")")) {
                bracketsStack.pop();
                if (bracketsStack.empty()) {
                    isEnd = true;
                }
                return;
            }

            if(!node.getText().matches("<missing '(.)'>")) {
                if (CodeGadgetUtils.CODE_GADGET_UTILS.isValidVarName(node.getText())) {
                    paramList.add(node.getText());
                }
            }
            return;
        }

        if (isReachApi) {
            if (node.getText().equals("(")) {
                isFirstReachLeft = true;
                bracketsStack.push("(");
                return;
            }
        }

        if (node.getText().equals(apiName)) {
            isReachApi = true;
            return;
        }

    }

    @Override
    public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx) {
        if (!isEnd && isFirstReachLeft) {
            // 当嵌套时会加入函数，需要去除函数名,只加入参数。
            if (ctx.parent.parent.parent.parent.getRuleContext() instanceof CPP14Parser.UnaryexpressionContext) {
                this.paramList.add(ctx.getText());
            }
        }
    }

}
