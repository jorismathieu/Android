package fr.zait.fragments.base;

import android.support.v4.app.Fragment;
import android.view.View;

public abstract class MyFragment extends Fragment
{
    protected abstract void initVariables();
    protected abstract void initViews(View view);
}
