package fr.zait;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences
{
    public static final String FIRST_RUN = "first_run";
    public static final String SELECTED_SUBREDDIT = "selected_subreddit";

    public static final String PACKAGE_NAME = "fr.zait";

    public static SharedPreferences getMySharedPreferences(Context context) {
        return context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

}
