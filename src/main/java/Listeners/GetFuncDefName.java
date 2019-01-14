package Listeners;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import models.*;
import antlrfiles.*;

/**
 * 配合ParseTreeWalker的walk方法使用，输入为FuncDefinition类型的树，返回该函数定义名称的String
 */
public class GetFuncDefName extends CPP14BaseListener {
    private String funcDefName = null;
    private boolean isFirstTriggered = true;
    private ArrayList<String> paramList = null;

    @Override
    public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx) {
        if(this.isFirstTriggered){
            this.funcDefName = ctx.getText();
        }
        this.isFirstTriggered = false;
    }

    @Override
    public void enterParameterdeclarationlist(CPP14Parser.ParameterdeclarationlistContext ctx) {
        ParseTree funcCallTree = ctx;
        ParseTreeWalker walker = new ParseTreeWalker();
        GetFuncDefParams getFuncDefParams = new GetFuncDefParams();
        walker.walk(getFuncDefParams,ctx.parent);
        ArrayList<String> paramList = getFuncDefParams.getParamList();
        this.paramList = paramList;
    }

    public String getFuncDefName(){
        return this.funcDefName;
    }

    public ArrayList<String> getParamList() {
        return paramList;
    }
}
