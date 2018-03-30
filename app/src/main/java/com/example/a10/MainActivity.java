package com.example.a10;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.a10.Fragments.Home.HomeFragment;
import com.example.a10.Fragments.Personal.PersonalFragment;
import com.example.a10.Fragments.Notification.NotificationFragment;
import com.example.a10.Fragments.Require.RequireFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectStatusChangeListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    public static List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fragmentManager;
    private long backTime = 0;//双击返回计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
        initData();
        Tool.linkServer();
    }

    private void initView() {
        /* BottomNavigationBar */
        BottomNavigationBar bottomBar = findViewById(R.id.navigation);
        bottomBar.setTabSelectedListener(this);
//        BadgeItem badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("3");
        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomBar.setBarBackgroundColor(R.color.colorWrite);
        bottomBar.addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, "首页")
                .setActiveColorResource(R.color.colorWrite))
                .addItem(new BottomNavigationItem(R.drawable.ic_dashboard_black_24dp, "任务")
                        .setActiveColorResource(R.color.colorWrite))
                .addItem(new BottomNavigationItem(R.drawable.ic_notifications_black_24dp, "消息")
                        .setActiveColorResource(R.color.colorWrite)
                        /*.setBadgeItem(badgeItem)*/)//添加角标
                .addItem(new BottomNavigationItem(R.drawable.ic_personal, "我的")
                        .setActiveColorResource(R.color.colorWrite))
                .setFirstSelectedPosition(0)
                .initialise();//所有的设置需在调用该方法前完成
    }

    /* 初始化Fragment */
    private void initFragment() {
        Fragment homeFragment = new HomeFragment();
        Fragment requireFragment = new RequireFragment();
        Fragment notificationFragment = new NotificationFragment();
        Fragment personalFragment = new PersonalFragment();
        fragments.add(homeFragment);
        fragments.add(requireFragment);
        fragments.add(notificationFragment);
        fragments.add(personalFragment);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root, fragments.get(0));
        fragmentTransaction.commit();
    }

    private void initData(){
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                if (status.getCode() == 2) {
                    Tool.isConnected = true;
                } else {
                    Tool.isConnected = false;
                }
            }
        });
    }

    /* 双击返回 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - backTime) > 1500) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                backTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTabSelected(int position) {
        int fragmentId=position;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root, fragments.get(fragmentId));
        fragmentTransaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
