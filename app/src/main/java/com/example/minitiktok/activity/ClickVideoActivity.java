package com.example.minitiktok.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.example.minitiktok.model.Video;
import com.example.minitiktok.model.LoopVideo;
import com.example.minitiktok.R;

import cn.jzvd.JZVideoPlayerStandard;
import static cn.jzvd.JZVideoPlayer.CURRENT_STATE_NORMAL;

public class ClickVideoActivity extends AppCompatActivity {

    public static void launch(Activity activity, Video video, View animView){
        Intent i = new Intent(activity, ClickVideoActivity.class);
        i.putExtra("image_url",  video.imageUrl);
        i.putExtra("video_url", video.videoUrl);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(i);
        } else {
            activity.startActivity(i,
                    ActivityOptions.makeSceneTransitionAnimation(activity, animView, "shareVideo").toBundle());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_video);
        LoopVideo mVideoPlayer = findViewById(R.id.click_pop_video);
        String imageUrl = getIntent().getStringExtra("image_url");
        String videoUrl = getIntent().getStringExtra("video_url");

        mVideoPlayer.setUp(videoUrl,JZVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN | CURRENT_STATE_NORMAL );
        mVideoPlayer.startVideo();

        GlideBuilder builder = new GlideBuilder(this);
        int diskSizeInBytes = 1024 * 1024 * 100;
        int memorySizeInBytes = 1024 * 1024 * 60;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(this, diskSizeInBytes));
        builder.setMemoryCache(new LruResourceCache(memorySizeInBytes));
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mVideoPlayer.thumbImageView);
        mVideoPlayer.thumbImageView.setVisibility(View.VISIBLE);

        initBtns();
    }

    private boolean like = false;

    private void initBtns() {
        ImageButton imageButton = findViewById(R.id.b_like);
        imageButton.setOnClickListener(v -> {
            ImageButton button = (ImageButton)v;
            like = !like;
            button.setImageDrawable(getResources().getDrawable(like ? R.drawable.ic_heart : R.drawable.ic_like));
            if (like) {
                ScaleAnimation animation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(200);
                animation.setRepeatCount(1);
                animation.setRepeatMode(Animation.REVERSE);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        final ImageView heart = findViewById(R.id.iv_like);
                        heart.setVisibility(View.VISIBLE);
                        Animation anim = AnimationUtils.loadAnimation(ClickVideoActivity.this, R.anim.like_anim);
                        heart.startAnimation(anim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                button.startAnimation(animation);
            }
        });
    }

    // 按home键，回到app后在原位置播放
    @Override
    protected void onResume() {
        super.onResume();
        //home back
        JZVideoPlayerStandard.goOnPlayOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.goOnPlayOnPause();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
