package com.example.a10.Fragments.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10.Login.LoginActivity;
import com.example.a10.R;
import com.example.a10.ToolClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.aigestudio.datepicker.bizs.calendars.DPCManager;
import cn.aigestudio.datepicker.bizs.decors.DPDecor;
import cn.aigestudio.datepicker.views.DatePicker;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by BieTong on 2018/1/22.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private ProgressBar progressBar;//进度条
    private TextView progressNum;//进度条进度
    List<String> startDate = new ArrayList<>();
    List<String> dateSign = new ArrayList<>();//标记日期
    DatePicker datePicker;//日历控件
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.view = view;
        initAnimation();
        initView();
        addData();
        return view;
    }

    private void initAnimation() {
        ScaleAnimation sa = new ScaleAnimation(0.7f, 1f, 0.7f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
        LayoutAnimationController lac = new LayoutAnimationController(sa, 0.3f);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        linearLayout.setLayoutAnimation(lac);
    }

    private void initView() {
        /* 控件 */
//        view.findViewById(R.id.face).setOnClickListener(this);
        view.findViewById(R.id.addProgress).setOnClickListener(this);
        view.findViewById(R.id.setProgress).setOnClickListener(this);
        view.findViewById(R.id.menuButton).setOnClickListener(this);
        view.findViewById(R.id.save).setOnClickListener(this);
        view.findViewById(R.id.sign).setOnClickListener(this);
        /* 进度条 */
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressNum =  view.findViewById(R.id.progressNum);
        progressNum.setText(" " + String.valueOf(progressBar.getProgress()) + " %");
        /* 日历初始化 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new java.util.Date());//获取日期
        datePicker =  view.findViewById(R.id.datePicker);
        datePicker.setDate(Integer.valueOf(date.substring(0, 4)), Integer.valueOf(date.substring(5, 7)));//设置日期，必须调用
    }

    private void addData() {
        BmobQuery<HomeGson> bmobQuery = new BmobQuery<HomeGson>();



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
            /*case R.id.face:
                Intent intent = new Intent("android.intent.action.GET_CONTENT");    //调用系统程序
                intent.setType("image*//*");
                startActivityForResult(intent, 0x000);
                break;*/
            case R.id.addProgress:
                progressBar.setProgress(progressBar.getProgress() + 1);
                progressNum.setText(" " + String.valueOf(progressBar.getProgress()) + " %");
                break;
            case R.id.setProgress:
                setProgress();
                break;
            case R.id.menuButton:
                showMenuButton();
                break;
            case R.id.save:
                saveAll();
                break;
        }
    }

    private void saveAll() {
        final HomeGson data=new HomeGson();
        data.setProgress(progressBar.getProgress());
        data.setStartDate(startDate);
        data.setDataSign(dateSign);
        data.setUsername(LoginActivity.username);
        BmobQuery<HomeGson> query = new BmobQuery<HomeGson>();
        query.addWhereEqualTo("username", LoginActivity.username);
        query.findObjects(new FindListener<HomeGson>() {
            @Override
            public void done(List<HomeGson> list, BmobException e) {
                if(e==null) {
                    String homeObjectId=null;
                    if(list.size()==0){
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null) {
                                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        return;
                    }else {
                        homeObjectId=list.get(0).getObjectId();
                    }
                    data.update(homeObjectId,new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null) {
                                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Log.e("查询ErrorCode",String.valueOf(e.getErrorCode()));
                    if(e.getErrorCode()==101){
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null) {
                                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showMenuButton() {
        try {
            LinearLayout menuLayout = view.findViewById(R.id.menuLayout);
            if (menuLayout.getVisibility() == View.GONE) {
                menuLayout.setVisibility(View.VISIBLE);
                TranslateAnimation ta = new TranslateAnimation(0, 0, 0, ToolClass.dp(12));
                ta.setDuration(200);
                ta.setInterpolator(new OvershootInterpolator());
                view.findViewById(R.id.linearLayout).startAnimation(ta);
            } else {
                menuLayout.setVisibility(View.GONE);
            }
        }catch (Exception e){
            //TODO:未知异常
            e.printStackTrace();
        }
    }

    private void setProgress() {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        /*if (requestCode == 0x000 && resultCode == Activity.RESULT_OK && data != null) {
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
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        progressNum.setText(" " + String.valueOf(progressBar.getProgress()) + " %");
    }
}
