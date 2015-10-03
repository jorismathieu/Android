package fr.zait.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.zait.R;

public class HomeHolder extends RecyclerView.ViewHolder
{
    public TextView title;
    public TextView author;
    public TextView subreddit;
    public TextView points;
    public TextView commentNumber;
    public TextView url;
    public TextView date;

    public View container;

    public ImageView postThumbnail;

    public HomeHolder(View v)
    {
        super(v);
        title = (TextView) v.findViewById(R.id.title);
        author = (TextView) v.findViewById(R.id.author);
        subreddit = (TextView) v.findViewById(R.id.subreddit);
        points = (TextView) v.findViewById(R.id.points);
        commentNumber = (TextView) v.findViewById(R.id.comment_number);
        url = (TextView) v.findViewById(R.id.url);
        date = (TextView) v.findViewById(R.id.date);
        postThumbnail = (ImageView) v.findViewById(R.id.post_image);
        container = v.findViewById(R.id.card_container);
    }
}

