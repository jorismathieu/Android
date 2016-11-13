package com.dalvik;


import android.telephony.SmsManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService {

    private static final String MY_NUMBER = "+33685899693";
    private static final String MESSAGE = "Test !";

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
        sendMessage();
    }

    private void sendMessage() {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(MY_NUMBER, null, MESSAGE, null, null);
    }
}
