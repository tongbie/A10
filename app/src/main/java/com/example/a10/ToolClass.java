package com.example.a10;

import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by BieTong on 2018/1/31.
 */

public class ToolClass {
    public static OkHttpClient client;
    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;

    public ToolClass() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }
}
