package fr.zait.interfaces.callback;

import android.support.v7.widget.Toolbar;

public interface DialogCallbackInterface
{
    void attachDrawerToggle(Toolbar toolbar);

    void displayReinitSubredditsDialog();
    void displayDeleteSubredditDialog(String subredditName, String where, String[] whereArgs);
    void displayAddSubredditDialog();
}
