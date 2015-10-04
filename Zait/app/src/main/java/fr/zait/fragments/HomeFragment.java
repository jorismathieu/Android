package fr.zait.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import fr.zait.R;
import fr.zait.activities.base.MyApplication;
import fr.zait.adapters.HomeAdapter;
import fr.zait.controllers.SubredditRefreshingController;
import fr.zait.data.beans.Subreddit;
import fr.zait.data.database.dao.SubredditsDao;
import fr.zait.requests.FetchPostFromSubreddit;
import fr.zait.utils.StringUtils;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener
{

    private HomeAdapter recyclerAdapter;
    private SubredditRefreshingController subredditRefreshingController;
    private FetchPostFromSubreddit fetchPostFromSubreddit;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private int previousTotal = 0;
    private int visibleThreshold = 2;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private List<String> spinnerArray;
    private String selectedSubreddit;
    private List<Subreddit> subreddits;

    /***
     *
     * ANDROID
     *
     * ***/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_layout, container, false);

        initViews(view);
        initVariables();
        if (!StringUtils.isEmpty(selectedSubreddit)) {
            getPostFromCurrentSubreddit();
        }

        return view;
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    private void getPostFromCurrentSubreddit() {
        fetchPostFromSubreddit.fetchPosts();
    }

    private void getMorePostFromCurrentSubreddit() {
        fetchPostFromSubreddit.fetchMorePosts();
    }

    private void initVariables() {
        fetchPostFromSubreddit =  new FetchPostFromSubreddit(getActivity(), selectedSubreddit, recyclerAdapter);
    }

    private void initViews(View view) {

        subredditRefreshingController = new SubredditRefreshingController(getActivity(), view, this);

        DrawerLayout drawerLayout =  (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.home_toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        toolbar.inflateMenu(R.menu.menu_home);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new HomeAdapter(getActivity(), subredditRefreshingController);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (subredditRefreshingController.isLoading)
                {
                    if (totalItemCount > previousTotal)
                    {
                        subredditRefreshingController.isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!subredditRefreshingController.isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))
                {
                    getMorePostFromCurrentSubreddit();
                    subredditRefreshingController.setProgressBarVisibility(View.VISIBLE);
                }
            }
        });

        SubredditsDao subredditsDao = new SubredditsDao(getActivity());
        subreddits = subredditsDao.getSubreddits();
        spinnerArray =  new ArrayList<String>();
        selectedSubreddit = MyApplication.getInstance().getSharedPreferences().getString(MyApplication.SELECTED_SUBREDDIT, "");
        for (int i = 0; i < subreddits.size(); i++) {
            spinnerArray.add(subreddits.get(i).name);
            if (StringUtils.isEmpty(selectedSubreddit) && i == 0) {
                selectedSubreddit = subreddits.get(i).name;
            }
        }

        Spinner subredditItems = (Spinner) view.findViewById(R.id.spinner_subreddits);
        if (spinnerArray.size() > 0) {
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerArray);
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            subredditItems.setVisibility(View.VISIBLE);
            subredditItems.setAdapter(spinnerAdapter);
            subredditItems.setSelection(spinnerAdapter.getPosition(selectedSubreddit));
            subredditItems.setOnItemSelectedListener(this);
        } else {
            subredditItems.setVisibility(View.GONE);
        }


    }

    /***
     *
     * OVERRIDED METHODS
     *
     * ***/

    @Override
    public void onRefresh()
    {
        subredditRefreshingController.setSwipeRefreshLayoutRefreshing(true);
        subredditRefreshingController.setSwipeRefreshLayoutEnabled(false);
        subredditRefreshingController.setProgressBarVisibility(View.GONE);
        getPostFromCurrentSubreddit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        subredditRefreshingController.setProgressBarVisibility(View.VISIBLE);
        fetchPostFromSubreddit.switchSubredditName(spinnerArray.get(position));
        selectedSubreddit = spinnerArray.get(position);
        MyApplication.getInstance().getSharedPreferences().edit().putString(MyApplication.SELECTED_SUBREDDIT, selectedSubreddit).commit();
        getPostFromCurrentSubreddit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }
}
