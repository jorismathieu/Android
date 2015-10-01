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

import fr.zait.R;
import fr.zait.adapters.HomeAdapter;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /***
     *
     * ANDROID
     *
     * ***/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_layout, container, false);

        initViews(view);

        return view;
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    private void initViews(View view) {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.primaryColor, R.color.primaryDarkColor);
        swipeRefreshLayout.setOnRefreshListener(this);

        DrawerLayout drawerLayout =  (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.home_toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
        toolbar.inflateMenu(R.menu.menu_home);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        String[] dataset = new String[300];
        for (int i = 0; i < 250; i++) {
            dataset[i] = "test " + i;
        }
        recyclerAdapter = new HomeAdapter(dataset);
        recyclerView.setAdapter(recyclerAdapter);
    }

    /***
     *
     * OVERRIDED METHODS
     *
     * ***/

    @Override
    public void onRefresh()
    {
        swipeRefreshLayout.setEnabled(false);
    }
}
