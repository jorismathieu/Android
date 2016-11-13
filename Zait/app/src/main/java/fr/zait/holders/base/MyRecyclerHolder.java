package fr.zait.holders.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class MyRecyclerHolder extends RecyclerView.ViewHolder {
    protected Context cxt;

    public MyRecyclerHolder(Context context, View view) {
        super(view);
        cxt = context;
    }

    public abstract void populateView(Object obj);
}
