package com.example.a10.Fragments.Notification;

/**
 * Created by BieTong on 2018/2/10.
 */

public class Message {
    private int imageId;
    private String name;
    private String message;
    private int type;

    public Message(int imageId, String name, String message, int type) {
        this.imageId = imageId;
        this.name = name;
        this.message = message;
        this.type = type;
    }

    public int getImageId() {
        return imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
