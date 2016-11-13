package fr.zait.behavior;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

public class SmoothScrollingLayoutManager extends LinearLayoutManager {
    private int lastPosition;
    private TopLinearSmoothScroller smoothScroller;

    public SmoothScrollingLayoutManager(Context context, int startPosition) {
        super(context);
        lastPosition = startPosition;
        smoothScroller = new TopLinearSmoothScroller(context);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    private class TopLinearSmoothScroller extends LinearSmoothScroller {

        private int direction = 1;

        public TopLinearSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            if (lastPosition < targetPosition) {
                direction = 1;
            } else {
                direction = -1;
            }
            lastPosition = targetPosition;
            return new PointF(0, direction);
        }

        @Override
        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return 50f / displayMetrics.densityDpi;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }
}
