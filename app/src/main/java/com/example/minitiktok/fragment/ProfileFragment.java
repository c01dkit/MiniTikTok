package com.example.minitiktok.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.minitiktok.R;
import com.example.minitiktok.activity.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String userName;
    private String userID;

    public ProfileFragment() {
        super();
    }

    public ProfileFragment(String userName, String userID) {
        super();
        this.userName = userName;
        this.userID = userID;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        v.findViewById(R.id.b_exit).setOnClickListener(view -> new AlertDialog.Builder(getActivity())
                .setTitle("警告")
                .setMessage("确认要退出吗？")
                .setNegativeButton("取消", (dialogInterface, i) -> {
                   //Do nothing
                })
                .setPositiveButton("确认", (dialogInterface, i) -> {
                   //(LoginActivity) getActivity())
                })
                .create()
                .show());
        ((TextView)v.findViewById(R.id.tv_username)).setText(userName);
        /*getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fl_placeholder, new DiscoverFragment(userName))
                .commit();*/
        return v;
    }
}