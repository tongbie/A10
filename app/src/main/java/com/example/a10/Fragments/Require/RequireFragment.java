package com.example.a10.Fragments.Require;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10.MyView.MyTextView;
import com.example.a10.R;
import com.example.a10.Tool;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class RequireFragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout rootView;
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
        initAnimation();
        return view;
    }

    private void addData() {
        requireDatas =null;
        ((MyTextView) view.findViewById(R.id.title)).setLoading(true);
        BmobQuery<RequireGsons> query = new BmobQuery<RequireGsons>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(BmobUser.class).getUsername());
        query.findObjects(new FindListener<RequireGsons>() {
            @Override
            public void done(List<RequireGsons> list, BmobException e) {
                if (e == null) {
                    RequireGsons data = new RequireGsons();
                    if (list.size() == 0) {
                        data.setUsername(BmobUser.getCurrentUser(BmobUser.class).getUsername());
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null) {
                                    toast(e.getMessage());
                                }
                            }
                        });
                        addData();
                        return;
                    }
                    requireDatas = list.get(0).getRequireGsons();
                    initItem();
                } else {
                    if (e.getErrorCode() == 101) {
                        new RequireGsons().save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
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
                ((MyTextView) view.findViewById(R.id.title)).setLoading(false);
            }
        });
    }

    private void save(String text) {
        final RequireGsons data = new RequireGsons();
        data.setRequireGsons(requireDatas);
        data.setUsername(BmobUser.getCurrentUser().getUsername());
        BmobQuery<RequireGsons> query = new BmobQuery<RequireGsons>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(BmobUser.class).getUsername());
        query.findObjects(new FindListener<RequireGsons>() {
            @Override
            public void done(List<RequireGsons> list, BmobException e) {
                if (e == null) {
                    data.update(list.get(0).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                toast(text);
                            } else {
                                toast("保存失败" + e.getMessage());
                                addData();
                            }
                        }
                    });
                } else {
                    toast("查找用户失败" + e.getMessage());
                }
            }
        });
    }

    private TextView spaceView;

    private void initView() {
        rootView = view.findViewById(R.id.root);
        view.findViewById(R.id.refresh).setOnClickListener(this);

        spaceView = new TextView(getContext());
        spaceView.setHeight(Tool.SCREEN_HEIGHT - Tool.px(240));
    }

    private void initItem() {
        rootView.removeAllViews();
        for (RequireGson rg : requireDatas) {
            RequireItem requireItem= new RequireItem(getContext(), rg.getTitle(), rg.getSender(), rg.getDate(), rg.getIntroduce()) {
                @Override
                public void complete() {

                }

                @Override
                public void refuse() {
                    requireDatas.remove(rg);
                    save("已移除该任务");
                    //TODO:需要手动刷新才能从列表中移除
                }
            };
            rootView.addView(requireItem);
        }
        rootView.addView(spaceView);
    }

    @Override
    public void onClick(View v) {
//        save("");
        switch (v.getId()){
            case R.id.refresh:
                addData();
                break;
        }
    }

    public void initAnimation() {
        try {
            TranslateAnimation ta = new TranslateAnimation(0, 0, Tool.dp(-50), 0);
            ta.setInterpolator(new OvershootInterpolator());
            ta.setDuration(200);
            LayoutAnimationController lac = new LayoutAnimationController(ta, 0.3f);
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            rootView.setLayoutAnimation(lac);
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
