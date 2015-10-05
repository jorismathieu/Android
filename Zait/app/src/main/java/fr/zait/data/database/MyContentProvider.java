package fr.zait.data.database;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.List;

import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.data.database.dao.SubredditsDao;
import fr.zait.data.entities.Subreddit;

public class MyContentProvider extends ContentProvider
{
    public static final String AUTHORITY = "content://fr.zait.data.database.MyContentProvider/";

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            if (uri.toString().equals(AUTHORITY + SubredditsContract.TABLE_NAME)) {
                MatrixCursor cursor = new MatrixCursor(SubredditsContract.PROJECTION);
                List<Subreddit> subreddits = SubredditsDao.getSubreddits();
                for (Subreddit subreddit : subreddits){
                    cursor.addRow(new Object[]{subreddit._id, subreddit.name});
                }
                return cursor;
            }

        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}