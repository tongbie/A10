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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a10.Fragments.Home.HomeGson;
import com.example.a10.LoginActivity;
import com.example.a10.MainActivity;
import com.example.a10.MyView.MyButton;
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
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static com.example.a10.Fragments.Notification.BmobIMApplication.getMyProcessName;

public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private List<Message> messagesList;
    private View view;
    private EditText editText;
    private boolean isLink=false;
    private boolean isFristLoad=true;

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
                        isLink=true;
                        if(isFristLoad) {
                            initData();
                        }
                    } else {
                        toast("无法连接至服务器");
                        isLink=false;
                    }
                    ((MyTextView) view.findViewById(R.id.title)).setLoading(false);
                }
            });
        }
    }

    private void initData() {
        if(!isLink){
            linkServer();
            return;
        }
        messagesList = new ArrayList<>();
        for (BmobIMConversation bimc : BmobIM.getInstance().loadAllConversation()) {
            messagesList.add(new Message(bimc.getConversationIcon(), bimc.getConversationTitle(),bimc.getMessages().get(bimc.getMessages().size()-1).toString()));
        }
        listView.setAdapter(new MessageAdapter(getContext(), 0, messagesList));
    }

    private void initView() {
        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        view.findViewById(R.id.refresh).setOnClickListener(this);
        editText = view.findViewById(R.id.editText);
        view.findViewById(R.id.add).setOnClickListener(this);
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
        if(!isLink){
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
                                Intent intent = new Intent(getActivity(), ChatActivity.class);
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
}