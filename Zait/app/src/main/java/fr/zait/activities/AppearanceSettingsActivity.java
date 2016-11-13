package fr.zait.activities;

import android.os.Bundle;
import android.view.View;

import fr.zait.R;
import fr.zait.activities.base.MyActivity;
import fr.zait.dialogs.ChangeTextSizeDialog;
import fr.zait.dialogs.ChangeThemeDialog;

public class AppearanceSettingsActivity extends MyActivity implements View.OnClickListener {

    private static final String CHANGE_THEME_DIALOG_TAG = "ChangeTheme";
    private static final String CHANGE_TEXT_SIZE_DIALOG_TAG = "ChangeTextSize";

    /***
     * ANDROID
     ***/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_appearance_layout);

        initVariables();
        initViews(savedInstanceState);
    }

    /***
     * PRIVATE METHODS
     ***/

    @Override
    protected void initVariables() {
        enterAnimId = 0;
        exitAnimId = R.anim.slide_out_right;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        findViewById(R.id.settings_back_arrow).setOnClickListener(this);
        findViewById(R.id.theme).setOnClickListener(this);
        findViewById(R.id.text_size).setOnClickListener(this);
        resetStatusBarColor();
    }

    /***
     * OVERRIDED METHODS
     ***/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_back_arrow:
                finishWithAnimation();
                break;
            case R.id.theme:
                new ChangeThemeDialog().show(getSupportFragmentManager(), CHANGE_THEME_DIALOG_TAG);
                break;
            case R.id.text_size:
                new ChangeTextSizeDialog().show(getSupportFragmentManager(), CHANGE_TEXT_SIZE_DIALOG_TAG);
                break;
        }
    }


}

