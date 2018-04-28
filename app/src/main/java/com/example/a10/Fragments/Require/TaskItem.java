package com.example.a10.Fragments.Require;

import android.view.View;
import android.widget.ScrollView;

/**
 * Created by BieTong on 2018/4/25.
 */

public class TaskItem {
    private String title = "任务标题";
    private String introduce = "任务介绍";
    private String sender = "某公司";
    private String date = "2018-3-12 2018-3-30";
    private boolean isShow = false;
    private String leftButtonText = "完 成";
    private String rightButtonText = "拒 绝";


    public TaskItem(String title, String sender, String date, String introduce) {
        this.title = title;
        this.introduce = introduce;
        this.sender = sender;
        this.date = date;
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

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getLeftButtonText() {
        return leftButtonText;
    }

    public void setLeftButtonText(String leftButtonText) {
        this.leftButtonText = leftButtonText;
    }

    public String getRightButtonText() {
        return rightButtonText;
    }

    public void setRightButtonText(String rightButtonText) {
        this.rightButtonText = rightButtonText;
    }
}
