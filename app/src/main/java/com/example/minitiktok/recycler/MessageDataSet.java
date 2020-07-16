package com.example.minitiktok.recycler;

import com.example.minitiktok.R;

import java.util.ArrayList;
import java.util.List;


public class MessageDataSet {

    public static List<MessageData> getData() {
        List<MessageData> result = new ArrayList();
        result.add(new MessageData("贾玲", "上火的陈芊芊[图片]", "17:28", R.drawable.jialing));
        result.add(new MessageData("杨迪", "我没有偷题！", "9:15", R.drawable.yangdi));
        result.add(new MessageData("郎朗", "媳妇真可爱", "10:45", R.drawable.langlang));
        result.add(new MessageData("范丞丞", "大白鹅就是我[直播]", "10:26", R.drawable.fcc));
        result.add(new MessageData("周深", "转发[视频]一起来唱达拉崩吧", "15:36", R.drawable.zhoushen));
        result.add(new MessageData("吉娜","什么玩意儿！", "10:40", R.drawable.jina));
        result.add(new MessageData("丁禹兮", "第一次上综艺好紧张", "11:51", R.drawable.dyx));
        result.add(new MessageData("沙溢", "我在隔壁录跑男", "20:57", R.drawable.shayi));
        result.add(new MessageData("沈腾", "我在隔壁录王牌", "19:18", R.drawable.shenteng));
        result.add(new MessageData("陌生人", "[Hi]", "12:04", R.drawable.ic_launcher_foreground));
        return result;
    }

}