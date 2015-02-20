package test.fr.petshop.database;

import android.provider.BaseColumns;

public final class AnimalContract
{
    public AnimalContract()
    {
    }

    public static abstract class AnimalEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "animals";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_COORDINATES = "coordinates";
    }

}
