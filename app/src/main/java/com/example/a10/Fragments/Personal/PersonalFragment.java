package com.example.a10.Fragments.Personal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.facedetection.AFD_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.example.a10.BmobManagers.User;
import com.example.a10.BusEvent;
import com.example.a10.LoginActivity;
import com.example.a10.MainActivity;
import com.example.a10.MyView.LoadButton;
import com.example.a10.R;
import com.example.a10.Tool;
import com.example.a10.Utils.BitmapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import Face.FD;
import Face.FR;
import Face.FaceRegist;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class PersonalFragment extends Fragment implements View.OnClickListener {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_personal, null);
            initView();
            EventBus.getDefault().register(this);//注册EventBus
        }
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        Tool.scaleAnimation(view, R.id.linearLayout);
        ((TextView) view.findViewById(R.id.username)).setText(BmobUser.getCurrentUser().getUsername());
        ((TextView) view.findViewById(R.id.userId)).setText(BmobUser.getCurrentUser().getObjectId());
        return view;
    }

    LoadButton regist;

    private void initView() {
        view.findViewById(R.id.logout).setOnClickListener(this);
        regist = view.findViewById(R.id.regist);
        regist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                BmobUser.logOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                BmobIM.getInstance().disConnect();//断开服务器连接
                MainActivity.fragments.clear();
                MainActivity.homeFragment = null;
                MainActivity.requireFragment = null;
                MainActivity.requireFragment = null;
                MainActivity.personalFragment = null;
                getActivity().finish();
                break;
            case R.id.regist:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setCancelable(true);
                dialog.setTitle("选择图片：");
                dialog.setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 0x000);
                    }
                });
                dialog.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(getContext(),"未获得相机权限，无法拍照",Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, 0x000);
                        }else {
                            startActivity(new Intent(getActivity(), CameraActivity.class));
                        }
                    }
                });
                dialog.show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0x000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getActivity(), CameraActivity.class));
                }else {
                    Toast.makeText(getContext(),"未获得相机权限，无法拍照",Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            Bitmap bitmap = BitmapFactory.decodeFile(null);
            switch (requestCode) {
                case 0x000:
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "图像获取异常", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

            bitmap = BitmapUtil.cutBitmap(getContext(), bitmap);//裁剪
            if (bitmap == null) {//判空
                Toast.makeText(getContext(), "图像获取异常", Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] bytes = BitmapUtil.bitmapToNV21Bytes(bitmap);//转格式
            getFDData(bytes, bitmap.getWidth(), bitmap.getHeight());//获取信息
        } else {
            BusEvent busEvent = new BusEvent("Toast");
            busEvent.setText("选择照片失败，请重新尝试");
            EventBus.getDefault().post(busEvent);
            return;
        }
    }

    /* 获取人脸检测信息 */
    public void getFDData(byte[] bytes, int width, int height) {
        FD fd = new FD();//人脸检测
        List<AFD_FSDKFace> fdData = fd.process(bytes, width, height);
        if (fdData.size() == 0) {
            Toast.makeText(getContext(), "未检测到人脸", Toast.LENGTH_SHORT).show();
            return;
        } else if (fdData.size() >= 2) {
            Toast.makeText(getContext(), "检测到多个人脸，请重新录入", Toast.LENGTH_SHORT).show();
            return;
        }
        AFD_FSDKFace fdFace = fdData.get(0);
        FR fr = new FR();
        AFR_FSDKFace frFace = fr.getFace(bytes, width, height, fdFace.getRect(), fdFace.getDegree());
        String username = User.getCurrentUser().getUsername();
        FaceRegist faceRegist = new FaceRegist(username, frFace);
        addData(username, faceRegist);
    }


    private void addData(String username, FaceRegist faceRegist) {
        regist.setLoading(true);
        BmobQuery<FaceData> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<FaceData>() {
            @Override
            public void done(List<FaceData> list, BmobException e) {
                if (e == null) {
                    FaceData data = new FaceData();
                    if (list.size() == 0) {
                        data.setUsername(username);
                        data.setFaceRegist(faceRegist);
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e != null) {
                                    EventBus.getDefault().post(new BusEvent("Toast", e.getMessage()));
                                } else {
                                    EventBus.getDefault().post(new BusEvent("Toast", "注册成功"));
                                }
                                regist.setLoading(false);
                            }
                        });
                        addData(username, faceRegist);
                        return;
                    } else {
                        data.update(list.get(0).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    EventBus.getDefault().post(new BusEvent("Toast", "注册成功"));
                                } else {
                                    EventBus.getDefault().post(new BusEvent("Toast", "注册失败"));
                                    addData(username, faceRegist);
                                }
                                regist.setLoading(false);
                            }
                        });
                    }
                } else {
                    if (e.getErrorCode() == 101) {
                        new FaceData().save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    addData(username, faceRegist);
                                } else {
                                    EventBus.getDefault().post(new BusEvent("Toast", e.getMessage()));
                                    regist.setLoading(false);
                                }
                            }
                        });
                    } else {
                        EventBus.getDefault().post(new BusEvent("Toast", e.getMessage()));
                        regist.setLoading(false);
                    }
                }
            }
        });
    }

    //TODO:
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);//解除注册
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiver(BusEvent busEvent) {
        if(busEvent.getEventName().equals("拍照完成")){
            byte[] data=busEvent.getBytes();
            Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = BitmapUtil.cutBitmap(getContext(),bitmap);//裁剪
            if (bitmap == null) {//判空
                Toast.makeText(getContext(),"图像获取异常",Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] bytes = BitmapUtil.bitmapToNV21Bytes(bitmap);//转格式
            getFDData(bytes, bitmap.getWidth(), bitmap.getHeight());//获取信息
        }
    }
}
