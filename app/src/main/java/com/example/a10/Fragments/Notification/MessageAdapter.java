package com.example.a10.Fragments.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a10.R;

import java.util.List;

/**
 * Created by BieTong on 2018/2/10.
 */

public class MessageAdapter extends ArrayAdapter {
    private List<Message> messages;
    private Context context;

    public MessageAdapter(Context context, int resourseId, List<Message> messages) {
        super(context,resourseId, messages);
        this.context=context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            if(getItemViewType(position)==0){
                view= LayoutInflater.from(context).inflate(R.layout.chat_item_in, viewGroup, false);
            }else {
                view= LayoutInflater.from(context).inflate(R.layout.chat_item_out, viewGroup, false);
            }
            viewHolder.imageView= (ImageView) view.findViewById(R.id.imageView);
            viewHolder.nameView= (TextView) view.findViewById(R.id.nameView);
            viewHolder.chatView= (TextView) view.findViewById(R.id.chatView);
            view.setTag(viewHolder);//setTag()用以向View追加额外数据
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
        Message message = (Message) getItem(position);
        viewHolder.imageView.setImageResource(message.getImageId());
        viewHolder.nameView.setText(message.getName());
        viewHolder.chatView.setText(message.getMessage());
        return view;
    }

    public class ViewHolder{
        public ImageView imageView;
        public TextView nameView;
        public TextView chatView;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.getType();
    }
}
