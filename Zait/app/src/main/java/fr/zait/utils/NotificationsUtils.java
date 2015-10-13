package fr.zait.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import fr.zait.MySharedPreferences;
import fr.zait.R;
import fr.zait.activities.MainActivity;
import fr.zait.activities.SettingsActivity;
import fr.zait.gcm.MyGcmListenerService;

public class NotificationsUtils
{

    private static final String OPEN_ACTION = "OpenAction";
    private static final String OPEN_TYPE = "OpenType";
    private static final String OPEN_PATH = "OpenPath";

    private static class PATHS {
        private static final String SETTINGS = "Settings";
    }

    public static void generateNotification(Context context, Bundle data) {

        try {

            String title = StringUtils.isEmpty(data.getString(MyGcmListenerService.TITLE)) ? "" : data.getString(MyGcmListenerService.TITLE);
            String mssg = StringUtils.isEmpty(data.getString(MyGcmListenerService.MESSAGE)) ? "" : data.getString(MyGcmListenerService.MESSAGE);
            boolean sound = false;
            if (!StringUtils.isEmpty(data.getString(MyGcmListenerService.SOUND))) {
                sound = data.getString(MyGcmListenerService.SOUND).equals("true") ? true : false;
            }
            boolean vibrate = false;
            if (!StringUtils.isEmpty(data.getString(MyGcmListenerService.VIBRATE))) {
                vibrate = data.getString(MyGcmListenerService.VIBRATE).equals("true") ? true : false;
            }
            int type = MyGcmListenerService.TYPES.DEFAULT;
            if (!StringUtils.isEmpty(data.getString(MyGcmListenerService.TYPE))) {
                type = Integer.valueOf(data.getString(MyGcmListenerService.TYPE));
            }
            String path = StringUtils.isEmpty(data.getString(MyGcmListenerService.PATH)) ? "" : data.getString(MyGcmListenerService.PATH);

            long when = System.currentTimeMillis();
            int notificationID = MySharedPreferences.getSharedPreferences(context).getInt(MySharedPreferences.NOTIFICATION_ID, 1);

            Intent intentNotif = null;

            if (type == MyGcmListenerService.TYPES.EXTERNAL_WEBVIEW) {
                intentNotif = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            } else if (type == MyGcmListenerService.TYPES.INTERNAL_WEBVIEW || type == MyGcmListenerService.TYPES.INTERNAL_FEATURE) {
                intentNotif = new Intent(context, MainActivity.class);
                intentNotif.setAction(OPEN_ACTION);
                intentNotif.putExtra(OPEN_TYPE, type);
                intentNotif.putExtra(OPEN_PATH, path);
            } else {
                intentNotif = new Intent(context, MainActivity.class);
            }

            if (intentNotif != null) {
                intentNotif.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent pendingIntentNotification = PendingIntent.getActivity(context, notificationID, intentNotif, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setAutoCancel(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setColor(0xff673ab7);
                    builder.setSmallIcon(R.drawable.ic_launcher_white);
                } else {
                    builder.setSmallIcon(R.drawable.ic_launcher);
                }

                builder.setLights(Color.WHITE, 1000, 3000);
                builder.setWhen(when);
                builder.setContentTitle(title);
                builder.setContentText(mssg);
                builder.setContentIntent(pendingIntentNotification);
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(mssg));

                if (sound) {
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    if (uri != null && !DateUtils.isDuringNight()) {
                        builder.setSound(uri);
                    }
                }

                if (vibrate && !DateUtils.isDuringNight()) {
                    builder.setVibrate(new long[]{200, 1000});
                }

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notificationID, builder.build());

                MySharedPreferences.getSharedPreferences(context).edit().putInt(MySharedPreferences.NOTIFICATION_ID, ++notificationID % 32);
            }

        } catch(Exception e) {
        }

    }

    public static void checkOpenInternal(MainActivity context, Intent currentIntent) {
        try {
            if ((currentIntent.getAction() != null) && (currentIntent.getAction().equals(NotificationsUtils.OPEN_ACTION))) {
                int type = currentIntent.getIntExtra(OPEN_TYPE, MyGcmListenerService.TYPES.DEFAULT);
                if (type == MyGcmListenerService.TYPES.INTERNAL_FEATURE) {
                    switch (currentIntent.getStringExtra(OPEN_PATH)) {
                        case PATHS.SETTINGS:
                            Intent intent = new Intent(context, SettingsActivity.class);
                            context.startActivity(intent);
                            context.overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                            break;
                    }
                }
            }
        } catch(Exception e) {
        }
    }


}
