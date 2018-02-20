package com.example.a10.Fragments.Notification;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.a10.R;
import com.example.a10.SlipBack;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    ListView listView;
    List<Chat> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initData();
//        new SlipBack(this);
    }

    private void initView() {
        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        listView.setAdapter(new ChatAdapter(this,0,list));
    }

    private void initData(){
        list.add(new Chat(R.drawable.navigation_information,"王栋","我说了一句话",0));
        list.add(new Chat(R.drawable.navigation_information,"别同","我说了一句话",1));
    }
}
