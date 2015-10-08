package fr.zait.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fr.zait.R;
import fr.zait.utils.DialogUtils;

public class ChangeTextSizeDialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        String[] textSizes = getContext().getResources().getStringArray(R.array.textSizes);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(textSizes, 1, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                dismiss();
            }
        }).setNegativeButton(R.string.cancel, null);

        final AlertDialog dialog = builder.create();

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
