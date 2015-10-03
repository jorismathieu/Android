package fr.zait.controllers.base;


import android.content.Context;
import android.view.View;

public abstract class MainController
{
    protected View rootView;
    protected Context context;

    public MainController(Context cxt, View rv) {
        context = cxt;
        rootView = rv;
    }

    protected abstract void initController();
}
