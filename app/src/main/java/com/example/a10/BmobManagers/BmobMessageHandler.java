package com.example.a10.BmobManagers;

import android.util.Log;

import com.example.a10.BusEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by BieTong on 2018/3/5.
 */

public class BmobMessageHandler extends BmobIMMessageHandler {

    @Override
    public void onMessageReceive(final MessageEvent messageEvent) {
        String senderId = messageEvent.getFromUserInfo().getUserId();
        queryUserName(senderId, messageEvent);
    }

    private void queryUserName(String objectId, MessageEvent messageEvent) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    User bUser = list.get(0);
                    String senderName = bUser.getUsername();
                    upDateMessage(senderName, messageEvent);
                }
            }
        });
    }

    private void queryOfflineUserName(String objectId, MessageEvent messageEvent) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    User bUser = list.get(0);
                    String senderName = bUser.getUsername();
                    upDateOfflineMessage(senderName, messageEvent);
                }
            }
        });
    }

    private void upDateMessage(String senderName, MessageEvent messageEvent) {
        BmobIMConversation conversation = messageEvent.getConversation();
        conversation.setConversationTitle(senderName);
        conversation.setDraft(senderName + "：" + messageEvent.getMessage().getContent());

//        BmobIM.getInstance().updateUserInfo(messageEvent.getFromUserInfo());
        BmobIM.getInstance().updateConversation(conversation);

        BusEvent busEvent = new BusEvent("在线消息");
        busEvent.setSenderName(senderName);
        busEvent.setConversation(conversation);
        EventBus.getDefault().post(busEvent);
    }

    private void upDateOfflineMessage(String senderName, MessageEvent messageEvent) {
        BmobIMConversation bmobIMConversation = messageEvent.getConversation();
        bmobIMConversation.setConversationTitle(senderName);
        bmobIMConversation.setDraft(senderName + "：" + messageEvent.getMessage().getContent());

//            BmobIM.getInstance().updateUserInfo(messageEvent.getFromUserInfo());
        BmobIM.getInstance().updateConversation(bmobIMConversation);

        BusEvent busEvent = new BusEvent("离线消息");
        busEvent.setSenderName(senderName);
        busEvent.setConversation(bmobIMConversation);
        EventBus.getDefault().post(busEvent);
    }

    @Override
    public void onOfflineReceive(final OfflineMessageEvent offlineMessageEvent) {
        List<MessageEvent> messageEventList = new ArrayList<>();
        Set<Map.Entry<String, List<MessageEvent>>> entrySet = offlineMessageEvent.getEventMap().entrySet();
        Iterator<Map.Entry<String, List<MessageEvent>>> it = entrySet.iterator();
        while (it.hasNext()) {
            messageEventList = it.next().getValue();
        }
        for (int i = 0; i < messageEventList.size(); i++) {
            MessageEvent messageEvent = messageEventList.get(i);
            String senderId = messageEvent.getFromUserInfo().getUserId();
            queryOfflineUserName(senderId, messageEvent);
        }
    }
}
