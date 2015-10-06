package fr.zait.dialogs.base;

public interface DialogCallbackActivity
{
    void displayReinitDialog();
    void displayDeleteDialog(String subredditName, String where, String[] whereArgs);
}
