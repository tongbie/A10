package com.example.a10.Fragments.Home;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by BieTong on 2018/2/27.
 */

public class HomeGson extends BmobObject {
    private int progress=0;
    private String username="";
    private List<String> dataSign =new ArrayList<>();

    public HomeGson(){

    }

    public HomeGson(int progress,String username,List<String> dataSign){
        this.progress=progress;
        this.username=username;
        this.dataSign =dataSign;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getDataSign() {
        return dataSign;
    }

    public void setDataSign(List<String> dataSign) {
        this.dataSign = dataSign;
    }
}
