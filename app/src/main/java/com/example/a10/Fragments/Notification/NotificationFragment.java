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
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.a10.BmobManagers.User;
import com.example.a10.R;
import com.example.a10.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NotificationFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        AdapterView.OnItemLongClickListener,
        MessageListHandler {
    private ListView listView;
    private List<Conversation> conversationList;
    private View view;
    private HashMap<String,String> map=new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_notification, null);
            initView();
            updateMyConversation();
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        Tool.translateAnimation(view);
        return view;
    }

    private void initView() {
        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        view.findViewById(R.id.refresh).setOnClickListener(this);
        view.findViewById(R.id.add).setOnClickListener(this);
    }

    public void updateMyConversation() {
        if (!Tool.isConnected) {
            toast("正在重连...");
            Tool.linkServer();
            return;
        }
        conversationList = new ArrayList<>();
        try {
            for (BmobIMConversation bConversation : BmobIM.getInstance().loadAllConversation()) {
                int size = bConversation.getMessages().size();
                conversationList.add(new Conversation(bConversation.getConversationTitle(),
                        size > 0 ? bConversation.getMessages().get(size - 1).getContent() : null
                        , bConversation));
            }
        } catch (Exception e) {
            toast(e.getMessage());
        }
        listView.setAdapter(new ConversationAdapter(getContext(), 0, conversationList));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                updateMyConversation();
                break;
            case R.id.add:
                judgeRepeatConversation(((EditText) view.findViewById(R.id.editText)).getText().toString());
                break;
        }
    }

    private void judgeRepeatConversation(String username){
        for(Conversation conversation:conversationList){
            if(username.equals(conversation.getName())){
                startMessageActivity(conversation.getBConversation(),username);
                return;
            }
        }
        startNewChat(username);
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
                                BmobIM.getInstance().updateConversation(bmobIMConversation);
                                updateMyConversation();
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
    public void onMessageReceive(List<MessageEvent> events) {
        for (MessageEvent event : events) {
            BmobIMConversation conversation = event.getConversation();
            map.put(conversation.getConversationId(),event.getFromUserInfo().getName());
            if (conversation != null) {
                BmobIM.getInstance().updateConversation(conversation);
            }
        }
        updateMyConversation();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Conversation conversation = conversationList.get(position);
        if (conversation != null) {
            startMessageActivity(conversation.getBConversation(), conversation.getName());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.delete, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            Conversation conversation=conversationList.get(position);
            BmobIM.getInstance().deleteConversation(conversation.getBConversation().getConversationId());
            updateMyConversation();
            return true;
        });
        popupMenu.show();
        return true;
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

    private void toast(String text) {
        try {
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}