package com.example.a10.Fragments.Notification;


import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by BieTong on 2018/2/9.
 */

public class Message extends BmobIMConversation {
    private String imageId;
    private String name;
    private String message;

    public Message(String imageId,String name,String message){
        this.imageId=imageId;
        this.name=name;
        this.message=message;

    }

    public int getImageId() {
        return Integer.valueOf(imageId);
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
