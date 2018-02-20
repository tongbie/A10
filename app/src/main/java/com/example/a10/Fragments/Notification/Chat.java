package com.example.a10.Fragments.Notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by BieTong on 2018/2/10.
 */

public class Chat {
    private int imageId;
    private String name;
    private String chat;
    private int type;

    public Chat(int imageId, String name, String chat,int type) {
        this.imageId=imageId;
        this.name=name;
        this.chat=chat;
        this.type=type;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getChat() {
        return chat;
    }

    public int getType(){
        return type;
    }
}
