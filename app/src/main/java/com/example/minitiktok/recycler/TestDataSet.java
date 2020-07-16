package com.example.minitiktok.recycler;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minitiktok.R;

import java.util.ArrayList;
import java.util.List;


public class TestDataSet {

    public static List<TestData> getData() {
        List<TestData> result = new ArrayList();
        result.add(new TestData("隔壁百度","8:37","30k干不干？", R.drawable.baidu1));
        result.add(new TestData("小脑斧","2020-7-7","在？借点钱？",R.drawable.laohu));
        result.add(new TestData("兔叽","2020-7-5","我先出门了，有事晚上说",R.drawable.baitu));
        result.add(new TestData("智能bot1号","2020-7-5","欢迎使用人工智能1号，呱！",R.drawable.qingwa));
        result.add(new TestData("老板","2020-7-3","周末加班",R.drawable.bytedance3));
        result.add(new TestData("路人甲","2020-7-2","路口开了一家新店",R.drawable.hashiqi));
        result.add(new TestData("数据库系统","2020-7-1","下周验收大作业",R.drawable.jiakechong));
        result.add(new TestData("一只泰迪","2020-7-1","上网了上网了",R.drawable.shizi));
        result.add(new TestData("铲屎官vip","2020-7-1","上次的猫砂还有吗？",R.drawable.mao));
        result.add(new TestData("熊猫协会会长","2020-7-1","这个月是爱熊猫月，每人要交200块钱",R.drawable.xiongmao));
        return result;
    }

}