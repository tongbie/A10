package com.example.a10.Fragments.Notification;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by BieTong on 2018/2/27.
 */

public class Conversations extends BmobObject {
    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    List<Conversation> conversations =new ArrayList<>();
}
