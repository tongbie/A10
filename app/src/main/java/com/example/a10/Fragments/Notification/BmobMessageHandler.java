package com.example.a10.Fragments.Notification;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by BieTong on 2018/3/5.
 */

public class BmobMessageHandler extends BmobIMMessageHandler {
    @Override
    public void onMessageReceive(final MessageEvent event) {
        //在线消息
//        updateUserInfo(event);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //离线消息，每次connect的时候会查询离线消息，如果有，此方法会被调用
        /*List<MessageEvent> bMessageEvents = new ArrayList<>();
        Set<Map.Entry<String, List<MessageEvent>>> entrySet = event.getEventMap().entrySet();
        Iterator<Map.Entry<String, List<MessageEvent>>> it = entrySet.iterator();
        while (it.hasNext()) {
            bMessageEvents = it.next().getValue();
        }
        for (int i = 0; i < bMessageEvents.size(); i++) {
            BmobIM.getInstance().updateConversation(bMessageEvents.get(i).getConversation());
            BmobIM.getInstance().updateUserInfo(bMessageEvents.get(i).getFromUserInfo());
        }*/
    }

    public void updateUserInfo(MessageEvent event) {
        BmobIMConversation bConversation = event.getConversation();
        BmobIMUserInfo bUserInfo = event.getFromUserInfo();
        BmobIMMessage bMessage = event.getMessage();
        String bUserName = bUserInfo.getName();
        String bTitle = bConversation.getConversationTitle();

        BmobIM.getInstance().updateConversation(bConversation);
        BmobIM.getInstance().updateUserInfo(bUserInfo);

        //SDK内部将新会话的会话标题用objectId表示，因此需要比对用户名和私聊会话标题，后续会根据会话类型进行判断
//        if (!bUserName.equals(bTitle)) {
//            UserModel.getInstance().queryUserInfo(bUserInfo.getUserId(), new QueryUserListener() {
//                @Override
//                public void done(User s, BmobException e) {
//                    if (e == null) {
//                        String name = s.getUsername();
//                        String avatar = s.getAvatar();
//                        bConversation.setConversationIcon(avatar);
//                        bConversation.setConversationTitle(name);
//                        bUserInfo.setName(name);
//                        bUserInfo.setAvatar(avatar);
//                        //TODO 会话：2.7、更新用户资料，用于在会话页面、聊天页面以及个人信息页面显示
//                        BmobIM.getInstance().updateUserInfo(bUserInfo);
//                        //TODO 会话：4.7、更新会话资料-如果消息是暂态消息，则不更新会话资料
//                        if (!bMessage.isTransient()) {
//                            BmobIM.getInstance().updateConversation(bConversation);
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
