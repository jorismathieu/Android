package fr.zait.fragments.base;

import android.support.v4.app.ListFragment;
import android.view.View;

public abstract class MyListFragment extends ListFragment
{
    protected abstract void initVariables();
    protected abstract void initViews(View view);
}
