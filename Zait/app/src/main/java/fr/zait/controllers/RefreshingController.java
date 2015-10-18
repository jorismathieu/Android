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
    public static class OPTIONS {
        public static final String HOLDER = "HOLDER";
        public static final String NO_HOLDER = "NO_HOLDER";
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private View noNetworkConnectionView;
    private View noResultView;

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
        noResultView = rootView.findViewById(R.id.no_result);
        noNetworkConnectionView = rootView.findViewById(R.id.no_network_connection);
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

    public void setNoNetworkConnectionView(int visibility) {
        noNetworkConnectionView.setVisibility(visibility);
    }

    public void setNoResultView(int visibility) {
        noResultView.setVisibility(visibility);
    }

    public void displayConnectionError(String option) {
        if (option.equals(OPTIONS.HOLDER)) {
            setNoNetworkConnectionView(View.VISIBLE);
        }
        ErrorUtils.displayError(context, rootView, ErrorUtils.NETWORK_CONNECTION_ERROR);
    }

    public boolean isConnectionError() {
        if (noNetworkConnectionView.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

}
