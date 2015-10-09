package fr.zait.holders;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import fr.zait.R;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.activities.base.DialogCallbackInterface;
import fr.zait.holders.base.MyHolder;

public class SubredditsHolder extends MyHolder
{
    private TextView subredditTv;
    private View removeIcon;

    public SubredditsHolder(Context context, View view) {
        super(context, view);
        subredditTv = (TextView) view.findViewById(R.id.subreddit_name);
        removeIcon = view.findViewById(R.id.remove_icon);
    }

    @Override
    public void populateView(Cursor c)
    {
        final long id = c.getLong(c.getColumnIndex(SubredditsContract.SubredditsEntry._ID));
        final String subredditName = c.getString(c.getColumnIndex(SubredditsContract.SubredditsEntry.COLUMN_NAME));

        subredditTv.setText(subredditName);
        removeIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String where = SubredditsContract.SubredditsEntry._ID + " = ?";
                String[] whereArgs = {String.valueOf(id)};
                DialogCallbackInterface callbackActivity = (DialogCallbackInterface) cxt;
                callbackActivity.displayDeleteSubredditDialog(subredditName, where, whereArgs);
            }
        });
    }
}
