package fr.zait.holders.base;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

public abstract class MyHolder {
    protected Context cxt;
    protected View rootView;

    public MyHolder(Context context, View view) {
        cxt = context;
        rootView = view;
    }

    public abstract void populateView(Cursor c);
}
