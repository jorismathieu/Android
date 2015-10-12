package fr.zait;

import android.app.Application;
import android.content.SharedPreferences;

import fr.zait.data.database.MyDbHelper;


public class MyApplication extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = MySharedPreferences.getSharedPreferences(getApplicationContext());

        MyDbHelper myDbHelper = new MyDbHelper(this);
        myDbHelper.createTables();
        myDbHelper.fillTables(sharedPreferences);

        sharedPreferences.edit().putBoolean(MySharedPreferences.FIRST_RUN, false).commit();

    }
}
