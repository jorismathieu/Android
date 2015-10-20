package fr.zait.activities.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import fr.zait.R;

public abstract class MyActivity extends AppCompatActivity
{
    private static final int DEFAULT_VALUE = -1;

    protected int enterAnimId = DEFAULT_VALUE;
    protected int exitAnimId = DEFAULT_VALUE;

    protected void resetStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(getResources().getColor(R.color.primaryDarkColor));
        }
    }

    @Override
    public void onBackPressed() {
        finishWithAnimation();
    }

    protected void finishWithAnimation() {
        finish();
        if (enterAnimId != DEFAULT_VALUE && exitAnimId != DEFAULT_VALUE) {
            overridePendingTransition(enterAnimId, exitAnimId);
        }
    }

    protected abstract void initVariables();
    protected abstract void initViews(Bundle savedInstanceState);
}
