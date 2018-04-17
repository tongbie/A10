package com.example.a10;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;

/**
 * Created by BieTong on 2018/4/17.
 */

public class BusEvent {
    String event;
    String senderName;
    String text;
    MessageEvent messageEvent;
    BmobIMConversation conversation;

    public BusEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMessageEvent(MessageEvent messageEvent) {
        this.messageEvent = messageEvent;
    }

    public MessageEvent getMessageEvent() {
        return messageEvent;
    }

    public void setConversation(BmobIMConversation conversation) {
        this.conversation = conversation;
    }

    public BmobIMConversation getConversation(){
        return conversation;
    }
}
