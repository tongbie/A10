package com.example.a10.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10.Fragments.Home.HomeGson;
import com.example.a10.MainActivity;
import com.example.a10.R;
import com.example.a10.ToolClass;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    public static String username = "";
    public static String password = "";
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new ToolClass(getApplicationContext());
        Bmob.initialize(this, "c2c1321b56eef48e75db1371f8153b80");
        skipLogin();
        initView();
        mUsernameView.setText("bietong");
        mPasswordView.setText("12345678");
    }

    private void skipLogin() {
        BmobUser user = BmobUser.getCurrentUser();
        if (null == user) {
            return;
        } else {
            username = user.getUsername();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

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
        }
    }

    private void regist() {
        final BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void login() {
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("登录错误信息", e.getMessage());
                }
            }
        });
    }

    /* 用于在线程中显示Toast */
    private void uiToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* 用于在线程中显示文本框的错误信息 */
    private void uiError(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mUsernameView.setError(text);
            }
        });
    }
}

