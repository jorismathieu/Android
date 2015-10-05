package fr.zait;

import android.app.Application;

import fr.zait.data.database.MyDbHelper;


public class MyApplication extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();

        MyDbHelper myDbHelper = new MyDbHelper(this);
        myDbHelper.createTables();
        myDbHelper.fillTables();

    }
}
