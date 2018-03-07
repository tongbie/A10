package com.example.a10.Fragments.Notification;

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

public class MessageActivity extends AppCompatActivity {
    private ListView listView;
    private List<Message> messages;
    private BmobIMConversation bConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        bConversation = BmobIMConversation.obtain(
                    BmobIMClient.getInstance(),
                    (BmobIMConversation) getIntent().getExtras().getSerializable("bmobIMConversation"));
        sendMessage("BieTong发了一条消息");

    }

    private void sendMessage(String message) {
        //可随意设置额外信息
//        Map<String, Object> map = new HashMap<>();
//        map.put("level", "1");
//        msg.setExtraMap(map);
        BmobIMTextMessage bMessage = new BmobIMTextMessage();
        bMessage.setContent(message);
        if(bConversation!=null) {
            bConversation.sendMessage(bMessage, new MessageSendListener() {
                @Override
                public void done(BmobIMMessage msg, BmobException e) {
                    if (e == null) {
                        toast("发送成功");
                    } else {
                        toast("发送失败\n"+e.getMessage());
                    }
                }
            });
        }
    }

    private void initView() {
        listView = findViewById(R.id.listView);
        messages = new ArrayList<>();
        listView.setAdapter(new MessageAdapter(this, 0, messages));
    }

    private void toast(String text) {
        try {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
