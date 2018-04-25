package com.example.a10.Views.datepicker.utils;

import android.content.Context;

/**
 * 测量工具类
 *
 * Util of measure.
 *
 * @author AigeStudio 2015-03-26
 */
public final class MeasureUtil {
    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
