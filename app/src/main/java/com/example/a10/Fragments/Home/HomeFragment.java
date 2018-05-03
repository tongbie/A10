package com.example.a10.Fragments.Home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.facedetection.AFD_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.example.a10.BmobManagers.User;
import com.example.a10.Utils.BusEvent;
import com.example.a10.Fragments.Personal.CameraActivity;
import com.example.a10.Fragments.Personal.FaceData;
import com.example.a10.Views.MenuButton;
import com.example.a10.Views.LoadButton;
import com.example.a10.Views.LoadTextView;
import com.example.a10.Views.RefreshButton;
import com.example.a10.Views.datepicker.bizs.calendars.DPCManager;
import com.example.a10.Views.datepicker.DPDecor;
import com.example.a10.Views.datepicker.views.DatePicker;
import com.example.a10.R;
import com.example.a10.Utils.Tool;
import com.example.a10.Utils.BitmapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Face.FD;
import Face.FR;
import Face.FaceRegist;
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
    List<String> dateSign = new ArrayList<>();//标记日期
    private View view;
    DateLayout dateLayout;
    private DatePicker picker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, null);
            initView();
            addData();
            EventBus.getDefault().register(this);
        }
//        ViewGroup viewGroup = (ViewGroup) view.getParent();
//        if (viewGroup != null) {
//            viewGroup.removeView(view);
//        }
        Tool.scaleAnimation(view,R.id.linearLayout);
        return view;
    }

    RefreshButton refreshButton;

    private void initView() {
        /* 控件 */
        view.findViewById(R.id.addProgress).setOnClickListener(this);
        view.findViewById(R.id.setProgress).setOnClickListener(this);
        view.findViewById(R.id.menuButton).setOnClickListener(this);
        view.findViewById(R.id.save).setOnClickListener(this);
        view.findViewById(R.id.refresh).setOnClickListener(this);
        view.findViewById(R.id.signIn).setOnClickListener(this);
        refreshButton=view.findViewById(R.id.refreshButton);
        /* 进度条 */
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressNum = view.findViewById(R.id.progressNum);
        progressNum.setText(" " + String.valueOf(progressBar.getProgress()) + " %");
        /* 日历初始化 */
        dateLayout = view.findViewById(R.id.dateLayout);
    }

    private void addData() {
        ((LoadTextView)view.findViewById(R.id.title)).setLoading(true);
        ((LoadButton) view.findViewById(R.id.refresh)).setLoading(true);
        ((LoadButton) view.findViewById(R.id.save)).setLoading(true);
        BmobQuery<HomeGson> query = new BmobQuery<>();
        query.addWhereEqualTo("username", User.getCurrentUser(User.class).getUsername());
        //通过username查HomeGson表
        query.findObjects(new FindListener<HomeGson>() {
            @Override
            public void done(List<HomeGson> list, BmobException e) {
                if (e == null) {
                    HomeGson data = new HomeGson();
                    if (list.size() == 0) {
                        data.setUsername(User.getCurrentUser(User.class).getUsername());
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
                ((LoadTextView)view.findViewById(R.id.title)).setLoading(false);
                ((LoadButton) view.findViewById(R.id.refresh)).setLoading(false);
                ((LoadButton) view.findViewById(R.id.save)).setLoading(false);
            }
        });
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new java.util.Date());//获取日期
        picker.setDate(Integer.valueOf(date.substring(0, 4)), Integer.valueOf(date.substring(5, 7)));
        picker.setDPDecor(new DPDecor());
        dateLayout.addView(picker);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addProgress:
                progressSet(progressBar.getProgress() + 1);
                break;
            case R.id.setProgress:
                setProgress();
                break;
            case R.id.menuButton:
                showMenu();
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
        setRefreshButtonLoading(true);
        startActivity(new Intent(getActivity(), CameraActivity.class));
    }

    Bitmap faceBitmap=null;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiver(BusEvent busEvent) {
        String eventName=busEvent.getEventName();
        if(eventName.equals("拍照完成")){
            byte[] data=busEvent.getBytes();
            Bitmap bitmap= BitmapFactory.decodeByteArray(data, 0, data.length);
            faceBitmap=bitmap;
            getFaceData();
        }else if(eventName.equals("未拍照")){
            setRefreshButtonLoading(false);
        }
    }

    private void getFaceData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                faceBitmap = BitmapUtil.cutBitmap(getContext(),faceBitmap);//裁剪
                if (faceBitmap == null) {//判空
                    EventBus.getDefault().post(new BusEvent("Toast","图像获取异常"));
                    setRefreshButtonLoading(false);
                    return;
                }
                byte[] bytes = BitmapUtil.bitmapToNV21Bytes(faceBitmap);//转格式
                FaceRegist faceRegist=getFDData(bytes, faceBitmap.getWidth(), faceBitmap.getHeight());//获取信息
                if(faceRegist==null){
                    setRefreshButtonLoading(false);
                    return;
                }
                String username = User.getCurrentUser().getUsername();
                faceSignIn(username,faceRegist);
            }
        }).start();
    }

    public FaceRegist getFDData(byte[] bytes, int width, int height) {
        FD fd = new FD();//人脸检测
        List<AFD_FSDKFace> fdData = fd.process(bytes, width, height);
        if (fdData.size() == 0) {
            EventBus.getDefault().post(new BusEvent("Toast", "未检测到人脸"));
            return null;
        } else if (fdData.size() >= 2) {
            EventBus.getDefault().post(new BusEvent("Toast", "检测到多个人脸，请重新录入"));
            return null;
        }
        AFD_FSDKFace fdFace = fdData.get(0);
        FR fr = new FR();
        AFR_FSDKFace frFace = fr.getFace(bytes, width, height, fdFace.getRect(), fdFace.getDegree());
        String username = User.getCurrentUser().getUsername();
        FaceRegist faceRegist = new FaceRegist(username, frFace);
        return faceRegist;
    }

    private void faceSignIn(String username, final FaceRegist faceLocal) {
        BmobQuery<FaceData> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<FaceData>() {
            @Override
            public void done(List<FaceData> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        EventBus.getDefault().post(new BusEvent("Toast","未注册人脸"));
                        setRefreshButtonLoading(false);
                        return;
                    } else {
                        FaceRegist faceNet = list.get(0).getFaceRegist();
                        FR fr=new FR();
                        float score=fr.getSimilarity(faceNet.getFace(),faceLocal.getFace());
                        if(score>=0.6f){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Time time = new Time("GMT+8");
                                    time.setToNow();
                                    String date = String.valueOf(time.year) + "-" + String.valueOf(time.month + 1) + "-" + String.valueOf(time.monthDay);
                                    dateSign.add(date);
                                    save("签到成功");
                                    addDateSign();
                                    setRefreshButtonLoading(false);
                                }
                            });
                        }else {
                            EventBus.getDefault().post(new BusEvent("Toast","人脸不匹配"));
                            setRefreshButtonLoading(false);
                            return;
                        }
                    }
                } else {
                    EventBus.getDefault().post(new BusEvent("Toast",e.getMessage()));
                    setRefreshButtonLoading(false);
                }
            }
        });
    }

    private void setRefreshButtonLoading(final boolean isLoading){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isLoading){
                    refreshButton.setVisibility(View.VISIBLE);
                    refreshButton.setRefreshing(true);
                }else {
                    refreshButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void save(final String text) {
        ((LoadButton) view.findViewById(R.id.save)).setLoading(true);
        final HomeGson data = new HomeGson();
        data.setProgress(progressBar.getProgress());
        data.setDataSign(dateSign);
        data.setUsername(User.getCurrentUser(User.class).getUsername());
        BmobQuery<HomeGson> query = new BmobQuery<>();
        query.addWhereEqualTo("username", User.getCurrentUser(User.class).getUsername());
        query.findObjects(new FindListener<HomeGson>() {
            @Override
            public void done(List<HomeGson> list, BmobException e) {
                if (e == null) {
                    //需要使用HomeGson表的ObjectId进行update 操作
                    data.update(list.get(0).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                toast(text);
                            } else {
                                toast("保存失败" + e.getMessage());
                                addData();
                            }
                        }
                    });
                } else {
                    toast("查找用户失败" + e.getMessage());
                }
                ((LoadButton) view.findViewById(R.id.save)).setLoading(false);
            }
        });
    }

    private void showMenu() {
        try {
            MenuButton button=view.findViewById(R.id.menuButton);
            final LinearLayout menuLayout = view.findViewById(R.id.menuLayout);
            if (menuLayout.getVisibility() == View.GONE) {
                button.setIsShow(1);
                menuLayout.setVisibility(View.VISIBLE);
                ValueAnimator animator = Tool.createDropAnimator(menuLayout, menuLayout.getLayoutParams(), 0, (int) (140 * Tool.getDensity(getContext()) + 0.5));
                animator.setInterpolator(new OvershootInterpolator());
                animator.start();
            } else {
                button.setIsShow(0);
                int origHeight = menuLayout.getHeight();
                ValueAnimator animator = Tool.createDropAnimator(menuLayout, menuLayout.getLayoutParams(), origHeight, 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        menuLayout.setVisibility(View.GONE);
                    }
                });
                animator.setInterpolator(new AnticipateInterpolator());
                animator.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setProgress() {
        final SeekBar seekBar = new SeekBar(getContext());
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

    private void toast(String text) {
        try {
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
