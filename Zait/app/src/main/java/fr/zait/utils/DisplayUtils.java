package fr.zait.utils;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class DisplayUtils
{
    public static void toast(Context context, int mssgId, int duration) {
        Toast.makeText(context, mssgId, duration).show();
    }

    public static void toast(Context context, String mssg, int duration) {
        Toast.makeText(context, mssg, duration).show();
    }

    public static void snackbar(View view, int mssgId, int duration) {
        Snackbar.make(view, mssgId, duration).show();
    }

    public static void snackbar(View view, String mssg, int duration) {
        Snackbar.make(view, mssg, duration).show();
    }

}
