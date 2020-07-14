package com.example.minitiktok.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.minitiktok.R;
import com.example.minitiktok.fragment.HomeFragment;
import com.example.minitiktok.fragment.MessageFragment;
import com.example.minitiktok.fragment.PlaceFragment;
import com.example.minitiktok.fragment.ProfileFragment;
import com.example.minitiktok.fragment.UploadFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonHome;
    private List<Fragment> mFragments = new ArrayList<>();
    private static final String TAG = "MainActivity";
    //private Intent get_intent = getIntent();
    //private String userName = get_intent.getStringExtra("user_name");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideExtra();
        loadFragments();
        initBottom();
    }
    //加载实例到集合中 TODO: 实例化方法应该进行修改
    private void loadFragments(){
        mFragments.add(HomeFragment.newInstance("a","0"));
        mFragments.add(PlaceFragment.newInstance("b","1"));
        mFragments.add(UploadFragment.newInstance("c","2"));
        mFragments.add(MessageFragment.newInstance("d","3"));
        mFragments.add(ProfileFragment.newInstance("userName","4")); //如果直接传入字符串是可以运行的
    }
    //底部状态按钮与碎片绑定
    private void initBottom(){
        mRadioGroup = findViewById(R.id.radio_group_buttons);
        mRadioButtonHome = findViewById(R.id.radio_button_home);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            Fragment fragment = null;
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioID) {
                switch (radioID){
                    case R.id.radio_button_home:{
                        fragment = mFragments.get(0);
                        break;
                    }
                    case R.id.radio_button_place:{
                        fragment = mFragments.get(1);
                        break;
                    }
                    case R.id.radio_button_upload:{
                        fragment = mFragments.get(2);
                        break;
                    }
                    case R.id.radio_button_message:{
                        fragment = mFragments.get(3);
                        break;
                    }
                    case R.id.radio_button_profile:{
                        fragment = mFragments.get(4);
                        break;
                    }
                }
                if (null!=fragment){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container,fragment)
                            .commit();
                }
                ((RadioButton)findViewById(radioID)).setTextColor(getResources().getColor(R.color.light_background,null));
            }
        });
        mRadioButtonHome.setChecked(true);
    }

}