package fr.zait.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import fr.zait.MySharedPreferences;
import fr.zait.R;
import fr.zait.activities.base.DialogCallbackInterface;
import fr.zait.activities.base.FragmentCallbackInterface;
import fr.zait.activities.base.PostDetailCallbackInterface;
import fr.zait.adapters.HomeAdapter;
import fr.zait.controllers.RefreshingController;
import fr.zait.data.database.dao.SubredditsDao;
import fr.zait.data.entities.Post;
import fr.zait.data.entities.Subreddit;
import fr.zait.fragments.base.MyFragment;
import fr.zait.listeners.RecyclerItemClickListener;
import fr.zait.requests.FetchPostsFromSubreddit;
import fr.zait.utils.StringUtils;

public class HomeFragment extends MyFragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener, View.OnTouchListener
{

    private HomeAdapter recyclerAdapter;
    private RefreshingController refreshingController;
    private FetchPostsFromSubreddit fetchPostsFromSubreddit;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private int previousTotal = 0;
    private int visibleThreshold = 2;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private List<String> spinnerArray;
    private String selectedSubreddit;
    private List<Subreddit> subreddits;
    private Spinner subredditItems;

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
        fetchPostsFromSubreddit.fetchPosts();
    }

    private void getMorePostFromCurrentSubreddit() {
        fetchPostsFromSubreddit.fetchMorePosts();
    }

    @Override
    protected void initVariables() {
        fetchPostsFromSubreddit =  new FetchPostsFromSubreddit(getActivity(), selectedSubreddit, recyclerAdapter);
    }

    @Override
    protected void initViews(View view) {

        // Main views
        refreshingController = new RefreshingController(getActivity(), view, this);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.home_toolbar);
        DialogCallbackInterface dialogCallbackInterface = (DialogCallbackInterface) getActivity();
        dialogCallbackInterface.attachDrawerToggle(toolbar);
        toolbar.inflateMenu(R.menu.menu_home);

        // Recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new HomeAdapter(getActivity(), refreshingController);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
//                CardView cardView = (CardView)view.findViewById(R.id.card_view);
//                cardView.setCardBackgroundColor(R.color.light_gray);

                recyclerAdapter.setPostHasBeenSeen(position);

                Post post = recyclerAdapter.getPost(position);
                PostDetailCallbackInterface postDetailCallbackInterface = (PostDetailCallbackInterface) getActivity();
                postDetailCallbackInterface.openPostDetail(post);
            }
        }));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (refreshingController.isLoading)
                {
                    if (totalItemCount > previousTotal)
                    {
                        refreshingController.isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!refreshingController.isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))
                {
                    getMorePostFromCurrentSubreddit();
                    refreshingController.setProgressBarVisibility(View.VISIBLE);
                }
            }
        });

        // Subreddits views
        subreddits = SubredditsDao.getSubreddits();
        spinnerArray =  new ArrayList<String>();
        if (getArguments() != null && !StringUtils.isEmpty(getArguments().getString(FragmentCallbackInterface.EXTRAS.SUBREDDIT_NAME))) {
            selectedSubreddit = getArguments().getString(FragmentCallbackInterface.EXTRAS.SUBREDDIT_NAME);
            MySharedPreferences.getSharedPreferences(getActivity()).edit().putString(MySharedPreferences.SELECTED_SUBREDDIT, selectedSubreddit).commit();
        } else {
            selectedSubreddit = MySharedPreferences.getSharedPreferences(getActivity()).getString(MySharedPreferences.SELECTED_SUBREDDIT, "");
        }
        for (int i = 0; i < subreddits.size(); i++) {
            spinnerArray.add(subreddits.get(i).name);
            if (StringUtils.isEmpty(selectedSubreddit) && i == 0) {
                selectedSubreddit = subreddits.get(i).name;
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
     *
     * OVERRIDED METHODS
     *
     * ***/

    @Override
    public void onRefresh()
    {
        refreshingController.setSwipeRefreshLayoutRefreshing(true);
        refreshingController.setSwipeRefreshLayoutEnabled(false);
        refreshingController.setProgressBarVisibility(View.GONE);
        getPostFromCurrentSubreddit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        refreshingController.setProgressBarVisibility(View.VISIBLE);
        fetchPostsFromSubreddit.switchSubredditName(spinnerArray.get(position));
        selectedSubreddit = spinnerArray.get(position);
        MySharedPreferences.getSharedPreferences(getActivity()).edit().putString(MySharedPreferences.SELECTED_SUBREDDIT, selectedSubreddit).commit();
        getPostFromCurrentSubreddit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            recyclerAdapter.deletePostThatHasBeenSeen();
            return true;
        }
        return true;
    }
}
