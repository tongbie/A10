package com.example.a10.Utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a10.BmobManagers.User;
import com.example.a10.R;

import java.util.concurrent.TimeUnit;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.exception.BmobException;


/**
 * Created by BieTong on 2018/1/31.
 * 在使用之前必须经过初始化
 */

public class Tool {
    public static int imageId = R.drawable.ic_personal;

    public static int getDensity(Context context){
        return (int) context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getTaskItemHeight(Context context){
        return getScreenHeight(context)-(int)dp(context,240);
    }

    public static float dp(Context context,float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return px * scale + 0.5f;
    }

    /* 展开动画 */
    public static ValueAnimator createDropAnimator(final View view1, final ViewGroup.LayoutParams layoutParams, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                layoutParams.height = value;
                view1.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public static int px(Context context,float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /* 连接聊天服务器 */
    public static void linkServer(final Context context) {
        User user = User.getCurrentUser(User.class);
        if (!TextUtils.isEmpty(user.getObjectId())) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        Toast.makeText(context, "已连接服务器", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(context, "无法连接至服务器", Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }

    public static boolean isConnected = false;

    /* 为LinearLayout添加缩放动画 */
    public static void scaleAnimation(View view, int viewId) {
        try {
            ScaleAnimation sa = new ScaleAnimation(0.9f, 1f, 0.9f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            sa.setInterpolator(new OvershootInterpolator());
            sa.setDuration(300);
            LayoutAnimationController lac = new LayoutAnimationController(sa, 0.3f);
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            LinearLayout linearLayout = view.findViewById(viewId);
            linearLayout.setLayoutAnimation(lac);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /* 为LinearLayout添加位移动画 */
    public static void translateAnimation(Context context,View view, int viewId) {
        try {
            TranslateAnimation ta = new TranslateAnimation(0, 0, dp(context,-50), 0);
            ta.setInterpolator(new OvershootInterpolator());
            ta.setDuration(200);
            LayoutAnimationController lac = new LayoutAnimationController(ta, 0.3f);
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            LinearLayout layout = view.findViewById(viewId);
            layout.setLayoutAnimation(lac);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
