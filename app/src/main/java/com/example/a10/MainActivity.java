package com.example.a10;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a10.Fragments.HomeFragment;
import com.example.a10.Fragments.PersonalFragment;
import com.example.a10.Fragments.Notification.NotificationFragment;
import com.example.a10.Fragments.RequireFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fragmentManager;
    private long backTime = 0;//双击返回计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
    }

    private void initView(){
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    /* 初始化Fragment */
    private void initFragment(){
        Fragment homeFragment = new HomeFragment();
        Fragment requireFragment = new RequireFragment();
        Fragment notificationFragment = new NotificationFragment();
        Fragment personalFragment = new PersonalFragment();
        fragments.add(homeFragment);
        fragments.add(requireFragment);
        fragments.add(notificationFragment);
        fragments.add(personalFragment);
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root,fragments.get(0));
        fragmentTransaction.commit();
    }

    /* 底栏点击事件 */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int fragmentId=0;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragmentId=0;
                break;
            case R.id.navigation_require:
                fragmentId=1;
                break;
            case R.id.navigation_notification:
                fragmentId=2;
                break;
            case R.id.navigation_personal:
                fragmentId=3;
                break;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root,fragments.get(fragmentId));
        fragmentTransaction.commit();
        return true;
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
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
