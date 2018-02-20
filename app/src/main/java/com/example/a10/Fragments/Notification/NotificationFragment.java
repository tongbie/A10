package com.example.a10.Fragments.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a10.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private List<Message> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        list.add(new Message(R.drawable.navigation_information,"别同","我发了一条消息"));
        list.add(new Message(R.drawable.navigation_information,"王栋","我发了一条消息"));
    }

    private void initView(View view) {
        listView = view.findViewById(R.id.listView);
        list = new ArrayList<>();
        listView.setAdapter(new MessageAdapter(getContext(), 0, list));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.getId();
        startActivity(new Intent(getContext(),ChatActivity.class));
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.empty);
    }
}
