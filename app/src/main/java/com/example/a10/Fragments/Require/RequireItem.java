package com.example.a10.Fragments.Require;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a10.R;
import com.example.a10.ToolClass;


/**
 * Created by BieTong on 2018/3/12.
 */

public class RequireItem extends LinearLayout {
    private Context context;
    private String title="任务标题";
    private String introduce="任务介绍";
    private String sender="某公司";
    private String date="2018-3-12 2018-3-30";
    private boolean isShow=false;
    private TextView introduceView;
    private int length;

    public RequireItem(Context context,String title,String sender,String date,String introduce) {
        super(context);
        this.context=context;
        this.title=title;
        this.introduce=introduce;
        this.sender=sender;
        this.date=date;
        init();
    }

    public RequireItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    public RequireItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    private void init(){
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int m=(int)ToolClass.dp(6);
        lp.setMargins(m,m,m,m);
        this.setPadding(m,m,m,m);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackground(getResources().getDrawable(R.drawable.background));
        this.setLayoutParams(lp);
        TextView titleView=new TextView(context);
        titleView.setTextSize(18);
        titleView.setTextColor(Color.BLACK);
        titleView.setText(title);
        this.addView(titleView);
        TextView senderView=new TextView(context);
        senderView.setText(sender);
        senderView.setTextSize(16);
        this.addView(senderView);
        TextView dateView=new TextView(context);
        dateView.setTextSize(16);
        dateView.setText(date);
        this.addView(dateView);
        introduceView=new TextView(context);
        introduceView.setTextSize(16);
        introduceView.setText(introduce);
        length=this.getHeight();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntroduce();
            }
        });
    }

    private void showIntroduce(){
        if(!isShow){
            this.addView(introduceView);
        }else {
            this.removeView(introduceView);
        }
        isShow=!isShow;
    }
}
