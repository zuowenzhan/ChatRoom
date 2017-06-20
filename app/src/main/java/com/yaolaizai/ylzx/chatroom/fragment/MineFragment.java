package com.yaolaizai.ylzx.chatroom.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.activity.LoginActivity;
import com.yaolaizai.ylzx.chatroom.model.Modle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ylzx on 2017/6/19.
 */
public class MineFragment extends Fragment {

    @BindView(R.id.iv_mine)
    ImageView ivMine;
    @BindView(R.id.rl_mine)
    RelativeLayout rlMine;
    @BindView(R.id.tv_usename)
    TextView tvUseName;
    @BindView(R.id.btn_back)
    Button btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_mine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 获取当前用户名称
        String currentUser = EMClient.getInstance().getCurrentUser();
        tvUseName.setText(currentUser);

    }

    @OnClick({R.id.rl_mine, R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_mine:
                //进入更改头像 和姓名

                break;
            case R.id.btn_back:

                // 登录环信服务器执行退出操作
                Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {

                                // 关闭数据库资源
                                Modle.getInstance().getDbManager().close();
//                                Model.getInstace().getUserAccountDao().close();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 提示
                                        Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_LONG).show();
                                        // 跳转到登录页面
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);

                                        startActivity(intent);
                                        // 结束当前页面
                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, final String s) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 提示
                                        Toast.makeText(getActivity(), "退出失败" + s, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }
                });

                break;
        }
    }
}
