package com.yaolaizai.ylzx.chatroom.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.base.BaseActvity;
import com.yaolaizai.ylzx.chatroom.model.Modle;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends BaseActvity {

    @BindView(R.id.tv_add_contact_search)
    TextView tvAddContactSearch;
    @BindView(R.id.et_add_contact_name)
    EditText etAddContactName;
    @BindView(R.id.iv_add_contact_photo)
    ImageView ivAddContactPhoto;
    @BindView(R.id.tv_add_contact_name)
    TextView tvAddContactName;
    @BindView(R.id.bt_add_contact_add)
    Button btAddContactAdd;
    @BindView(R.id.rl_add_contact)
    RelativeLayout rlAddContact;

    private String searchName;
    private UserInfo userInfo;

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_contact);

    }

    @Override
    public void initData() {
        rlAddContact.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_add_contact_search, R.id.bt_add_contact_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_contact_search:


                // 获取输入的名称
           searchName = etAddContactName.getText().toString();
                // 校验
                if (TextUtils.isEmpty(searchName)) {
                    Toast.makeText(AddContactActivity.this, "输入的名称不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                // 去服务器查找
                userInfo = new UserInfo(searchName);

                // 数据显示
                rlAddContact.setVisibility(View.VISIBLE);
                tvAddContactName.setText(userInfo.getName());



                break;
            case R.id.bt_add_contact_add:



                // 去服务器发送添加好友消息
                Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 去服务器添加好友
                            EMClient.getInstance().contactManager().addContact(userInfo.getName(), "添加好友");

                            // 提示成功
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddContactActivity.this, "发送添加好友信息成功", Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (final HyphenateException e) {
                            e.printStackTrace();
                            // 提示成功
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddContactActivity.this, "发送添加好友信息失败" + e.toString(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                });

                break;
        }
    }
}
