package com.example.a10.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.a10.Tool;

/**
 * Created by BieTong on 2018/3/4.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    private Paint paint;
    private float currentX;
    private float currentY;
    private float width;
    private float height;
    private float positionX;
    private float positionY;
    private boolean isFirstMeasure=true;
    private boolean isDraw=false;
    private boolean isLeft=false;
    private boolean isUp=false;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLoading(boolean isLoading){
        if(isLoading) {
            isDraw=true;
            invalidate();
        }else {
            isDraw=false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isDraw){
            return;
        }
        canvas.drawCircle(currentX,currentY, Tool.dp(6),paint);
        if(isLeft){
            currentX-=5;
            if(currentX<positionX){
                isLeft=false;
            }
        }else {
            currentX+=5;
            if(currentX>positionX+width){
                isLeft=true;
            }
        }
        if(isUp){
            currentY-=4;
            if(currentY<positionY){
                isUp=false;
            }
        }else {
            currentY+=4;
            if(currentY>positionY+height){
                isUp=true;
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!isFirstMeasure){
            return;
        }
        width=getMeasuredWidth();
        height=getMeasuredHeight();
        positionX=getX();
        positionY=getY();
        currentX=positionX;
        currentY=positionY+this.getMeasuredHeight()/2;
        isFirstMeasure=false;
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
    }
}
