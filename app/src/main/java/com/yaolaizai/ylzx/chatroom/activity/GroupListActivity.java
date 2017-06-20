package com.yaolaizai.ylzx.chatroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.adapter.GroupListAdapter;
import com.yaolaizai.ylzx.chatroom.base.BaseActvity;
import com.yaolaizai.ylzx.chatroom.model.Modle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupListActivity extends BaseActvity {

    @BindView(R.id.lv_group_list)
    ListView lvGroupList;
    private LinearLayout ll_group_list;
    private GroupListAdapter mGroupListAdapter;
    @Override
    public void initView() {
        setContentView(R.layout.activity_group_list);
    }

    @Override
    public void initData() {
        // 获取头布局view
        View headerView = View.inflate(GroupListActivity.this, R.layout.header_group_list, null);
        // 添加头布局view
        lvGroupList.addHeaderView(headerView);

        // 获取头布局对象
        ll_group_list = (LinearLayout) headerView.findViewById(R.id.ll_group_list);


        // 创建适配器
        mGroupListAdapter = new GroupListAdapter(GroupListActivity.this);
        // 将适配器添加到listview中
        lvGroupList.setAdapter(mGroupListAdapter);
        // 刷新显示
        mGroupListAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());

        // 联网获取群信息
        getGroupFromHxServier();

        initListener();
    }
    // 联网获取群信息
    private void getGroupFromHxServier() {
        Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 联网获取群信息
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this, "加载群信息成功", Toast.LENGTH_SHORT).show();
                            // 刷新显示
                            mGroupListAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this, "加载群信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // 刷新页面
        mGroupListAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }

    private void initListener() {
        // 群组条目的点击事件
        ll_group_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到新建群页面
                Intent intent = new Intent(GroupListActivity.this, NewGroupActivity.class);

                startActivity(intent);
            }
        });

        // 群列表的点击事件
        lvGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("TAG", "positon:" + position);

                // 跳转到聊天页面
                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);

                // 获取群id
                String groupId = EMClient.getInstance().groupManager().getAllGroups().get(position - 1).getGroupId();
                intent.putExtra(EaseConstant.EXTRA_USER_ID, groupId);

                // 保存群聊天类型
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);

                startActivity(intent);
            }
        });
    }

}
