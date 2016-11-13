package fr.zait.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fr.zait.R;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.utils.DialogUtils;

public class ReinitSubredditsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_reinit_subreddits_title).setMessage(R.string.dialog_reinit_subreddits_message).setIcon(R.drawable.ic_warning_black_24dp).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getActivity().getContentResolver().delete(SubredditsContract.DELETE_URI, null, null);
                getActivity().getContentResolver().insert(SubredditsContract.REINIT_URI, null);
            }
        }).setNegativeButton(R.string.cancel, null);

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg) {
                DialogUtils.setButtonStyle(dialog, getActivity());
            }
        });

        return dialog;
    }
}
