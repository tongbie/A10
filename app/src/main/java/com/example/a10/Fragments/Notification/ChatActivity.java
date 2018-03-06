package com.example.a10.Fragments.Notification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a10.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends AppCompatActivity {
    private ListView listView;
    private List<Chat> chats;
    private BmobIMConversation bmobIMConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        try {
            bmobIMConversation = BmobIMConversation.obtain(
                    BmobIMClient.getInstance(),
                    (BmobIMConversation) getIntent().getExtras().getSerializable("bmobIMConversation"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BmobIMTextMessage msg = new BmobIMTextMessage();
            msg.setContent("shit");
            bmobIMConversation.sendMessage(msg, new MessageSendListener() {
                @Override
                public void done(BmobIMMessage msg, BmobException e) {
                    if (e != null) {
                        toast("发送成功");
                    } else {
                        toast(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        listView = findViewById(R.id.listView);
        chats = new ArrayList<>();
        listView.setAdapter(new ChatAdapter(this, 0, chats));
    }

    private void toast(String text) {
        try {
            Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
