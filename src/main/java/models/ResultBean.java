package models;

/** api相关的单个代码行
 * @author
 * @version
 */
public class ResultBean {
    private Integer nodeLine = null; // 代码行
    private Integer funcLine = null; // 代码行所在函数行

    public ResultBean() {
    }

    public Integer getFuncLine() {
        return funcLine;
    }

    public Integer getNodeLine() {
        return nodeLine;
    }

    public void setFuncLine(Integer funcLine) {
        this.funcLine = funcLine;
    }

    public void setNodeLine(Integer nodeLine) {
        this.nodeLine = nodeLine;
    }
}