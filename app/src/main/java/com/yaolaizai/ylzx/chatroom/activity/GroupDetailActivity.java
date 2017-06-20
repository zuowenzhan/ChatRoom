package com.yaolaizai.ylzx.chatroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.widget.EaseExpandGridView;
import com.hyphenate.exceptions.HyphenateException;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.adapter.GroupDetailAdapter;
import com.yaolaizai.ylzx.chatroom.base.BaseActvity;
import com.yaolaizai.ylzx.chatroom.model.Modle;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;
import com.yaolaizai.ylzx.chatroom.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupDetailActivity extends BaseActvity {

    @BindView(R.id.gv_member_list)
    EaseExpandGridView gvMemberList;
    @BindView(R.id.btn_exit_group)
    Button btnExitGroup;
    private EMGroup group;
    private LocalBroadcastManager mLBM;


    private GroupDetailAdapter.OnGroupDetailListener mOnGroupDetailListener = new GroupDetailAdapter.OnGroupDetailListener() {
        @Override
        public void onAddMembers() {
            // 跳转到选择联系人页面
            Intent intent = new Intent(GroupDetailActivity.this, PickContactsActivity.class);

            intent = intent.putExtra(Constant.GROUP_ID, group.getGroupId());

            startActivityForResult(intent, 0);
        }

        @Override
        public void onDeleteMember(final UserInfo user) {
            Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 从环信服务器中删除该成员
                        EMClient.getInstance().groupManager().removeUserFromGroup(group.getGroupId(),user.getHxid() );

                        // 从环信服务器再次获取群成员，并刷新页面
                        getUsersFromHxServer();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "删除该成员成功", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "删除该成员失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };
    private GroupDetailAdapter mGroupDetailAdapter;



    @Override
    public void initView() {
        setContentView(R.layout.activity_group_detail);
    }

    @Override
    public void initData() {

        // 获取传递过来的群id
        final String groupId = getIntent().getStringExtra(Constant.GROUP_ID);
        // 校验
        if (groupId == null) {
            finish();
            return;
        } else {
            // 获取群信息
            group = EMClient.getInstance().groupManager().getGroup(groupId);

        }

        // 初始化button的点击事件
        initButtonClick(groupId);

        // 你是群主 或者 你这个群是公开  你就可以添加和删除群成员
        boolean mIsCanModify = EMClient.getInstance().getCurrentUser().equals(group.getOwner()) || group.isPublic();

        // 创建适配器
        mGroupDetailAdapter = new GroupDetailAdapter(GroupDetailActivity.this, mIsCanModify, mOnGroupDetailListener);

        // 添加到gridview
        gvMemberList.setAdapter(mGroupDetailAdapter);

        // 从环信服务器获取数据
        getUsersFromHxServer();

        initListener();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            // 获取返回的联系人信息
            final String[] memberses = data.getStringArrayExtra("members");

            Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 添加群成员到该群组
                        EMClient.getInstance().groupManager().addUsersToGroup(group.getGroupId(), memberses);

                        // 访问服务器刷新页面
//                        getUsersFromHxServer();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "发送加入该群邀请成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "发送加入该群邀请失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void initButtonClick(final String groupId) {
        //显示
        // 判断是否是群主
        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
            // 更新显示
            btnExitGroup.setText("解散群");


            // 解散群的点击事件
            btnExitGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            // 联网
                            try {
                                // 联网成功
                                EMClient.getInstance().groupManager().destroyGroup(groupId);

                                // 发送解散群广播
                                exitGroupBroadCast();
                                // 提示
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "解散群成功", Toast.LENGTH_SHORT).show();
                                        // 销毁当前页面
                                        finish();
                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                // 提示
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "解散群失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        } else {// 群成员
            btnExitGroup.setText("退群");

            btnExitGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 联网
                    Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().leaveGroup(groupId);

                                // 发送广播
                                exitGroupBroadCast();

                                // 提示
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "退群成功", Toast.LENGTH_SHORT).show();
                                        // 销毁当前页面
                                        finish();
                                    }
                                });
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "退群失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });

        }
    }
    // 从环信服务器获取数据
    private void getUsersFromHxServer() {
        Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroup groupFromServer = EMClient.getInstance().groupManager().getGroupFromServer(group.getGroupId());
                    //  获取的 是群里面所有成员的环信id
                    List<String> members = groupFromServer.getMembers();

                    // 是我的联系人
                    final List<UserInfo> contacts =    Modle.getInstance().getDbManager().getContactTableDao().getContacts();

                    // 保存群中所有的好友（联系人）的信息
                    final List<UserInfo> membersContacts = new ArrayList<UserInfo>();

                    for (UserInfo contact : contacts) {

                        if (members.contains(contact.getHxid())) {

                            // 保存群中所有的好友信息
                            membersContacts.add(contact);

                            members.remove(contact.getHxid());
                        }
                    }

                    // 不是我的联系人好友(isMyContacts)
                    List<UserInfo> unContacts = new ArrayList<UserInfo>();

                    if (members != null && members.size() >= 0) {
                        UserInfo userInfo = null;
                        for (String hxid : members) {
                            userInfo = new UserInfo(hxid);

                            unContacts.add(userInfo);
                        }

                        // 保存非联系人到本地数据库中
                        Modle.getInstance().getDbManager().getContactTableDao().saveContacts(unContacts, false);
                    }

                    // 拼接群成员中所有联系和非联系人集合
                    if(unContacts != null && unContacts.size() >= 0) {
                        membersContacts.addAll(unContacts);
                    }

                    // 刷新数据
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGroupDetailAdapter.refresh(membersContacts);
                            Toast.makeText(GroupDetailActivity.this, "获取群成员信息成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupDetailActivity.this, "获取群成员信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
    private void initListener() {
        // 触摸事件的监听事件
        gvMemberList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mGroupDetailAdapter.ismIsDeleteModel()) {
                            mGroupDetailAdapter.setmIsDeleteModel(false);

                            // 刷新页面
                            mGroupDetailAdapter.notifyDataSetChanged();
                        }

                        break;
                }
                return false;
            }
        });
    }

    // 解散群和退群广播
    private void exitGroupBroadCast() {
        // 获取管理者
        mLBM = LocalBroadcastManager.getInstance(GroupDetailActivity.this);

        // 发送广播
        Intent intent = new Intent(Constant.EXIT_GROUP);
        // 传递群id参数
        intent.putExtra(Constant.GROUP_ID, group.getGroupId());
        mLBM.sendBroadcast(intent);
    }
}
