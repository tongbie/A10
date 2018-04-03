package com.example.a10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        new Tool(getApplicationContext());
        requestPermission();
    }

    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 0x000);
        }else {
            if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0x001);
            }else {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0x000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0x001);
                    }else {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                        finish();
                    }
                }else {
                    Toast.makeText(WelcomeActivity.this,"未获得电话权限，程序无法正常使用",Toast.LENGTH_LONG).show();
                    finish();
                }
            break;
            case 0x001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }else {
                    Toast.makeText(WelcomeActivity.this,"未获得存储权限，程序无法正常使用",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }
}
