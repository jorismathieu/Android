package fr.zait.data.database;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.List;

import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.data.database.dao.SubredditsDao;
import fr.zait.data.entities.Subreddit;

public class MyContentProvider extends ContentProvider {
    private static final String AUTHORITY = "fr.zait.data.database.MyContentProvider";
    public static final String CONTENT_URI = "content://" + AUTHORITY;

    private static final Uri NOTIFICATION_URI = Uri.parse(CONTENT_URI);

    private static final int SUBREDDITS_BASE = 0;
    private static final int SUBREDDITS_SELECT = SUBREDDITS_BASE + 1;
    private static final int SUBREDDITS_DELETE = SUBREDDITS_BASE + 2;
    private static final int SUBREDDITS_REINIT = SUBREDDITS_BASE + 3;
    private static final int SUBREDDITS_ADD = SUBREDDITS_BASE + 4;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, SubredditsContract.SELECT_URL, SUBREDDITS_SELECT);
        uriMatcher.addURI(AUTHORITY, SubredditsContract.DELETE_URL, SUBREDDITS_DELETE);
        uriMatcher.addURI(AUTHORITY, SubredditsContract.REINIT_URL, SUBREDDITS_REINIT);
        uriMatcher.addURI(AUTHORITY, SubredditsContract.ADD_URL, SUBREDDITS_ADD);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int match = uriMatcher.match(uri);

        try {
            switch (match) {
                case SUBREDDITS_SELECT:
                    MatrixCursor cursor = new MatrixCursor(SubredditsContract.PROJECTION);
                    List<Subreddit> subreddits = SubredditsDao.getSubreddits();
                    for (Subreddit subreddit : subreddits) {
                        cursor.addRow(new Object[]{subreddit._id, subreddit.name});
                    }

                    cursor.setNotificationUri(getContext().getContentResolver(), NOTIFICATION_URI);
                    return cursor;
            }
        }
        catch (Exception e) {
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = uriMatcher.match(uri);

        try {
            switch (match) {
                case SUBREDDITS_REINIT:
                    SubredditsDao.saveDefaultSubreddits(getContext());
                    break;
                case SUBREDDITS_ADD:
                    SubredditsDao.insertPost(contentValues);
                    break;
            }
            getContext().getContentResolver().notifyChange(NOTIFICATION_URI, null);

        }
        catch (Exception e) {

        }
        return null;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        final int match = uriMatcher.match(uri);
        int nbDeletion = 0;

        try {
            switch (match) {
                case SUBREDDITS_DELETE:
                    nbDeletion = SubredditsDao.deletePost(where, whereArgs);
                    break;
            }
        }
        catch (Exception e) {

        }

        getContext().getContentResolver().notifyChange(NOTIFICATION_URI, null);

        return nbDeletion;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}