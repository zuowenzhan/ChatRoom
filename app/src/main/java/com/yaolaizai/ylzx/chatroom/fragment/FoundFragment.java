package com.yaolaizai.ylzx.chatroom.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yaolaizai.ylzx.chatroom.R;

/**
 * Created by ylzx on 2017/6/19.
 */
public class FoundFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_found, container, false);
        return view;
    }
}
