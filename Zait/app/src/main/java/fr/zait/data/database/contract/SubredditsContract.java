package fr.zait.data.database.contract;

import android.net.Uri;
import android.provider.BaseColumns;

import fr.zait.data.database.MyContentProvider;
import fr.zait.data.database.MyDbHelper;

public class SubredditsContract {
    public static final String TABLE_NAME = "subreddits";


    private static final String CONTENT_URI = MyContentProvider.CONTENT_URI + "/" + TABLE_NAME;

    public static final Uri SELECT_URI = Uri.parse(CONTENT_URI + "/SELECT");
    public static final Uri DELETE_URI = Uri.parse(CONTENT_URI + "/DELETE");
    public static final Uri REINIT_URI = Uri.parse(CONTENT_URI + "/REINIT");
    public static final Uri ADD_URI = Uri.parse(CONTENT_URI + "/ADD");


    public static final String SELECT_URL = TABLE_NAME + "/SELECT";
    public static final String DELETE_URL = TABLE_NAME + "/DELETE";
    public static final String REINIT_URL = TABLE_NAME + "/REINIT";
    public static final String ADD_URL = TABLE_NAME + "/ADD";


    public static final String[] PROJECTION = {SubredditsEntry._ID, SubredditsEntry.COLUMN_NAME};

    public static final int IS_SELECTED = 1;
    public static final int IS_NOT_SELECTED = 0;

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            SubredditsEntry._ID + MyDbHelper.INT_TYPE + MyDbHelper.PRIMARY_KEY + MyDbHelper.COMMA_SEP +
            SubredditsEntry.COLUMN_NAME + MyDbHelper.TEXT_TYPE + ")";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SubredditsContract() {
    }

    public static abstract class SubredditsEntry implements BaseColumns {
        public static final String COLUMN_NAME = "name";
    }
}
