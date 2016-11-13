package fr.zait.controllers.base;


import android.content.Context;
import android.view.View;

public abstract class MyController {
    protected View rootView;
    protected Context context;

    public MyController(Context cxt, View rv) {
        context = cxt;
        rootView = rv;
    }

    protected abstract void initController();
}
