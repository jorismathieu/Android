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
import fr.zait.adapters.HomeAdapter;
import fr.zait.controllers.SubredditRefreshingController;
import fr.zait.requests.FetchPostFromSubreddit;

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
        getPostFromCurrentSubreddit();

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
        fetchPostFromSubreddit =  new FetchPostFromSubreddit(getActivity(), "Android", recyclerAdapter);
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

        spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Android");
        spinnerArray.add("Art");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        Spinner subredditItems = (Spinner) view.findViewById(R.id.spinner_subreddits);
        subredditItems.setAdapter(spinnerAdapter);
        subredditItems.setOnItemSelectedListener(this);

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
        getPostFromCurrentSubreddit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }
}
