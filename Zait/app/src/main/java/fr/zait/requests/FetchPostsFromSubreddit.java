package fr.zait.requests;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.zait.adapters.HomeAdapter;
import fr.zait.data.entities.Post;
import fr.zait.requests.base.Request;
import fr.zait.utils.NetworkUtils;

public class FetchPostsFromSubreddit extends Request {
    private static final String URL_TEMPLATE = "https://www.reddit.com/r/" + SUBREDDIT_NAME_KEY + "/" + FILTER + "/.json?after=" + AFTER_KEY;

    private String subreddit;
    private String after;
    private Context context;
    private String url;
    private String filter;

    public List<Post> posts;
    private HomeAdapter adapter;

    public FetchPostsFromSubreddit(Context cxt, String subr, String filtr, HomeAdapter adapt) {
        subreddit = subr;
        context = cxt;
        after = "";
        filter = filtr;
        adapter = adapt;
        generateURL();
    }

    @Override
    protected String generateURL() {
        url = URL_TEMPLATE.replace(SUBREDDIT_NAME_KEY, subreddit);
        url = url.replace(AFTER_KEY, after);
        url = url.replace(FILTER, filter);
        return url;
    }

    public void startRequest() {
        new Request().execute();
    }

    public void fetchPosts() {
        startRequest();
    }

    public void fetchMorePosts() {
        generateURL();
        fetchPosts();
    }

    public void switchSubredditName(String newSubr) {
        subreddit = newSubr;
        after = "";
        adapter.clearPostsList();
        generateURL();
    }

    public void switchFilter(String newFilter) {
        filter = newFilter;
        after = "";
        adapter.clearPostsList();
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

                    after = data.getString("after");

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
                    adapter.setNewPosts(posts);
                }
                catch (Exception e) {
                    adapter.displayConnectionError();
                }
            } else {
                adapter.displayConnectionError();
            }
            adapter.loadingIsDone();
        }
    }


}
