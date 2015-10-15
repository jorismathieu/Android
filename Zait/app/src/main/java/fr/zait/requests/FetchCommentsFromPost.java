package fr.zait.requests;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.zait.adapters.CommentsAdapter;
import fr.zait.data.entities.Comment;
import fr.zait.data.entities.Post;
import fr.zait.requests.base.Request;
import fr.zait.utils.NetworkUtils;

public class FetchCommentsFromPost extends Request
{
    private static final String URL_TEMPLATE = "http://www.reddit.com/r/" + SUBREDDIT_NAME_KEY + "/comments/" + POST_ID_KEY + ".json?after=" + AFTER_KEY;
    private String after;
    private Post post;
    private Context context;
    private String url;
    private List<Comment> comments;
    private CommentsAdapter adapter;

    public FetchCommentsFromPost(Context cxt, Post pst, CommentsAdapter adpt) {
        context = cxt;
        post = pst;
        after = "";
        adapter = adpt;
        generateURL();
    }

    @Override
    protected String generateURL()
    {
        url = URL_TEMPLATE.replace(SUBREDDIT_NAME_KEY, post.subreddit);
        url = url.replace(POST_ID_KEY, String.valueOf(post.id));
        url = url.replace(AFTER_KEY, after);
        return url;
    }

    @Override
    protected void startRequest()
    {
        new Request().execute();
    }

    public void fetchComments() {
        startRequest();
    }

    public void fetchMoreComments()
    {
        generateURL();
        fetchComments();
    }


    private class Request extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... uri) {
            String raw = NetworkUtils.readContents(context, url);
            return raw;
        }

        @Override
        protected void onPostExecute(String raw) {
            super.onPostExecute(raw);
            if (raw != null) {
                try {
                    JSONObject commentsRoot = new JSONArray(raw).getJSONObject(1).getJSONObject("data");
                    after = commentsRoot.getString("after");

                    JSONArray children = commentsRoot.getJSONArray("children");
                    comments = getComments(children);

                    adapter.setNewComments(comments);
                } catch (Exception e){
                    adapter.displayConnectionError();
                }
            } else {
                adapter.displayConnectionError();
            }
            adapter.loadingIsDone();
        }
    }

    private List<Comment> getComments(JSONArray children) {
        List<Comment> comments = new ArrayList<>();
        try
        {
            for (int i = 0; i < children.length(); i++) {
                JSONObject commentJson = children.getJSONObject(i).getJSONObject("data");
                Comment comment = new Comment();

                comment.author = commentJson.optString("author");
                comment.body = commentJson.optString("body");
                comment.score = commentJson.optInt("score");
                comment.createdUtc = commentJson.optLong("created_utc");

                if (!commentJson.get("replies").equals("")) {
                    comment.answers = getComments(commentJson.getJSONObject("replies").getJSONObject("data").getJSONArray("children"));
                    comment.nbAnswers = comment.answers.size();
                } else {
                    comment.nbAnswers = 0;
                    comment.answers = null;
                }

                comments.add(comment);
            }
        }
        catch (Exception e)
        {
        }

        return comments;
    }

}
