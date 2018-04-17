package com.example.a10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.facedetection.AFD_FSDKFace;
import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.example.a10.BmobManagers.User;
import com.example.a10.Fragments.Personal.CameraActivity;
import com.example.a10.Fragments.Personal.FaceData;
import com.example.a10.MyView.LoadButton;
import com.example.a10.Utils.BitmapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import Face.FD;
import Face.FR;
import Face.FaceRegist;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private String username = "";
    private String password = "";
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        skipLogin();
        initView();
        mUsernameView.setText("别同");
        mPasswordView.setText("12345678");
        EventBus.getDefault().register(this);//注册EventBus
    }

    private void skipLogin() {
        User user = User.getCurrentUser(User.class);
        if (null == user) {
            return;
        } else {
            username = user.getUsername();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    LoadButton faceLogin;

    private void initView() {
        mUsernameView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.regist).setOnClickListener(this);
        faceLogin = findViewById(R.id.faceLogin);
        faceLogin.setOnClickListener(this);
    }

    private void attemptLogin() {
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        username = mUsernameView.getText().toString();
        password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (username.isEmpty()) {
            mUsernameView.setError("用户名不能为空");
            focusView = mUsernameView;
            cancel = true;
        }
        if (password.isEmpty()) {
            mPasswordView.setError("密码不能为空");
            if (cancel == false) {
                focusView = mPasswordView;
                cancel = true;
            }
        }
        if (cancel) {
            focusView.requestFocus();
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        username = mUsernameView.getText().toString();
        password = mPasswordView.getText().toString();
        switch (v.getId()) {
            case R.id.regist:
                regist();
                break;
            case R.id.login:
                login();
                break;
            case R.id.faceLogin:
                startActivity(new Intent(LoginActivity.this, CameraActivity.class));
                break;
        }
    }

    private void regist() {
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAvatar(username);
        user.signUp(new SaveListener<User>() {

            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        BmobIM.getInstance().disConnect();
        user.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiver(BusEvent busEvent) {
        if(busEvent.getEventName().equals("拍照完成")){
            byte[] data=busEvent.getBytes();
            Bitmap bitmap= BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = BitmapUtil.cutBitmap(getApplicationContext(),bitmap);//裁剪
            if (bitmap == null) {//判空
                Toast.makeText(getApplicationContext(),"图像获取异常",Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] bytes = BitmapUtil.bitmapToNV21Bytes(bitmap);//转格式
            getFDData(bytes, bitmap.getWidth(), bitmap.getHeight());//获取信息
        }else if(busEvent.getEventName().equals("LoginToast")){
            Toast.makeText(LoginActivity.this,busEvent.getText(),Toast.LENGTH_SHORT).show();
        }
    }

    /* 获取人脸检测信息 */
    public void getFDData(byte[] bytes, int width, int height) {
        FD fd = new FD();//人脸检测
        List<AFD_FSDKFace> fdData = fd.process(bytes, width, height);
        if (fdData.size() == 0) {
            Toast.makeText(LoginActivity.this, "未检测到人脸", Toast.LENGTH_SHORT).show();
            return;
        } else if (fdData.size() >= 2) {
            Toast.makeText(LoginActivity.this, "检测到多个人脸，请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        AFD_FSDKFace fdFace = fdData.get(0);
        FR fr = new FR();
        AFR_FSDKFace frFace = fr.getFace(bytes, width, height, fdFace.getRect(), fdFace.getDegree());
        String username = mUsernameView.getText().toString();
        FaceRegist faceRegist = new FaceRegist(username, frFace);
        faceLogin(username,faceRegist);
    }

    private FaceRegist faceRegist;

    private void faceLogin(String username,FaceRegist face1) {
        faceLogin.setLoading(true);
        BmobQuery<FaceData> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        query.findObjects(new FindListener<FaceData>() {
            @Override
            public void done(List<FaceData> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        EventBus.getDefault().post(new BusEvent("LoginToast","未注册人脸"));
                        faceLogin.setLoading(false);
                        return;
                    } else {
                        faceRegist = list.get(0).getFaceRegist();
                        FR fr=new FR();
                        float score=fr.getSimilarity(faceRegist.getFace(),face1.getFace());
                        Log.e("face2",faceRegist.getFace().toString());
                        if(score>=0.6f){
                            Log.e("SCORE", String.valueOf(score));
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }else {
                            Log.e("SCORE", String.valueOf(score));
                            EventBus.getDefault().post(new BusEvent("LoginToast","人脸不匹配"));
                            faceLogin.setLoading(false);
                            return;
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    faceLogin.setLoading(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除注册
    }
}

