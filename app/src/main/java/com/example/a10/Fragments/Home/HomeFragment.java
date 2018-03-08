package com.example.a10.Fragments.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10.LoginActivity;
import com.example.a10.MyView.MyButton;
import com.example.a10.MyView.MyTextView;
import com.example.a10.MyView.datepicker.bizs.calendars.DPCManager;
import com.example.a10.MyView.datepicker.bizs.decors.DPDecor;
import com.example.a10.MyView.datepicker.views.DatePicker;
import com.example.a10.R;
import com.example.a10.ToolClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
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
    List<String> dateSign = new ArrayList<>();//标记日期
    private View view;
    LinearLayout dateLayout;
    private DatePicker picker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, null);
            initView();
            addData();
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        initAnimation();
        return view;
    }

    private void initView() {
        /* 控件 */
//        view.findViewById(R.id.face).setOnClickListener(this);
        view.findViewById(R.id.addProgress).setOnClickListener(this);
        view.findViewById(R.id.setProgress).setOnClickListener(this);
        view.findViewById(R.id.menuButton).setOnClickListener(this);
        view.findViewById(R.id.save).setOnClickListener(this);
        view.findViewById(R.id.refresh).setOnClickListener(this);
        view.findViewById(R.id.signIn).setOnClickListener(this);
        /* 进度条 */
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressNum = view.findViewById(R.id.progressNum);
        progressNum.setText(" " + String.valueOf(progressBar.getProgress()) + " %");
        /* 日历初始化 */
        dateLayout = view.findViewById(R.id.dateLayout);
    }

    private void addData() {
        ((MyTextView) view.findViewById(R.id.title)).setLoading(true);
        ((MyButton) view.findViewById(R.id.refresh)).setLoading(true);
        ((MyButton) view.findViewById(R.id.save)).setLoading(true);
        BmobQuery<HomeGson> query = new BmobQuery<HomeGson>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(BmobUser.class).getUsername());
        query.findObjects(new FindListener<HomeGson>() {
            @Override
            public void done(List<HomeGson> list, BmobException e) {
                if (e == null) {
                    HomeGson data = new HomeGson();
                    if (list.size() == 0) {
                        data.setUsername(BmobUser.getCurrentUser(BmobUser.class).getUsername());
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null) {
                                    toast(e.getMessage());
                                }
                            }
                        });
                        addData();
                        return;
                    }
                    data = list.get(0);
                    progressNum.setText(" " + String.valueOf(data.getProgress()) + " %");
                    progressBar.setProgress(data.getProgress());
                    dateSign = data.getDataSign();
                    addDateSign();
                } else {
                    if (e.getErrorCode() == 101) {
                        new HomeGson().save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    addData();
                                } else {
                                    toast(e.getMessage());
                                }
                            }
                        });
                    } else {
                        toast(e.getMessage());
                    }
                }
                ((MyTextView) view.findViewById(R.id.title)).setLoading(false);
                ((MyButton) view.findViewById(R.id.refresh)).setLoading(false);
                ((MyButton) view.findViewById(R.id.save)).setLoading(false);
            }
        });
    }

    private void toast(String text) {
        try {
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDateSign() {
        dateLayout.removeView(picker);
        picker = new DatePicker(getContext());
        picker.getTvEnsure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        DPCManager dpcManager = new DPCManager();
        dpcManager.setDecorTR(dateSign);
        picker.setDPCManager(dpcManager);
//        TODO:双位数日期未测试
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new java.util.Date());//获取日期
        picker.setDate(Integer.valueOf(date.substring(0, 4)), Integer.valueOf(date.substring(5, 7)));
        picker.setDPDecor(new DPDecor());
        dateLayout.addView(picker);

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
                progressSet(progressBar.getProgress() + 1);
                break;
            case R.id.setProgress:
                setProgress();
                break;
            case R.id.menuButton:
                showMenuButton();
                break;
            case R.id.save:
                save("保存成功");
                break;
            case R.id.refresh:
                dateSign = null;
                addData();
                break;
            case R.id.signIn:
                signIn();
                break;
        }
    }

    private void progressSet(int progress) {
        progressBar.setProgress(progress);
        progressNum.setText(" " + progress + " %");
    }

    private void signIn() {
        Time time = new Time("GMT+8");
        time.setToNow();
        String date = String.valueOf(time.year) + "-" + String.valueOf(time.month + 1) + "-" + String.valueOf(time.monthDay);
        for (String day : dateSign) {
            if (date.equals(day)) {
                toast("今天已经签到了哦");
                return;
            }
        }
        dateSign.add(date);
        save("签到成功");
        addDateSign();
    }

    private void save(String text) {
        ((MyButton) view.findViewById(R.id.save)).setLoading(true);
        final HomeGson data = new HomeGson();
        data.setProgress(progressBar.getProgress());
        data.setDataSign(dateSign);
        data.setUsername(BmobUser.getCurrentUser(BmobUser.class).getUsername());
        data.update(BmobUser.getCurrentUser(BmobUser.class).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toast(text);
                } else {
                    toast(e.getMessage());
                }
            }
        });
    }

    private void showMenuButton() {
        try {
            LinearLayout menuLayout = view.findViewById(R.id.menuLayout);
            LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
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
        SeekBar seekBar = new SeekBar(getContext());
        seekBar.setMax(100);
        seekBar.setProgress(progressBar.getProgress());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressSet(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                progressSet(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressSet(seekBar.getProgress());
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("进度设置")
                .setView(seekBar)
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
                            int progress = seekBar.getProgress();
                            progressBar.setProgress(progress);
                            progressNum.setText(" " + String.valueOf(progress) + " %");
                        } catch (Exception e) {
                            toast("error");
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

    private void initAnimation() {
        ScaleAnimation sa = new ScaleAnimation(0.9f, 1f, 0.9f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setInterpolator(new OvershootInterpolator());
        sa.setDuration(300);
        LayoutAnimationController lac = new LayoutAnimationController(sa, 0.3f);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        linearLayout.setLayoutAnimation(lac);
    }
}
