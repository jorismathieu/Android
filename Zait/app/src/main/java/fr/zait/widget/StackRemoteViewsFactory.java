package fr.zait.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import fr.zait.MySharedPreferences;
import fr.zait.R;
import fr.zait.data.database.dao.SubredditsDao;
import fr.zait.data.entities.Post;
import fr.zait.data.entities.Subreddit;
import fr.zait.interfaces.callback.LastPostCallbackInterface;
import fr.zait.requests.FetchLastPostsFromSubreddit;
import fr.zait.utils.StringUtils;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory, LastPostCallbackInterface
{

    private static final int POST_LIMIT = 10;

    private int appWidgetId;
    private Context context;
    private List<Post> posts;
    private FetchLastPostsFromSubreddit fetchLastPostsFromSubreddit;

    public StackRemoteViewsFactory(Context cxt, Intent itt) {
        Bundle extras = itt.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        context = cxt;
        fetchLastPostsFromSubreddit = new FetchLastPostsFromSubreddit(context, POST_LIMIT, this);
    }

    @Override
    public void onCreate()
    {
        Log.e("ON CREATE", "******");
        String selectedSubreddit = MySharedPreferences.getSharedPreferences(context).getString(MySharedPreferences.SELECTED_SUBREDDIT, "");

        if (StringUtils.isEmpty(selectedSubreddit)) {
            List<Subreddit> subreddits = SubredditsDao.getSubreddits();
            for (int i = 0; i < subreddits.size(); i++) {
                selectedSubreddit = subreddits.get(i).name;
                break;
            }
        }

        if (!StringUtils.isEmpty(selectedSubreddit)) {
            fetchLastPostsFromSubreddit.switchSubredditName(selectedSubreddit);
            fetchLastPostsFromSubreddit.fetchPosts();
        }

    }

    @Override
    public void onDataSetChanged()
    {
        Log.e("ON DATA SET CHANGED", "******");
    }

    @Override
    public void onDestroy()
    {

    }

    @Override
    public int getCount()
    {
        if (posts != null)
            return posts.size();
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.appwidget_item);
        if (posts != null) {
            rv.setTextViewText(R.id.post_title, posts.get(position).title);
            rv.setTextViewText(R.id.author, posts.get(position).author);
            rv.setTextViewText(R.id.subreddit, posts.get(position).subreddit);

            // Click event
            Bundle extras = new Bundle();
            extras.putParcelable(AppWidgetReceiver.EXTRA_POST, posts.get(position));
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.appwidget_item, fillInIntent);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }


    @Override
    public void postsReady()
    {
        Log.e("POST READY", "******");
        posts = fetchLastPostsFromSubreddit.getPosts();
        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view);
    }

}
