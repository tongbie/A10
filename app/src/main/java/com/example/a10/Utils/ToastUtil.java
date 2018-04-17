package com.example.a10.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by BieTong on 2018/4/9.
 */

public class ToastUtil {

    private static Toast toast = null;

    private ToastUtil(){

    }

    public static void show(Context context, String text) {
//        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//        }
//        toast.setText(text);
        toast.show();
    }
}
