package fr.zait.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fr.zait.R;
import fr.zait.activities.base.DialogCallbackInterface;
import fr.zait.activities.base.PostDetailCallbackInterface;
import fr.zait.adapters.HomeAdapter;
import fr.zait.controllers.RefreshingController;
import fr.zait.data.entities.Post;
import fr.zait.fragments.base.MyFragment;
import fr.zait.listeners.RecyclerItemClickListener;
import fr.zait.requests.FetchPostsFromSearch;
import fr.zait.utils.DisplayUtils;
import fr.zait.utils.StringUtils;

public class SearchFragment extends MyFragment implements SwipeRefreshLayout.OnRefreshListener
{

    private HomeAdapter recyclerAdapter;
    private RefreshingController refreshingController;
    private FetchPostsFromSearch fetchPostsFromSearch;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private int previousTotal = 0;
    private int visibleThreshold = 2;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private String lastQuery = "";

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
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        initVariables(view);
        initViews(view);
        return view;
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    @Override
    protected void initVariables(View view) {
        refreshingController = new RefreshingController(getActivity(), view, this);
        recyclerAdapter = new HomeAdapter(getActivity(), refreshingController);
        fetchPostsFromSearch = new FetchPostsFromSearch(getActivity(), recyclerAdapter);
    }

    @Override
    protected void initViews(final View view) {

        // Toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.search_toolbar);
        DialogCallbackInterface dialogCallbackInterface = (DialogCallbackInterface) getActivity();
        dialogCallbackInterface.attachDrawerToggle(toolbar);

        toolbar.inflateMenu(R.menu.menu_search);

        MenuItem searchItem = toolbar.getMenu().findItem(R.id.search);
        MenuItemCompat.expandActionView(searchItem);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
              {
                  @Override
                  public boolean onQueryTextSubmit(String query)
                  {
                      if (!StringUtils.isEmpty(query) && query.length() >= 2)
                      {
                          refreshingController.setProgressBarVisibility(View.VISIBLE);
                          fetchPostsFromSearch.fetchPosts(query);
                          lastQuery = query;
                      }
                      else
                      {
                          DisplayUtils.snackbar(view, R.string.search_length_error, Snackbar.LENGTH_LONG);
                      }
                      return false;
                  }

                  @Override
                  public boolean onQueryTextChange(String newText)
                  {
                      return false;
                  }
              }

            );

            // Recycler view
            recyclerView=(RecyclerView)view.findViewById(R.id.search_recycler_view);
            layoutManager=new

            LinearLayoutManager(getActivity()

            );
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.addOnItemTouchListener(new

                    RecyclerItemClickListener(getActivity(),

                    new RecyclerItemClickListener.OnItemClickListener()

                    {
                        @Override
                        public void onItemClick(View view, int position)
                        {
                            Post post = recyclerAdapter.getPost(position);
                            PostDetailCallbackInterface postDetailCallbackInterface = (PostDetailCallbackInterface) getActivity();
                            postDetailCallbackInterface.openPostDetail(post);
                        }
                    }

            ));

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override public void onScrolled (RecyclerView recyclerView,int dx, int dy)
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
                        fetchPostsFromSearch.fetchMorePosts();
                        refreshingController.setProgressBarVisibility(View.VISIBLE);
                    }
                }
            }

            );


        }

        /***
         *
         * OVERRIDED METHODS
         *
         * ***/

        @Override
        public void onRefresh()
        {
            if (!StringUtils.isEmpty(lastQuery)) {
                refreshingController.setSwipeRefreshLayoutRefreshing(true);
                refreshingController.setSwipeRefreshLayoutEnabled(false);
                refreshingController.setProgressBarVisibility(View.GONE);
                refreshingController.setNoNetworkConnectionView(View.GONE);
                fetchPostsFromSearch.fetchPosts(lastQuery);
            } else {
                refreshingController.setSwipeRefreshLayoutRefreshing(false);
            }
        }
}
