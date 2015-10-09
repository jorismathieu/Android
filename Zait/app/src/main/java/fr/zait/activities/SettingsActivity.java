package fr.zait.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.zait.R;
import fr.zait.activities.base.MyActivity;

public class SettingsActivity extends MyActivity implements View.OnClickListener
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
        setContentView(R.layout.settings_main_layout);

        initVariables();
        initViews(savedInstanceState);
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    @Override
    protected void initVariables() {
        enterAnimId = 0;
        exitAnimId = R.anim.slide_out_right;
    }


    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        findViewById(R.id.settings_back_arrow).setOnClickListener(this);
        resetStatusBarColor();

        populateSettingsRows(R.id.general_settings, R.drawable.ic_settings_gray_24dp, R.string.general_title, R.string.general_subtitle);
        populateSettingsRows(R.id.appearance_settings, R.drawable.ic_color_lens_gray_24dp, R.string.appearance_title, R.string.appearance_subtitle);


    }

    private void populateSettingsRows(int rowId, int drawableId, int titleId, int subTitleId) {
        View rowView = findViewById(rowId);

        ImageView mainSettingsIcon = (ImageView) rowView.findViewById(R.id.main_setting_icon);
        mainSettingsIcon.setImageResource(drawableId);

        TextView mainSettingsTitle = (TextView) rowView.findViewById(R.id.main_settings_title);
        mainSettingsTitle.setText(titleId);

        TextView mainSettingsSubtitle = (TextView) rowView.findViewById(R.id.main_settings_subtitle);
        mainSettingsSubtitle.setText(subTitleId);

        rowView.setOnClickListener(this);
    }

    /***
     *
     * OVERRIDED METHODS
     *
     * ***/

    @Override
    public void onClick(View v)
    {
        Intent intent;

        switch (v.getId()) {
            case R.id.settings_back_arrow:
                finishWithAnimation();
                break;
            case R.id.general_settings:
                intent = new Intent(this, GeneralSettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                break;
            case R.id.appearance_settings:
                intent = new Intent(this, AppearanceSettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
                break;
        }
    }
}
