package com.example.minitiktok.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.minitiktok.R;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.slide);
            getWindow().setEnterTransition(slide);
        }

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.98f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.activity_login);
        hideExtra();
//        permissionRequest(1);
        findViewById(R.id.ib_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.fab_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = ((EditText)findViewById(R.id.et_name)).getText().toString();
                String userID = ((EditText)findViewById(R.id.et_id)).getText().toString();
                Intent i = getIntent();
                i.putExtra(getString(R.string.username), userName);
                i.putExtra(getString(R.string.userId), userID);
                if (userID.isEmpty() || userName.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "登录失败：用户名或学号不能为空", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED,i);
                } else {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK,i);
                }
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }
}
