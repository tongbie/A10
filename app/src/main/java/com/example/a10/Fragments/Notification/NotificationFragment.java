package com.example.a10.Fragments.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.a10.BmobManagers.User;
import com.example.a10.R;
import com.example.a10.Tool;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private List<Conversation> conversationList;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_notification, null);
            initView();
            initData();
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        initAnimation(view);
        return view;
    }

    private void initView() {
        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        view.findViewById(R.id.refresh).setOnClickListener(this);
        view.findViewById(R.id.add).setOnClickListener(this);
    }

    /*@Override//消息接收回调
    public void onMessageReceive(List<MessageEvent> messageEvents) {
        for (MessageEvent messageEvent : messageEvents) {
            Log.e("收到了新消息：", messageEvent.getMessage().getContent());
            BmobIM.getInstance().updateUserInfo(messageEvent.getFromUserInfo());
            BmobIM.getInstance().updateConversation(messageEvent.getConversation());
        }
        initData();
    }*/

    public void initData() {
        if (!Tool.isConnected) {
            toast("正在重连...");
            Tool.linkServer();
            return;
        }
        conversationList = new ArrayList<>();
        try {
            for (BmobIMConversation bimc : BmobIM.getInstance().loadAllConversation()) {
                int size = bimc.getMessages().size();
                conversationList.add(new Conversation(String.valueOf(R.drawable.ic_personal),
                        bimc.getConversationTitle(),
                        size > 0 ? bimc.getMessages().get(size - 1).getContent() : null
                        , bimc));
            }
        } catch (Exception e) {
            toast(e.getMessage());
        }
        listView.setAdapter(new ConversationAdapter(getContext(), 0, conversationList));
    }

    private void initAnimation(View view) {
        TranslateAnimation ta = new TranslateAnimation(0, 0, Tool.dp(-50), 0);
        ta.setInterpolator(new OvershootInterpolator());
        ta.setDuration(200);
        LayoutAnimationController lac = new LayoutAnimationController(ta, 0.3f);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        linearLayout.setLayoutAnimation(lac);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                initData();
                break;
            case R.id.add:
                startNewChat(((EditText) view.findViewById(R.id.editText)).getText().toString());
                break;
        }
    }

    private void startNewChat(String username) {
        if (!Tool.isConnected) {
            Tool.linkServer();
            return;
        }
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    User bUser = list.get(0);
                    BmobIMUserInfo info = new BmobIMUserInfo();
                    info.setUserId(bUser.getObjectId());
                    info.setName(bUser.getUsername());
                    info.setAvatar(bUser.getAvatar());
                    BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                        @Override
                        public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                            if (e == null) {
                                startMessageActivity(bmobIMConversation, bUser.getUsername());
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

    private void startMessageActivity(BmobIMConversation bmobIMConversation, String linkMan) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bmobIMConversation", bmobIMConversation);
        bundle.putString("linkMan", linkMan);
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        if (bundle != null) {
            intent.putExtra(getActivity().getPackageName(), bundle);
        }
        getActivity().startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Conversation conversation= conversationList.get(position);
        if (conversation != null) {
            startMessageActivity(conversation.getBConversation(), conversation.getName());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onResume() {
//        BmobIM.getInstance().addMessageListHandler(this);
        super.onResume();
    }

    @Override
    public void onPause() {
//        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    private void toast(String text) {
        try {
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}