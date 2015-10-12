package fr.zait.data.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.zait.MySharedPreferences;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.data.database.dao.SubredditsDao;

public class MyDbHelper extends SQLiteOpenHelper
{
    public static MyDbHelper instance = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Zait.db";

    public static final String TEXT_TYPE = " TEXT";
    public static final String INT_TYPE = " INTEGER";
    public static final String COMMA_SEP = ", ";
    public static final String PRIMARY_KEY = " PRIMARY KEY";

    private Context cxt;

    public static MyDbHelper getInstance() {
        return instance;
    }

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        instance = this;
        cxt = context;
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createTables() {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.execSQL(SubredditsContract.SQL_CREATE_ENTRIES);
        } catch (Exception e) {
        } finally {
            db.close();
        }

    }

    public void fillTables(SharedPreferences sharedPreferences) {
        if (sharedPreferences.getBoolean(MySharedPreferences.FIRST_RUN, true)) {
            SubredditsDao.saveDefaultSubreddits(cxt);
        }
    }

}