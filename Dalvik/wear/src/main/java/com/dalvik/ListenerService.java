package com.dalvik;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import static com.dalvik.SendPhoneMessageActivity.PHONE_NODE_ID;

public class ListenerService extends WearableListenerService {

    public static final int NOTIF_ID = 100;

    public static final long[] VIBRATION_PATTERN = new long[] { 500, 500, 500, 500, 500 };

    private static final String TITLE = "Prévenir Yann ?";
    private static final String ENVOYER = "Envoyer";

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        Wearable.MessageApi.addListener(googleApiClient, this);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String phoneNodeId = messageEvent.getSourceNodeId();
        displayNotification(getApplicationContext(), phoneNodeId);
    }

    private void displayNotification(Context context, String phoneNodeId) {

        Intent sendPhoneMessageIntent = new Intent(context, SendPhoneMessageActivity.class);
        sendPhoneMessageIntent.putExtra(PHONE_NODE_ID, phoneNodeId);
        PendingIntent sendPhoneMessagePendingIntent = PendingIntent.getActivity(this, 0, sendPhoneMessageIntent, 0);

        NotificationCompat.Builder notificationBuilder =
        new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.geo_fence_icon)
                .setContentTitle(TITLE)
                .addAction(R.drawable.ic_message_white_48dp, ENVOYER, sendPhoneMessagePendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.big_geofence));

        notificationBuilder.setVibrate(VIBRATION_PATTERN);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIF_ID, notificationBuilder.build());
    }
}
