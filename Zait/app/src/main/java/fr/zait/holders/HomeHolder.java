package fr.zait.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fr.zait.R;

public class HomeHolder extends RecyclerView.ViewHolder
{
    public TextView textView;

    public HomeHolder(View v)
    {
        super(v);
        textView = (TextView) v.findViewById(R.id.description);
    }
}

