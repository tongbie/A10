package com.example.a10.Fragments.Personal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a10.Face.FD;
import com.example.a10.LoginActivity;
import com.example.a10.R;
import com.example.a10.Tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_personal, null);
            initView();
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        Tool.scaleAnimation(view);
        ((TextView)view.findViewById(R.id.username)).setText(BmobUser.getCurrentUser().getUsername());
        ((TextView)view.findViewById(R.id.userId)).setText(BmobUser.getCurrentUser().getObjectId());
        return view;
    }


    private void initView() {
        view.findViewById(R.id.logout).setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == 0x000 && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();// outputstream
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] appicon = byteArrayOutputStream.toByteArray();// 转为byte数组
                FD fd=new FD();
                fd.process(appicon,bitmap.getWidth(),bitmap.getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                /*BmobUser.logOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                BmobIM.getInstance().disConnect();//断开服务器连接
                getActivity().finish();*/
                Intent intent = new Intent("android.intent.action.GET_CONTENT");    //调用系统程序
                intent.setType("image*//*");
                startActivityForResult(intent, 0x000);
                break;
        }
    }
}
