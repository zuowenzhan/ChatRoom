package com.yaolaizai.ylzx.chatroom.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yaolaizai.ylzx.chatroom.MainActivity;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.base.BaseActvity;
import com.yaolaizai.ylzx.chatroom.model.Modle;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;
import com.yaolaizai.ylzx.chatroom.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ylzx on 2017/6/16.
 */
public class LoginActivity extends BaseActvity {


    @BindView(R.id.et_login_name)
    EditText etLoginName;
    @BindView(R.id.tl_name)
    android.support.design.widget.TextInputLayout tlName;
    @BindView(R.id.login_pwd)
    EditText loginPwd;
    @BindView(R.id.tl_pwd)
    android.support.design.widget.TextInputLayout tlPwd;
    @BindView(R.id.login_login)
    Button loginLogin;
    @BindView(R.id.login_regist)
    TextView loginRegist;

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_login, R.id.login_regist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login:

                login();

                break;
            case R.id.login_regist:

                regist();

                break;
        }
    }

    // 登录
    private void login() {
        // 获取用户名和密码
        final String loginName = etLoginName.getText().toString();
        final String loginPd = loginPwd.getText().toString();

        // 校验
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPd)) {
            // 提示
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            // 返回
            return;
        }


//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        finish();


        // 去服务器登录
        // 进度条
        final ProgressDialog pb = new ProgressDialog(LoginActivity.this);
        pb.setMessage("正在登录中");
        pb.show();

        // 访问网络
        Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                EMClient.getInstance().login(loginName, loginPd, new EMCallBack() {
                    @Override
                    public void onSuccess() {

                        Modle.getInstance().loginSuccess(new UserInfo(loginName));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 进度条隐藏
                                pb.cancel();
                                // 数据库保存
                                Modle.getInstance().getUserAccountDao().addAccount(new UserInfo(loginName));

                                // 提示
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                 SharedPreferencesUtil.saveBoolean(LoginActivity.this, "islogin", true);

                                // 登录成功 跳转到主页面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                startActivity(intent);
                                // 销毁当前页面
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 失败处理
                                pb.cancel();

                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

    }

    // 注册
    private void regist() {
        // 获取用户名和密码
        final String registName = etLoginName.getText().toString();
        final String registPwd = loginPwd.getText().toString();

        // 校验
        if (TextUtils.isEmpty(registName) || TextUtils.isEmpty(registPwd)) {
            // 提示
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            // 返回
            return;
        }

        // 去服务器注册
        // 进度条
        final ProgressDialog pb = new ProgressDialog(LoginActivity.this);
        pb.setMessage("正在注册中");
        pb.show();
        // 访问网络
        Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
           @Override
           public void run() {
               try {
                   // 去环信服务器创建用户账号
                   EMClient.getInstance().createAccount(registName, registPwd);
                   // 保存数据到数据库
                   Modle.getInstance().getUserAccountDao().addAccount(new UserInfo(registName));

                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           // 提示成功
                           Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                           // pd隐藏
                           pb.cancel();
                       }
                   });
               } catch (final HyphenateException e) {
                   e.printStackTrace();
                   // 失败
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           // 提示成功
                           Toast.makeText(LoginActivity.this, "注册失败" + e.toString(), Toast.LENGTH_SHORT).show();
                           // pd隐藏
                           pb.cancel();
                       }
                   });

               }
           }
       });
    }



}
