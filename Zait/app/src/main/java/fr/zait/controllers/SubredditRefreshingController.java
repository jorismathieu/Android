package fr.zait.controllers;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;

import fr.zait.R;
import fr.zait.controllers.base.MyController;
import fr.zait.fragments.HomeFragment;
import fr.zait.utils.ErrorUtils;

public class SubredditRefreshingController extends MyController
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private HomeFragment homeFragment;

    public boolean isLoading;

    public SubredditRefreshingController(Context context, View rootView, HomeFragment hf) {
        super(context, rootView);
        homeFragment = hf;
        isLoading = true;
        initController();
    }

    @Override
    protected void initController()
    {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primaryColor, R.color.primaryDarkColor);
        swipeRefreshLayout.setOnRefreshListener(homeFragment);
        progressBar = (ProgressBar) rootView.findViewById(R.id.main_progress_bar);
    }

    public void setSwipeRefreshLayoutEnabled(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

    public void setSwipeRefreshLayoutRefreshing(boolean enable) {
        swipeRefreshLayout.setRefreshing(enable);
    }

    public void setProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            isLoading = true;
        } else {
            isLoading = false;
        }
    }

    public void displayConnectionError() {
        ErrorUtils.displayError(context, rootView, ErrorUtils.CONNECTION_ERROR);
    }

}
