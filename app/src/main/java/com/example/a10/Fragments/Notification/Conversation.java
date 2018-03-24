package com.example.a10.Fragments.Notification;


import android.support.annotation.Nullable;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by BieTong on 2018/2/9.
 */

public class Conversation extends BmobIMConversation {
    private String imageId;
    private String name;
    private String lastMessage;
    private BmobIMConversation bConversation;

    public Conversation(String imageId, String name, @Nullable String lastMessage, BmobIMConversation bConversation) {
        this.imageId = imageId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.bConversation = bConversation;
    }

    public int getImageId() {
        return Integer.valueOf(imageId);
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public BmobIMConversation getBConversation() {
        return bConversation;
    }
}
