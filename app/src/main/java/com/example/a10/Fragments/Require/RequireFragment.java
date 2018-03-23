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
import com.example.a10.ToolClass;

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
    List<RequireGson> requireData = new ArrayList<>();

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
        requireData =null;
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
                    requireData = list.get(0).getRequireGsons();
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
        List<RequireGson> requireGsons = new ArrayList<>();
        RequireGson requireGson = new RequireGson();
        requireGson.setTitle("智能外包管理平台");
        requireGson.setSender("虹软集团");
        requireGson.setDate("2018-1-10至2018-4-2");
        requireGson.setIntroduce("    人员管理 \n" +
                "系统需要对这些人员提供下面的管理功能： \n" +
                "（1）\t外包人员信息登记； \n" +
                "（2）\t外包人员各项保密协议，合同签订情况登记； \n" +
                "（3）\t人员信息的查询和维护。 \n" +
                "人员考勤 \n" +
                "我们希望系统能够提供下面的功能： \n" +
                "（1）\t每个外包人员的开始工作时间，结束工作时间被记录； \n" +
                "（2）\t希望可以很方便的知道在一个时间区间内，我们外包的各项工作的大致完成情况； \n" +
                "（3）\t在考勤时，系统需要对当前的用户进行必要的身份验证。 \n" +
                "任务管理 \n" +
                "我们希望系统能够可以外包任务管理方面，提供下面的功能： \n" +
                "（1）\t可以以项目的方式被管理，发包人员可以制定项目实施计划，指定参与人员，确定项目安全等级。 \n" +
                "（2）\t接包人员可以登录到系统，通过人员识别验证后，查看分配到自己的任务，选择任务，开始工作。 \n" +
                "（3）\t在任务完成之后，提交工作成果。 \n" +
                "（4）\t任务的发包人员可以在系统上看到各个分发出去的任务状态，及时跟进各个任务的完成情况。 \n" +
                "安全平台 \n" +
                "虹软公司是一家高科技公司，对于工作内容具有严格的保密级别要求，任何资源的访问都需要具有相应的授权，高级别的安全仅能供指定的人在指定的时间，指定的地点（比如：可通过网络 IP 段限定方式）才能访问。我们希望平台包含安全检测功能，在： \n" +
                "（1）\t接包人员在登录系统开始工作时，安全平台自动启动，在后台不定时的检测当前的用户是否为授权的用户。 \n" +
                "（2）\t在登录时检查登录人员是否与人脸特征库中的人脸信息相匹配。如果不匹配就不能登录系统。 \n" +
                "（3）\t在访问高安全等级的资源和任务时，需要在访问期间没有第三方人脸（也就是除了指定的操作人脸外，不允许有第二个人脸）的介入，如果有，就暂停资源的显示。  \n");
        requireGsons.add(requireGson);
        requireGsons.add(requireGson);
        requireGsons.add(requireGson);
        requireGsons.add(requireGson);
        requireGsons.add(requireGson);
        requireGsons.add(requireGson);
        requireGsons.add(requireGson);
        requireGsons.add(requireGson);
        requireGsons.add(requireGson);
        data.setRequireGsons(requireGsons);
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
        spaceView.setHeight(ToolClass.SCREEN_HEIGHT - ToolClass.px(240));
    }

    private void initItem() {
        rootView.removeAllViews();
        for (RequireGson rg : requireData) {
            rootView.addView(new RequireItem(getContext(), rg.getTitle(), rg.getSender(), rg.getDate(), rg.getIntroduce()));
        }
        rootView.addView(spaceView);
    }

    public void initAnimation() {
        try {
            TranslateAnimation ta = new TranslateAnimation(0, 0, ToolClass.dp(-50), 0);
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

    @Override
    public void onClick(View v) {
//        save("");
        switch (v.getId()){
            case R.id.refresh:
                addData();
                break;
        }
    }
}
