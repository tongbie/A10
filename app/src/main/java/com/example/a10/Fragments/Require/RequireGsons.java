package com.example.a10.Fragments.Require;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by BieTong on 2018/3/12.
 */

public class RequireGsons extends BmobObject {
    private List<RequireGson> requireGsons=new ArrayList<>();
    private String username;

    public RequireGsons(){

    }

    public RequireGsons(List<RequireGson> requireGsons,String username){
        this.requireGsons=requireGsons;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<RequireGson> getRequireGsons() {
        return requireGsons;
    }

    public void setRequireGsons(List<RequireGson> requireGsons) {
        this.requireGsons = requireGsons;
    }
}
