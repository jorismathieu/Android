package fr.zait.holders;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import fr.zait.R;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.holders.base.MyHolder;

public class SubredditsHolder extends MyHolder
{
    private TextView subredditTv;

    public SubredditsHolder(Context context, View view) {
        super(context, view);

        subredditTv = (TextView) view.findViewById(R.id.subreddit_name);
    }

    @Override
    public void populateView(Cursor c)
    {
        String subredditName = c.getString(c.getColumnIndex(SubredditsContract.SubredditsEntry.COLUMN_NAME));

        subredditTv.setText(subredditName);

    }
}
