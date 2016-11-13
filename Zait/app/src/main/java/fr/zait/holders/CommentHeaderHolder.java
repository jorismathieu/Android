package fr.zait.holders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import fr.zait.R;
import fr.zait.data.entities.Post;
import fr.zait.holders.base.MyRecyclerHolder;
import fr.zait.utils.DateUtils;

public class CommentHeaderHolder extends MyRecyclerHolder {

    private TextView title;
    private TextView author;
    private TextView body;
    private TextView subreddit;
    private TextView points;
    private TextView commentNumber;
    private TextView url;
    private TextView date;

    public View container;
    public CardView cardView;

    private ImageView postThumbnail;


    public CommentHeaderHolder(Context context, View v) {
        super(context, v);

        title = (TextView) v.findViewById(R.id.title);
        author = (TextView) v.findViewById(R.id.author);
        subreddit = (TextView) v.findViewById(R.id.subreddit);
        points = (TextView) v.findViewById(R.id.points);
        commentNumber = (TextView) v.findViewById(R.id.comment_number);
        url = (TextView) v.findViewById(R.id.url);
        date = (TextView) v.findViewById(R.id.date);
        body = (TextView) v.findViewById(R.id.body);
        postThumbnail = (ImageView) v.findViewById(R.id.post_image);
        container = v.findViewById(R.id.card_container);
        cardView = (CardView) v.findViewById(R.id.card_view);
    }

    @Override
    public void populateView(Object obj) {
        try {
            Post post = (Post) obj;

            title.setText(post.title);
            author.setText(post.author);
            subreddit.setText(post.subreddit);
            points.setText(Integer.toString(post.points));
            commentNumber.setText(Integer.toString(post.numComments) + " " +
                    (post.numComments > 1 ? cxt.getResources().getString(R.string.comment_plur) : cxt.getResources().getString(R.string.comment)));
            url.setText(post.domain);
            date.setText(DateUtils.getDateFromTimestamp(post.createdUtc));

            body.setText(post.text);

            if (!post.thumbnail.equals("self")) {
                postThumbnail.setVisibility(View.VISIBLE);
                Ion.with(postThumbnail).load(post.thumbnail);
            }
            else {
                postThumbnail.setVisibility(View.GONE);
            }

        }
        catch (Exception e) {

        }
    }
}
