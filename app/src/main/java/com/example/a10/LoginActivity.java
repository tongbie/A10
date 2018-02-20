package com.example.a10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    private String username = "";
    private String password = "";
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ToolClass toolClass = new ToolClass();
        initView();
    }

    /* 添加控件 */
    private void initView() {
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
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
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    /* 判断账号密码是否为空，启动loginRunnable */
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
        switch (v.getId()) {
            case R.id.login://登录
                username = mUsernameView.getText().toString();
                password = mPasswordView.getText().toString();
                new Thread(loginRunnable).start();
        }
    }

    Runnable loginRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                FormBody formBody = new FormBody.Builder()
                        .add("userName", username)
                        .add("password", password)
                        .build();
                Request request = new Request.Builder()
                        //TODO:set URL
                        .url("")
                        .post(formBody)
                        .build();
                Response response = ToolClass.client.newCall(request).execute();
                String responseData = response.body().string();
                if (responseData != null) {
                    if (String.valueOf(response.code()).charAt(0) == '2') {
                        //TODO:登录成功
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        uiError("用户名或密码错误");
                    }
                } else {
                    uiError("网络连接失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                uiToast("出现错误,请重启应用");
            }
        }
    };

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

