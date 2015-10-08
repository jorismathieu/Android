package fr.zait.fragments;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fr.zait.R;
import fr.zait.activities.base.DialogCallbackActivity;
import fr.zait.fragments.base.MyListFragment;

public class SearchFragment extends MyListFragment
{
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
        initViews(view);
        initVariables();
        return view;
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.search_toolbar);
        DialogCallbackActivity dialogCallbackActivity = (DialogCallbackActivity) getActivity();
        dialogCallbackActivity.attachDrawerToggle(toolbar);

        toolbar.inflateMenu(R.menu.menu_search);

        MenuItem searchItem = toolbar.getMenu().findItem(R.id.search);
        MenuItemCompat.expandActionView(searchItem);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });


    }

}
