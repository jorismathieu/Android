package fr.zait.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import fr.zait.R;
import fr.zait.adapters.SubredditsAdapter;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.dialogs.base.DialogCallbackActivity;

public class MySubredditsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, View.OnClickListener
{

    /***
     *
     * ANDROID
     *
     * ***/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_subreddits_layout, container, false);
        initViews(view);
        initVariables();
        return view;
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    private void initVariables() {
        if (getActivity().getSupportLoaderManager().getLoader(R.id.SQL_QUERY_GET_SUBREDDITS) != null) {
            getActivity().getSupportLoaderManager().restartLoader(R.id.SQL_QUERY_GET_SUBREDDITS, null, this);
        } else {
            getActivity().getSupportLoaderManager().initLoader(R.id.SQL_QUERY_GET_SUBREDDITS, null, this);
        }
}

    private void initViews(View view) {
        DrawerLayout drawerLayout =  (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_subreddit_toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        View reinitIcon = view.findViewById(R.id.reinit_icon);
        reinitIcon.setOnClickListener(this);

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.my_subreddit_fab);
        floatingActionButton.setOnClickListener(this);
    }

    /***
     *
     * OVERRIDED METHODS
     *
     * ***/

    @Override
    public Loader<Cursor> onCreateLoader(int query, Bundle bundle) {
        switch (query) {
            case R.id.SQL_QUERY_GET_SUBREDDITS:
                CursorLoader cursorLoader = new CursorLoader(getActivity(), SubredditsContract.SELECT_URI, null, null, null, null);
                return cursorLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        try {
            if (loader.getId() == R.id.SQL_QUERY_GET_SUBREDDITS) {
                if (cursor != null) {
                    SubredditsAdapter adapter = (SubredditsAdapter) getListAdapter();
                    if (adapter == null) {
                        adapter = new SubredditsAdapter(getActivity(), cursor);
                        setListAdapter(adapter);
                    } else {
                        adapter.changeCursor(cursor);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Context c = null;
        try {
            c = getActivity();
            if (c == null) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        if (loader.getId() == R.id.SQL_QUERY_GET_SUBREDDITS) {
            SubredditsAdapter adapter = (SubredditsAdapter) getListAdapter();
            if (adapter == null) {
                adapter = new SubredditsAdapter(c, null);
                setListAdapter(adapter);
            } else {
                adapter.changeCursor(null);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3)
    {

    }

    @Override
    public void onClick(View v)
    {
        DialogCallbackActivity callbackActivity = (DialogCallbackActivity) getActivity();
        switch (v.getId()) {
            case R.id.reinit_icon:
                callbackActivity.displayReinitSubredditsDialog();
                break;
            case R.id.my_subreddit_fab:
                callbackActivity.displayAddSubredditDialog();
                break;
        }
    }
}
