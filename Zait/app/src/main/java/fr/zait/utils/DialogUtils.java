package fr.zait.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import fr.zait.R;

public class DialogUtils
{
    public static void setButtonStyle(AlertDialog dialog, Context context) {
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.primaryDarkColor));
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.primaryDarkColor));

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setAllCaps(true);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setAllCaps(true);
    }

}
