package com.example.a10.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.a10.R;
import com.example.a10.ToolClass;

/**
 * Created by BieTong on 2018/3/3.
 * 用以实现网络延时按钮功能
 * 点击后setLoading()可变成灰色且不可点击
 * 然后有小球的弹跳动画
 * 性能堪忧
 */

public class MyButton extends android.support.v7.widget.AppCompatButton {
    private Drawable background;
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


    public MyButton(Context context) {
        super(context);
        saveBackground();
        init();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        saveBackground();
        init();
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        saveBackground();
        init();
    }

    private void init(){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
    }

    private void saveBackground(){
        this.background=this.getBackground();
    }

    public void setLoading(boolean isLoading){
        if(isLoading) {
            this.setClickable(false);
            this.setBackground(getResources().getDrawable(R.drawable.pressedbutton));
            isDraw=true;
            invalidate();
        }else {
            this.setClickable(true);
            this.setBackground(this.background);
            isDraw=false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isDraw){
            return;
        }
        canvas.drawCircle(currentX,currentY,ToolClass.dp(6),paint);
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
    }
}