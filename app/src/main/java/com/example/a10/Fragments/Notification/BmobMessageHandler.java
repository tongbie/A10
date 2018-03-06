package com.example.a10.Fragments.Notification;


import android.content.Intent;
import android.os.Bundle;

import com.example.a10.Fragments.Home.HomeGson;
import com.example.a10.MainActivity;

import java.util.List;
import java.util.logging.Logger;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by BieTong on 2018/3/5.
 */

public class BmobMessageHandler extends BmobIMMessageHandler {
    @Override
    public void onMessageReceive(final MessageEvent event) {
        //在线消息
        updateUserInfo(event);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
//        updateUserInfo(event);

    }

    public void updateUserInfo(MessageEvent event) {
        BmobIMConversation conversation = event.getConversation();
        BmobIMUserInfo info = event.getFromUserInfo();
        BmobIMMessage msg = event.getMessage();
        String username = info.getName();
        String title = conversation.getConversationTitle();

        BmobIM.getInstance().updateConversation(conversation);
        BmobIM.getInstance().updateUserInfo(info);

        //SDK内部将新会话的会话标题用objectId表示，因此需要比对用户名和私聊会话标题，后续会根据会话类型进行判断
//        if (!username.equals(title)) {
//            UserModel.getInstance().queryUserInfo(info.getUserId(), new QueryUserListener() {
//                @Override
//                public void done(User s, BmobException e) {
//                    if (e == null) {
//                        String name = s.getUsername();
//                        String avatar = s.getAvatar();
//                        conversation.setConversationIcon(avatar);
//                        conversation.setConversationTitle(name);
//                        info.setName(name);
//                        info.setAvatar(avatar);
//                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
//                        BmobIM.getInstance().updateUserInfo(info);
//                        //TODO 会话：4.7、更新会话资料-如果消息是暂态消息，则不更新会话资料
//                        if (!msg.isTransient()) {
//                            BmobIM.getInstance().updateConversation(conversation);
//                        }
//                    } else {
//                        Logger.e(e);
//                    }
//                    listener.done(null);
//                }
//            });
//        } else {
//            listener.done(null);
//        }
    }
}
