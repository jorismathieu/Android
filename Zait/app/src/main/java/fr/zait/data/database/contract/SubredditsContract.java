package fr.zait.data.database.contract;

import android.provider.BaseColumns;

import fr.zait.data.database.base.MyDbHelper;

public class SubredditsContract
{
    public static final String TABLE_NAME = "subreddits";

    public static final int IS_SELECTED = 1;
    public static final int IS_NOT_SELECTED = 0;

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    SubredditsEntry._ID + MyDbHelper.INT_TYPE + MyDbHelper.PRIMARY_KEY + MyDbHelper.COMMA_SEP +
                    SubredditsEntry.COLUMN_NAME + MyDbHelper.TEXT_TYPE + ")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SubredditsContract() {}

    public static abstract class SubredditsEntry implements BaseColumns
    {
        public static final String COLUMN_NAME = "name";
    }
}
