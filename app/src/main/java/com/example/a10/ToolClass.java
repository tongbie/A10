package com.example.a10;

import android.content.Context;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import java.util.concurrent.TimeUnit;


/**
 * Created by BieTong on 2018/1/31.
 */

public class ToolClass{
    private static Context context;
    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;

    public ToolClass(Context context) {
        this.context=context;
    }

    public static float dp(float px){
        float scale = context.getResources().getDisplayMetrics().density;
        return px*scale+0.5f;
    }
}
