package com.example.a10.Fragments.Require;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by BieTong on 2018/3/12.
 */

public class RequireGson extends BmobObject {
    private String title;
    private String sender;
    private String date;
    private String introduce;

    private String username;
    private int state;// 0.拒绝 1.接受 2.未处理

    public RequireGson(){

    }

    public RequireGson(String title, String sender, String date, String introduce){
        this.title=title;
        this.sender=sender;
        this.date=date;
        this.introduce=introduce;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
