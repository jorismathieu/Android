package fr.zait.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import fr.zait.R;

public class ErrorUtils
{
    public final static int CONNECTION_ERROR = 1000;

    public static void displayError(Context context, View view, int error) {
        switch (error) {
            case CONNECTION_ERROR:
                DisplayUtils.snackbar(view, R.string.connection_error, Snackbar.LENGTH_LONG);
                break;
        }
    }

}
