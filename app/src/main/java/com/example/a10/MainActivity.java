package com.example.a10;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.a10.Fragments.HomeFragment;
import com.example.a10.Fragments.InformationFragment;
import com.example.a10.Fragments.NotificationFragment;
import com.example.a10.Fragments.RequireFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
        {//控件
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(this);
        }
    }

    /* 初始化Fragment */
    private void initFragment(){
        Fragment homeFragment = new HomeFragment();
        Fragment requireFragment = new RequireFragment();
        Fragment notificationFragment = new NotificationFragment();
        Fragment informationFragment = new InformationFragment();
        fragments.add(homeFragment);
        fragments.add(requireFragment);
        fragments.add(notificationFragment);
        fragments.add(informationFragment);
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragments.get(0));
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
            case R.id.navigation_information:
                fragmentId=3;
                break;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragments.get(fragmentId));
        fragmentTransaction.commit();
        return true;
    }
}
