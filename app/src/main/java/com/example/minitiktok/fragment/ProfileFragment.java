package com.example.minitiktok.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.example.minitiktok.R;
import com.example.minitiktok.activity.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private List<Fragment> list = new ArrayList<>();
    private String[] title = {"对话", "通知", "好友"};
    private ImageView imageView;
    private static final String url = "https://uploadbeta.com/api/pictures/random/";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String userName;
    private String userID;

    private static final String TAG = "ProfileFragment";
    public ProfileFragment() {
        super();
    }

    public ProfileFragment(String userName, String userID) {
        super();
        this.userName = userName;
        this.userID = userID;
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment(param1, param2);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout myTabLayout = view.findViewById(R.id.my_tab_layout);
        ViewPager2 myViewPager = view.findViewById(R.id.my_view_pager);
        imageView = view.findViewById(R.id.imageView);
        TextView tv_userName = view.findViewById(R.id.user_name);
        TextView tv_userID = view.findViewById(R.id.user_id);
        String string = "用户：" + userName;
        tv_userName.setText(string);
        string = "抖音号：" + userID;
        tv_userID.setText(string);
        list.add(MyFragment.newInstance(title[0], "您尚未发布作品~"));
        list.add(MyFragment.newInstance(title[1], "暂时没有通知消息哦"));
        list.add(MyFragment.newInstance(title[2], "快去关注别人吧XD"));

        loadImage();
        myViewPager.setAdapter(new MyPagerAdapter(getActivity()));

        new TabLayoutMediator(myTabLayout, myViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(title[position]);
            }
        }).attach();
    }

    public class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        @Override
        public Fragment createFragment(int position) {
            return list.get(position);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }



    private void loadImage(){
        Glide.with(getActivity())
                .load(url)
                .skipMemoryCache(true)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不在disk硬盘缓存
                .into(imageView);
    }

}