package Listeners;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import models.*;
import antlrfiles.*;

/**
 * 传入function根节点
 */
public class FuncTreeListener extends CPP14BaseListener {
    private ArrayList<ResultBean> tmp = new ArrayList<>(); // 当前变量相关流经的代码行集合
    private ArrayList<QueueItemBean> tmpQueue = new ArrayList<>(); // 和当前变量相关的变量集合
    private QueueItemBean item = null;
    private Integer topLine = 0;
    private boolean isFinished = false;

    public FuncTreeListener(QueueItemBean _item) throws Exception{
        this.item = _item;
        CPP14Parser.FunctiondefinitionContext topTree = (CPP14Parser.FunctiondefinitionContext)(_item.getTopTree());
        this.topLine = topTree.start.getLine();
        //this.item.getPreviousLines().add(this.topLine);
    }

    /**
     * 如果发现某id节点的名称与追踪变量名称相同，则把它加入到临时结果内
     * @param ctx
     */
    @Override
    public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx){
        if(!isFinished){
            if(ctx.getText().equals(item.getTraceVarName())){
                Integer targetLine = ctx.start.getLine();
                ResultBean tmpResult = new ResultBean();
                tmpResult.setNodeLine(targetLine);
                tmpResult.setFuncLine(this.topLine);
                this.tmp.add(tmpResult);
            }
        }
    }

    /**
     * 对于赋值语句，如果赋值号左边的变量名与追踪变量名称相同，则把右边的符号加入到待追踪列表内。
     * （先简单处理，不考虑定值范围和污染传递，那样太复杂）
     * @param ctx
     */
    @Override
    public void enterAssignmentexpression(CPP14Parser.AssignmentexpressionContext ctx) {
        //CPP14这个规则比较复杂，其语法树节点中会存在冗余信息，所以需要找出真正的Assignmentexpression
        //System.out.println(ctx.getChildCount());
        if(ctx.getChildCount() != 3){
            return;
        }
        if(!isFinished){
            ParseTreeWalker walker = new ParseTreeWalker();
            GetLeftRightValue getLeftValue = new GetLeftRightValue();
            walker.walk(getLeftValue,ctx.parent);
            QueueItemBean lvalue = getLeftValue.getLvalue();
            //lvalue.setTopTree(this.item.getTopTree());
            ArrayList<QueueItemBean> rvalues = getLeftValue.getRvalueList();
            if(rvalues != null && lvalue != null){
                for(QueueItemBean rvalue : rvalues){
                    rvalue.setTopTree(this.item.getTopTree());
                    rvalue.setTopTreeName(this.item.getTopTreeName());
                }
                if(lvalue.getTraceVarName().equals(this.item.getTraceVarName())){
                    this.tmpQueue.clear();
                    for(QueueItemBean rvalue : rvalues){
                        if(!rvalue.getTraceVarName().equals(this.item.getTraceVarName())){
                            rvalue.getPreviousLines().addAll(item.getPreviousLines());
                            this.tmpQueue.add(rvalue);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        if(!isFinished){
            if(ctx == this.item.getCurrentPointTree()){
                Integer targetLine = ctx.start.getLine();
                ResultBean tmpResult = new ResultBean();
                tmpResult.setNodeLine(targetLine);
                tmpResult.setFuncLine(this.topLine);
                this.tmp.add(tmpResult);
                //说明后面的不需要进行搜索了,success
                this.isFinished = true;
            }
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        if (!isFinished) {
            if (node == this.item.getCurrentPointTree()) {
                Integer targetLine = node.getSymbol().getLine();
                ResultBean tmpResult = new ResultBean();
                tmpResult.setNodeLine(targetLine);
                tmpResult.setFuncLine(this.topLine);
                this.tmp.add(tmpResult);
                //说明后面的不需要进行搜索了,success
                this.isFinished = true;
            }
        }
    }

    public ArrayList<ResultBean> getTmp() {
        return tmp;
    }

    public ArrayList<QueueItemBean> getTmpQueue() {
        return tmpQueue;
    }

    public Integer getTopLine() {
        return topLine;
    }
}
