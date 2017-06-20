package com.yaolaizai.ylzx.chatroom.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends FragmentActivity {

    @BindView(R.id.fl_chat)
    FrameLayout flChat;
    private EaseChatFragment easeChatFragment;
    private String hxid;
    private LocalBroadcastManager mLBM;
    private int chatType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);


        initData();

        initListener();
    }
    private void initData() {
        easeChatFragment = new EaseChatFragment();

        // 获取环信id
        hxid = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        // 获取聊天类型
        chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);
        // 设置参数
        easeChatFragment.setArguments(getIntent().getExtras());

        // 替换
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_chat, easeChatFragment).commit();

        // 获取本地广播的管理者对象
        mLBM = LocalBroadcastManager.getInstance(this);

    }


    private void initListener() {

        easeChatFragment.setChatFragmentListener(new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {

            }

            @Override
            public void onEnterToChatDetails() {
                // 跳转到群详情页面
                Intent intent = new Intent(ChatActivity.this, GroupDetailActivity.class);
                intent.putExtra(Constant.GROUP_ID, hxid);
                startActivity(intent);
            }

            @Override
            public void onAvatarClick(String username) {

            }

            @Override
            public void onAvatarLongClick(String username) {

            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        });

        // 如果当前类型是群聊
        if(chatType == EaseConstant.CHATTYPE_GROUP) {

            // 退群广播的接收
            BroadcastReceiver ExitGroupReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(hxid.equals(intent.getStringExtra(Constant.GROUP_ID))) {
                        //结束当前页面
                        finish();
                    }
                }
            };
            mLBM.registerReceiver(ExitGroupReceiver,new IntentFilter(Constant.EXIT_GROUP));
        }
    }

}
