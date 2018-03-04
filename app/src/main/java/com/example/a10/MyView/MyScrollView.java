package com.example.a10.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by BieTong on 2018/3/4.
 */

public class MyScrollView extends ScrollView {
    private int lastX;
    private int lastY;
    private int currentX;
    private int currentY;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                float dx = currentX - lastX;
                float dy = currentY - lastY;
                if (Math.abs(dy)/2 > Math.abs(dx)) {
                    return true;
                }
        }
        return false;
    }
}
