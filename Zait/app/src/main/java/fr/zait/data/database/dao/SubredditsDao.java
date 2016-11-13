package fr.zait.data.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fr.zait.R;
import fr.zait.data.database.MyDbHelper;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.data.entities.Subreddit;

public class SubredditsDao {

    public static void saveDefaultSubreddits(Context context) {
        SQLiteDatabase db = MyDbHelper.getInstance().getWritableDatabase();

        try {
            String[] defaultSubreddits = context.getResources().getStringArray(R.array.defaultSubreddits);

            ContentValues values = new ContentValues();
            for (int i = 0; i < defaultSubreddits.length; i++) {
                values.put(SubredditsContract.SubredditsEntry.COLUMN_NAME, defaultSubreddits[i]);
                db.insert(SubredditsContract.TABLE_NAME, "null", values);
            }
        }
        catch (Exception e) {
        }
        finally {
            db.close();
        }
    }

    public static List<Subreddit> getSubreddits() {
        SQLiteDatabase db = MyDbHelper.getInstance().getReadableDatabase();
        List<Subreddit> subreddits = new ArrayList<>();

        try {
            String[] projection = {SubredditsContract.SubredditsEntry._ID, SubredditsContract.SubredditsEntry.COLUMN_NAME,};

            String sortOrder = SubredditsContract.SubredditsEntry._ID + " ASC";

            Cursor c = db.query(SubredditsContract.TABLE_NAME, projection, null, null, null, null, sortOrder);
            if (c != null) {
                while (c.moveToNext()) {
                    Subreddit subreddit = new Subreddit();
                    subreddit.name = c.getString(c.getColumnIndex(SubredditsContract.SubredditsEntry.COLUMN_NAME));
                    subreddit._id = c.getLong(c.getColumnIndex(SubredditsContract.SubredditsEntry._ID));
                    subreddits.add(subreddit);
                }
            }
        }
        catch (Exception e) {
        }
        finally {
            db.close();
        }
        return subreddits;
    }

    public static long insertPost(ContentValues values) {
        SQLiteDatabase db = MyDbHelper.getInstance().getReadableDatabase();
        long idInserted = -1;

        try {
            idInserted = db.insert(SubredditsContract.TABLE_NAME, null, values);
        }
        catch (Exception e) {
        }
        finally {
            db.close();
        }
        return idInserted;
    }

    public static int deletePost(String where, String[] whereArgs) {
        SQLiteDatabase db = MyDbHelper.getInstance().getReadableDatabase();
        int nbDeletion = 0;

        try {
            nbDeletion = db.delete(SubredditsContract.TABLE_NAME, where, whereArgs);
        }
        catch (Exception e) {
        }
        finally {
            db.close();
        }
        return nbDeletion;
    }


}
