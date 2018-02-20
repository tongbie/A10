package com.example.a10.Fragments.Notification;

import android.graphics.Bitmap;

/**
 * Created by BieTong on 2018/2/9.
 */

public class Message {
    private int imageId;
    private String name;
    private String message;

    public Message(int imageId,String name,String message){
        this.imageId=imageId;
        this.name=name;
        this.message=message;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
