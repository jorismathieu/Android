package fr.zait.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.zait.R;
import fr.zait.dialogs.DeleteSubredditDialog;
import fr.zait.dialogs.LoginDialog;
import fr.zait.dialogs.ReinitSubredditsDialog;
import fr.zait.dialogs.base.DialogCallbackActivity;
import fr.zait.fragments.HomeFragment;
import fr.zait.fragments.MySubredditsFragment;
import fr.zait.utils.AnimationUtils;


public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, DialogCallbackActivity
{

    private static final String HOME_FRAGMENT_TAG = "HOME";
    private static final String MY_SUBREDDITS_TAG = "SUBREDDITS";

    private static final String LOGIN_TAG = "LOGIN";
    private static final String REINIT_DIALOG_TAG = "REINIT";
    private static final String DELETE_DIALOG_TAG = "DELETE";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View navigationDrawerHeader;
    private View navigationDrawerHeaderLogin;

    private HomeFragment homeFragment;
    private MySubredditsFragment mySubredditsFragment;

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
        initViews(savedInstanceState);
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    private void initVariables() {
    }

    private void loadFragment(Fragment fragment, String tag) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment == null || !currentFragment.isVisible()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment, tag);
            fragmentTransaction.commit();
        }
    }

    private void initViews(Bundle savedInstanceState) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationDrawerHeader = findViewById(R.id.navigation_drawer_header);
        navigationDrawerHeader.setOnClickListener(this);
        expandIcon = (ImageView) findViewById(R.id.navigation_header_expand_icon);
        navigationDrawerHeaderLogin = findViewById(R.id.navigation_drawer_header_login);

        homeFragment = new HomeFragment();
        mySubredditsFragment = new MySubredditsFragment();

        if (savedInstanceState == null) {
            loadFragment(homeFragment, HOME_FRAGMENT_TAG);
        }

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

    private void makeExpandIconRotate() {
        AnimationUtils.makeHalfRotation(expandIcon, rotationAngle);
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
                loadFragment(homeFragment, HOME_FRAGMENT_TAG);
                break;
            case R.id.nav_mysubreddits:
                loadFragment(mySubredditsFragment, MY_SUBREDDITS_TAG);
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

    @Override
    public void displayReinitDialog()
    {
        new ReinitSubredditsDialog().show(getSupportFragmentManager(), REINIT_DIALOG_TAG);
    }

    @Override
    public void displayDeleteDialog(String subredditName, String where, String[] whereArgs) {
        DeleteSubredditDialog dialog = DeleteSubredditDialog.newInstance(subredditName, where, whereArgs);
        dialog.show(getSupportFragmentManager(), DELETE_DIALOG_TAG);
    }


}
