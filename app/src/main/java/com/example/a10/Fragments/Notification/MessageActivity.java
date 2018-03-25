package com.example.a10.Fragments.Notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10.BmobManagers.User;
import com.example.a10.MyView.SlipBack;
import com.example.a10.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.exception.BmobException;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener, MessageListHandler {
    private ListView listView;
    private List<Message> messages;
    private BmobIMConversation bConversation;
    private EditText editText;
    private String linkMan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_message);
        getConversation();
        initView();
        updateMessages();
        new SlipBack(this);
    }

    private void initView() {
        messages = new ArrayList<>();
        listView = findViewById(R.id.listView);
        listView.setAdapter(new MessageAdapter(this, 0, messages));
        editText = this.findViewById(R.id.editText);
        findViewById(R.id.send).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void getConversation() {
        BmobIMConversation conversationEntrance = (BmobIMConversation) getBundle().getSerializable("bmobIMConversation");
        if (conversationEntrance == null) {
            toast("开启会话失败，请重试");
            return;
        }
        bConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        linkMan = getBundle().getString("linkMan");
        if (linkMan != null) {
            ((TextView) findViewById(R.id.linkManView)).setText(linkMan);
        }
    }

    public void updateMessages() {
        messages = new ArrayList<>();
        bConversation.queryMessages(null, 50, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        for (BmobIMMessage bMessage : list) {
                            if (bMessage.getBmobIMUserInfo() == null) {
                                //TODO:???为null???
                                return;
                            }
                            Message message = new Message(R.drawable.ic_personal, "", bMessage.getContent(), 0);
                            if (bMessage.getBmobIMUserInfo().getUserId().equals(User.getCurrentUser().getObjectId())) {
                                message.setName(User.getCurrentUser().getUsername());
                                message.setType(1);
                            } else {
                                //TODO:最后一项头像会错位
                                message.setName(linkMan);
                                message.setType(0);
                            }
                            messages.add(message);
                        }
                    }
                } else {
                    toast(e.getMessage());
                }
            }
        });
        listView.setAdapter(new MessageAdapter(this, 0, messages));
    }

    private void sendMessage(String message) {
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(message);
        bConversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {
                    messages.add(new Message(R.drawable.ic_personal, User.getCurrentUser().getUsername(), message, 1));
                    listView.setAdapter(new MessageAdapter(MessageActivity.this, 0, messages));
                    BmobIM.getInstance().updateConversation(bConversation);
                    editText.setText("");
                } else {
                    toast(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onMessageReceive(List<MessageEvent> events) {
        for (MessageEvent event : events) {
            BmobIMConversation conversation = event.getConversation();
            if (conversation != null && conversation.getConversationId().equals(bConversation.getConversationId())) {
                BmobIM.getInstance().updateConversation(conversation);
                bConversation=conversation;
            }
        }
        updateMessages();
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

    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }

    private void toast(String text) {
        try {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BmobIM.getInstance().addMessageListHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BmobIM.getInstance().removeMessageListHandler(this);
    }
}
