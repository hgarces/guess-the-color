package com.henriquegarces.guessthecolor.util;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by henrique on 24/05/15.
 */
public class GUIUtils {

    public static void hideRevealEffect (final View v, int centerX, int centerY,
                                         int initialRadius, Animator.AnimatorListener lis) {
        v.setVisibility(View.VISIBLE);

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(
                v, centerX, centerY, initialRadius, 0);

        anim.setDuration(1000);

        // make the view invisible when the animation is done
        anim.addListener(lis);

        anim.start();
    }

    public static void showRevealEffect(final View v, int centerX, int centerY, Animator.AnimatorListener lis) {

        v.setVisibility(View.VISIBLE);
        Log.d("here","");
        int height = v.getHeight();

        Animator anim = ViewAnimationUtils.createCircularReveal(
                v, centerX, centerY, 0, height);

        anim.setDuration(500);

        anim.addListener(lis);
        anim.start();
    }

    public static int getActionBarHeight(Context c) {
        TypedValue tv = new TypedValue();
        int result = 0;

        if(c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            result = c.getResources().getDimensionPixelSize(tv.resourceId);
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result += c.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
