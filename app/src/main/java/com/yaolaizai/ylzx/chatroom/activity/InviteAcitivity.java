package com.yaolaizai.ylzx.chatroom.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.adapter.InviteAdapter;
import com.yaolaizai.ylzx.chatroom.base.BaseActvity;
import com.yaolaizai.ylzx.chatroom.model.Modle;
import com.yaolaizai.ylzx.chatroom.model.bean.InvitationInfo;
import com.yaolaizai.ylzx.chatroom.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteAcitivity extends BaseActvity {

    @BindView(R.id.lv_invite)
    ListView lvInvite;
    private InviteAdapter mInviteAdapter;
    private LocalBroadcastManager mLBM;

    private BroadcastReceiver InviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ///联系人邀请信息变化  || 群邀请信息变化
            if(intent.getAction() == Constant.CONTACT_INVITE_CHANGED || intent.getAction() == Constant.GROUP_INVITE_CHANGED) {
                mInviteAdapter.refresh(Modle.getInstance().getDbManager().getInviteTableDao().getInvitations());
            }
        }
    };


    private InviteAdapter.OnInviteListener mOnInviteListener = new InviteAdapter.OnInviteListener() {
        @Override
        public void onAccept(final InvitationInfo invitationInfo) {
            // 接受按钮的点击事件

            Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 通知环信服务器接受好友邀请
                        EMClient.getInstance().contactManager().acceptInvitation(invitationInfo.getUser().getHxid());

                        // 更新数据库
                        Modle.getInstance().getDbManager().getInviteTableDao()
                                .updateInvitationStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT, invitationInfo.getUser().getHxid());



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mInviteAdapter.refresh(Modle.getInstance().getDbManager().getInviteTableDao().getInvitations());

                                Toast.makeText(InviteAcitivity.this, "接受了好友的邀请", Toast.LENGTH_LONG).show();
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "接受好友的邀请失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onReject(final InvitationInfo invitationInfo) {
            // 拒绝按钮的点击事件
            Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 拒绝了邀请
                        EMClient.getInstance().contactManager().declineInvitation(invitationInfo.getUser().getHxid());

                        // 删除邀请信息
                        Modle.getInstance().getDbManager().getInviteTableDao().removeInvitation(invitationInfo.getUser().getHxid());
//                        Model.getInstace().getDbManager().getContactTableDao().deleteContactByHxId(invitationInfo.getUser().getHxId());


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "拒绝成功",Toast.LENGTH_LONG).show();
                                mInviteAdapter.refresh(Modle.getInstance().getDbManager().getInviteTableDao().getInvitations());
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "拒绝失败", Toast.LENGTH_LONG).show();
                                mInviteAdapter.refresh(Modle.getInstance().getDbManager().getInviteTableDao().getInvitations());
                            }
                        });
                    }
                }
            });

        }

        @Override
        public void onApplicationAccept(final InvitationInfo invitationInfo) {
            // 联网让服务器处理申请操作
            Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 访问环信服务器操作
                        EMClient.getInstance().groupManager().acceptApplication(invitationInfo.getGroup().getGroupId(),invitationInfo.getGroup().getInvitePerson());

                        invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);
                        // 保存邀请信息到数据库
                        Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

                        // 刷新页面显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "接受申请成功", Toast.LENGTH_SHORT).show();
                                // 刷新页面
                                refresh();
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "接受申请失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onApplicationReject(final InvitationInfo invitationInfo) {
            Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().declineApplication(invitationInfo.getGroup().getGroupId(),invitationInfo.getGroup().getInvitePerson(),"拒绝接受你");

                        // 保存邀请状态到数据库
                        invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_REJECT_APPLICATION);
                        Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

                        // 刷新页面显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "拒绝申请成功", Toast.LENGTH_SHORT).show();
                                // 刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "拒绝申请失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onInviteAccept(final InvitationInfo invitationInfo) {
            Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().acceptInvitation(invitationInfo.getGroup().getGroupId(),invitationInfo.getGroup().getInvitePerson());

                        // 更新本地数据库状态
                        invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
                        Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

                        // 刷新页面显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "接受邀请成功", Toast.LENGTH_SHORT).show();
                                // 刷新页面
                                refresh();
                            }
                        });

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "接受邀请失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onInviteReject(final InvitationInfo invitationInfo) {
            Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().declineInvitation(invitationInfo.getGroup().getGroupId(),invitationInfo.getGroup().getInvitePerson(),"拒绝你的邀请");

                        // 更新本地数据库
                        invitationInfo.setStatus(InvitationInfo.InvitationStatus.GROUP_REJECT_INVITE);
                        Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

                        // 刷新页面显示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "拒绝"+invitationInfo.getGroup().getInvitePerson()+"的邀请成功", Toast.LENGTH_SHORT).show();
                                // 刷新页面
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteAcitivity.this, "接受邀请失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };


    @Override
    public void initView() {
        setContentView(R.layout.activity_invite_acitivity);
    }

    @Override
    public void initData() {
        // 创建适配器
        mInviteAdapter = new InviteAdapter(this, mOnInviteListener);

        // 添加适配器
        lvInvite.setAdapter(mInviteAdapter);
        refresh();


        // 注册邀请信变化的广播监听
        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(InviteChangedReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(InviteChangedReceiver, new IntentFilter(Constant.GROUP_INVITE_CHANGED));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void refresh() {
        // 刷新适配器
        List<InvitationInfo> invitations = Modle.getInstance().getDbManager().getInviteTableDao().getInvitations();
        mInviteAdapter.refresh(invitations);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解注册
        mLBM.unregisterReceiver(InviteChangedReceiver);
    }
}
