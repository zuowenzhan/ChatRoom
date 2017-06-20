package com.yaolaizai.ylzx.chatroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.adapter.PickContactsAdapter;
import com.yaolaizai.ylzx.chatroom.base.BaseActvity;
import com.yaolaizai.ylzx.chatroom.model.Modle;
import com.yaolaizai.ylzx.chatroom.model.bean.PickContactInfo;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;
import com.yaolaizai.ylzx.chatroom.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickContactsActivity extends BaseActvity {

    @BindView(R.id.tv_pick_contacts_save)
    TextView tvPickContactsSave;
    @BindView(R.id.lv_pick_contacts)
    ListView lvPickContacts;

    private PickContactsAdapter mPickContactsAdapter;
    private List<PickContactInfo> mPicks;
    private List<String> mExistingMembers;
    @Override
    public void initView() {
        setContentView(R.layout.activity_pick_contacts);
    }

    @Override
    public void initData() {

        getData();

        // 获取所有联系人的数据
        List<UserInfo> contacts = Modle.getInstance().getDbManager().getContactTableDao().getContacts();

        mPicks = new ArrayList<>();

        // 校验
        if(contacts != null && contacts.size() >= 0) {

            // 将联系人信息转换为选择联系人bean信息
            PickContactInfo pickContactInfo = null;

            for (UserInfo contact: contacts){
                pickContactInfo = new PickContactInfo(contact, false);

                mPicks.add(pickContactInfo);
            }
        }

        // 创建适配器
        mPickContactsAdapter = new PickContactsAdapter(PickContactsActivity.this, mPicks, mExistingMembers);
        // 添加适配器
        lvPickContacts.setAdapter(mPickContactsAdapter);

        initListener();
    }
    // 获取传递过来的群id信息
    private void getData() {
        String groupId = getIntent().getStringExtra(Constant.GROUP_ID);

        if(groupId != null) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
            mExistingMembers = group.getMembers();
        }

        if(mExistingMembers == null) {
            mExistingMembers = new ArrayList<>();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_pick_contacts_save)
    public void onClick() {


        // 获取被选择的联系人
        List<String> addMembers = mPickContactsAdapter.getAddMembers();
        // 设置数据准备返回创建群页面
        Intent intent = new Intent();
        intent.putExtra("members", addMembers.toArray(new String[0]));
        setResult(RESULT_OK, intent);
        // 结束当前页面
        finish();
    }


    private void initListener() {
        // listView团队item点击事件处理
        lvPickContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //  获取当前item的checkbox对象
                CheckBox cb_item_pick_contacts = (CheckBox) view.findViewById(R.id.cb_item_pick_contacts);

                // 设置状态
                cb_item_pick_contacts.setChecked(!cb_item_pick_contacts.isChecked());

//                cb_item_pick_contacts.toggle();

                // 更新数据
                PickContactInfo pickContactInfo = mPicks.get(position);

                pickContactInfo.setIsChecked(cb_item_pick_contacts.isChecked());

                // 刷新列表数据
                mPickContactsAdapter.notifyDataSetChanged();
            }
        });

    }
}
