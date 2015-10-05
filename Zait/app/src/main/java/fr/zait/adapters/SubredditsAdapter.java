package fr.zait.adapters;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.zait.R;
import fr.zait.adapters.base.MyAdapter;
import fr.zait.holders.SubredditsHolder;

public class SubredditsAdapter extends MyAdapter
{
    public SubredditsAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        cxt = context;
        layoutInflater = LayoutInflater.from(cxt);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = layoutInflater.inflate(R.layout.my_subreddits_layout_listview, parent, false);
        view.setTag(new SubredditsHolder(context, view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ((SubredditsHolder) view.getTag()).populateView(cursor);
    }

    @Override
    public int getCount() {
        if (getCursor() != null) {
            return getCursor().getCount();
        }
        return 0;
    }

}
