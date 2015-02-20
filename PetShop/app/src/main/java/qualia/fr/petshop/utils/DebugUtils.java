package qualia.fr.petshop.utils;

import android.content.Context;
import android.widget.Toast;

public class DebugUtils
{
    public static void  log(String text)
    {
        System.out.println("DEBUG ===> " + text);
    }

    public static void logToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_LONG);
    }
}
