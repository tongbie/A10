package com.example.a10.Fragments.Require;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10.Views.LoadTextView;
import com.example.a10.Views.MenuButton;
import com.example.a10.Views.RefreshButton;
import com.example.a10.R;
import com.example.a10.Utils.Tool;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class RequireFragment extends Fragment implements View.OnClickListener {
    private View view;
    private List<TaskItem> taskItemList = new ArrayList<>();
    private List<RequireGson> requireDatas = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_require, null);
            initView();
            addData();
        }
//        ViewGroup viewGroup = (ViewGroup) view.getParent();
//        if (viewGroup != null) {
//            viewGroup.removeView(view);
//        }
        return view;
    }

    private int state = 1;//当前界面显示任务类型标志

    private void addData() {
        requireDatas = new ArrayList<>();
        refreshButton.setRefreshing(true);
        BmobQuery<RequireGson> query = new BmobQuery<>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(BmobUser.class).getUsername());
        query.addWhereEqualTo("state", state);//查询指定状态的任务
        query.findObjects(new FindListener<RequireGson>() {
            @Override
            public void done(List<RequireGson> datas, BmobException e) {
                if (e == null) {
                    if (datas.size() == 0) {
                        Toast.makeText(getContext(), "任务列表为空", Toast.LENGTH_SHORT).show();
                    }
                    for (RequireGson data : datas) {
                        requireDatas.add(data);
                    }
                    addItems();
                    return;
                }
                Toast.makeText(getContext(), "数据获取失败", Toast.LENGTH_SHORT).show();
                refreshButton.setRefreshing(false);
            }
        });
    }

    private void addItems() {
        taskItemList = new ArrayList<>();
        for (int i = 0; i < requireDatas.size(); i++) {
            RequireGson data = requireDatas.get(i);
            String[] buttonText = null;
            if (state == 1) {
                buttonText = new String[]{"完 成", "拒 绝"};
            } else if (state == 2) {
                buttonText = new String[]{"接 受", "拒 绝"};
            } else if (state == 0) {
                buttonText = new String[]{"接 受", "确 定"};
            }
            TaskItem taskItem = new TaskItem(
                    data.getTitle(),
                    data.getSender(),
                    data.getDate(),
                    data.getIntroduce());
            taskItem.setLeftButtonText(buttonText[0]);
            taskItem.setRightButtonText(buttonText[1]);
            taskItemList.add(taskItem);
        }
        taskItemList.add(new TaskItem(null, null, null, null));
//        adapter.notifyDataSetChanged();
        adapter.setTaskItemList(taskItemList);
        recyclerView.setAdapter(adapter);
        refreshButton.setRefreshing(false);
    }

    private void updateChange(final String text, final RequireGson data, final boolean isRefresh) {
        BmobQuery<RequireGson> query = new BmobQuery<>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(BmobUser.class).getUsername());
        query.addWhereEqualTo("title", data.getTitle());
        query.findObjects(new FindListener<RequireGson>() {
            @Override
            public void done(List<RequireGson> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    data.update(list.get(0).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
                                if(isRefresh) {
                                    addData();
                                }
                            }
                        }
                    });
                    return;
                }
                Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TextView spaceView;//用以添加空白，确保item展开后能完全显示
    private RefreshButton refreshButton;

    private void initView() {
        spaceView = new TextView(getContext());
        spaceView.setHeight(Tool.getTaskItemHeight(getContext()));//这里与RequireItem高度保持一致
        refreshButton = view.findViewById(R.id.refreshButton);
        titleView = view.findViewById(R.id.titleView);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(getActivity());
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutmanager);

        refreshButton.setOnClickListener(this);
        view.findViewById(R.id.menuButton).setOnClickListener(this);
        view.findViewById(R.id.taskAcceptButton).setOnClickListener(this);
        view.findViewById(R.id.taskWaitAcceptButton).setOnClickListener(this);
        view.findViewById(R.id.taskRefusedButton).setOnClickListener(this);

        adapter = new TaskItemAdapter(getContext(), taskItemList);
        adapter.setTaskItemList(taskItemList);
        adapter.setOnItemButtonClickListener(new TaskItemAdapter.OnItemButtonClickListener() {
            @Override
            public void OnItemButtonClick(View v, int position, boolean isLeft) {
                RequireGson data = requireDatas.get(position);
                if (isLeft) {
                    if (state == 0 || state == 2) {
                        data.setState(1);
                        updateChange("已接受此任务", data,false);
                        adapter.removeItem(position);
                    } else if (state == 1) {
                        //TODO:完成任务
                    }
                } else {
                    if (state == 1 || state == 2) {
                        data.setState(0);
                        updateChange("已拒绝此任务", data,false);
                        adapter.removeItem(position);
                    } else if (state == 0) {
                        Toast.makeText(getContext(), "此任务已被拒绝", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    LoadTextView titleView;
    String[] titles = new String[]{"已拒任务", "已接任务", "未接任务"};

    private void itemButtonClickEvent() {
        recyclerView.removeAllViews();
        titleView.setText(titles[state]);
        addData();
        showMenu();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuButton:
                ((MenuButton) view.findViewById(R.id.menuButton)).setIsShow(1);
                showMenu();
                break;
            case R.id.taskAcceptButton:
                state = 1;
                itemButtonClickEvent();
                break;
            case R.id.taskWaitAcceptButton:
                state = 2;
                itemButtonClickEvent();
                break;
            case R.id.taskRefusedButton:
                state = 0;
                itemButtonClickEvent();
                break;
            case R.id.refreshButton:
                recyclerView.removeAllViews();
                addData();
                break;
        }
    }

    private void showMenu() {
        try {
            MenuButton button = view.findViewById(R.id.menuButton);
            final LinearLayout menuLayout = view.findViewById(R.id.menuLayout);
            if (menuLayout.getVisibility() == View.GONE) {
                button.setIsShow(1);
                menuLayout.setVisibility(View.VISIBLE);
                ValueAnimator animator = Tool.createDropAnimator(menuLayout, menuLayout.getLayoutParams(), 0, (int) (140 * Tool.getDensity(getContext()) + 0.5));
                animator.setInterpolator(new OvershootInterpolator());
                animator.start();
            } else {
                button.setIsShow(0);
                int origHeight = menuLayout.getHeight();
                ValueAnimator animator = Tool.createDropAnimator(menuLayout, menuLayout.getLayoutParams(), origHeight, 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        menuLayout.setVisibility(View.GONE);
                    }
                });
                animator.setInterpolator(new AnticipateInterpolator());
                animator.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
