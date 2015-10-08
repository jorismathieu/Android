package fr.zait.dialogs;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.EditText;

import fr.zait.R;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.utils.DialogUtils;
import fr.zait.utils.StringUtils;

public class AddSubredditDialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_add_subreddit_view, null))
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText subredditEt = (EditText) getDialog().findViewById(R.id.subreddit_name);
                        String subredditName = subredditEt.getText().toString();
                        if (!StringUtils.isEmpty(subredditName)) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(SubredditsContract.SubredditsEntry.COLUMN_NAME, subredditName);
                            getActivity().getContentResolver().insert(SubredditsContract.ADD_URI, contentValues);
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, null);

        final AlertDialog dialog = builder.create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface arg)
            {
                DialogUtils.setButtonStyle(dialog, getActivity());
            }
        });

        return dialog;
    }
}
