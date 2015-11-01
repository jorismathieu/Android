package fr.zait.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.zait.R;
import fr.zait.interfaces.callback.DialogCallbackInterface;
import fr.zait.interfaces.callback.FragmentCallbackInterface;
import fr.zait.activities.base.MyActivity;
import fr.zait.interfaces.callback.PostDetailCallbackInterface;
import fr.zait.data.entities.Post;
import fr.zait.dialogs.AddSubredditDialog;
import fr.zait.dialogs.DeleteSubredditDialog;
import fr.zait.dialogs.LoginDialog;
import fr.zait.dialogs.ReinitSubredditsDialog;
import fr.zait.fragments.HomeFragment;
import fr.zait.fragments.MySubredditsFragment;
import fr.zait.fragments.SearchFragment;
import fr.zait.utils.AnimationUtils;
import fr.zait.utils.NotificationsUtils;
import fr.zait.widget.AppWidgetReceiver;


public class MainActivity extends MyActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        DialogCallbackInterface, FragmentCallbackInterface, PostDetailCallbackInterface
{

    private static final String LOGIN_TAG = "LOGIN";
    private static final String REINIT_SUBREDDITS_DIALOG_TAG = "REINIT_SUBS";
    private static final String DELETE_SUBREDDIT_DIALOG_TAG = "DELETE_SUB";
    private static final String ADD_SUBBREDIT_DIALOG_TAG = "ADD_SUB";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View navigationDrawerHeader;
    private View navigationDrawerHeaderLogin;

    private HomeFragment homeFragment;
    private MySubredditsFragment mySubredditsFragment;
    private SearchFragment searchFragment;

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

        if ((getIntent().getAction() != null)) {
            if (getIntent().getAction().equals(AppWidgetReceiver.APP_WIDGET_OPEN_POST_ACTION)) {
                Post post = getIntent().getParcelableExtra(AppWidgetReceiver.EXTRA_POST);
                openPostDetail(post);
            } else {
                NotificationsUtils.checkOpenInternal(this, getIntent());
            }
        }
        initVariables();
        initViews(savedInstanceState);
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    private void loadFragment(Fragment fragment, Bundle args, String tag) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment == null || !currentFragment.isVisible()) {
            fragment.setArguments(args);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment, tag);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void initVariables() {
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationDrawerHeader = findViewById(R.id.navigation_drawer_header);
        navigationDrawerHeader.setOnClickListener(this);
        expandIcon = (ImageView) findViewById(R.id.navigation_header_expand_icon);
        navigationDrawerHeaderLogin = findViewById(R.id.navigation_drawer_header_login);

        homeFragment = new HomeFragment();
        mySubredditsFragment = new MySubredditsFragment();
        searchFragment = new SearchFragment();

        if (savedInstanceState == null) {
            loadFragment(homeFragment, null, HOME_FRAGMENT_TAG);
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
        menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                drawerLayout.closeDrawers();
                loadFragment(homeFragment, null, HOME_FRAGMENT_TAG);
                break;
            case R.id.nav_mysubreddits:
                drawerLayout.closeDrawers();
                loadFragment(mySubredditsFragment, null, MY_SUBREDDITS_TAG);
                break;
            case R.id.nav_search:
                drawerLayout.closeDrawers();
                loadFragment(searchFragment, null, SEARCH_FRAGMENT);
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
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
    public void attachDrawerToggle(Toolbar toolbar)
    {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public void displayReinitSubredditsDialog()
    {
        new ReinitSubredditsDialog().show(getSupportFragmentManager(), REINIT_SUBREDDITS_DIALOG_TAG);
    }

    @Override
    public void displayDeleteSubredditDialog(String subredditName, String where, String[] whereArgs) {
        DeleteSubredditDialog dialog = DeleteSubredditDialog.newInstance(subredditName, where, whereArgs);
        dialog.show(getSupportFragmentManager(), DELETE_SUBREDDIT_DIALOG_TAG);
    }

    @Override
    public void displayAddSubredditDialog() {
        new AddSubredditDialog().show(getSupportFragmentManager(), ADD_SUBBREDIT_DIALOG_TAG);
    }


    @Override
    public void switchFragment(String tag, Bundle args)
    {
        switch (tag) {
            case HOME_FRAGMENT_TAG:
                loadFragment(homeFragment, args, HOME_FRAGMENT_TAG);
                break;
        }
    }

    @Override
    public void openPostDetail(Post post)
    {
        Intent intent = null;
        if (post != null)
        {
            if (!post.thumbnail.equals("self"))
            {
                intent = new Intent(this, PostWebviewActivity.class);
            }
            else
            {
                intent = new Intent(this, PostCommentsActivity.class);
            }

            if (intent != null)
            {
                intent.putExtra(PostWebviewActivity.EXTRAS.POST, post);
                startActivity(intent);
                overridePendingTransition(R.anim.push_in_left, R.anim.push_out_left);
            }
        }

    }
}
