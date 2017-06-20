package com.yaolaizai.ylzx.chatroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMClient;
import com.yaolaizai.ylzx.chatroom.MainActivity;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.base.BaseActvity;
import com.yaolaizai.ylzx.chatroom.model.Modle;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActvity {

    @BindView(R.id.ll_splash)
    RelativeLayout llSplash;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            // 判断是进入登录页面还是主页面
            toMainOrLogin();
        }
    };
    @Override
    public void initView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void initData() {

        //渐变动画
        AlphaAnimation animation = new AlphaAnimation(1f, 1.0f);
        animation.setDuration(1500);
        llSplash.startAnimation(animation);
        // 发送延时消息
        handler.sendMessageDelayed(Message.obtain(), 2000);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    // 判断是进入登录页面还是主页面
    private void toMainOrLogin() {

        Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去环信服务器获取当前用户是否处于登录状态
//                boolean islogin = SharedPreferencesUtil.getBoolean(SplashActivity.this, "islogin", false);
                //EMClient.getInstance().isLoggedInBefore()
                if (EMClient.getInstance().isLoggedInBefore() ) {
                    // 获取当前用户账号信息
                    UserInfo account = Modle.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
                    // 登录后处理
                    Modle.getInstance().loginSuccess(account);

                    // 跳转到主页面
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                    startActivity(intent);
                } else {
                    // 跳转到登录页面
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                    startActivity(intent);
                }

                // 结束当前页
                finish();
            }
        });
    }
}
