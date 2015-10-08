package fr.zait.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import fr.zait.R;
import fr.zait.activities.base.DialogCallbackActivity;
import fr.zait.activities.base.FragmentCallbackActivity;
import fr.zait.adapters.SubredditsAdapter;
import fr.zait.data.database.contract.SubredditsContract;
import fr.zait.fragments.base.MyListFragment;

public class MySubredditsFragment extends MyListFragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, AdapterView.OnItemClickListener
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

    @Override
    protected void initVariables() {
        if (getActivity().getSupportLoaderManager().getLoader(R.id.SQL_QUERY_GET_SUBREDDITS) != null) {
            getActivity().getSupportLoaderManager().restartLoader(R.id.SQL_QUERY_GET_SUBREDDITS, null, this);
        } else {
            getActivity().getSupportLoaderManager().initLoader(R.id.SQL_QUERY_GET_SUBREDDITS, null, this);
        }
}
    @Override
    protected void initViews(View view) {
        DialogCallbackActivity dialogCallbackActivity = (DialogCallbackActivity) getActivity();
        dialogCallbackActivity.attachDrawerToggle((Toolbar) view.findViewById(R.id.my_subreddit_toolbar));

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
                    getListView().setOnItemClickListener(this);
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
            getListView().setOnItemClickListener(null);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        SubredditsAdapter subredditsAdapter = (SubredditsAdapter) getListAdapter();
        Cursor cursor = subredditsAdapter.getCursor(position);
        String subredditName = cursor.getString(cursor.getColumnIndex(SubredditsContract.SubredditsEntry.COLUMN_NAME));
        Bundle args = new Bundle();
        args.putString(FragmentCallbackActivity.EXTRAS.SUBREDDIT_NAME, subredditName);

        FragmentCallbackActivity activity = (FragmentCallbackActivity) getActivity();
        activity.switchFragment(FragmentCallbackActivity.HOME_FRAGMENT_TAG, args);
    }
}
