package fr.zait.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import fr.zait.utils.NotificationsUtils;

public class MyGcmListenerService extends GcmListenerService
{
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";
    public static final String VIBRATE = "vibrate";
    public static final String SOUND = "sound";
    public static final String TYPE = "type";
    public static final String PATH = "path";

    public static class TYPES {
        public static final int DEFAULT = 0;
        public static final int EXTERNAL_WEBVIEW = 100;
        public static final int INTERNAL_WEBVIEW = 200;
        public static final int INTERNAL_FEATURE = 300;
    }

    @Override
    public void onMessageReceived (String from, Bundle data) {
        NotificationsUtils.generateNotification(getApplicationContext(), data);
    }
}
