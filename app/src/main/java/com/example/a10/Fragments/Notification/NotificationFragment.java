package com.example.a10.Fragments.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a10.MyView.MyTextView;
import com.example.a10.R;
import com.example.a10.ToolClass;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView listView;
    private List<Message> messagesList;
    private View view;
    private String userId="6925eb69db";

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
                        toast("连接成功");
                        initData();
                    } else {
                        toast(e.getMessage());
                    }
                    ((MyTextView) view.findViewById(R.id.title)).setLoading(false);
                }
            });
        }
    }

    private void initData() {
        for(BmobIMConversation bimc:BmobIM.getInstance().loadAllConversation()){
            messagesList.add(new Message(bimc.getConversationIcon(),bimc.getConversationId(),bimc.getConversationTitle()));
        }
    }

    private void initView() {
        listView = view.findViewById(R.id.listView);
        messagesList = new ArrayList<>();
        listView.setAdapter(new MessageAdapter(getContext(), 0, messagesList));
        listView.setOnItemClickListener(this);
        view.findViewById(R.id.refresh).setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//        view.getId();
//        startActivity(new Intent(getContext(), ChatActivity.class));
//        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.empty);
        BmobIMUserInfo info =new BmobIMUserInfo();
        info.setName("test");
        info.setUserId(userId);
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                if(e==null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bmobIMConversation", bmobIMConversation);
                    Intent intent=new Intent(getActivity(),ChatActivity.class);
                    intent.putExtra("bmobIMConversation",bundle);
                    getActivity().startActivity(intent);
                /*BmobIMTextMessage message=new BmobIMTextMessage();
                bmobIMConversation.sendMessage(message, new MessageSendListener() {
                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "发送成功", Toast.LENGTH_SHORT).show();
                        }else{
                        }
                    }
                });*/
                }else {
                    toast(e.getMessage());
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
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                messagesList.add(new Message(null,"别同","shit"));
                break;
        }
    }
}
