package com.example.a10.Fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10.R;
import com.example.a10.ToolClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.aigestudio.datepicker.bizs.calendars.DPCManager;
import cn.aigestudio.datepicker.bizs.decors.DPDecor;
import cn.aigestudio.datepicker.views.DatePicker;

/**
 * Created by BieTong on 2018/1/22.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private ProgressBar progressBar;//进度条
    private TextView progressNum;//进度条进度
    List<String> dateSign = new ArrayList<>();//标记日期
    DatePicker datePicker;//日历控件

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initAnimation(view);
        getData();
        initView(view);
        addDate();
        return view;
    }

    private void initAnimation(View view) {
        ScaleAnimation sa = new ScaleAnimation(0.4f, 1f, 0.4f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
        LayoutAnimationController lac = new LayoutAnimationController(sa, 0.3f);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        linearLayout.setLayoutAnimation(lac);
    }

    /* 获取数据 */
    private void getData() {

    }

    /* 添加控件 */
    private void initView(View view) {
        /* 控件 */
        Button face = (Button) view.findViewById(R.id.face);
        face.setOnClickListener(this);
        Button addProgress = (Button) view.findViewById(R.id.addProgress);
        addProgress.setOnClickListener(this);
        Button setProgress = (Button) view.findViewById(R.id.setProgress);
        setProgress.setOnClickListener(this);
        /* 进度条 */
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressNum = (TextView) view.findViewById(R.id.progressNum);
        progressNum.setText(" " + String.valueOf(progressBar.getProgress()) + " %");
        /* 日历初始化 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new java.util.Date());//获取日期
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        datePicker.setDate(Integer.valueOf(date.substring(0, 4)), Integer.valueOf(date.substring(5, 7)));//设置日期，必须调用
    }

    /* 添加日期标记 */
    private void addDate() {
        dateSign.add("2018-2-26");
        DPCManager.getInstance().setDecorBG(dateSign);
        datePicker.setDPDecor(new DPDecor() {//绘制指定日期圆圈
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(Color.parseColor("#b0b0b0"));
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2F, paint);
            }
        });
        datePicker.invalidate();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face:
                Intent intent = new Intent("android.intent.action.GET_CONTENT");    //调用系统程序
                intent.setType("image/*");
                startActivityForResult(intent, 0x000);
                break;
            case R.id.addProgress://添加进度
                progressBar.setProgress(progressBar.getProgress() + 1);
                progressNum.setText(" " + String.valueOf(progressBar.getProgress()) + " %");
                break;
            case R.id.setProgress://设置进度
                final EditText editText = new EditText(getContext());
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("进度设置")//设置对话框的标题
                        .setView(editText)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    int progress = Integer.valueOf(editText.getText().toString());
                                    progressBar.setProgress(progress);
                                    progressNum.setText(" " + String.valueOf(progress) + " %");
                                } catch (Exception e) {
                                    Toast.makeText(getContext(), "请输入0-100间的整数", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).create();
                dialog.show();
                break;
        }
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
//                FD fd=new FD();
//                fd.process(appicon,bitmap.getWidth(),bitmap.getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        progressNum.setText(" " + String.valueOf(progressBar.getProgress()) + " %");
    }
}
