package com.example.a10.Fragments.Require;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.a10.R;
import com.example.a10.Tool;

/**
 * Created by BieTong on 2018/4/3.
 */

public abstract class WaitAcceptItem extends LinearLayout implements View.OnClickListener {
    private Context context;
    private String title = "任务标题";
    private String introduce = "任务介绍";
    private String sender = "某公司";
    private String date = "2018-3-12 2018-3-30";
    private boolean isShow = false;
    private View view;
    private int scrollHeight = 0;
    private ScrollView scrollView;

    public WaitAcceptItem(Context context, String title, String sender, String date, String introduce) {
        super(context);
        this.context = context;
        this.title = title;
        this.introduce = introduce;
        this.sender = sender;
        this.date = date;
        init();
    }

    public WaitAcceptItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaitAcceptItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.item_waitaccept, this);
        ((TextView) view.findViewById(R.id.titleView)).setText(title);
        ((TextView) view.findViewById(R.id.senderView)).setText(sender);
        ((TextView) view.findViewById(R.id.dateView)).setText(date);
        ((TextView) view.findViewById(R.id.introduceView)).setText(introduce);
        view.findViewById(R.id.showButton).setOnClickListener(this);
        view.findViewById(R.id.hideButton).setOnClickListener(this);
        view.findViewById(R.id.accept).setOnClickListener(this);
        view.findViewById(R.id.refuse).setOnClickListener(this);

        scrollView = view.findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(childScrollTouchListener);
        scrollHeight = Tool.requireItemHeight;//这里指定item展开高度
    }

    private OnTouchListener childScrollTouchListener = (v, event) -> {
        getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    };

    /*private OnTouchListener childScrollTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };*/

    private void showIntroduce() {
        if (scrollView.getVisibility() == GONE) {
            scrollView.setVisibility(VISIBLE);
            ValueAnimator animator = Tool.createDropAnimator(scrollView, scrollView.getLayoutParams(), 0, scrollHeight);
            animator.start();
            view.findViewById(R.id.showButton).setBackground(getResources().getDrawable(R.drawable.button_hide));
        } else {
            ValueAnimator animator = Tool.createDropAnimator(scrollView, scrollView.getLayoutParams(), scrollHeight, 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scrollView.setVisibility(GONE);
                }
            });
            animator.start();
            view.findViewById(R.id.showButton).setBackground(getResources().getDrawable(R.drawable.button_show));
        }
        isShow = !isShow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showButton:
                if (!isShow) {
                    int y = ((View) this).getTop();
                    ((ScrollView) ((this.getParent()).getParent())).smoothScrollTo(0, y);
                }
            case R.id.hideButton:
                showIntroduce();
                break;
            case R.id.accept:
                accept();
                break;
            case R.id.refuse:
                refuse();
                break;
        }
    }

    public abstract void accept();

    public abstract void refuse();
}
