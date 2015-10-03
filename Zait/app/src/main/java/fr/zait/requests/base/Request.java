package fr.zait.requests.base;


public abstract class Request
{
    protected String url;

    protected abstract String generateURL();
    protected abstract void startRequest();
}
