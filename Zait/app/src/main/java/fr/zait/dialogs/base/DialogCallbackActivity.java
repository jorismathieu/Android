package fr.zait.dialogs.base;

public interface DialogCallbackActivity
{
    void displayReinitSubredditsDialog();
    void displayDeleteSubredditDialog(String subredditName, String where, String[] whereArgs);
    void displayAddSubredditDialog();
}
