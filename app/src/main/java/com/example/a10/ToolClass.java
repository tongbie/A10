package com.example.a10;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by BieTong on 2018/1/31.
 */

public class ToolClass {
    public static OkHttpClient client;

    public ToolClass() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }
}
