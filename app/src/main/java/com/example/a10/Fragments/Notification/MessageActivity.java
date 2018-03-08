package com.example.a10.Fragments.Notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import cn.bmob.v3.BmobUser;
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
        if(getIntent().getExtras().getSerializable("bmobIMConversation")==null){
            finish();
            return;
        }
        bConversation = BmobIMConversation.obtain(
                    BmobIMClient.getInstance(),
                    (BmobIMConversation) getIntent().getExtras().getSerializable("bmobIMConversation"));

        editText.setText(BmobUser.getCurrentUser().getUsername()+"发送了一条消息");
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
                        Log.e("发送失败：",e.getMessage()+" "+String.valueOf(e.getErrorCode()));
                    }
                }
            });
        }
    }

    private void initView() {
        listView = findViewById(R.id.listView);
        messages = new ArrayList<>();
        listView.setAdapter(new MessageAdapter(this, 0, messages));
        editText=findViewById(R.id.editText);
        findViewById(R.id.send).setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.send:
                sendMessage(editText.getText().toString());
                break;
        }
    }
}
