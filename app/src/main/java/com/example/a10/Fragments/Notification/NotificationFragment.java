package com.example.a10.Fragments.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a10.Fragments.Home.HomeGson;
import com.example.a10.MyView.MyTextView;
import com.example.a10.R;
import com.example.a10.ToolClass;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NotificationFragment extends Fragment
        implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener, MessageListHandler {
    private ListView listView;
    private List<Conversation> messagesList;
    private View view;
    private EditText editText;
    private boolean isConnected = false;
    private boolean isFristLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_notification, null);
            initView();
            linkServer();
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        initAnimation(view);
        editText.setText("test");
        return view;
    }

    private void linkServer() {
        ((MyTextView) view.findViewById(R.id.title)).setLoading(true);
        BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
        if (!TextUtils.isEmpty(user.getObjectId())) {
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String uid, BmobException e) {
                    if (e == null) {
                        if (isFristLoad) {
                            initData();
                        }
                    } else {
                        toast("无法连接至服务器");
                    }
                    ((MyTextView) view.findViewById(R.id.title)).setLoading(false);
                }
            });
        }
    }

    @Override//消息接收回调
    public void onMessageReceive(List<MessageEvent> messageEvents) {
        for (MessageEvent messageEvent : messageEvents) {
            BmobIM.getInstance().updateUserInfo(messageEvent.getFromUserInfo());
            BmobIM.getInstance().updateConversation(messageEvent.getConversation());
        }
        initData();
    }

    private void initData() {
        if (!isConnected) {
            linkServer();
            return;
        }
        messagesList = new ArrayList<>();
        for (BmobIMConversation bimc : BmobIM.getInstance().loadAllConversation()) {
            messagesList.add(new Conversation(String.valueOf(R.drawable.ic_personal), bimc.getConversationTitle(), bimc.getMessages().get(bimc.getMessages().size() - 1).getContent()));
        }
        listView.setAdapter(new ConversationAdapter(getContext(), 0, messagesList));
    }

    private void initView() {
        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        view.findViewById(R.id.refresh).setOnClickListener(this);
        editText = view.findViewById(R.id.editText);
        view.findViewById(R.id.add).setOnClickListener(this);
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                Log.e("服务器连接状态：", status.getMsg() + " " + String.valueOf(status.getCode()));
                if (status.getCode() == 2) {
                    isConnected = true;
                } else {
                    isConnected = false;
                }
            }
        });
    }

    private void initAnimation(View view) {
        TranslateAnimation ta = new TranslateAnimation(0, 0, ToolClass.dp(-50), 0);
        ta.setInterpolator(new OvershootInterpolator());
        ta.setDuration(200);
        LayoutAnimationController lac = new LayoutAnimationController(ta, 0.3f);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        linearLayout.setLayoutAnimation(lac);
    }

    private void toast(String text) {
        try {
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                initData();
                break;
            case R.id.add:
                startChat(editText.getText().toString());
                break;
        }
    }

    private void startChat(String username) {
        if (!isConnected) {
            linkServer();
            return;
        }
        BmobQuery<HomeGson> query = new BmobQuery<HomeGson>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<HomeGson>() {
            @Override
            public void done(List<HomeGson> list, BmobException e) {
                if (e == null) {
                    BmobIMUserInfo info = new BmobIMUserInfo();
                    info.setName(username);
                    info.setUserId(list.get(0).getObjectId());
                    BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                        @Override
                        public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                            if (e == null) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("bmobIMConversation", bmobIMConversation);
                                Intent intent = new Intent(getActivity(), MessageActivity.class);
                                intent.putExtras(bundle);
                                getActivity().startActivity(intent);
                            } else {
                                toast("创建对话失败");
                            }
                        }
                    });
                } else {
                    toast("未查找到该用户");
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onResume() {
        BmobIM.getInstance().addMessageListHandler(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }
}