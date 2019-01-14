package Listeners;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;
import models.*;
import antlrfiles.*;

public class GetDataFlow {
    private LinkedList<QueueItemBean> searchNodeQueue = new LinkedList<>();
    private ArrayList<FinalResultBean> result = new ArrayList<>();
    private Map<String, ArrayList<String>> paramMap = null;
    private Map<String, ArrayList<CallerBean>> callRelation = null;
    private ArrayList<APITreeBean> apiList = null;

    public GetDataFlow(Map<String, ArrayList<CallerBean>> _callRelation, ArrayList<APITreeBean> _apiList, Map<String, ArrayList<String>> _paramMap) {
        this.callRelation = _callRelation;
        this.apiList = _apiList;
        this.paramMap = _paramMap;
    }

    public void addSearchNode(ParseTree _top, ParseTree _target, String _paramName) {
        QueueItemBean searchNode = new QueueItemBean();
        searchNode.setTraceVarName(_paramName);
        searchNode.setCurrentPointTree(_target);
        searchNode.setTopTree(_top);
        this.searchNodeQueue.add(searchNode);
    }

    public ArrayList<FinalResultBean> getResult() {
        return this.result;
    }

    /**
     * 思路：先追踪一个变量的数据流，追踪过程中的其他相关变量加入队列中，然后这个变量的数据流追踪完毕后，再从队列中取另一个
     * 变量进行追踪。
     * 由于追踪变量的过程中，经过的函数的顺序是可以确定的，那么将经过的函数的顺序分别存储起来即可；result只用一个ArrayList，
     * 取出经过函数的数量最多的那个函数顺序，用它来作为排序的基准，将result中的node进行排序即可。
     *
     * @param node
     */
    public void searchAndProcess(QueueItemBean node, ArrayList<Integer> funcSeq, ArrayList<ResultBean> curResult, ArrayList<Integer> callSeq) throws Exception {
        ParseTreeWalker walker = new ParseTreeWalker();
        FuncTreeListener funcTreeListener = new FuncTreeListener(node);
        walker.walk(funcTreeListener, node.getTopTree());
        //遍历完成，保存结果
        curResult.addAll(funcTreeListener.getTmp());
        //new
        //funcSeq.addAll(node.getPreviousLines());
        Integer topLine = funcTreeListener.getTopLine();
        // 防止循环调用
        if (callSeq.contains(topLine)) {
            return;
        } else {
            callSeq.add(topLine);
        }
        if (!funcSeq.contains(topLine)) {
            funcSeq.add(topLine);
        }
        searchNodeQueue.addAll(funcTreeListener.getTmpQueue());
        //查询是否在其他函数中存在调用，递归
        //这个保存函数调用关系的数据结构可能需要修改一下，不宜使用Map来存
        //对调用这个函数的所有函数，均递归调用本函数，结果保存在类全局变量中
        //未保存TopTreeName
        /*
        System.out.println(node.getTopTreeName());
        System.out.println(node.getTraceVarName());
        System.out.println(this.callRelation.containsKey(node.getTopTreeName()));
        System.out.println(this.paramMap.containsKey(node.getTopTreeName()));
        System.out.println(this.paramMap.get(node.getTopTreeName()));
        System.out.println();
        */
        if (this.callRelation.containsKey(node.getTopTreeName()) && this.paramMap.get(node.getTopTreeName()) != null && this.paramMap.get(node.getTopTreeName()).contains(node.getTraceVarName())) {
            Integer paramIndex = this.paramMap.get(node.getTopTreeName()).indexOf(node.getTraceVarName());
            for (CallerBean caller : this.callRelation.get(node.getTopTreeName())) {
                //对于函数重载的情况，在追踪时只要参数数量一致，就认为是同一个函数。用函数类型作为函数重载的特征比较复杂，这里先简单处理。
                //if(caller.getCalleeParamList().size() != )
                QueueItemBean newNode = new QueueItemBean();
                newNode.setTopTree(caller.getCallerTree());
                newNode.setCurrentPointTree(caller.getCalleeTree());
                newNode.setTopTreeName(caller.getCallerName());
                if (paramIndex <= caller.getCalleeParamList().size() - 1) {
                    newNode.setTraceVarName(caller.getCalleeParamList().get(paramIndex));
                } else {
                    return;
                }
                newNode.getPreviousLines().addAll(node.getPreviousLines());
                if (!newNode.getPreviousLines().contains(funcTreeListener.getTopLine())) {
                    newNode.getPreviousLines().add(funcTreeListener.getTopLine());
                }
                searchAndProcess(newNode, funcSeq, curResult, callSeq);
            }
        }
    }

    /**
     * 将每个API的参数作为待追踪变量放入队列，当这个变量的所有结果均以得出，再放入另一个API的参数。
     */
    public void process() {
        for (APITreeBean api : this.apiList) {
            ArrayList<ResultBean> lines = new ArrayList<>(); //当前API的所有相关代码行的列表
            ArrayList<ArrayList<Integer>> funcSeq = new ArrayList<>(); //当前API的所有参数在追踪时所经过的函数行号
            for (String param : api.getParamList()) {
                ArrayList<Integer> curFuncSeq = new ArrayList<>();
                QueueItemBean item = new QueueItemBean();
                item.setTraceVarName(param);
                item.setTopTreeName(api.getTopName());
                item.setTopTree(api.getTopTree());
                item.setCurrentPointTree(api.getAPITree());
                this.searchNodeQueue.add(item);
                while (!this.searchNodeQueue.isEmpty()) {
                    QueueItemBean node = this.searchNodeQueue.getFirst();
                    this.searchNodeQueue.removeFirst();
                    curFuncSeq.addAll(node.getPreviousLines());
                    ArrayList<Integer> callSeq = new ArrayList<>();
                    try {
                        this.searchAndProcess(node, curFuncSeq, lines, callSeq);
                    } catch (Exception e) {
                        curFuncSeq.clear();
                        continue;
                    }
                    ArrayList<Integer> addedFuncSeq = (ArrayList<Integer>) curFuncSeq.clone();
                    funcSeq.add(addedFuncSeq);
                    curFuncSeq.clear();
                }
            }
            //System.out.println("---");
            //System.out.println(funcSeq);
            if (funcSeq.isEmpty()) {
                return;
            }
            ArrayList<Integer> maxFuncSeq = Collections.max(funcSeq, new Comparator<ArrayList<Integer>>() {
                @Override
                public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                    return o1.size() - o2.size();
                }
            });
            FinalResultBean finalResult = new FinalResultBean();
            finalResult.setFuncSeq(maxFuncSeq);
            finalResult.setLines(lines);
            ParserRuleContext apitree = (ParserRuleContext) api.getAPITree();
            finalResult.setApiLine(apitree.start.getLine());
            this.result.add(finalResult);
        }
    }
}
