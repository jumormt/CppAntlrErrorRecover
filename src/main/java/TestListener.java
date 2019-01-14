//: TestListener.java


import Listeners.*;
import antlrfiles.CPP14Lexer;
import antlrfiles.CPP14Parser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import models.*;

import java.io.*;
import java.util.*;
import utils.*;

public class TestListener {



    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if (args.length > 0) inputFile = args[0];
        else inputFile = "/Users/chengxiao/Desktop/源码检测/Antlr4L/cppDataFlow/testgrammar0/errorRecover/src/main/resources/CWE426_Untrusted_Search_Path__char_popen_82a.cpp";
        InputStream is = System.in;
        if ( inputFile!=null ) {
            is = new FileInputStream(inputFile);
        }
        ANTLRInputStream input = new ANTLRInputStream(is);
        CPP14Lexer lexer = new CPP14Lexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        CPP14Parser cpp14Parser = new CPP14Parser(tokenStream);
        cpp14Parser.removeErrorListeners(); // remove ConsoleErrorListener
        TestErrorListner testErrorListner = new TestErrorListner();
        cpp14Parser.addErrorListener(testErrorListner); // add ours
        CPP14Parser.TranslationunitContext translationunit = cpp14Parser.translationunit();

        ParseTreeWalker walker = new ParseTreeWalker();

        CppFileLoader loader = new CppFileLoader();
        walker.walk(loader, translationunit);
        loader.mergeCallerList();


        //如果在解析语法树的时候发生错误，那么停止对该文件的处理
        if (testErrorListner.isErrorOccurred()) {
            for (TestErrorListner.ErrorLineBean errorLineBean : testErrorListner.getErrorLineBeans()) {
                System.out.println(errorLineBean.getErrorLineTitle());
                System.out.println();
                System.out.println(errorLineBean.getErrorLineString());
                System.out.println(errorLineBean.getErrorLineflag());
                System.out.println("-----------------------------------");

            }
        }
        ArrayList<APITreeBean> apis = loader.getAPITreeList(); // 该代码包含的api列表
        Map<String, ArrayList<String>> paramMap = loader.getParamMap(); // 该代码包含的函数名及其参数映射
        loader.mergeCallerList();
        Map<String, ArrayList<CallerBean>> call = loader.getCallRelation(); // 该代码包含的函数及调用该函数的函数列表映射（这里只考虑了backward）

        GetDataFlow getDataFlow = new GetDataFlow(call, apis, paramMap);
        getDataFlow.process();
        ArrayList<FinalResultBean> result = getDataFlow.getResult();
        is = null;
        input = null;
        lexer = null;
        tokenStream = null;
        cpp14Parser = null;
        walker = null;
        loader = null;
        getDataFlow = null;

        ArrayList<String> fileContents = CodeGadgetUtils.CODE_GADGET_UTILS.readFile(new File(inputFile));

        for (FinalResultBean finalResult : result) {
            LinkedList<FuncLocListItem> tmp = new LinkedList<>();
            for (Integer i : finalResult.getFuncSeq()) {
                FuncLocListItem funcLocListItem = new FuncLocListItem();
                funcLocListItem.setLineNum(i);
                tmp.add(0, funcLocListItem);
            }
            FuncLocListItem otherItem = new FuncLocListItem();
            otherItem.setLineNum(0);
            tmp.add(0, otherItem);
            for (ResultBean r : finalResult.getLines()) {
                LinkedList<Integer> tmpList = CodeGadgetUtils.CODE_GADGET_UTILS.findByLineNum(tmp, r.getFuncLine());
                if (tmpList != null) {
                    if (!tmpList.contains(r.getNodeLine()))
                        tmpList.add(r.getNodeLine());
                } else {
                    if (!tmp.get(0).getLineList().contains(r.getNodeLine()))
                        tmp.get(0).getLineList().add(r.getNodeLine());
                }
            }
            for (FuncLocListItem f : tmp) {
                f.getLineList().sort(Comparator.comparing(Integer::intValue));
            }
            try {
                Integer lineCount = CodeGadgetUtils.CODE_GADGET_UTILS.getLineCount(tmp);
                if (lineCount >= 3) {
                    CodeGadgetUtils.CODE_GADGET_UTILS.output(tmp, fileContents, new File(inputFile), finalResult.getApiLine());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



//        System.out.println(loader);

        System.out.println("end");
    }

}
