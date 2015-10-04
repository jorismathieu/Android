package fr.zait.activities.base;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import fr.zait.data.database.base.MyDbHelper;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.data.database.dao.SubredditsDao;


public class MyApplication extends Application
{

    public static final String FIRST_RUN = "first_run";
    public static final String SELECTED_SUBREDDIT = "selected_subreddit";

    private SharedPreferences sharedPreferences = null;
    private static MyApplication instance = null;

    public static MyApplication getInstance() {
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        sharedPreferences = getSharedPreferences("fr.zait", MODE_PRIVATE);

        MyDbHelper myDbHelper = new MyDbHelper(this);
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        db.execSQL(SubredditsContract.SQL_CREATE_ENTRIES);

        if (sharedPreferences.getBoolean(FIRST_RUN, true)) {
            SubredditsDao subredditsDao = new SubredditsDao(this);
            subredditsDao.saveDefaultSubreddits();
            sharedPreferences.edit().putBoolean(FIRST_RUN, false).commit();
        }
    }
}
