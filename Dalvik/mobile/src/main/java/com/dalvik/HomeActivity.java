package com.dalvik;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity {

    /*
     * Debug mode phone and watch
     * adb forward tcp:4444 localabstract:/adb-hub
     * adb connect 127.0.0.1:4444
     */

    private static final String TAG = "HomeActivity";

    public static final String FENCE_RECEIVER_ACTION = "com.dalvik.fence_receiver_action";
    public static final String FENCE_KEY = "FENCE_KEY";

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int DELAY_STOP_ACTIVITY_MS = 2000;

    // Chez moi
    private static final double HOME_LATITUDE = 48.83620570000001;
    private static final double HOME_LONGITUDE = 2.347884600000043;
    private static final double HOME_RADIUS = 100.0;
    private static final long TIME_START = 0;
    private static final long TIME_END = 80000000;

    private GoogleApiClient mGoogleApiClient;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Awareness.API).build();
        mGoogleApiClient.connect();

        Intent intent = new Intent(FENCE_RECEIVER_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            startGeoFencing();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String permissions[], @NonNull  int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startGeoFencing();
                } else {
                    Toast.makeText(this, "No permission, quitting...", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void startGeoFencing() {
        AwarenessFence timeFence = TimeFence.inDailyInterval(TimeZone.getDefault(), TIME_START, TIME_END);
        AwarenessFence locationFence = LocationFence.in(HOME_LATITUDE, HOME_LONGITUDE, HOME_RADIUS, 0);
        AwarenessFence fence = AwarenessFence.and(timeFence, locationFence);
        Awareness.FenceApi.updateFences(mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .addFence(FENCE_KEY, fence, mPendingIntent)
                        .build())
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.d(TAG, "Fence was successfully registered.");
                            Toast.makeText(HomeActivity.this, "Everything is set!", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, "Fence could not be registered: " + status);
                            Toast.makeText(HomeActivity.this, "Could not start API request", Toast.LENGTH_LONG).show();
                        }
                    }
                });

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
                finish();
           }
       }, DELAY_STOP_ACTIVITY_MS);
    }
}
