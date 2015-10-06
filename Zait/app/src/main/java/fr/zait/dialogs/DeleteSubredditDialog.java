package fr.zait.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fr.zait.R;
import fr.zait.data.database.MyContentProvider;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.utils.DialogUtils;

public class DeleteSubredditDialog extends DialogFragment
{

    private static final String SUBREDDIT_NAME = "subredditName";
    private static final String WHERE = "where";
    private static final String WHERE_ARGS = "where_args";

    public static DeleteSubredditDialog newInstance(String subredditName, String where, String[] whereArgs) {
        DeleteSubredditDialog dialog = new DeleteSubredditDialog();

        Bundle args = new Bundle();
        args.putString(SUBREDDIT_NAME, subredditName);
        args.putString(WHERE, where);
        args.putStringArray(WHERE_ARGS, whereArgs);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        String subredditName = "";
        if (getArguments() != null)
        {
            subredditName = getArguments().getString(SUBREDDIT_NAME);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_delete_subreddit_title)
                .setMessage(getString(R.string.delete) + " " + subredditName + "?")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(R.string.ok_upp, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        String where = null;
                        String[] whereArgs = null;
                        if (getArguments() != null) {
                            where = getArguments().getString(WHERE);
                            whereArgs = getArguments().getStringArray(WHERE_ARGS);
                        }

                        getActivity().getContentResolver().delete(MyContentProvider.getURIFromTable(SubredditsContract.TABLE_NAME), where, whereArgs);
                    }
                })
                .setNegativeButton(R.string.cancel_upp, null);

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface arg)
            {
                DialogUtils.setDefaultColors(dialog, getActivity());
            }
        });

        return dialog;
    }
}
