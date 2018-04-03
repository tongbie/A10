package com.example.a10.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.a10.R;
import com.example.a10.Tool;

/**
 * Created by BieTong on 2018/4/3.
 */

public class RequireButton extends android.support.v7.widget.AppCompatButton {

    public RequireButton(Context context) {
        super(context);
        init();
    }

    public RequireButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RequireButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
//        this.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//        this.setTextColor(getResources().getColor(R.color.colorWrite));
    }

    private boolean isChoosed=false;

    public void setChoosed(boolean isChoosed){
//        this.setBackgroundColor();
    }
}
