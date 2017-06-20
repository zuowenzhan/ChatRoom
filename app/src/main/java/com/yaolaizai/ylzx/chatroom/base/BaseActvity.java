package com.yaolaizai.ylzx.chatroom.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.utils.StatusBarCompat;

import butterknife.ButterKnife;

/**
 * Created by ylzx on 2017/6/16.
 */
public abstract class BaseActvity extends FragmentActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        ButterKnife.bind(this);

        //统一  状态栏  和  项目的颜色
        if (Build.VERSION.SDK_INT>=21) {

            StatusBarCompat.compat(this);
            StatusBarCompat.compat(this, getResources().getColor(R.color.lanse));
        }

        initData();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }



    public   abstract  void initView();

    public   abstract  void initData();
}
