package Listeners;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import models.*;
import antlrfiles.*;

public class CppFileLoader extends CPP14BaseListener {
    private Map<String,ArrayList<CallerBean>> callRelation = new HashMap<>();
    private Map<String,ArrayList<String>> paramMap = new HashMap<>();
    private ArrayList<APITreeBean> APITreeList = new ArrayList<>();
    private ParseTree currentTopTree = null; //当前顶层函数的语法树
    private String currentTopName = null; //当前顶层函数的名称
    private ArrayList<String> currentTopParamList = null;
    private ArrayList<CallerBean> tmpCallerList = new ArrayList<>();
    private ArrayList<CallerBean> callerList = new ArrayList<>();
    private final HashSet<String> apiList = new HashSet<String>(){
        {
            add("memcpy");add("strcmp");add("fputc");
            add("fprintf");add("memmove");add("sprintf");
            add("free");add("memcmp");add("abs");
            add("memset");add("strlen");add("strncmp");
            add("fclose");add("fopen");add("wprintf");
            add("printf");add("qsort");add("bind");
            add("va_end");add("strtoul");add("wcslen");
            add("fflush");add("vfprintf");add("va_start");
            add("wcsncat");add("vsnprintf");add("isspace");
            add("wcscpy");add("scanf");add("fscanf");
            add("get_fid");add("strtol");add("wmemset");
            add("calloc");add("malloc");add("atoi");
            add("_wgetenv");add("_wgetenv_s");add("catgets");
            add("gets");add("getchar");add("wmemcpy");
            add("_memccpy");add("wmemmove");add("wmemcmp");
            add("memchr");add("wmemchr");add("_strncpy");
            add("lstrcpyn");add("_tcsncpy");add("_mbsnbcpy");
            add("_wcsncpy");add("snprintf");add("getenv_s");
            add("mkdir");add("longjmp");add("strncat");
            add("strncpy");add("snprintf");add("getenv_s");
            add("mkdir");add("longjmp");add("strncat");
            add("strncpy");add("snprintf");add("getenv_s");
            add("g_snprintf");add("strcpy");
        }
    };

    /**
     * ctor,添加需要追踪的api名称
     */
    public CppFileLoader(){
        //可以从文件中读取API列表
        //this.apiList.add("sscanf");
    }

    /**
     * 进入函数定义语法树，保存当前语法树根节点，并提取出当前函数定义的名称
     * @param ctx
     */
    @Override
    public void enterFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        this.currentTopTree = ctx;
        ParseTreeWalker walker = new ParseTreeWalker();
        GetFuncDefName getFuncDefName = new GetFuncDefName();
        walker.walk(getFuncDefName,ctx);
        this.currentTopName = getFuncDefName.getFuncDefName();
        this.currentTopParamList = getFuncDefName.getParamList();
        this.paramMap.put(this.currentTopName,this.currentTopParamList);
    }

    @Override
    public void exitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        this.currentTopTree = null;
        this.currentTopName = null;
        this.currentTopParamList = null;
    }

