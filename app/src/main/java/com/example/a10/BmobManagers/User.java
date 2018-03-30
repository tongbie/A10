package com.example.a10.BmobManagers;

import cn.bmob.v3.BmobUser;

/**
 * Created by BieTong on 2018/3/24.
 */

public class User extends BmobUser {
    private String avatar;

    public User(){}

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
