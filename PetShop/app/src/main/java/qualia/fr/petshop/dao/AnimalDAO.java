package qualia.fr.petshop.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import qualia.fr.petshop.database.AnimalContract;
import qualia.fr.petshop.database.AnimalHelper;
import qualia.fr.petshop.entities.Animal;

public class AnimalDAO implements IMyPetDAO<Animal>
{

    private Context             mContext;
    private AnimalHelper        mDbHelper;

    public AnimalDAO(Context context)
    {
        mContext = context;
        mDbHelper = new AnimalHelper(mContext);
    }

    @Override
    public List<Animal> read(Integer id_user)
    {
        ArrayList<Animal> list = new ArrayList<Animal>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                AnimalContract.AnimalEntry.COLUMN_NAME_ID,
                AnimalContract.AnimalEntry.COLUMN_NAME_NAME,
                AnimalContract.AnimalEntry.COLUMN_NAME_DESCRIPTION,
                AnimalContract.AnimalEntry.COLUMN_NAME_TYPE,
                AnimalContract.AnimalEntry.COLUMN_NAME_ADDRESS,
                AnimalContract.AnimalEntry.COLUMN_NAME_COORDINATES,
        };

        String sortOrder =
                AnimalContract.AnimalEntry.COLUMN_NAME_ID + " DESC";


        Cursor cursor = db.query(
                AnimalContract.AnimalEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
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
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String coordinates = cursor.getString(cursor.getColumnIndex("coordinates"));

                    list.add(new Animal(id, name, description, type, address, coordinates));
                } while (cursor.moveToNext());
            }
        }

        return list;
    }

    @Override
    public void delete(Animal object)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = AnimalContract.AnimalEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        db.delete(AnimalContract.AnimalEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void update(Animal object)
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_NAME, object.getName());
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_DESCRIPTION, object.getDescription());
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_TYPE, object.getType());
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_ADDRESS, object.getAddress());
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_COORDINATES, object.getCoordinates());


        String selection = AnimalContract.AnimalEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        int count = db.update(
                AnimalContract.AnimalEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    @Override
    public void create(Animal object)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_ID, object.getId());
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_NAME, object.getName());
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_DESCRIPTION, object.getDescription());
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_TYPE, object.getType());
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_ADDRESS, object.getAddress());
        values.put(AnimalContract.AnimalEntry.COLUMN_NAME_COORDINATES, object.getCoordinates());

        long newRowId;
        newRowId = db.insert(AnimalContract.AnimalEntry.TABLE_NAME, "null", values);
    }
}
