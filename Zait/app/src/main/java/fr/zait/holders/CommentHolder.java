package fr.zait.holders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import fr.zait.R;
import fr.zait.data.entities.Comment;
import fr.zait.holders.base.MyRecyclerHolder;
import fr.zait.utils.DateUtils;

public class CommentHolder extends MyRecyclerHolder {
    private TextView author;
    private TextView body;
    private TextView score;
    private TextView date;
    private TextView nbAnswers;

    public View container;
    public CardView cardView;


    public CommentHolder(Context context, View v) {
        super(context, v);

        author = (TextView) v.findViewById(R.id.author);
        body = (TextView) v.findViewById(R.id.body);
        score = (TextView) v.findViewById(R.id.score);
        date = (TextView) v.findViewById(R.id.date);
        nbAnswers = (TextView) v.findViewById(R.id.nb_answers);

        container = v.findViewById(R.id.card_container);
        cardView = (CardView) v.findViewById(R.id.card_view);
    }

    @Override
    public void populateView(Object obj) {
        try {
            Comment comment = (Comment) obj;

            author.setText(comment.author);
            body.setText(comment.body);
            score.setText(String.valueOf(comment.score) + " " + (comment.score > 1 ? cxt.getResources().getString(R.string.points) : cxt.getResources().getString(R.string.point)));
            date.setText(DateUtils.getDateFromTimestamp(comment.createdUtc));

            if (comment.nbAnswers > 0) {
                nbAnswers.setVisibility(View.VISIBLE);
                nbAnswers.setText("(" + comment.nbAnswers + " " + (comment.nbAnswers > 1 ? cxt.getResources().getString(R.string.answers) : cxt.getResources().getString(R.string.answer)) + ")");
            }
            else {
                nbAnswers.setVisibility(View.GONE);
            }

        }
        catch (Exception e) {

        }
    }
}

