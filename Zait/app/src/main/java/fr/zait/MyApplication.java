package fr.zait;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;

import fr.zait.data.database.MyDbHelper;
import fr.zait.gcm.MyRegistrationIntentService;
import io.fabric.sdk.android.Fabric;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        SharedPreferences sharedPreferences = MySharedPreferences.getSharedPreferences(getApplicationContext());

        MyDbHelper myDbHelper = new MyDbHelper(this);
        myDbHelper.createTables();
        myDbHelper.fillTables(sharedPreferences);

        if (sharedPreferences.getBoolean(MySharedPreferences.FIRST_RUN, true)) {
            Intent gcmRegistration = new Intent(this, MyRegistrationIntentService.class);
            startService(gcmRegistration);
        }

        sharedPreferences.edit().putBoolean(MySharedPreferences.FIRST_RUN, false).commit();

    }
}
