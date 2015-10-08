package fr.zait.activities.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import fr.zait.R;

public abstract class MyActivity extends FragmentActivity
{
    protected abstract void initVariables();
    protected abstract void initViews(Bundle savedInstanceState);

    protected void resetStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.primaryDarkColor));
    }
}
