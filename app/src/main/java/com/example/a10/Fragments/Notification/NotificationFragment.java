package com.example.a10.Fragments.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.a10.BmobManagers.User;
import com.example.a10.Utils.BusEvent;
import com.example.a10.R;
import com.example.a10.Utils.Tool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class NotificationFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        AdapterView.OnItemLongClickListener {

    private ListView listView;
    private List<BmobIMConversation> conversationList;
    private View view = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_notification, null);
            initView();
            EventBus.getDefault().register(this);//注册EventBus
            updateMyConversation();
        }
//        ViewGroup viewGroup = (ViewGroup) view.getParent();
//        if (viewGroup != null) {
//            viewGroup.removeView(view);
//        }
        Tool.translateAnimation(getContext(), view, R.id.linearLayout);
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
            toast("服务器连接失败");
            Tool.linkServer(getContext());
            return;
        }
        if (conversationList != null) {
            conversationList.clear();
            conversationList = null;
        }
        conversationList = BmobIM.getInstance().loadAllConversation();
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

    private void judgeRepeatConversation(String username) {
        for (BmobIMConversation conversation : conversationList) {
            if (username.equals(conversation.getConversationTitle())) {
                startMessageActivity(conversation, username);
                return;
            }
        }
        startNewChat(username);
    }

    private void startNewChat(String username) {
        if (!Tool.isConnected) {
            Tool.linkServer(getContext());
            toast("服务器连接失败");
            return;
        }
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    final User bUser = list.get(0);
                    BmobIMUserInfo info = new BmobIMUserInfo();
                    final String bName = bUser.getUsername();
                    info.setUserId(bUser.getObjectId());
                    info.setName(bName);
                    info.setAvatar(bUser.getAvatar());
                    BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                        @Override
                        public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                            if (e == null) {
                                startMessageActivity(bmobIMConversation, bUser.getUsername());
                                bmobIMConversation.setConversationTitle(bName);
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
        if (!Tool.isConnected) {
            toast("服务器连接失败");
            return;
        }
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
        BmobIMConversation conversation = conversationList.get(position);
        if (!Tool.isConnected) {
            toast("正在尝试连接服务器...");
            Tool.linkServer(getContext());
            return;
        }
        if (conversation != null) {
            startMessageActivity(conversation, conversation.getConversationTitle());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.delete, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BmobIMConversation conversation = conversationList.get(position);
                BmobIM.getInstance().deleteConversation(conversation.getConversationId());
                updateMyConversation();
                return true;
            }
        });
        popupMenu.show();
        return true;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);//解除注册
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiver(BusEvent busEvent) {
        if (busEvent.getEventName().equals("发送消息")) {
//            updateMyConversation();
        } else if (busEvent.getEventName().equals("在线消息")) {
            updateMyConversation();
        } else if (busEvent.getEventName().equals("离线消息")) {
            updateMyConversation();
        }
    }
}