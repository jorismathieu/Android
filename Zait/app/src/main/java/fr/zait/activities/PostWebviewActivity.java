package fr.zait.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import fr.zait.R;
import fr.zait.activities.base.MyActivity;
import fr.zait.data.entities.Post;

public class PostWebviewActivity extends MyActivity implements View.OnClickListener
{

    public static class EXTRAS {
        public final static String POST = "Post";
    }

    private Post post;
    private WebView webview;
    private ProgressBar progressBar;

    /***
     *
     * ANDROID
     *
     * ***/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_webview_layout);

        initVariables();
        initViews(savedInstanceState);
    }

    /***
     *
     * PRIVATE METHODS
     *
     * ***/

    @Override
    protected void initVariables() {
        enterAnimId = R.anim.push_out_right;
        exitAnimId = R.anim.push_in_right;

        Intent intent = getIntent();
        post = intent.getParcelableExtra(EXTRAS.POST);
    }


    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        findViewById(R.id.post_webview_back_arrow).setOnClickListener(this);
        resetStatusBarColor();
        findViewById(R.id.bottom_previous).setOnClickListener(this);
        findViewById(R.id.bottom_next).setOnClickListener(this);
        findViewById(R.id.refresh_icon).setOnClickListener(this);

        progressBar = (ProgressBar)findViewById(R.id.webview_progress_bar);

        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(post.url);
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if (progress < 100 && progressBar.getVisibility() == ProgressBar.INVISIBLE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                if (progress == 100) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }
        });
    }

    /***
     *
     * OVERRIDED METHODS
     *
     * ***/

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.post_webview_back_arrow:
                finishWithAnimation();
                break;
            case R.id.bottom_next:
                if (webview.canGoForward()) {
                    webview.goForward();
                }
                break;
            case R.id.bottom_previous:
                if (webview.canGoBack()) {
                    webview.goBack();
                }
                break;
            case R.id.refresh_icon:
                webview.reload();
                break;
        }
    }

}
