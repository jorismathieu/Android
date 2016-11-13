package com.dalvik;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

import static com.dalvik.ListenerService.NOTIF_ID;

public class SendPhoneMessageActivity extends Activity {

    private static final int TIMEOUT = 5000;
    public static final String PHONE_NODE_ID = "PHONE_NODE_ID";
    private static final String MESSAGE = "MESSAGE";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIF_ID);
        Intent intent = getIntent();
        if (intent != null) {
            final String phoneNodeId = intent.getStringExtra(PHONE_NODE_ID);
            if (phoneNodeId != null) {
                if (mGoogleApiClient == null) {
                    mGoogleApiClient = new GoogleApiClient.Builder(this)
                            .addApi(Wearable.API)
                            .build();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mGoogleApiClient.blockingConnect(TIMEOUT, TimeUnit.MILLISECONDS);
                        Wearable.MessageApi.sendMessage(mGoogleApiClient, phoneNodeId, MESSAGE, null);
                        mGoogleApiClient.disconnect();
                        finish();
                    }
                }).start();
            }
        }
    }
}
