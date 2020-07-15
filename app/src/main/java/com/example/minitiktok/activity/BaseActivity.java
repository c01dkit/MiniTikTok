package com.example.minitiktok.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * 此类为所有活动的父类，提供公共方法
 */
public class BaseActivity extends AppCompatActivity {

    public void hideExtra(){
        // 隐藏标题栏
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar!=null) {
            mActionBar.hide();
        }
        // 隐藏手机顶部状态栏
//        Window mWindow = getWindow();
//        if (mWindow!=null) {
//            mWindow.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
    }

    public void permissionRequest(int PERMISSION_REQUEST_CAMERA_PATH_CODE){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getApplicationContext(), "请授予全部权限！", Toast.LENGTH_SHORT).show();
            String[] permissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permissions, PERMISSION_REQUEST_CAMERA_PATH_CODE);

        }
    }
}
