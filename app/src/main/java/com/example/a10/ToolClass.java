package com.example.a10;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


/**
 * Created by BieTong on 2018/1/31.
 */

public class ToolClass {
    private static Context context;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static float mDensity;

    public ToolClass(Context context) {
        this.context = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mDensity = dm.density;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
    }

    public static float dp(float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return px * scale + 0.5f;
    }

    public static ValueAnimator createDropAnimator(final View view1, ViewGroup.LayoutParams layoutParams, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                layoutParams.height = value;
                view1.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public static int px(float dp){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dp*scale+0.5f);
    }


}
