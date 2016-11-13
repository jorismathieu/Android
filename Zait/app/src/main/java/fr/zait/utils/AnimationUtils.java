package fr.zait.utils;

import android.animation.ObjectAnimator;
import android.view.View;

public class AnimationUtils {
    public static void makeHalfRotation(View v, int rotationAngle) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", rotationAngle, rotationAngle + 180);
        anim.setDuration(200);
        anim.start();
    }
}
