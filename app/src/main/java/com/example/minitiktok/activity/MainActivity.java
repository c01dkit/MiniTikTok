package com.example.minitiktok.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.minitiktok.R;
import com.example.minitiktok.api.IMiniDouyinService;
import com.example.minitiktok.fragment.HomeFragment;
import com.example.minitiktok.fragment.MessageFragment;
import com.example.minitiktok.fragment.PlaceFragment;
import com.example.minitiktok.fragment.ProfileFragment;
import com.example.minitiktok.fragment.UploadFragment;
import com.example.minitiktok.model.PostVideoResponse;
import com.example.minitiktok.util.ResourceUtils;

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

public class MainActivity extends BaseActivity {
    private String mSelectedImagePath;
    private String mSelectedVideoPath;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonHome;
    private List<Fragment> mFragments = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private Intent get_intent;
    private String userName;
    private String userID;
    private static final int RECORD = 1280;
    private static final int PICK_IMAGE = 1;
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
        loadFragments();
        initBottom();
    }
    //加载实例到集合中 TODO: 实例化方法应该进行修改
    private void loadFragments(){
        get_intent = getIntent();
        userName = get_intent.getStringExtra("user_name");
        userID = get_intent.getStringExtra("user_id");
        mFragments.add(HomeFragment.newInstance("a","0"));
        mFragments.add(PlaceFragment.newInstance("b","1"));
        mFragments.add(UploadFragment.newInstance("c","2"));
        mFragments.add(MessageFragment.newInstance("d","3"));
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
                        Intent intent = new Intent(MainActivity.this,VideoRecordingActivity.class);
                        startActivityForResult(intent,RECORD);
                        return;
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
                updateRadioColor(radioID);
            }
        });
        mRadioButtonHome.setChecked(true);
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
            }
        }
    }

    private void postVideo() {
        Log.d(TAG, "postVideo: Process check1");
        MultipartBody.Part videoPart = getMultipartFromString("video", mSelectedVideoPath);

        Log.d(TAG, "postVideo: Process check2");
        MultipartBody.Part coverImagePart = getMultipartFromString("cover_image", mSelectedImagePath);

        //TODO 这里的ID和Username换成用户登陆使用的内容
        Log.d(TAG, "postVideo: Process check3");
        miniDouyinService.postVideo(userID, userName, coverImagePart, videoPart).enqueue(
                new Callback<PostVideoResponse>() {
                    @Override
                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                        if (response.body() != null) {
                            Toast.makeText(MainActivity.this, "视频已上传", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    @Override
                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                        Toast.makeText(MainActivity.this,"上传失败", Toast.LENGTH_SHORT).show();
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