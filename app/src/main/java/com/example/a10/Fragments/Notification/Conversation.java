package com.example.a10.Fragments.Notification;


import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by BieTong on 2018/2/9.
 */

public class Conversation extends BmobIMConversation {
    private String imageId;
    private String name;
    private String conversation;
    private String objectId;

    public Conversation(String imageId, String name, String conversation){
        this.imageId=imageId;
        this.name=name;
        this.conversation = conversation;

    }

    public int getImageId() {
        return Integer.valueOf(imageId);
    }

    public String getName() {
        return name;
    }

    public String getConversation() {
        return conversation;
    }
}
