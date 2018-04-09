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

import com.example.a10.LoginActivity;
import com.example.a10.MainActivity;
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
        Tool.scaleAnimation(view, R.id.linearLayout);
        ((TextView) view.findViewById(R.id.username)).setText(BmobUser.getCurrentUser().getUsername());
        ((TextView) view.findViewById(R.id.userId)).setText(BmobUser.getCurrentUser().getObjectId());
        return view;
    }


    private void initView() {
        view.findViewById(R.id.logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                BmobUser.logOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                BmobIM.getInstance().disConnect();//断开服务器连接
                MainActivity.fragments.clear();
                MainActivity.homeFragment = null;
                MainActivity.requireFragment = null;
                MainActivity.requireFragment = null;
                MainActivity.personalFragment = null;
                getActivity().finish();
                break;
        }
    }
}
