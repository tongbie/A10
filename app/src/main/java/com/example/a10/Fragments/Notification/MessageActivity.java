package com.example.a10.Fragments.Notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10.BmobManagers.User;
import com.example.a10.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private List<Message> messages;
    private BmobIMConversation bConversation;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_message);
        initView();
        BmobIMConversation conversationEntrance = (BmobIMConversation) getBundle().getSerializable("bmobIMConversation");
        if(conversationEntrance==null){
            toast("开启会话失败，请重试");
            return;
        }
        bConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        ((TextView)findViewById(R.id.linkManView)).setText(getBundle().getString("linkMan"));
    }

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }

    private void sendMessage(String message) {
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(message);
        bConversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e != null) {
                    toast(e.getMessage());
                }else {
                    toast("发送成功");
                }
            }
        });
    }

    private void initView() {
        listView = findViewById(R.id.listView);
        messages = new ArrayList<>();
        listView.setAdapter(new MessageAdapter(this, 0, messages));
        editText = findViewById(R.id.editText);
        findViewById(R.id.send).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void toast(String text) {
        try {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                sendMessage(editText.getText().toString());
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
