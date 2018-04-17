package com.example.a10.Fragments.Personal;

import Face.FaceRegist;
import cn.bmob.v3.BmobObject;

/**
 * Created by BieTong on 2018/4/17.
 */

public class FaceData extends BmobObject {
    private String username;
    private FaceRegist faceRegist;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public FaceRegist getFaceRegist() {
        return faceRegist;
    }

    public void setFaceRegist(FaceRegist faceRegist) {
        this.faceRegist = faceRegist;
    }
}
