package fr.zait.adapters.base;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;

public abstract class MyAdapter extends CursorAdapter {
    protected LayoutInflater layoutInflater;
    protected Context cxt;

    public MyAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

}
