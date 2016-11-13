package fr.zait;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MySharedPreferences {

    public static final String SELECTED_SUBREDDIT_CHANGE_BROADCAST = "fr.zait.MySharedPreferences.SELECTED_SUBREDDIT_CHANGE_BROADCAST";

    public static final String FIRST_RUN = "FirstRun";
    public static final String SELECTED_SUBREDDIT = "SelectedSubreddit";
    public static final String SELECTED_FILTER = "SelectedFilter";
    public static final String SENT_TOKEN_TO_SERVER = "SentTokenToServer";
    public static final String NOTIFICATION_ID = "NotificationId";

    public static final String PACKAGE_NAME = "fr.zait";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public static void saveSelectedSubreddit(Context context, String selectedSubreddit) {
        MySharedPreferences.getSharedPreferences(context).edit().putString(SELECTED_SUBREDDIT, selectedSubreddit).commit();


//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        ComponentName thisWidget = new ComponentName(context, AppWidgetReceiver.class);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);

//        Intent intent = new Intent(context, AppWidgetReceiver.class);
//        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, AppWidgetReceiver.class));
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//        context.sendBroadcast(intent);

        Intent intent = new Intent();
        intent.setAction(SELECTED_SUBREDDIT_CHANGE_BROADCAST);
        context.sendBroadcast(intent);

    }

    public static void saveSelectedFilter(Context context, String selectedFilter) {
        MySharedPreferences.getSharedPreferences(context).edit().putString(SELECTED_FILTER, selectedFilter).commit();
    }

}
