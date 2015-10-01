package fr.zait.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.zait.R;
import fr.zait.holders.HomeHolder;

public class HomeAdapter extends RecyclerView.Adapter<HomeHolder> {

    private String[] dataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeAdapter(String[] myDataset) {
        dataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_layout_cardview, parent, false);
        HomeHolder vh = new HomeHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(HomeHolder holder, int position) {
        holder.textView.setText(dataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.length;
    }
}

