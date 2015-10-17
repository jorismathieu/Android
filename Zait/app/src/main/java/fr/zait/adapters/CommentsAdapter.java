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
import fr.zait.data.entities.Comment;
import fr.zait.data.entities.Post;
import fr.zait.holders.CommentHeaderHolder;
import fr.zait.holders.CommentHolder;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context cxt;
    private Post post;
    private RefreshingController refreshingController;

    private List<Comment> comments;
    private int lastPosition;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public CommentsAdapter(Context context, RefreshingController rc, Post pst) {
        cxt = context;
        refreshingController = rc;
        lastPosition = -1;
        comments = new ArrayList<>();
        post = pst;
    }

    public void setNewComments(List<Comment> newComments) {
        comments.addAll(newComments);
        notifyDataSetChanged();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void loadingIsDone() {
        refreshingController.setProgressBarVisibility(View.GONE);
        refreshingController.setSwipeRefreshLayoutRefreshing(false);
        refreshingController.setSwipeRefreshLayoutEnabled(true);
    }

    public void displayConnectionError() {
        refreshingController.displayConnectionError();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comment_cardview, parent, false);
            return new CommentHolder(cxt, v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comment_header_layout, parent, false);
            return new CommentHeaderHolder(cxt, v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentHolder) {
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.populateView(comments.get(position - 1));
        } else {
            CommentHeaderHolder commentHeaderHolder = (CommentHeaderHolder) holder;
            commentHeaderHolder.populateView(post);
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
        if (comments != null) {
            return comments.size();
        }
        return 0;
    }

    private void setAnimation(View viewToAnimate)
    {
        Animation animation = AnimationUtils.loadAnimation(cxt, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
    }

}

