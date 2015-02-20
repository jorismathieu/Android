package qualia.fr.petshop.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnimalHelper extends SQLiteOpenHelper
{
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AnimalContract.AnimalEntry.TABLE_NAME + " (" +
                    AnimalContract.AnimalEntry.COLUMN_NAME_ID + DBSettings.INTEGER_TYPE + " PRIMARY KEY," +
                    AnimalContract.AnimalEntry.COLUMN_NAME_NAME + DBSettings.TEXT_TYPE + DBSettings.COMMA_SEP +
                    AnimalContract.AnimalEntry.COLUMN_NAME_DESCRIPTION + DBSettings.TEXT_TYPE + DBSettings.COMMA_SEP +
                    AnimalContract.AnimalEntry.COLUMN_NAME_TYPE + DBSettings.TEXT_TYPE + DBSettings.COMMA_SEP +
                    AnimalContract.AnimalEntry.COLUMN_NAME_ADDRESS + DBSettings.TEXT_TYPE + DBSettings.COMMA_SEP +
                    AnimalContract.AnimalEntry.COLUMN_NAME_COORDINATES + DBSettings.TEXT_TYPE + DBSettings.COMMA_SEP + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + AnimalContract.AnimalEntry.TABLE_NAME;

    public AnimalHelper(Context context)
    {
        super(context, DBSettings.DATABASE_NAME, null, DBSettings.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}
