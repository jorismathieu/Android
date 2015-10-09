package fr.zait.activities;

import android.os.Bundle;
import android.view.View;

import fr.zait.R;
import fr.zait.activities.base.MyActivity;

public class GeneralSettingsActivity extends MyActivity implements View.OnClickListener
{

    /***
     *
     * ANDROID
     *
     * ***/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_general_layout);

        initVariables();
        initViews(savedInstanceState);
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    @Override
    protected void initVariables()
    {
        enterAnimId = 0;
        exitAnimId = R.anim.slide_out_right;
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        findViewById(R.id.settings_back_arrow).setOnClickListener(this);
        resetStatusBarColor();
    }

    /***
     *
     * OVERRIDED METHODS
     *
     * ***/

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.settings_back_arrow:
                finishWithAnimation();
                break;
        }
    }
}
