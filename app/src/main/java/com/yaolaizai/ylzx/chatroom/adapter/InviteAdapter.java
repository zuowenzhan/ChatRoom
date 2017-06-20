package com.yaolaizai.ylzx.chatroom.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.model.bean.InvitationInfo;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/9.
 */
public class InviteAdapter extends BaseAdapter {
    private Context mContext;
    private  OnInviteListener mOnInviteListener;

    // 邀请信息数据源
    private List<InvitationInfo> mInvitationInfos = new ArrayList<>();


    public InviteAdapter(Context context, OnInviteListener onInviteListener) {
        mContext = context;
        mOnInviteListener = onInviteListener;
    }


    // 数据刷新方法
    public void refresh(List<InvitationInfo> invitationInfos) {
        // 校验
        if (invitationInfos != null && invitationInfos.size() >= 0) {
            mInvitationInfos.clear();
            mInvitationInfos.addAll(invitationInfos);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        Log.e("TAG", ""+mInvitationInfos.size());

        return mInvitationInfos == null ? 0 : mInvitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1 创建或获取viewholder
        ViewHolder hodler = null;
        if(convertView == null) {
            hodler = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.item_invite, null);

            // 名称
            hodler.name = (TextView) convertView.findViewById(R.id.tv_invite_name);
            // 原因
            hodler.reason = (TextView) convertView.findViewById(R.id.tv_invite_reason);
            // 接受按钮
            hodler.btAccept = (Button) convertView.findViewById(R.id.bt_invite_accept);
            // 拒绝按钮
            hodler.btReject = (Button) convertView.findViewById(R.id.bt_invite_reject);
            // 保存holder
            convertView.setTag(hodler);
        }else {
            hodler = (ViewHolder) convertView.getTag();
        }

        // 2 获取当前item数据
        final InvitationInfo invitationInfo = mInvitationInfos.get(position);

        UserInfo user = invitationInfo.getUser();

        // 3 显示数据
        // 群信息和联系人信息判断
        if(user != null) {// 联系人
            // 显示名称
            hodler.name.setText(user.getName());

            // 好友邀请
            if(invitationInfo.getStatus() == InvitationInfo.InvitationStatus.NEW_INVITE) {

                if(invitationInfo.getReason() == null) {
                    hodler.reason.setText("添加好友");
                }else {
                    hodler.reason.setText(invitationInfo.getReason());
                }
            }else if(invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT) {
                hodler.btAccept.setVisibility(View.GONE);
                hodler.btReject.setVisibility(View.GONE);

                if(invitationInfo.getReason() == null) {
                    hodler.reason.setText("接受"+user.getName()+"的邀请");
                }else {
                    hodler.reason.setText(invitationInfo.getReason());
                }
            }else if(invitationInfo.getStatus() == InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER) {
                hodler.btAccept.setVisibility(View.GONE);
                hodler.btReject.setVisibility(View.GONE);

                if(invitationInfo.getReason() == null) {
                    hodler.reason.setText(user.getName()+"接受了你的邀请");
                }else {
                    hodler.reason.setText(invitationInfo.getReason());
                }
            }

            // 接受按钮的点击事件
            hodler.btAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onAccept(invitationInfo);
                }
            });

            // 拒绝按钮的点击事件
            hodler.btReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onReject(invitationInfo);
                }
            });

        }else {// 群信息
            // 根据状态
            if(invitationInfo.getStatus() == InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION) {
                // 接受按钮
                hodler.btAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnInviteListener.onApplicationAccept(invitationInfo);
                    }
                });

                // 拒绝按钮
                hodler.btReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnInviteListener.onApplicationReject(invitationInfo);
                    }
                });

            }else if(invitationInfo.getStatus() ==InvitationInfo.InvitationStatus.NEW_GROUP_INVITE) {
                // 接受按钮
                hodler.btAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnInviteListener.onInviteAccept(invitationInfo);
                    }
                });

                // 拒绝按钮
                hodler.btReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnInviteListener.onInviteReject(invitationInfo);
                    }
                });
            }

            // 名称显示
            hodler.name.setText(invitationInfo.getGroup().getGroupName()+":"+invitationInfo.getGroup().getInvitePerson());

            // 按钮隐藏处理
            hodler.btAccept.setVisibility(View.GONE);
            hodler.btReject.setVisibility(View.GONE);

            // 原因显示
            switch(invitationInfo.getStatus()){
                // 您的群申请请已经被接受
                case GROUP_APPLICATION_ACCEPTED:
                    hodler.reason.setText("您的群申请请已经被接受");
                    break;
                //  您的群邀请已经被接收
                case GROUP_INVITE_ACCEPTED:
                    hodler.reason.setText("您的群邀请已经被接收");
                    break;

                // 你的群申请已经被拒绝
                case GROUP_APPLICATION_DECLINED:
                    hodler.reason.setText("你的群申请已经被拒绝");
                    break;

                // 您的群邀请已经被拒绝
                case GROUP_INVITE_DECLINED:
                    hodler.reason.setText("您的群邀请已经被拒绝");
                    break;

                // 您收到了群邀请
                case NEW_GROUP_INVITE:
                    hodler.btAccept.setVisibility(View.VISIBLE);
                    hodler.btReject.setVisibility(View.VISIBLE);
                    hodler.reason.setText("您收到了群邀请");
                    break;

                // 您收到了群申请
                case NEW_GROUP_APPLICATION:
                    hodler.btAccept.setVisibility(View.VISIBLE);
                    hodler.btReject.setVisibility(View.VISIBLE);
                    hodler.reason.setText("您收到了群申请");
                    break;

                // 你接受了群邀请
                case GROUP_ACCEPT_INVITE:
                    hodler.reason.setText("你接受了群邀请");
                    break;

                // 您批准了群加入
                case GROUPO_ACCEPT_APPLICATION:
                    hodler.reason.setText("您批准了群加入");
                    break;
            }
        }

        // 4 返回view
        return convertView;
    }

    static class ViewHolder{
        TextView name;
        TextView reason;
        Button btAccept;
        Button btReject;
    }

    // 回调监听接口
    public interface OnInviteListener{
        // 接受的点击事件处理
        void onAccept(InvitationInfo invitationInfo);

        // 拒绝的点击事件处理
        void onReject(InvitationInfo invitationInfo);

        // 接受申请信息的事件处理
        void onApplicationAccept(InvitationInfo invitationInfo);
        // 拒绝申请信息的事件处理
        void onApplicationReject(InvitationInfo invitationInfo);

        // 接受邀请信息的事件处理
        void onInviteAccept(InvitationInfo invitationInfo);
        // 拒绝邀请信息的事件处理
        void onInviteReject(InvitationInfo invitationInfo);
    }
}
