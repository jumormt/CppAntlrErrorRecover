package Listeners;

import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import models.*;
import antlrfiles.*;

/**
 * 从assignmentoperator的parent树中找出赋值号左边的值。
 */
public class GetLeftRightValue extends CPP14BaseListener {
    /**
     * 配合enterInitializerclause使用
     */
    public class GetAllRightValue extends CPP14BaseListener {
        ArrayList<QueueItemBean> rvalueList = new ArrayList<>();

        @Override
        public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx) {
            QueueItemBean queueItem = new QueueItemBean();
            queueItem.setTraceVarName(ctx.getText());
            queueItem.setCurrentPointTree(ctx);
            this.rvalueList.add(queueItem);
        }

        public ArrayList<QueueItemBean> getRvalueList() {
            return rvalueList;
        }
    }

    private QueueItemBean lvalue = null;
    private ArrayList<QueueItemBean> rvalueList = null;
    boolean isFirstTriggered = true;

    /**
     * 用于获取Lvalue
     * @param ctx
     */
    @Override
    public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx) {
        if(isFirstTriggered){
            QueueItemBean queueItem = new QueueItemBean();
            queueItem.setTraceVarName(ctx.getText());
            queueItem.setCurrentPointTree(ctx);
            this.lvalue = queueItem;
            isFirstTriggered = false;
        }
    }

    @Override
    public void enterInitializerclause(CPP14Parser.InitializerclauseContext ctx) {
        ParseTreeWalker walker = new ParseTreeWalker();
        GetAllRightValue getAllRightValue = new GetAllRightValue();
        walker.walk(getAllRightValue,ctx);
        this.rvalueList = getAllRightValue.getRvalueList();
    }

    public QueueItemBean getLvalue() {
        return lvalue;
    }

    public ArrayList<QueueItemBean> getRvalueList() {
        return rvalueList;
    }
}
