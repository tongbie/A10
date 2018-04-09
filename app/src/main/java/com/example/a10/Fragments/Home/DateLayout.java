package com.example.a10.Fragments.Home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by BieTong on 2018/4/9.
 * 用以解决DatePicker与ScrollView的滑动冲突
 */

public class DateLayout extends LinearLayout {

    public DateLayout(Context context) {
        super(context);
    }

    public DateLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DateLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int lastX;
    private int lastY;
    private int currentX;
    private int currentY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getX();
                currentY = (int) event.getY();
                int dx = Math.abs(currentX - lastX);
                int dy = Math.abs(currentY - lastY);
                if (dx > dy) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
