//: TestListener.java


import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TestListener {

    public static void main(String[] args) throws Exception {
        String inputFile = null;
        if (args.length > 0) inputFile = args[0];
        else inputFile = "/Users/chengxiao/Desktop/源码检测/Antlr4L/cppDataFlow/testgrammar0/errorRecover/src/main/resources/value_string_1.c";
        InputStream is = System.in;
        if ( inputFile!=null ) {
            is = new FileInputStream(inputFile);
        }
        ANTLRInputStream input = new ANTLRInputStream(is);
        CPP14Lexer lexer = new CPP14Lexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        CPP14Parser cpp14Parser = new CPP14Parser(tokenStream);
        cpp14Parser.removeErrorListeners(); // remove ConsoleErrorListener
        cpp14Parser.addErrorListener(new TestErrorListner()); // add ours
        CPP14Parser.TranslationunitContext translationunit = cpp14Parser.translationunit();

        ParseTreeWalker walker = new ParseTreeWalker();

        CppFileLoader loader = new CppFileLoader();
        walker.walk(loader, translationunit);
        loader.mergeCallerList();
//        System.out.println(loader);
    }

}
