package com.example.minitiktok.model;

import android.content.Context;
import android.util.AttributeSet;

import cn.jzvd.JZVideoPlayerStandard;

public class LoopVideo extends JZVideoPlayerStandard {

    public LoopVideo (Context context) {
        super(context);
    }

    public LoopVideo (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        startVideo();
    }
}
