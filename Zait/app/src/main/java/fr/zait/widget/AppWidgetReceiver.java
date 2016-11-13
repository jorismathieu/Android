package fr.zait.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import fr.zait.MySharedPreferences;
import fr.zait.R;
import fr.zait.activities.MainActivity;

public class AppWidgetReceiver extends android.appwidget.AppWidgetProvider {
    public static final String APP_WIDGET_OPEN_POST_ACTION = "fr.zait.widget.AppWidgetProvider.APP_WIDGET_OPEN_POST_ACTION";
    public static final String EXTRA_POST = "fr.zait.widget.AppWidgetProvider.EXTRA_ITEM";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            // Creation des vues
            Intent intent = new Intent(context, StackWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);
            rv.setEmptyView(R.id.stack_view, R.id.empty_view);

            // Creation du template de l'intent pour click
            Intent openIntent = new Intent(context, MainActivity.class);
            openIntent.setAction(AppWidgetReceiver.APP_WIDGET_OPEN_POST_ACTION);
            openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.stack_view, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(MySharedPreferences.SELECTED_SUBREDDIT_CHANGE_BROADCAST)) {
            int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, AppWidgetReceiver.class));
            onUpdate(context, AppWidgetManager.getInstance(context), ids);

        } else {
            super.onReceive(context, intent);
        }
    }
}
