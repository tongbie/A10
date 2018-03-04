package com.example.a10.Fragments.Home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.example.a10.MyView.MyButton;
import com.example.a10.MyView.MyTextView;
import com.example.a10.R;
import com.example.a10.ToolClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, null);
            initView();
            ((MyTextView)view.findViewById(R.id.title)).setLoading(true);
            addData();
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        initAnimation();
        return view;
    }

    private void initAnimation() {
        ScaleAnimation sa = new ScaleAnimation(0.9f, 1f, 0.9f, 1f,
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
        view.findViewById(R.id.addDate).setOnClickListener(this);
        /* 进度条 */
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressNum = view.findViewById(R.id.progressNum);
        progressNum.setText(" " + String.valueOf(progressBar.getProgress()) + " %");
        /* 日历初始化 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new java.util.Date());//获取日期
        datePicker = view.findViewById(R.id.datePicker);
        datePicker.setDate(Integer.valueOf(date.substring(0, 4)), Integer.valueOf(date.substring(5, 7)));//设置日期，必须调用
    }

    private void addData() {
        BmobQuery<HomeGson> query = new BmobQuery<HomeGson>();
        query.addWhereEqualTo("username", LoginActivity.username);
        query.findObjects(new FindListener<HomeGson>() {
            @Override
            public void done(List<HomeGson> list, BmobException e) {
                if (e == null) {
                    HomeGson data = new HomeGson();
                    if (list.size() == 0) {
                        data.setUsername(LoginActivity.username);
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        addData();
                        return;
                    }
                    data = list.get(0);
                    progressNum.setText(" " + String.valueOf(data.getProgress()) + " %");
                    progressBar.setProgress(data.getProgress());
                    startDate = data.getStartDate();
                    dateSign = data.getDataSign();
                    addDateSign();
                    ((MyTextView)view.findViewById(R.id.title)).setLoading(false);
                } else {
                    if (e.getErrorCode() == 101) {
                        new HomeGson().save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    addData();
                                }else {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void addDateSign() {
        DPCManager.getInstance().setDecorBG(dateSign);
        try {
            datePicker.setDPDecor(new DPDecor() {
                @Override
                public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
                    paint.setColor(Color.RED);
                    canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2F, paint);
                }
            });
            datePicker.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                ((MyButton)view.findViewById(R.id.save)).setLoading(true);
                save();
            case R.id.sign:
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//                String date = sdf.format(new java.util.Date());//获取日期
                Calendar calendar = Calendar.getInstance();
                String date = String.valueOf(calendar.get(Calendar.YEAR)) + "-"
                        + String.valueOf(calendar.get(Calendar.MONTH)) + "-"
                        + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                for (String newDate : dateSign) {
                    if (newDate.equals(date)) {
                        return;
                    }
                }
                dateSign.add(date);
                addDateSign();
                break;
        }
    }

    private void save() {
        final HomeGson data = new HomeGson();
        data.setProgress(progressBar.getProgress());
        data.setStartDate(startDate);
        data.setDataSign(dateSign);
        data.setUsername(LoginActivity.username);
        BmobQuery<HomeGson> query = new BmobQuery<HomeGson>();
        query.addWhereEqualTo("username", LoginActivity.username);
        query.findObjects(new FindListener<HomeGson>() {
            @Override
            public void done(List<HomeGson> list, BmobException e) {
                if (e == null) {
                    data.update(list.get(0).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                ((MyButton)view.findViewById(R.id.save)).setLoading(false);
            }
        });
    }

    private void showMenuButton() {
        try {
            LinearLayout menuLayout = view.findViewById(R.id.menuLayout);
            LinearLayout linearLayout=view.findViewById(R.id.linearLayout);
            if (menuLayout.getVisibility() == View.GONE) {
                linearLayout.animate()
                        .y(ToolClass.dp(200))
                        .setDuration(200)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
                menuLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.animate()
                        .y(ToolClass.dp(62))
                        .setDuration(200)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
                menuLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProgress() {
        final EditText editText = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("进度设置")
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
}
