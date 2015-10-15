package fr.zait.requests.base;


public abstract class Request
{
    public static final String SUBREDDIT_NAME_KEY = "SUBREDDIT_NAME";
    public static final String AFTER_KEY = "AFTER";
    public static final String POST_ID_KEY = "POST_ID";

    protected abstract String generateURL();
    protected abstract void startRequest();
}
