package com.example.a10.Fragments.Require;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10.MyView.LoadTextView;
import com.example.a10.MyView.MenuButton;
import com.example.a10.MyView.RefreshButton;
import com.example.a10.R;
import com.example.a10.Tool;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class RequireFragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout listLayout;

    List<RequireGson> requireDatas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_require, null);
            initView();
            addData();
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        Tool.translateAnimation(view, R.id.listLayout);
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
                    if(datas.size()==0){
                        toast("任务列表为空");
                    }
                    for (RequireGson data : datas) {
                        requireDatas.add(data);
                    }
                    addItems();
                } else {
                    toast(e.getMessage());
                }
                refreshButton.setRefreshing(false);
            }
        });
    }

    private void addItems() {
        listLayout.removeAllViews();
        for (int i=0;i<requireDatas.size();i++) {
            RequireGson data=requireDatas.get(i);
            int finalI=i;
            if (state == 1) {
                listLayout.addView(new AcceptedItem(getContext(),
                        data.getTitle(),
                        data.getDate(),
                        data.getSender(),
                        data.getIntroduce()) {
                    @Override
                    public void complete() {

                    }

                    @Override
                    public void refuse() {
                        requireDatas.get(finalI).setState(0);
                        save("已拒绝此任务", data);
                    }
                });
            } else if (state == 2) {
                listLayout.addView(new WaitAcceptItem(getContext(),
                        data.getTitle(),
                        data.getDate(),
                        data.getSender(),
                        data.getIntroduce()) {
                    @Override
                    public void accept() {
                        requireDatas.get(finalI).setState(1);
                        save("已接受此任务", data);
                    }

                    @Override
                    public void refuse() {
                        requireDatas.get(finalI).setState(0);
                        save("已拒绝此任务", data);
                    }
                });
            }else if(state==0){
                listLayout.addView(new WaitAcceptItem(getContext(),
                        data.getTitle(),
                        data.getDate(),
                        data.getSender(),
                        data.getIntroduce()) {
                    @Override
                    public void accept() {
                        requireDatas.get(finalI).setState(1);
                        save("已接受此任务", data);
                    }

                    @Override
                    public void refuse() {
                        toast("该任务已被拒绝");
                    }
                });
            }
        }
        listLayout.addView(spaceView);
    }

    private void save(String text, RequireGson data) {
        BmobQuery<RequireGson> query = new BmobQuery<>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(BmobUser.class).getUsername());
        query.addWhereEqualTo("title", data.getTitle());
        query.findObjects(new FindListener<RequireGson>() {
            @Override
            public void done(List<RequireGson> list, BmobException e) {
                if (e == null) {
                    data.update(list.get(0).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                toast(text);
                                addData();
                            } else {
                                toast(e.getMessage());
                            }
                        }
                    });
                } else {
                    toast(e.getMessage());
                }
            }
        });
    }

    private TextView spaceView;//用以添加空白，确保item展开后能完全显示
    private RefreshButton refreshButton;

    private void initView() {
        spaceView = new TextView(getContext());
        spaceView.setHeight(Tool.requireItemHeight);//这里与RequireItem高度保持一致

        listLayout = view.findViewById(R.id.listLayout);
        refreshButton = view.findViewById(R.id.refreshButton);
        titleView=view.findViewById(R.id.titleView);

        refreshButton.setOnClickListener(this);
        view.findViewById(R.id.menuButton).setOnClickListener(this);
        view.findViewById(R.id.acceptButtion).setOnClickListener(this);
        view.findViewById(R.id.waitAcceptButton).setOnClickListener(this);
        view.findViewById(R.id.refusedButton).setOnClickListener(this);
    }

    LoadTextView titleView;
    String[] titles=new String[]{"已拒任务","已接任务","未接任务"};

    private void clickEvent(){
        listLayout.removeAllViews();
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
            case R.id.acceptButtion:
                state = 1;
                clickEvent();
                break;
            case R.id.waitAcceptButton:
                state = 2;
                clickEvent();
                break;
            case R.id.refusedButton:
                state = 0;
                clickEvent();
                break;
            case R.id.refreshButton:
                listLayout.removeAllViews();
                addData();
                break;
        }
    }

    private void showMenu() {
        try {
            MenuButton button = view.findViewById(R.id.menuButton);
            LinearLayout menuLayout = view.findViewById(R.id.menuLayout);
            if (menuLayout.getVisibility() == View.GONE) {
                button.setIsShow(1);
                menuLayout.setVisibility(View.VISIBLE);
                ValueAnimator animator = Tool.createDropAnimator(menuLayout, menuLayout.getLayoutParams(), 0, (int) (140 * Tool.mDensity + 0.5));
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

    private void toast(String text) {
        try {
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
