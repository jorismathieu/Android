package fr.zait.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fr.zait.R;
import fr.zait.activities.base.MyActivity;
import fr.zait.data.entities.Post;

public class PostCommentsActivity extends MyActivity implements View.OnClickListener
{

    private Post post;

    public static class EXTRAS {
        public final static String POST = "Post";
    }

    /***
     *
     * ANDROID
     *
     * ***/

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_comments_layout);

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
        findViewById(R.id.post_comments_back_arrow).setOnClickListener(this);
        resetStatusBarColor();
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
            case R.id.post_comments_back_arrow:
                finishWithAnimation();
                break;
        }
    }

}
