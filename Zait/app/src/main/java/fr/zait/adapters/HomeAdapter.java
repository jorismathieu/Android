package fr.zait.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import fr.zait.R;
import fr.zait.controllers.RefreshingController;
import fr.zait.data.entities.Post;
import fr.zait.holders.HomeHolder;

public class HomeAdapter extends RecyclerView.Adapter<HomeHolder> {

    private Context cxt;
    private RefreshingController refreshingController;

    private List<Post> posts;
    private int lastPosition;

    public HomeAdapter(Context context, RefreshingController rc) {
        cxt = context;
        refreshingController = rc;
        lastPosition = -1;
        posts = new ArrayList<>();
    }

    public void setNewPosts(List<Post> newPosts) {
        posts.addAll(newPosts);
        notifyDataSetChanged();
    }

    public Post getPost(int position) {
        return posts.get(position);
    }

    public void setPostHasBeenSeen(int position) {
        posts.get(position).hasBeenSeen = true;
    }

    public void deletePostThatHasBeenSeen() {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).hasBeenSeen == true) {
                notifyItemRemoved(i);
                posts.remove(i);
            }
        }
    }

    public void loadingIsDone() {
        refreshingController.setProgressBarVisibility(View.GONE);
        refreshingController.setSwipeRefreshLayoutRefreshing(false);
        refreshingController.setSwipeRefreshLayoutEnabled(true);
        if (!refreshingController.isConnectionError()) {
            if (posts.size() > 0) {
                refreshingController.setNoResultView(View.GONE);
            }
            else {
                refreshingController.setNoResultView(View.VISIBLE);
            }
        }
    }

    public void clearPostsList() {
        posts.clear();
        lastPosition = -1;
    }

    public void displayConnectionError() {
        if (posts.size() > 0) {
            refreshingController.displayConnectionError(RefreshingController.OPTIONS.NO_HOLDER);
        }
        else {
            refreshingController.displayConnectionError(RefreshingController.OPTIONS.HOLDER);
        }
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_layout_cardview, parent, false);
        HomeHolder vh = new HomeHolder(cxt, v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HomeHolder holder, int position) {

        holder.populateView(posts.get(position));

//        if (position > lastPosition)
//        {
//            setAnimation(holder.container);
//            lastPosition = position;
//        }

//        if (posts.get(position).hasBeenSeen) {
//            holder.cardView.setCardBackgroundColor(R.color.light_gray);
//        } else {
//            holder.cardView.setCardBackgroundColor(R.color.white);
//        }


    }

    @Override
    public int getItemCount() {
        if (posts != null) {
            return posts.size();
        }
        return 0;
    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(cxt, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
    }

}

