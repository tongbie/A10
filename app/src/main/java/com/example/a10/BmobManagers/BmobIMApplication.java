package com.example.a10.BmobManagers;

import android.app.Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by BieTong on 2018/3/5.
 */

public class BmobIMApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        if (getApplicationInfo().packageName.equals(getMyProcessName())){
            Bmob.initialize(this,"c2c1321b56eef48e75db1371f8153b80");
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new BmobMessageHandler());
        }
    }

    /* 获取当前运行的进程名 */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
