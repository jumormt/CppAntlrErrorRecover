package models;

import java.util.ArrayList;

/** 一个api的数据流
 * @author
 * @version
 */
public class FinalResultBean {

    private ArrayList<ResultBean> lines = null; // 和该api形参数据流相关的代码行
    private ArrayList<Integer> funcSeq = null;
    private Integer apiLine = null;

    public ArrayList<ResultBean> getLines() {
        return lines;
    }

    public void setLines(ArrayList<ResultBean> lines) {
        this.lines = lines;
    }

    public Integer getApiLine() {
        return apiLine;
    }

    public void setApiLine(Integer apiLine) {
        this.apiLine = apiLine;
    }

    public ArrayList<Integer> getFuncSeq() {
        return funcSeq;

    }

    public void setFuncSeq(ArrayList<Integer> funcSeq) {
        this.funcSeq = funcSeq;
    }
}
