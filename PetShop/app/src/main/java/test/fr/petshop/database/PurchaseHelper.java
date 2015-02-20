package test.fr.petshop.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PurchaseHelper extends SQLiteOpenHelper
{

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PurchaseContract.PurchaseEntry.TABLE_NAME + " (" +
                    PurchaseContract.PurchaseEntry.COLUMN_NAME_ID + DBSettings.INTEGER_TYPE + " PRIMARY KEY," +
                    PurchaseContract.PurchaseEntry.COLUMN_NAME_ID_USER + DBSettings.INTEGER_TYPE + DBSettings.COMMA_SEP +
                    PurchaseContract.PurchaseEntry.COLUMN_NAME_ID_ANIMAL + DBSettings.INTEGER_TYPE + DBSettings.COMMA_SEP + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PurchaseContract.PurchaseEntry.TABLE_NAME;

    public PurchaseHelper(Context context)
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
