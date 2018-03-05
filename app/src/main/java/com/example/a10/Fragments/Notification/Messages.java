package com.example.a10.Fragments.Notification;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.v3.BmobObject;

/**
 * Created by BieTong on 2018/2/27.
 */

public class Messages extends BmobObject {
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    List<Message> messages=new ArrayList<>();
}
