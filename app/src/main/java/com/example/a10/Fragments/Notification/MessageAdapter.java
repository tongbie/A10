package com.example.a10.Fragments.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a10.R;
import com.example.a10.Utils.Tool;

import java.util.List;

/**
 * Created by BieTong on 2018/2/10.
 */

public class MessageAdapter extends ArrayAdapter {
    private List<Message> messages;
    private Context context;

    public MessageAdapter(Context context, int resourseId, List<Message> messages) {
        super(context, resourseId, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Message message = (Message) getItem(position);
//        ViewHolder viewHolder = null;
//        if (view == null) {
//            viewHolder = new ViewHolder();
        if (message.getType() == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_in, viewGroup, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_out, viewGroup, false);
        }
//            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
//            viewHolder.nameView = (TextView) view.findViewById(R.id.nameView);
//            viewHolder.chatView = (TextView) view.findViewById(R.id.chatView);
//            view.setTag(viewHolder);//setTag()用以向View追加额外数据
//        } else {
//            viewHolder = (ViewHolder) view.getTag();
//        }
//        viewHolder.imageView.setImageResource(Tool.imageId);
//        viewHolder.nameView.setText(message.getName());
//        viewHolder.chatView.setText(message.getMessage());
        ((ImageView) view.findViewById(R.id.imageView)).setImageResource(Tool.imageId);
        ((TextView) view.findViewById(R.id.nameView)).setText(message.getName());
        ((TextView) view.findViewById(R.id.chatView)).setText(message.getMessage());
        return view;
    }

    /*public class ViewHolder {
        public ImageView imageView;
        public TextView nameView;
        public TextView chatView;
    }*/
}
