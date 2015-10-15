package fr.zait.controllers;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;

import fr.zait.R;
import fr.zait.controllers.base.MyController;
import fr.zait.utils.ErrorUtils;

public class RefreshingController extends MyController
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    private SwipeRefreshLayout.OnRefreshListener listener;

    public boolean isLoading;

    public RefreshingController(Context context, View rootView, SwipeRefreshLayout.OnRefreshListener lsner) {
        super(context, rootView);
        listener = lsner;
        isLoading = true;
        initController();
    }

    @Override
    protected void initController()
    {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primaryColor, R.color.primaryDarkColor);
        swipeRefreshLayout.setOnRefreshListener(listener);
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
