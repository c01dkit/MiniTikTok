package com.example.minitiktok.activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.minitiktok.R;
import com.example.minitiktok.api.IMiniDouyinService;
import com.example.minitiktok.fragment.HomeFragment;
import com.example.minitiktok.fragment.MessageFragment;
import com.example.minitiktok.fragment.PlaceFragment;
import com.example.minitiktok.fragment.ProfileFragment;
import com.example.minitiktok.fragment.UploadFragment;
import com.example.minitiktok.model.PostVideoResponse;

public class MainActivity extends BaseActivity {
    private String mSelectedImagePath;
    private String mSelectedVideoPath;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonHome;
    private List<Fragment> mFragments = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private String userName = "visitor";
    private String userID = "001";
    private static final int PICK_IMAGE = 1;
    private static final int LOGIN = 2;
    private static final int RECORD = 3;
    private long lastTime;
    private boolean login = false;
    private final int EXIT_TIME = 2000;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IMiniDouyinService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideExtra();
        permissionRequest(1);
        loadFragments();
        initBottom();
    }
    //加载实例到集合中 TODO: 实例化方法应该进行修改
    private void loadFragments(){
        Intent get_intent = getIntent();
        userName = get_intent.getStringExtra("user_name");
        userID = get_intent.getStringExtra("user_id");
        mFragments.add(HomeFragment.newInstance("a","0"));
        mFragments.add(PlaceFragment.newInstance("b","1"));
        mFragments.add(UploadFragment.newInstance("c","2"));
        mFragments.add(MessageFragment.newInstance(userName,userID));
        mFragments.add(ProfileFragment.newInstance(userName,userID));
    }
    //底部状态按钮与碎片绑定
    private void initBottom(){
        mRadioGroup = findViewById(R.id.radio_group_buttons);
        mRadioButtonHome = findViewById(R.id.radio_button_home);
        ((RadioButton)findViewById(R.id.radio_button_home)).setTextColor(getResources().getColor(R.color.light_background));
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
                        ((RadioButton)findViewById(R.id.radio_button_upload)).setChecked(false);
                        Intent intent = new Intent(MainActivity.this,VideoRecordingActivity.class);
                        startActivityForResult(intent,RECORD);
                        return;
                    }
                    case R.id.radio_button_message:{
                        if (login) {
                            fragment = mFragments.get(3);
                        } else {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent,LOGIN);
                            ((RadioButton)findViewById(R.id.radio_button_message)).setChecked(false);
                            return;
                        }
                        break;
                    }
                    case R.id.radio_button_profile:{
                        if (login) {
                            fragment = mFragments.get(4);
                        } else {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent,LOGIN);
                            ((RadioButton)findViewById(R.id.radio_button_profile)).setChecked(false);
                            return;
                        }
                        break;
                    }
                }
                if (null!=fragment){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_container,fragment)
                            .commit();
                }
                updateRadioColor(radioID);
            }
        });
        mRadioButtonHome.setChecked(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container,mFragments.get(0))
                .commit();
    }

    private void updateRadioColor(int radioID) {
        ((RadioButton)findViewById(R.id.radio_button_home)).setTextColor(getResources().getColor(R.color.dark_background));
        ((RadioButton)findViewById(R.id.radio_button_message)).setTextColor(getResources().getColor(R.color.dark_background));
        ((RadioButton)findViewById(R.id.radio_button_place)).setTextColor(getResources().getColor(R.color.dark_background));
        ((RadioButton)findViewById(R.id.radio_button_profile)).setTextColor(getResources().getColor(R.color.dark_background));
        ((RadioButton)findViewById(radioID)).setTextColor(getResources().getColor(R.color.light_background));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode  = "+requestCode+" resultCode = "+resultCode);
        if(null == data || RESULT_OK != resultCode) return;
        switch (requestCode){
            case RECORD:{
                mSelectedVideoPath = data.getStringExtra(getString(R.string.video_path));
                Toast.makeText(MainActivity.this,"请拍摄封面图",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,TakePhotoActivity.class);
                startActivityForResult(intent,PICK_IMAGE);
                break;
            }
            case PICK_IMAGE:{
                mSelectedImagePath = data.getStringExtra(getString(R.string.photo_path));
                Log.d(TAG, "selectedImage = " + mSelectedImagePath);
                Log.d(TAG, "selectedVideo = " + mSelectedVideoPath);
                //上传
                if (mSelectedVideoPath != null && mSelectedImagePath != null) {
                    postVideo();
                } else {
                    throw new IllegalArgumentException("error data uri, mSelectedVideo = "
                            + mSelectedVideoPath
                            + ", mSelectedImage = "
                            + mSelectedImagePath);
                }
                break;
            }
            case LOGIN:{
                userID = data.getStringExtra(getString(R.string.userId));
                userName = data.getStringExtra(getString(R.string.username));
//                重新初始化后两个fragment
                mFragments.remove(mFragments.size()-1);
                mFragments.remove(mFragments.size()-1);
                mFragments.add(MessageFragment.newInstance(userName,userID));
                mFragments.add(ProfileFragment.newInstance(userName,userID));
                Log.d(TAG, "onActivityResult: Set login = true");
                login = true;
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //双击返回退出App
        if (System.currentTimeMillis() - lastTime > EXIT_TIME) {
            if (mRadioGroup.getCheckedRadioButtonId() != R.id.radio_button_home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container,mFragments.get(0))
                        .commit();
                mRadioGroup.check(R.id.radio_button_home);
                updateRadioColor(R.id.radio_button_home);
            }else{
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                lastTime = System.currentTimeMillis();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void postVideo() {
        MultipartBody.Part videoPart = getMultipartFromString("video", mSelectedVideoPath);

        MultipartBody.Part coverImagePart = getMultipartFromString("cover_image", mSelectedImagePath);

        Toast.makeText(MainActivity.this,"视频正在上传中，请耐心等待……",Toast.LENGTH_SHORT).show();
        miniDouyinService.postVideo(userID, userName, coverImagePart, videoPart).enqueue(
                new Callback<PostVideoResponse>() {
                    @Override
                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                        if (response.body() != null) {
                            Toast.makeText(MainActivity.this, "视频已上传", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: Upload video");
                        }
                    }
                    @Override
                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                        Toast.makeText(MainActivity.this,"上传失败", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: Upload Failed");
                    }
                });
    }

    private MultipartBody.Part getMultipartFromString(String name, String path) {
        File f = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        Log.d(TAG, "getMultipartFromUri: Process check3");
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

}