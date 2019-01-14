package Listeners;//: TestErrorListner.java
/**
 * cpp的错误转发机制，解决函数调用无法解析的问题 e.g.
 * g_snprintf(p, (gulong) (1024-(p-buf)), fmt, val_to_str((val & mask) >> shift, tab, "Unknown"));无法解析为函数调用
 * @author:jumormt
 * @version:1.0
 */

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.*;
import models.*;
import antlrfiles.*;


public class TestErrorListner extends BaseErrorListener {

    public static class ErrorLineBean {
        private int errorLine;
//        private String errorMsg;

        public int getErrorLine() {
            return errorLine;
        }

        public void setErrorLine(int errorLine) {
            this.errorLine = errorLine;
        }

//        public String getErrorMsg() {
//            return errorMsg;
//        }
//
//        public void setErrorMsg(String errorMsg) {
//            this.errorMsg = errorMsg;
//        }

        private String errorLineTitle = null;
        private String errorLineString = null;
        private String errorLineflag = null;

        public String getErrorLineTitle() {
            return errorLineTitle;
        }

        public void setErrorLineTitle(String errorLineTitle) {
            this.errorLineTitle = errorLineTitle;
        }

        public String getErrorLineString() {
            return errorLineString;
        }

        public void setErrorLineString(String errorLineString) {
            this.errorLineString = errorLineString;
        }

        public String getErrorLineflag() {
            return errorLineflag;
        }

        public void setErrorLineflag(String errorLineflag) {
            this.errorLineflag = errorLineflag;
        }
    }
    private boolean errorOccurred = false;

    public List<ErrorLineBean> getErrorLineBeans() {
        return errorLineBeans;
    }

    private List<ErrorLineBean> errorLineBeans = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        this.errorOccurred = true;
        String errorMsg = "line"+line+":"+charPositionInLine+" "+msg;
//        System.err.println(errorMsg);
        writeErrorBean(recognizer, (Token)offendingSymbol, line, charPositionInLine, errorMsg);
    }

    /**
     * 标记错误的代码行及错误位置 e.g.
     * g_snprintf(p, (gulong) (1024-(p-buf)), fmt, val_to_str((val & mask) >> shift, tab, "Unknown"));
     *
     * @param recognizer
     * @param offendingToken
     * @param line
     * @param charPositionInLine
     * @param errorMsg
     */
    protected void writeErrorBean(Recognizer recognizer,
                                  Token offendingToken, int line,
                                  int charPositionInLine, String errorMsg) {
        ErrorLineBean errorLineBean = new ErrorLineBean();
        errorLineBean.setErrorLineTitle(errorMsg);
        errorLineBean.setErrorLine(line);
//        errorLineBean.setErrorMsg(errorMsg);
        CommonTokenStream tokens =
                (CommonTokenStream)recognizer.getInputStream();
        String input = tokens.getTokenSource().getInputStream().toString();
        String[] lines = input.split("\n");
        String errorLine = lines[line - 1];
//        System.err.println(errorLine);
        errorLineBean.setErrorLineString(errorLine);
        StringBuilder errFlg = new StringBuilder();
        for (int i=0; i<charPositionInLine; i++) errFlg.append(" ");
        int start = offendingToken.getStartIndex();
        int stop = offendingToken.getStopIndex();
        if ( start>=0 && stop>=0 ) {
            for (int i=start; i<=stop; i++) errFlg.append("^");
        }
        errorLineBean.setErrorLineflag(errFlg.toString());
        errorLineBeans.add(errorLineBean);
//        System.err.println();
    }


    public boolean isErrorOccurred() {
        return errorOccurred;
    }
}
