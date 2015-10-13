package fr.zait;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences
{
    public static final String FIRST_RUN = "FirstRun";
    public static final String SELECTED_SUBREDDIT = "SelectedSubreddit";
    public static final String SENT_TOKEN_TO_SERVER = "SentTokenToServer";
    public static final String NOTIFICATION_ID = "NotificationId";

    public static final String PACKAGE_NAME = "fr.zait";

    public static SharedPreferences getSharedPreferences(Context context) {
       return context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

}
