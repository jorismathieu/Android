package fr.zait.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import fr.zait.R;
import fr.zait.activities.base.MyActivity;
import fr.zait.adapters.CommentsAdapter;
import fr.zait.controllers.RefreshingController;
import fr.zait.data.entities.Post;
import fr.zait.listeners.RecyclerItemClickListener;
import fr.zait.requests.FetchCommentsFromPost;
import fr.zait.utils.StringUtils;

public class PostCommentsActivity extends MyActivity implements View.OnClickListener, View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener
{

    public static class EXTRAS {
        public final static String POST = "Post";
    }

    private Post post;

    private FetchCommentsFromPost fetchCommentsFromPost;
    private RefreshingController refreshingController;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CommentsAdapter recyclerAdapter;

    private int previousTotal = 0;
    private int visibleThreshold = 2;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    /***
     *
     * ANDROID
     *
     * ***/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_comments_layout);

        initVariables();
        initViews(savedInstanceState);
        if (post != null && !StringUtils.isEmpty(String.valueOf(post.id))) {
            getCommentsFromPost();
        }

    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    @Override
    protected void initVariables() {
        enterAnimId = R.anim.push_out_right;
        exitAnimId = R.anim.push_in_right;

        Intent intent = getIntent();
        post = intent.getParcelableExtra(EXTRAS.POST);

        refreshingController = new RefreshingController(this, findViewById(android.R.id.content), this);
        recyclerAdapter = new CommentsAdapter(this, refreshingController, post);

        fetchCommentsFromPost = new FetchCommentsFromPost(this, post, recyclerAdapter);

    }


    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        findViewById(R.id.post_comments_back_arrow).setOnClickListener(this);
        resetStatusBarColor();

        // Recycler view
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
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
                    getMoreCommentsFromPost();
                    refreshingController.setProgressBarVisibility(View.VISIBLE);
                }
            }
        });

        // FAB
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.post_comments_fab);
        floatingActionButton.setOnTouchListener(this);

    }


    private void getCommentsFromPost() {
        fetchCommentsFromPost.fetchComments();
    }
    private void getMoreCommentsFromPost() {
        fetchCommentsFromPost.fetchMoreComments();
    }


    /***
     *
     * OVERRIDED METHODS
     *
     * ***/

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.post_comments_back_arrow:
                finishWithAnimation();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return true;
    }

    @Override
    public void onRefresh()
    {
        refreshingController.setSwipeRefreshLayoutRefreshing(true);
        refreshingController.setSwipeRefreshLayoutEnabled(false);
        refreshingController.setProgressBarVisibility(View.GONE);
        getCommentsFromPost();
    }

}
