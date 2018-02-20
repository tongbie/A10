package com.example.a10;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by BieTong on 2018/1/18.
 * 右滑退出
 */

public class SlipBack extends FrameLayout {
    private Activity activity;
    private ViewGroup viewGroup;
    private View view;
    private float lastX;
    private float lastY;
    private float currentX;
    private float currentY;
    private float startY;//设置生效起始坐标
    private float finishY;
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private float SLIP;


    public SlipBack(Activity activity) {
        super(activity);
        this.activity = activity;
        this.setOnTouchListener(new onTouch());
        SCREEN_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;
        SCREEN_HEIGHT = getContext().getResources().getDisplayMetrics().heightPixels;
        SLIP = SCREEN_WIDTH / 10;
        startY = 0;
        finishY = SCREEN_HEIGHT;
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

    public void setStartY(float startY){
        this.startY=startY;
    }

    public void setFinishY(float finishY){
        this.finishY=finishY;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getRawY() < startY || event.getRawY() > finishY) {
            return false;
        }
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