//    /**
//     * 进入函数调用的语法树，保存当前语法树根节点，提取函数调用的函数名，及参数列表
//     * @param ctx
//     */
//    @Override
//    public void enterFuncnameexpr(CPP14Parser.FuncnameexprContext ctx) {
//        ParseTree funcCallTree = (ctx.parent);
//        ParseTreeWalker walker = new ParseTreeWalker();
//        GetFuncCall getFuncCall = new GetFuncCall();
//        walker.walk(getFuncCall,funcCallTree);
//        String funcCallName = getFuncCall.getFuncCallName();
//        ArrayList<String> paramList = getFuncCall.getParamList();
//        if(this.apiList.contains(funcCallName)){
//            APITreeBean api = new APITreeBean();
//            api.setAPITree(funcCallTree);
//            api.setTopTree(this.currentTopTree);
//            api.setParamList(paramList);
//            api.setAPIName(funcCallName);
//            api.setTopName(this.currentTopName);
//            this.APITreeList.add(api);
//        }
//        CallerBean caller = new CallerBean();
//        caller.setCallerName(this.currentTopName);
//        caller.setCalleeName(funcCallName);
//        caller.setCallerTree(this.currentTopTree);
//        caller.setCallerParamList(this.currentTopParamList);
//        caller.setCalleeTree(funcCallTree);
//        caller.setCalleeParamList(paramList);
//        this.tmpCallerList.add(caller);
//    }

    @Override
    public void enterPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {
        // 判断是函数调用
        if (ctx.getChild(0).getClass() == ctx.getClass() && ctx.getChild(1).getText().equals("(")) {
//            System.out.println("funcall");
            ParseTreeWalker walker = new ParseTreeWalker();
            GetFuncCall getFuncCall = new GetFuncCall();
            walker.walk(getFuncCall,ctx);
            String funcCallName = getFuncCall.getFuncCallName();
            ArrayList<String> paramList = getFuncCall.getParamList();
            if(this.apiList.contains(funcCallName)){
                APITreeBean api = new APITreeBean();
                api.setAPITree(ctx);
                api.setTopTree(this.currentTopTree);
                api.setParamList(paramList);
                api.setAPIName(funcCallName);
                api.setTopName(this.currentTopName);
                api.setApiLine(ctx.start.getLine());
                this.APITreeList.add(api);
            }
            CallerBean caller = new CallerBean();
            caller.setCallerName(this.currentTopName);
            caller.setCalleeName(funcCallName);
            caller.setCallerTree(this.currentTopTree);
            caller.setCallerParamList(this.currentTopParamList);
            caller.setCalleeTree(ctx);
            caller.setCalleeParamList(paramList);
            this.tmpCallerList.add(caller);
        }
    }

    /**
     *
     * @param ctx
     */
    @Override
    public void visitErrorNode(ErrorNode ctx) {
        if (this.apiList.contains(ctx.getText())) {
            APITreeBean api = new APITreeBean();
//            api.setAPITree(ctx);
            api.setAPITree(ctx);
            api.setApiLine(ctx.getSymbol().getLine());
            api.setTopTree(this.currentTopTree);
//            api.setParamList(paramList);
            api.setParamList(null);
            api.setAPIName(ctx.getText());
            api.setTopName(this.currentTopName);
            //TODO:获取api的参数列表
            ParseTreeWalker walker = new ParseTreeWalker();
            GetErrorApiParam getErrorApiParam = new GetErrorApiParam(ctx.getText());
            ParseTree errorParnt = ctx.getParent();
            while(!getErrorApiParam.isEnd() && errorParnt!=null) {
                getErrorApiParam = new GetErrorApiParam(ctx.getText());
                walker.walk(getErrorApiParam, errorParnt);
                errorParnt = errorParnt.getParent();
            }

            api.setParamList(getErrorApiParam.getParamList());

            this.APITreeList.add(api);
        }
    }

    @Override
    public void exitPostfixexpression(CPP14Parser.PostfixexpressionContext ctx) {
        if (ctx.getChild(0).getClass() == ctx.getClass() && ctx.getChild(1).getText().equals("(")) {
            this.callerList.addAll(this.tmpCallerList);
            this.tmpCallerList.clear();
        }
    }

    @Override
    public void enterDeleteexpression(CPP14Parser.DeleteexpressionContext ctx) {
        ParseTree deleteTree = ctx.parent;
        ParseTreeWalker walker = new ParseTreeWalker();
        GetDeleteId getDeleteId = new GetDeleteId();
        walker.walk(getDeleteId,deleteTree);
        String deleteId = getDeleteId.getId();
        APITreeBean api = new APITreeBean();
        api.setAPITree(deleteTree);
        api.setTopTree(this.currentTopTree);
        api.setApiLine(ctx.start.getLine());
        ArrayList<String> paramList = new ArrayList<>();
        paramList.add(deleteId);
        api.setParamList(paramList);
        api.setAPIName("delete");
        api.setTopName(this.currentTopName);
        this.APITreeList.add(api);
    }

    @Override
    public void enterStatement(CPP14Parser.StatementContext ctx) {
        super.enterStatement(ctx);
    }

    //    /**
//     * 退出函数体后，更新本函数所调用的所有其他函数的列表。
//     * @param ctx
//     */
//    @Override
//    public void exitFuncnameexpr(CPP14Parser.FuncnameexprContext ctx) {
//        /*
//        Map<String,ArrayList<CallerBean>> tmp = new HashMap<>();
//        for(CallerBean caller : this.tmpCallerList){
//            if(!tmp.containsKey(caller.getCalleeName())){
//                tmp.put(caller.getCalleeName(),new ArrayList<CallerBean>());
//            }
//            tmp.get(caller.getCalleeName()).add(caller);
//        }
//        for(Map.Entry<String,ArrayList<CallerBean>> item : tmp.entrySet()){
//            ArrayList<CallerBean> callerList = item.getValue();
//            this.callerList.add(callerList.get(callerList.size() - 1));
//        }
//        */
//        this.callerList.addAll(this.tmpCallerList);
//        this.tmpCallerList.clear();
//    }

    public void mergeCallerList(){
        for(CallerBean caller : this.callerList){
            if(!this.callRelation.containsKey(caller.getCalleeName())){
                this.callRelation.put(caller.getCalleeName(),new ArrayList<CallerBean>());
            }
            this.callRelation.get(caller.getCalleeName()).add(caller);
        }
    }

    public ArrayList<APITreeBean> getAPITreeList() {
        return APITreeList;
    }

    public Map<String, ArrayList<CallerBean>> getCallRelation() {
        return callRelation;
    }

    public Map<String, ArrayList<String>> getParamMap() {
        return paramMap;
    }


    public void removeExtraChar(){
//        this.outPutLine = this.outPutLine.replaceAll("<missing \'(.)\'>","");

/*
        if(this.outPutLine.endsWith("<missing ';'> ")){
            this.outPutLine=this.outPutLine.substring(0,this.outPutLine.length()-14);
        }
*/
    }
}