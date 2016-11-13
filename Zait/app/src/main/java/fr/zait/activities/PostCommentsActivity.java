package fr.zait.activities;

import android.content.Intent;
import android.net.Uri;
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
import fr.zait.behavior.SmoothScrollingLayoutManager;
import fr.zait.controllers.RefreshingController;
import fr.zait.data.entities.Post;
import fr.zait.listeners.RecyclerItemClickListener;
import fr.zait.requests.FetchCommentsFromPost;
import fr.zait.utils.StringUtils;

public class PostCommentsActivity extends MyActivity implements View.OnClickListener, View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener {

    public static class EXTRAS {
        public final static String POST = "Post";
    }

    private Post post;

    private FetchCommentsFromPost fetchCommentsFromPost;
    private RefreshingController refreshingController;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CommentsAdapter recyclerAdapter;

    private int position = 0;
    private int previousTotal = 0;
    private int visibleThreshold = 2;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    /***
     * ANDROID
     ***/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_comments_layout);

        initVariables();
        initViews(savedInstanceState);
        if (post != null && !StringUtils.isEmpty(String.valueOf(post.id))) {
            fetchCommentsFromPost.fetchComments();
        }

    }

    /***
     * PRIVATE METHODS
     ***/

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
    protected void initViews(Bundle savedInstanceState) {
        findViewById(R.id.post_comments_back_arrow).setOnClickListener(this);
        resetStatusBarColor();

        findViewById(R.id.open_nav_icon).setOnClickListener(this);

        // Recycler view
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new SmoothScrollingLayoutManager(this, position);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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
//                    fetchCommentsFromPost.fetchMoreComments();
//                    refreshingController.setProgressBarVisibility(View.VISIBLE);
                }
            }
        });

        // FAB
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.post_comments_fab);
        floatingActionButton.setOnTouchListener(this);

        findViewById(R.id.close_navigation_fab).setOnTouchListener(this);
        findViewById(R.id.next_comment_fab).setOnClickListener(this);
        findViewById(R.id.previous_comment_fab).setOnClickListener(this);

    }


    /***
     * OVERRIDED METHODS
     ***/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_comments_back_arrow:
                finishWithAnimation();
                break;
            case R.id.open_nav_icon:
                String url = "https://www.reddit.com/r/" + post.subreddit + "/comments/" + post.id;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.next_comment_fab:
                if (recyclerAdapter.getComments().size() > (position + 1)) {
                    layoutManager.smoothScrollToPosition(recyclerView, null, ++position);
                }
                break;
            case R.id.previous_comment_fab:
                if ((position - 1) >= 0) {
                    layoutManager.smoothScrollToPosition(recyclerView, null, --position);
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.post_comments_fab:
                findViewById(R.id.post_comments_fab).setVisibility(View.GONE);
                findViewById(R.id.comment_navigation_layout).setVisibility(View.VISIBLE);
                break;
            case R.id.close_navigation_fab:
                findViewById(R.id.comment_navigation_layout).setVisibility(View.GONE);
                findViewById(R.id.post_comments_fab).setVisibility(View.VISIBLE);
                break;
        }
        return true;
    }

    @Override
    public void onRefresh() {
        refreshingController.setSwipeRefreshLayoutRefreshing(true);
        refreshingController.setSwipeRefreshLayoutEnabled(false);
        refreshingController.setProgressBarVisibility(View.GONE);
        refreshingController.setNoNetworkConnectionView(View.GONE);
        fetchCommentsFromPost.fetchComments();
    }

}
