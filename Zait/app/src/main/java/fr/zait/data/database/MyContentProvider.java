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
    private static final String AUTHORITY = "content://fr.zait.data.database.MyContentProvider/";
    private static final Uri NOTIFICATION_URI = Uri.parse(AUTHORITY);


    private static final String SUBREDDITS_REQUEST = AUTHORITY + SubredditsContract.TABLE_NAME;



    public static Uri getURIFromTable(String table) {
        return Uri.parse(AUTHORITY + table);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            if (uri.toString().equals(SUBREDDITS_REQUEST)) {
                MatrixCursor cursor = new MatrixCursor(SubredditsContract.PROJECTION);
                List<Subreddit> subreddits = SubredditsDao.getSubreddits();
                for (Subreddit subreddit : subreddits){
                    cursor.addRow(new Object[]{subreddit._id, subreddit.name});
                }

                cursor.setNotificationUri(getContext().getContentResolver(), NOTIFICATION_URI);

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
        try {
            if (uri.toString().equals(SUBREDDITS_REQUEST)) {
                SubredditsDao.saveDefaultSubreddits(getContext());
            }

            getContext().getContentResolver().notifyChange(NOTIFICATION_URI, null);

        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        int nbDeletion = 0;

        try {
            if (uri.toString().equals(SUBREDDITS_REQUEST)) {
                nbDeletion = SubredditsDao.deletePost(where, whereArgs);
            }
        } catch (Exception e) {

        }

        getContext().getContentResolver().notifyChange(NOTIFICATION_URI, null);

        return nbDeletion;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}