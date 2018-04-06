package com.example.a10.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.a10.R;
import com.example.a10.Tool;

/**
 * Created by BieTong on 2018/4/1.
 */

public class MenuButton extends android.support.v7.widget.AppCompatButton {
    private Paint paint = new Paint();
    private int width;
    private int height;

    public MenuButton(Context context) {
        super(context);
        init();
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorWrite));
        paint.setStrokeWidth(Tool.dp(4));
    }

    private int isShow = 2;

    public void setIsShow(int isShow) {
        this.isShow = isShow;
        invalidate();
    }

    public int getIsShow() {
        return isShow;
    }

    private int[] oldXs;
    private int[] newXs;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 3; i++) {
            canvas.drawPoint(newXs[i], height / 2, paint);
        }
        if (isShow == 1) {
            newXs[0] += 3;
            newXs[2] -= 3;
            if (newXs[0] >= oldXs[1]) {
                newXs[0] = newXs[2] = oldXs[1];
                isShow = 2;
                return;
            }
            invalidate();
        } else if (isShow == 0) {
            newXs[0] -= 3;
            newXs[2] += 3;
            if (newXs[0] <= oldXs[0]) {
                newXs[0] = oldXs[0];
                newXs[2] = oldXs[2];
                isShow = 2;
                return;
            }
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width=getMeasuredWidth();
        height=getMeasuredHeight();
        oldXs = new int[]{width / 7 * 2, width / 2, width / 7 * 5};
        newXs = new int[]{width / 7 * 2, width / 2, width / 7 * 5};
    }
}
