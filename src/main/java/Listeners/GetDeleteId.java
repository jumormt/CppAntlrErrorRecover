package Listeners;
import models.*;
import antlrfiles.*;

/**
 * 配合deleteexpression使用
 */
public class GetDeleteId extends CPP14BaseListener{
    private String id = null;

    @Override
    public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx) {
        this.id = ctx.getText();
    }

    public String getId() {
        return id;
    }
}
