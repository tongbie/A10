package com.example.a10.Views.datepicker.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a10.Views.datepicker.bizs.calendars.DPCManager;
import com.example.a10.Views.datepicker.DPDecor;
import com.example.a10.Views.datepicker.utils.MeasureUtil;
import com.example.a10.R;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * DatePicker
 *
 * @author AigeStudio 2015-06-29
 */
public class DatePicker extends LinearLayout {

    private String[] titleWeek = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    private String[] titleMonth = new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
    private String titleBC = "公元前";

    private MonthView monthView;// 月视图
    private TextView tvYear, tvMonth;// 年份 月份显示
    private TextView tvEnsure;// 确定按钮显示

    public TextView getTvEnsure() {
        return tvEnsure;
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 设置排列方向为竖向
        setOrientation(VERTICAL);

        LayoutParams llParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // 标题栏根布局
        RelativeLayout rlTitle = new RelativeLayout(context);
        rlTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        int rlTitlePadding = MeasureUtil.dp2px(context, 10);
        rlTitle.setPadding(rlTitlePadding, rlTitlePadding, rlTitlePadding, rlTitlePadding);

        // 周视图根布局
        LinearLayout llWeek = new LinearLayout(context);
        llWeek.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        llWeek.setOrientation(HORIZONTAL);
        int llWeekPadding = MeasureUtil.dp2px(context, 5);
        llWeek.setPadding(0, llWeekPadding, 0, llWeekPadding);
        LayoutParams lpWeek = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpWeek.weight = 1;

        // 标题栏子元素布局参数
        RelativeLayout.LayoutParams lpYear =
                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpYear.addRule(RelativeLayout.CENTER_VERTICAL);
        RelativeLayout.LayoutParams lpMonth =
                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpMonth.addRule(RelativeLayout.CENTER_IN_PARENT);
        RelativeLayout.LayoutParams lpEnsure =
                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpEnsure.addRule(RelativeLayout.CENTER_VERTICAL);
        lpEnsure.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        // --------------------------------------------------------------------------------标题栏
        // 年份显示
        tvYear = new TextView(context);
        tvYear.setText("2018");
        tvYear.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tvYear.setTextColor(Color.parseColor("#FFFFFF"));

        // 月份显示
        tvMonth = new TextView(context);
        tvMonth.setText("三月");
        tvMonth.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tvMonth.setTextColor(Color.parseColor("#FFFFFF"));

        // 确定显示
        tvEnsure = new TextView(context);
        tvEnsure.setText("签到");
        tvEnsure.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tvEnsure.setTextColor(Color.parseColor("#FFFFFF"));

        rlTitle.addView(tvYear, lpYear);
        rlTitle.addView(tvMonth, lpMonth);
        rlTitle.addView(tvEnsure, lpEnsure);

        addView(rlTitle, llParams);

        // --------------------------------------------------------------------------------周视图
        for (int i = 0; i < titleWeek.length; i++) {
            TextView tvWeek = new TextView(context);
            tvWeek.setText(titleWeek[i]);
            tvWeek.setGravity(Gravity.CENTER);
            tvWeek.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tvWeek.setTextColor(Color.parseColor("#FFFFFF"));
            llWeek.addView(tvWeek, lpWeek);
        }
        addView(llWeek, llParams);

        // ------------------------------------------------------------------------------------月视图
        monthView = new MonthView(context);
        monthView.setOnDateChangeListener(new MonthView.OnDateChangeListener() {
            @Override
            public void onMonthChange(int month) {
                tvMonth.setText(titleMonth[month - 1]);
            }

            @Override
            public void onYearChange(int year) {
                String tmp = String.valueOf(year);
                if (tmp.startsWith("-")) {
                    tmp = tmp.replace("-", titleBC);
                }
                tvYear.setText(tmp);
            }
        });
        addView(monthView, llParams);
    }

    /**
     * 设置初始化年月日期
     *
     * @param year  ...
     * @param month ...
     */
    public void setDate(int year, int month) {
        if (month < 1) {
            month = 1;
        }
        if (month > 12) {
            month = 12;
        }
        monthView.setDate(year, month);
    }

    public void setDPDecor(DPDecor decor) {
        monthView.setDPDecor(decor);
    }

    public void setDPCManager(DPCManager dpcManager) {
        monthView.setDPCManager(dpcManager);
    }
}
