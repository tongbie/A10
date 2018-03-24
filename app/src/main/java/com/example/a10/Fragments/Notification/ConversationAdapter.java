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
 * Created by BieTong on 2018/2/9.
 */

public class ConversationAdapter extends ArrayAdapter {
    private List<Conversation> conversations;
    private Context context;

    public ConversationAdapter(Context context, int resourseId, List<Conversation> conversations) {
        super(context,resourseId, conversations);
        this.context=context;
        this.conversations = conversations;
    }

    public List<Conversation> getConversations(){
        return conversations;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.message_item, viewGroup, false);
            viewHolder.imageView= (ImageView) view.findViewById(R.id.imageView);
            viewHolder.nameView= (TextView) view.findViewById(R.id.nameView);
            viewHolder.messageView= (TextView) view.findViewById(R.id.messageView);
            view.setTag(viewHolder);//setTag()用以向View追加额外数据
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
        Conversation conversation = (Conversation) getItem(position);
        viewHolder.imageView.setImageResource(conversation.getImageId());
        viewHolder.nameView.setText(conversation.getName());
        viewHolder.messageView.setText(conversation.getLastMessage());
        return view;
    }

    public class ViewHolder{
        public ImageView imageView;
        public TextView nameView;
        public TextView messageView;
    }
}
