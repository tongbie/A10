package com.example.a10.Utils;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;

/**
 * Created by BieTong on 2018/4/17.
 */

public class BusEvent {
    private String eventName;
    private String senderName;
    private String text;
    private BmobIMConversation conversation;
    private byte[] bytes;

    public BusEvent(String eventName) {
        this.eventName = eventName;
    }

    public BusEvent(String eventName, String text){
        this.eventName = eventName;
        this.text=text;
    }

    public String getEventName() {
        return eventName;
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

    public void setConversation(BmobIMConversation conversation) {
        this.conversation = conversation;
    }

    public BmobIMConversation getConversation(){
        return conversation;
    }

    public void setBytes(byte[] bytes){
        this.bytes=bytes;
    }

    public byte[] getBytes(){
        return bytes;
    }
}
