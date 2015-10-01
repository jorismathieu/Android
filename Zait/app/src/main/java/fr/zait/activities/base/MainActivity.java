package fr.zait.activities.base;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fr.zait.R;
import fr.zait.dialogs.LoginDialog;
import fr.zait.fragments.HomeFragment;
import fr.zait.utils.CustomAnimations;


public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{

    private static final String LOGIN_TAG = "LOGIN";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View navigationDrawerHeader;
    private View navigationDrawerHeaderLogin;
    private HomeFragment homeFragment;

    private ImageView expandIcon;
    private int rotationAngle = 0;

    /***
     *
     * ANDROID
     *
     * ***/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initVariables();
        initViews();
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    private void initVariables() {
    }

    private void initViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationDrawerHeader = findViewById(R.id.navigation_drawer_header);
        navigationDrawerHeader.setOnClickListener(this);
        expandIcon = (ImageView) findViewById(R.id.navigation_header_expand_icon);
        navigationDrawerHeaderLogin = findViewById(R.id.navigation_drawer_header_login);

        homeFragment = (HomeFragment) Fragment.instantiate(this, HomeFragment.class.getName());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, homeFragment, null);
        fragmentTransaction.commit();

        View addAccountRow = initLoginRows(R.id.add_account_row, R.drawable.ic_add_gray_24dp);

        addAccountRow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new LoginDialog().show(getSupportFragmentManager(), LOGIN_TAG);
            }
        });
    }

    private View initLoginRows(int rowId, int srcId) {
        View row = findViewById(rowId);

        TextView firstLabel = (TextView) row.findViewById(R.id.log_row_label);
        firstLabel.setText(R.string.add_an_account);

        if (srcId != -1) {
            ImageView icon = (ImageView) row.findViewById(R.id.log_row_icon);

            icon.setImageResource(srcId);
        }

        return row;
    }

    private void selectItem(int position) {

    }

    private void makeExpandIconRotate() {
        CustomAnimations.makeHalfRotation(expandIcon, rotationAngle);
        rotationAngle += 180;
        rotationAngle = rotationAngle % 360;
    }

    /***
     *
     * OVERRIDED METHODS
     *
     * ***/

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem)
    {
        drawerLayout.closeDrawers();
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.nav_search:
                break;
            case R.id.nav_settings:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        makeExpandIconRotate();
        if (navigationDrawerHeaderLogin.getVisibility() == View.GONE) {
            navigationDrawerHeaderLogin.setVisibility(View.VISIBLE);
        } else {
            navigationDrawerHeaderLogin.setVisibility(View.GONE);
        }
    }

    /***
     *
     * PRIVATE CLASS
     *
     * ***/

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

}
