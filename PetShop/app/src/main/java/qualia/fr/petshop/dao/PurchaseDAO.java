package qualia.fr.petshop.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import qualia.fr.petshop.database.PurchaseContract;
import qualia.fr.petshop.database.PurchaseHelper;
import qualia.fr.petshop.entities.Purchase;

public class PurchaseDAO implements IMyPetDAO<Purchase>
{
    private Context mContext;
    private PurchaseHelper mDbHelper;

    public PurchaseDAO(Context context)
    {
        mContext = context;
        mDbHelper = new PurchaseHelper(mContext);
    }

    @Override
    public List<Purchase> read(Integer idUser)
    {
        ArrayList<Purchase> list = new ArrayList<Purchase>();

        if (idUser >= 0)
        {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {PurchaseContract.PurchaseEntry.COLUMN_NAME_ID, PurchaseContract.PurchaseEntry.COLUMN_NAME_ID_USER, PurchaseContract.PurchaseEntry.COLUMN_NAME_ID_ANIMAL,};

            String sortOrder = PurchaseContract.PurchaseEntry.COLUMN_NAME_ID + " DESC";

            String where = "id_user=?";
            String[] args = {String.valueOf(idUser)};

            Cursor cursor = db.query(PurchaseContract.PurchaseEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    where,                                // The columns for the WHERE clause
                    args,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    do
                    {
                        Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                        Integer id_user = cursor.getInt(cursor.getColumnIndex("id_user"));
                        Integer id_animal = cursor.getInt(cursor.getColumnIndex("id_animal"));

                        list.add(new Purchase(id, id_user, id_animal));
                    } while (cursor.moveToNext());
                }
            }

        }
        return list;
    }

    @Override
    public void delete(Purchase object)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = PurchaseContract.PurchaseEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        db.delete(PurchaseContract.PurchaseEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void update(Purchase object)
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(PurchaseContract.PurchaseEntry.COLUMN_NAME_ID_USER, object.getIdUser());
        values.put(PurchaseContract.PurchaseEntry.COLUMN_NAME_ID_ANIMAL, object.getIdAnimal());


        String selection = PurchaseContract.PurchaseEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        int count = db.update(
                PurchaseContract.PurchaseEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    @Override
    public void create(Purchase object)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PurchaseContract.PurchaseEntry.COLUMN_NAME_ID, object.getId());
        values.put(PurchaseContract.PurchaseEntry.COLUMN_NAME_ID_USER, object.getIdUser());
        values.put(PurchaseContract.PurchaseEntry.COLUMN_NAME_ID_ANIMAL, object.getIdAnimal());

        long newRowId;
        newRowId = db.insert(PurchaseContract.PurchaseEntry.TABLE_NAME, "null", values);
    }
}
