package Listeners;

import java.util.ArrayList;
import models.*;
import antlrfiles.*;

public class GetFuncDefParams extends CPP14BaseListener{
    private ArrayList<String> paramList = new ArrayList<>();

    @Override
    public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx) {
        this.paramList.add(ctx.getText());
    }

    public ArrayList<String> getParamList() {
        return paramList;
    }
}
