package com.example.a10.MyView.datepicker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 月历装饰物类
 * Decor of Calendar
 *
 * @author AigeStudio 2015-07-22
 * @author AigeStudio 2015-10-29
 *         为每一个装饰物的绘制方法增加一个含参data的重载方法
 *         Add a parameter for each method.
 */
public class DPDecor {

    public DPDecor(){}
    /**
     * 绘制当前日期区域右上角的装饰物
     * Draw decor on Top right of current date area
     *
     * @param canvas 绘制图形的画布 Canvas of image drew
     * @param rect   可以绘制的区域范围 Area you can draw
     * @param paint  画笔对象 Paint
     * @param data   日期
     */
    public void drawDecorTR(Canvas canvas, Rect rect, Paint paint, String data) {
        drawDecorTR(canvas, rect, paint);
    }

    /**
     * @see #drawDecorTR(Canvas, Rect, Paint, String)
     */
    public void drawDecorTR(Canvas canvas, Rect rect, Paint paint) {
        paint.setColor(Color.parseColor("#E95464"));
        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
    }

    /**
     * 绘制当前日期区域背景的装饰物
     * Draw decor of background of current date area
     *
     * @param canvas 绘制图形的画布 Canvas of image drew
     * @param rect   可以绘制的区域范围 Area you can draw
     * @param paint  画笔对象 Paint
     * @param data   日期
     */
    public void drawDecorBG(Canvas canvas, Rect rect, Paint paint, String data) {
        drawDecorBG(canvas, rect, paint);
    }

    /**
     * @see #drawDecorBG(Canvas, Rect, Paint, String)
     */
    public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {

    }
}
