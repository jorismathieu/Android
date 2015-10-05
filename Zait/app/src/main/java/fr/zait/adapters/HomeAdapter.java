package fr.zait.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import fr.zait.R;
import fr.zait.controllers.SubredditRefreshingController;
import fr.zait.data.entities.Post;
import fr.zait.holders.HomeHolder;
import fr.zait.utils.DateUtils;

public class HomeAdapter extends RecyclerView.Adapter<HomeHolder>
{

    private Context cxt;
    private SubredditRefreshingController subredditRefreshingController;

    private List<Post> posts;
    private int lastPosition;

    public HomeAdapter(Context context, SubredditRefreshingController subrc) {
        cxt = context;
        subredditRefreshingController = subrc;
        lastPosition = -1;
        posts = new ArrayList<>();
    }

    public void setNewPosts(List<Post> newPosts) {
        posts.addAll(newPosts);
        notifyDataSetChanged();
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
        subredditRefreshingController.setProgressBarVisibility(View.GONE);
        subredditRefreshingController.setSwipeRefreshLayoutRefreshing(false);
        subredditRefreshingController.setSwipeRefreshLayoutEnabled(true);
    }

    public void clearPostsList() {
        posts.clear();
        lastPosition = -1;
    }

    public void displayConnectionError() {
        subredditRefreshingController.displayConnectionError();
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_layout_cardview, parent, false);
        HomeHolder vh = new HomeHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HomeHolder holder, int position) {
        holder.title.setText(posts.get(position).title);
        holder.author.setText(posts.get(position).author);
        holder.subreddit.setText(posts.get(position).subreddit);
        holder.points.setText(Integer.toString(posts.get(position).points));
        holder.commentNumber.setText(Integer.toString(posts.get(position).numComments) + " " +
                    (posts.get(position).numComments > 1 ? cxt.getResources().getString(R.string.comment_plur) : cxt.getResources().getString(R.string.comment)));
        holder.url.setText(posts.get(position).domain);
        holder.date.setText(DateUtils.getDateFromTimestamp(posts.get(position).createdUtc));

        if (!posts.get(position).thumbnail.equals("self")) {
            holder.postThumbnail.setVisibility(View.VISIBLE);
            Ion.with(holder.postThumbnail).load(posts.get(position).thumbnail);
        } else {
            holder.postThumbnail.setVisibility(View.GONE);
        }

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

    private void setAnimation(View viewToAnimate)
    {
        Animation animation = AnimationUtils.loadAnimation(cxt, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
    }

}

