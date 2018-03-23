package com.example.a10.Fragments.Require;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.a10.R;
import com.example.a10.ToolClass;


/**
 * Created by BieTong on 2018/3/12.
 */

public class RequireItem extends LinearLayout implements View.OnClickListener {
    private Context context;
    private String title = "任务标题";
    private String introduce = "任务介绍";
    private String sender = "某公司";
    private String date = "2018-3-12 2018-3-30";
    private boolean isShow = false;
    private View view;
    private int scrollHeight = 0;
    private LinearLayout root;
    private ScrollView scrollView;
    private int titleHeight = 0;

    public RequireItem(Context context, String title, String sender, String date, String introduce) {
        super(context);
        root = this;
        this.context = context;
        this.title = title;
        this.introduce = introduce;
        this.sender = sender;
        this.date = date;
        init();
    }

    public RequireItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public RequireItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.require_item, this);
        ((TextView) view.findViewById(R.id.titleView)).setText(title);
        ((TextView) view.findViewById(R.id.senderView)).setText(sender);
        ((TextView) view.findViewById(R.id.dateView)).setText(date);
        ((TextView) view.findViewById(R.id.introduceView)).setText(introduce);
        ((Button) view.findViewById(R.id.showButton)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.hideButton)).setOnClickListener(this);

        scrollView = view.findViewById(R.id.scrollView);
        scrollView.setOnTouchListener(childScrollTouchListener);
        scrollHeight = ToolClass.SCREEN_HEIGHT - ToolClass.px(240);
//        ViewGroup.LayoutParams scrollParams = scrollView.getLayoutParams();
//        scrollParams.height = scrollHeight;
//        scrollView.setLayoutParams(scrollParams);
    }

    private int lastY = 0;
    private int currentY = 0;

    private OnTouchListener childScrollTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //TODO:内部ScrollView滑动拦截事件，体验不太好先不加了
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    lastY = (int) event.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    currentY = (int) event.getRawY();
//                    int dy = currentY - lastY;
//                    if (dy > 0 && scrollView.getScrollY() == 0) {
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                        return false;
//                    } else if (dy < 0 && (scrollView.getChildAt(0).getHeight() - scrollView.getHeight()
//                            == scrollView.getScrollY())) {
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                        return false;
//                    }
//            }
            getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        }
    };

    private void showIntroduce() {
        if (scrollView.getVisibility() == GONE) {
            scrollView.setVisibility(VISIBLE);
            ValueAnimator animator = ToolClass.createDropAnimator(scrollView, scrollView.getLayoutParams(), 0, scrollHeight);
            animator.start();
            view.findViewById(R.id.showButton).setBackground(getResources().getDrawable(R.drawable.button_hide));
        } else {
            ValueAnimator animator = ToolClass.createDropAnimator(scrollView, scrollView.getLayoutParams(), scrollHeight, 0);
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
                if(!isShow) {
                    int y = ((View) this).getTop();
                    ((ScrollView) ((this.getParent()).getParent())).smoothScrollTo(0, y);
                }
            case R.id.hideButton:
                showIntroduce();
                break;
        }
    }
}
