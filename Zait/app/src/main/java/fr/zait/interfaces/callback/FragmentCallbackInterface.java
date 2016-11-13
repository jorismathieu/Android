package fr.zait.interfaces.callback;

import android.os.Bundle;

public interface FragmentCallbackInterface {
    String HOME_FRAGMENT_TAG = "HOME";
    String MY_SUBREDDITS_TAG = "SUBREDDITS";
    String SEARCH_FRAGMENT = "SEARCH";

    final class EXTRAS {
        public static final String SUBREDDIT_NAME = "subredditName";
    }

    void switchFragment(String tag, Bundle args);
}
