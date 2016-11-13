package com.dalvik;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.dalvik.HomeActivity.FENCE_KEY;

public class FenceReceiver extends BroadcastReceiver {

    private static final int TIMEOUT = 5000;
    private static final String MESSAGE = "MESSAGE";

    @Nullable
    private GoogleApiClient mGoogleApiClient;

    @Nullable
    private String mNodeId = null;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);
        if (TextUtils.equals(fenceState.getFenceKey(), FENCE_KEY)) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    tryToSendMessageToNode(context);
                    break;
                case FenceState.FALSE:
                default:
                    break;
            }
        }
    }

    private void tryToSendMessageToNode(Context context) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .build();
        }
        if (mNodeId == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mGoogleApiClient.blockingConnect(TIMEOUT, TimeUnit.MILLISECONDS);
                    NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                    List<Node> nodes = result.getNodes();
                    if (nodes.size() > 0) {
                        mNodeId = nodes.get(0).getId();
                    }
                    if (mNodeId != null) {
                        sendMessageToNode();
                    }
                }
            }).start();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendMessageToNode();
                }
            }).start();
        }
    }

    private void sendMessageToNode() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.blockingConnect(TIMEOUT, TimeUnit.MILLISECONDS);
            Wearable.MessageApi.sendMessage(mGoogleApiClient, mNodeId, MESSAGE, null);
            mGoogleApiClient.disconnect();
        }
    }
}