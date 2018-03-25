package com.example.a10.MyView;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.a10.R;
import com.example.a10.Tool;

/**
 * Created by BieTong on 2018/1/18.
 * 右滑退出
 * 使用时只需在对应Activity或Fragment中实例化一个对象
 * 对于被强制拦截滑动事件的控件，可调用setStartY()设置作用范围
 */

public class SlipBack extends FrameLayout {
    private Activity activity;
    private ViewGroup viewGroup;
    private View view;
    private float lastX;
    private float lastY;
    private float currentX;
    private float currentY;
    private float SLIP;


    public SlipBack(Activity activity) {
        super(activity);
        this.activity = activity;
        this.setOnTouchListener(new onTouch());
        SLIP = Tool.SCREEN_WIDTH / 10;
        /* 替换原根布局 */
        viewGroup = (ViewGroup) activity.getWindow().getDecorView();//获取最顶层View
        view = viewGroup.getChildAt(0);//获取根LinearLayout
        viewGroup.removeView(view);
        this.addView(view);
        viewGroup.addView(this);
    }

    /* 触摸事件 */
    class onTouch implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int eventAction = event.getAction();
            switch (eventAction) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getRawX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    currentX = event.getRawX();
                    break;
                case MotionEvent.ACTION_UP:
                    if (currentX - lastX > SLIP) {
                        activity.finish();
                        activity.overridePendingTransition(0, R.anim.out_from_left);
                    }
                    break;
            }
            invalidate();
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getRawX();
                currentY = event.getRawY();
                float dx = currentX - lastX;
                float dy = currentY - lastY;
                if (dy < dx / 2 && dx > SLIP) {
                    return true;
                }
        }
        return false;
    }
}


