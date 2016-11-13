package fr.zait.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.zait.MySharedPreferences;
import fr.zait.R;
import fr.zait.interfaces.callback.DialogCallbackInterface;
import fr.zait.interfaces.callback.FragmentCallbackInterface;
import fr.zait.interfaces.callback.PostDetailCallbackInterface;
import fr.zait.adapters.HomeAdapter;
import fr.zait.controllers.RefreshingController;
import fr.zait.data.database.dao.SubredditsDao;
import fr.zait.data.entities.Post;
import fr.zait.data.entities.Subreddit;
import fr.zait.fragments.base.MyFragment;
import fr.zait.listeners.RecyclerItemClickListener;
import fr.zait.requests.FetchPostsFromSubreddit;
import fr.zait.utils.StringUtils;

public class HomeFragment extends MyFragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener, View.OnTouchListener, Toolbar.OnMenuItemClickListener {

    private HomeAdapter recyclerAdapter;
    private RefreshingController refreshingController;
    private FetchPostsFromSubreddit fetchPostsFromSubreddit;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TextView filter;

    private int previousTotal = 0;
    private int visibleThreshold = 2;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private List<String> spinnerArray;
    private String selectedSubreddit;
    private List<Subreddit> subreddits;
    private Spinner subredditItems;

    /***
     * ANDROID
     ***/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_layout, container, false);

        initVariables(view);
        initViews(view);
        if (!StringUtils.isEmpty(selectedSubreddit)) {
            fetchPostsFromSubreddit.fetchPosts();
        }

        return view;
    }

    /***
     * PRIVATE METHODS
     ***/


    @Override
    protected void initVariables(View view) {
        String filterName = MySharedPreferences.getSharedPreferences(getActivity()).getString(MySharedPreferences.SELECTED_FILTER, "Hot");

        if (getArguments() != null && !StringUtils.isEmpty(getArguments().getString(FragmentCallbackInterface.EXTRAS.SUBREDDIT_NAME))) {
            selectedSubreddit = getArguments().getString(FragmentCallbackInterface.EXTRAS.SUBREDDIT_NAME);
            MySharedPreferences.saveSelectedSubreddit(getActivity(), selectedSubreddit);
        } else {
            selectedSubreddit = MySharedPreferences.getSharedPreferences(getActivity()).getString(MySharedPreferences.SELECTED_SUBREDDIT, "Android");
        }

        refreshingController = new RefreshingController(getActivity(), view, this);

        filter = (TextView) view.findViewById(R.id.filter);
        filter.setText(filterName);

        recyclerAdapter = new HomeAdapter(getActivity(), refreshingController);
        fetchPostsFromSubreddit = new FetchPostsFromSubreddit(getActivity(), selectedSubreddit, filterName.toLowerCase(), recyclerAdapter);

    }

    @Override
    protected void initViews(View view) {

        // Main views

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.home_toolbar);
        DialogCallbackInterface dialogCallbackInterface = (DialogCallbackInterface) getActivity();
        dialogCallbackInterface.attachDrawerToggle(toolbar);
        toolbar.inflateMenu(R.menu.menu_home);
        toolbar.setOnMenuItemClickListener(this);

        // Recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                CardView cardView = (CardView)view.findViewById(R.id.card_view);
//                cardView.setCardBackgroundColor(R.color.light_gray);

                recyclerAdapter.setPostHasBeenSeen(position);

                Post post = recyclerAdapter.getPost(position);
                PostDetailCallbackInterface postDetailCallbackInterface = (PostDetailCallbackInterface) getActivity();
                postDetailCallbackInterface.openPostDetail(post);
            }
        }));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (refreshingController.isLoading) {
                    if (totalItemCount > previousTotal) {
                        refreshingController.isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!refreshingController.isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    fetchPostsFromSubreddit.fetchMorePosts();
                    refreshingController.setProgressBarVisibility(View.VISIBLE);
                }
            }
        });

        // Subreddits views
        subreddits = SubredditsDao.getSubreddits();
        spinnerArray = new ArrayList<String>();

        for (int i = 0; i < subreddits.size(); i++) {
            spinnerArray.add(subreddits.get(i).name);
            if (StringUtils.isEmpty(selectedSubreddit) && i == 0) {
                selectedSubreddit = subreddits.get(i).name;
                MySharedPreferences.saveSelectedSubreddit(getActivity(), selectedSubreddit);
            }
        }

        subredditItems = (Spinner) view.findViewById(R.id.spinner_subreddits);
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

        // FAB
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.home_fab);
        floatingActionButton.setOnTouchListener(this);

    }

    /***
     * OVERRIDED METHODS
     ***/

    @Override
    public void onRefresh() {
        refreshingController.setSwipeRefreshLayoutRefreshing(true);
        refreshingController.setSwipeRefreshLayoutEnabled(false);
        refreshingController.setProgressBarVisibility(View.GONE);
        refreshingController.setNoNetworkConnectionView(View.GONE);
        fetchPostsFromSubreddit.fetchPosts();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        refreshingController.setProgressBarVisibility(View.VISIBLE);
        refreshingController.setNoNetworkConnectionView(View.GONE);
        fetchPostsFromSubreddit.switchSubredditName(spinnerArray.get(position));
        selectedSubreddit = spinnerArray.get(position);
        MySharedPreferences.saveSelectedSubreddit(getActivity(), selectedSubreddit);
        fetchPostsFromSubreddit.fetchPosts();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            recyclerAdapter.deletePostThatHasBeenSeen();
            return true;
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hot:
            case R.id.action_new:
            case R.id.action_top:
            case R.id.action_controversial:
                MySharedPreferences.saveSelectedFilter(getActivity(), item.getTitle().toString());
                filter.setText(item.getTitle().toString());
                fetchPostsFromSubreddit.switchFilter(item.getTitle().toString().toLowerCase());
                fetchPostsFromSubreddit.fetchPosts();
                break;
        }
        return true;
    }

}
