package test.fr.petshop.database;

import android.provider.BaseColumns;

public final class PurchaseContract
{
    public PurchaseContract()
    {
    }

    public static abstract class PurchaseEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "purchases";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_ID_USER = "id_user";
        public static final String COLUMN_NAME_ID_ANIMAL = "id_animal";
    }
}
