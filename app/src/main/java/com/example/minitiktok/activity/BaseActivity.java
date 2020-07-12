package com.example.minitiktok.activity;

import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
}
