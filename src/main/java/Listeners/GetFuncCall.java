package Listeners;

import java.util.ArrayList;
import models.*;
import antlrfiles.*;

public class GetFuncCall extends CPP14BaseListener {
    private String funcCallName = null;
    private ArrayList<String> paramList = new ArrayList<>();
    boolean isFirstTriggered = true;

//    @Override
//    public void enterFuncnameexpr(CPP14Parser.FuncnameexprContext ctx) {
//        if(this.isFirstTriggered){ //只选取离根节点最近的那个funcnameexpr作为函数名，因为可能函数调用内部嵌套另一个函数调用
//            this.funcCallName = ctx.getText();
//        }
//        this.isFirstTriggered = false;
//    }


    @Override
    public void enterPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {
        if (this.isFirstTriggered) {
            this.funcCallName = ctx.getChild(0).getText();
        }
        this.isFirstTriggered = false;
    }

    /**
     * 获取参数列表，这个方法不是很严格，会把嵌套函数调用的参数一起包含进来。但最后可以进行去重。
     * @param ctx
     */
    @Override
    public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx) {
        // 当嵌套时会加入函数，需要去除函数名,只加入参数。
        if (ctx.parent.parent.parent.parent.getRuleContext() instanceof CPP14Parser.UnaryexpressionContext) {
            this.paramList.add(ctx.getText());
        }
    }

    public String getFuncCallName() {
        return funcCallName;
    }

    public ArrayList<String> getParamList() {
//        paramList.remove(0);// 移除函数名
        return paramList;
    }
}
