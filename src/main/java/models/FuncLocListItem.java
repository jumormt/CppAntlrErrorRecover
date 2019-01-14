package models;

import java.util.*;

public class FuncLocListItem {
    Integer lineNum = null; // 该自定义函数所在行号
    LinkedList<Integer> lineList = new LinkedList<Integer>(); // 数据流流经该函数块内的代码行

    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public LinkedList<Integer> getLineList() {
        return lineList;
    }

    public void setLineList(LinkedList<Integer> lineList) {
        this.lineList = lineList;
    }
}
