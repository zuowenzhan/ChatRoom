package com.yaolaizai.ylzx.chatroom.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.activity.AddContactActivity;
import com.yaolaizai.ylzx.chatroom.activity.ChatActivity;
import com.yaolaizai.ylzx.chatroom.activity.GroupListActivity;
import com.yaolaizai.ylzx.chatroom.activity.InviteAcitivity;
import com.yaolaizai.ylzx.chatroom.base.BaseAppliction;
import com.yaolaizai.ylzx.chatroom.model.Modle;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;
import com.yaolaizai.ylzx.chatroom.utils.Constant;
import com.yaolaizai.ylzx.chatroom.utils.SpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ylzx on 2017/6/19.
 */
public class MaillistFragment extends EaseContactListFragment {


    private ImageView iv_contact_notify;
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver InviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 显示红点
            iv_contact_notify.setVisibility(View.VISIBLE);
            // 保存红点状态
            SpUtils.getInstace(BaseAppliction.getApplication()).save(SpUtils.IS_INVITE_NOTIY, true);
        }
    };

    private LinearLayout ll_contact_invite;
    private BroadcastReceiver ContactChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 联系人变化
            if (intent.getAction() == Constant.CONTACT_CHANGED) {
                // 刷新页面
                refreshContacts();
            }
        }
    };
    private String mHxid;


    @Override
    protected void initView() {
        super.initView();

        // 联系人的头布局
        View headerView = View.inflate(getActivity(), R.layout.fragment_home_maillist, null);
        listView.addHeaderView(headerView);

        // 头部图标设置
        titleBar.setRightImageResource(R.mipmap.em_add);

        // 获取红点对象
        iv_contact_notify = (ImageView) headerView.findViewById(R.id.iv_contact_notify);

        // 获取邀请信息条目对象
        ll_contact_invite = (LinearLayout) headerView.findViewById(R.id.ll_contact_invite);

        // 联系人条目的点击事件
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                // 跳转到会话页面
                Intent intent = new Intent(getActivity(), ChatActivity.class);

                // 传递参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());

                getActivity().startActivity(intent);
            }
        });

        // 群组条目的点击事件
        LinearLayout ll_contact_group = (LinearLayout) headerView.findViewById(R.id.ll_contact_group);
        ll_contact_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到群列表页面
                Intent intent = new Intent(getActivity(), GroupListActivity.class);

                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    protected void setUpView() {
        super.setUpView();

        // 加号添加联系人
        titleBar.getRightLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);

                getActivity().startActivity(intent);
            }
        });

        // 获取当前是否有新的邀请信息
        boolean is_notify = SpUtils.getInstace(BaseAppliction.getApplication()).getBoolean(SpUtils.IS_INVITE_NOTIY, false);
        iv_contact_notify.setVisibility(is_notify ? View.VISIBLE : View.GONE);

        // 好友邀请的点击事件
        ll_contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 消失红点
                iv_contact_notify.setVisibility(View.GONE);
                // 保存红点状态
                SpUtils.getInstace(BaseAppliction.getApplication()).save(SpUtils.IS_INVITE_NOTIY, false);

                // 跳转到邀请信息列表页面
                Intent intent = new Intent(getActivity(), InviteAcitivity.class);

                getActivity().startActivity(intent);
            }
        });

        // 从环信服务器获取联系人对象
        getContactsFromHxServer();

        // listview绑定(注册)contextmenu
        registerForContextMenu(listView);


        // 接受广播
        mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.registerReceiver(InviteChangedReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(InviteChangedReceiver, new IntentFilter(Constant.GROUP_INVITE_CHANGED));
        mLBM.registerReceiver(ContactChangedReceiver, new IntentFilter(Constant.CONTACT_CHANGED));

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        EaseUser easeUser = (EaseUser) listView.getItemAtPosition(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
        mHxid = easeUser.getUsername();
        Log.e("TAG", "11:" + mHxid);
        // 加载布局
        getActivity().getMenuInflater().inflate(R.menu.contact_delete, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 选择的相应item
        if (item.getItemId() == R.id.contact_delete) {

            // 删除选中的联系人
            deleteContact();

            return true;
        }

        return super.onContextItemSelected(item);
    }

    // 删除联系人
    private void deleteContact() {
        Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 环信服务器删除联系人
                    EMClient.getInstance().contactManager().deleteContact(mHxid);

                    // 本地数据库
                    Modle.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(mHxid);
                    // 清除邀请信息
                    Modle.getInstance().getDbManager().getInviteTableDao().removeInvitation(mHxid);
                    // 刷新页面
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshContacts();
                            // 提示
                            Toast.makeText(getActivity(), "删除联系人" + mHxid + "成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "删除联系人失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    // 从环信服务器获取联系人对象
    private void getContactsFromHxServer() {
        Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<String> hxids = null;
                try {
                    // 获取所有的环信id
                    hxids = EMClient.getInstance().contactManager().getAllContactsFromServer();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

                // 校验环信ids
                if (hxids != null && hxids.size() >= 0) {

                    // 保存联系人到本地数据库
                    List<UserInfo> contacts = new ArrayList<UserInfo>();

                    UserInfo userInfo = null;

                    // 遍历
                    for (String hxid : hxids) {
                        userInfo = new UserInfo(hxid);

                        contacts.add(userInfo);
                    }

                    // 保存到本地数据库
                    Modle.getInstance().getDbManager().getContactTableDao().saveContacts(contacts, true);

                    if(getActivity() == null) {
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 刷新联系人列表
                            refreshContacts();
                        }
                    });
                }
            }
        });
    }

    // 刷新联系人列表
    private void refreshContacts() {
        // 从本地数据库获取联系人信息
        List<UserInfo> contacts = Modle.getInstance().getDbManager().getContactTableDao().getContacts();

        if (contacts != null && contacts.size() >= 0) {
            // 给适配器设置数据
            Map<String, EaseUser> contactsMap = new HashMap<>();

            EaseUser easeUser = null;
            for (UserInfo contact : contacts) {
                easeUser = new EaseUser(contact.getHxid());

                contactsMap.put(contact.getHxid(), easeUser);
            }

            setContactsMap(contactsMap);
            // 通知适配器数据变化，刷新列表
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 解注册广播
        mLBM.unregisterReceiver(InviteChangedReceiver);
        mLBM.unregisterReceiver(ContactChangedReceiver);
    }
}
