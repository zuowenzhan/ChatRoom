package com.yaolaizai.ylzx.chatroom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.base.BaseActvity;
import com.yaolaizai.ylzx.chatroom.model.Modle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewGroupActivity extends BaseActvity {

    @BindView(R.id.et_new_group_name)
    EditText etNewGroupName;
    @BindView(R.id.et_new_group_desc)
    EditText etNewGroupDesc;
    @BindView(R.id.cb_new_group_public)
    CheckBox cbNewGroupPublic;
    @BindView(R.id.cb_new_group_invite)
    CheckBox cbNewGroupInvite;
    @BindView(R.id.bt_new_group_create)
    Button btNewGroupCreate;

    @Override
    public void initView() {
        setContentView(R.layout.activity_new_group);
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

    @OnClick(R.id.bt_new_group_create)
    public void onClick() {

        // 跳转到联系页面获取联系人数据
        Intent intent = new Intent(NewGroupActivity.this, PickContactsActivity.class);

        startActivityForResult(intent, 110);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // 创建群
            createGroup(data.getExtras().getStringArray("members"));
        }
    }

    // 创建群
    private void createGroup(final String[] memberses) {
        // 获取群名称
        final String groupName = etNewGroupName.getText().toString();
        // 获取群的描述信息
        final String groupDesc = etNewGroupDesc.getText().toString();

        // 联网
        Modle.getInstance().getGlobleThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String reason = "申请加入群";
                EMGroupManager.EMGroupStyle groupStyle = null;

                // 群公开
                if (cbNewGroupPublic.isChecked()) {
                    if (cbNewGroupInvite.isChecked()) {// 群邀请公开
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    } else {// 群邀请不公开
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                } else {// 群不公开
                    if (cbNewGroupInvite.isChecked()) {// 群邀请公开
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    } else {// 群邀请不公开
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }

                // 群参数设置
                EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
                // 群最多多少人
                options.maxUsers = 200;
                // 创建群的类型
                options.style = groupStyle;

                try {
                    // 联网创建群
                    EMClient.getInstance().groupManager().createGroup(groupName, groupDesc, memberses, reason, options);


                    // 更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群：" + groupName + "成功", Toast.LENGTH_SHORT).show();
                            // 结束当前页面
                            finish();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();

                    // 更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群：" + groupName + "失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
