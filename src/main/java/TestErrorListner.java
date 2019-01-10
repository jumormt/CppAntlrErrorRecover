//: TestErrorListner.java
/**
 * cpp的错误转发机制，解决函数调用无法解析的问题 e.g.
 * g_snprintf(p, (gulong) (1024-(p-buf)), fmt, val_to_str((val & mask) >> shift, tab, "Unknown"));无法解析为函数调用
 * @author:jumormt
 * @version:1.0
 */

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;


public class TestErrorListner extends BaseErrorListener {

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        System.err.println("line "+line+":"+charPositionInLine+" "+msg);
        underlineError(recognizer,(Token)offendingSymbol,
                line, charPositionInLine);
    }

    /**
     * 标记错误的代码行及错误位置 e.g.
     * g_snprintf(p, (gulong) (1024-(p-buf)), fmt, val_to_str((val & mask) >> shift, tab, "Unknown"));
     *                                                                      ^
     * @param recognizer
     * @param offendingToken
     * @param line
     * @param charPositionInLine
     */
    protected void underlineError(Recognizer recognizer,
                                  Token offendingToken, int line,
                                  int charPositionInLine) {
        CommonTokenStream tokens =
                (CommonTokenStream)recognizer.getInputStream();
        String input = tokens.getTokenSource().getInputStream().toString();
        String[] lines = input.split("\n");
        String errorLine = lines[line - 1];
        System.err.println(errorLine);
        for (int i=0; i<charPositionInLine; i++) System.err.print(" ");
        int start = offendingToken.getStartIndex();
        int stop = offendingToken.getStopIndex();
        if ( start>=0 && stop>=0 ) {
            for (int i=start; i<=stop; i++) System.err.print("^");
        }
        System.err.println();
    }
}
