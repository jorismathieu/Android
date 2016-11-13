package fr.zait.requests;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.zait.data.entities.Post;
import fr.zait.interfaces.callback.LastPostCallbackInterface;
import fr.zait.requests.base.Request;
import fr.zait.utils.NetworkUtils;

public class FetchLastPostsFromSubreddit extends Request {
    private static final String URL_TEMPLATE = "https://www.reddit.com/r/" + SUBREDDIT_NAME_KEY + "/new.json?limit=" + LIMIT_KEY;

    private String subreddit;
    private String limit;
    private Context context;
    private String url;
    private LastPostCallbackInterface lastPostCallbackInterface;
    public List<Post> posts;

    public FetchLastPostsFromSubreddit(Context cxt, int limitNb, LastPostCallbackInterface callbackInterface) {
        limit = String.valueOf(limitNb);
        context = cxt;
        lastPostCallbackInterface = callbackInterface;
    }

    @Override
    protected String generateURL() {
        url = URL_TEMPLATE.replace(SUBREDDIT_NAME_KEY, subreddit);
        url = url.replace(LIMIT_KEY, limit);
        return url;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void startRequest() {
        new Request().execute();
    }

    public void fetchPosts() {
        startRequest();
    }

    public void switchSubredditName(String newSubr) {
        if (posts != null) {
            posts.clear();
        }
        subreddit = newSubr;
        generateURL();
    }

    private class Request extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... uri) {
            String raw = NetworkUtils.readContents(context, url);
            return raw;
        }

        @Override
        protected void onPostExecute(String raw) {
            super.onPostExecute(raw);
            if (raw != null) {
                posts = new ArrayList<>();
                try {
                    JSONObject data = new JSONObject(raw).getJSONObject("data");
                    JSONArray children = data.getJSONArray("children");

                    for (int i = 0; i < children.length(); i++) {
                        JSONObject cur = children.getJSONObject(i).getJSONObject("data");
                        Post p = new Post();
                        p.title = cur.optString("title");
                        p.url = cur.optString("url");
                        p.numComments = cur.optInt("num_comments");
                        p.points = cur.optInt("score");
                        p.author = cur.optString("author");
                        p.subreddit = cur.optString("subreddit");
                        p.permalink = cur.optString("permalink");
                        p.domain = cur.optString("domain");
                        p.id = cur.optString("id");
                        p.createdUtc = cur.optLong("created_utc");
                        p.thumbnail = cur.optString("thumbnail");
                        p.text = cur.optString("selftext");
                        p.hasBeenSeen = false;
                        if (p.title != null) {
                            posts.add(p);
                        }
                    }

                    lastPostCallbackInterface.postsReady();

                }
                catch (Exception e) {
                }
            } else {
            }
        }
    }
}
