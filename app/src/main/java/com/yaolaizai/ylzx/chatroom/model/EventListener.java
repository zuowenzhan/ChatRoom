package com.yaolaizai.ylzx.chatroom.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.yaolaizai.ylzx.chatroom.model.bean.GroupInfo;
import com.yaolaizai.ylzx.chatroom.model.bean.InvitationInfo;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;
import com.yaolaizai.ylzx.chatroom.utils.Constant;
import com.yaolaizai.ylzx.chatroom.utils.SpUtils;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
// 全局监听
public class EventListener {

    private Context mContext;
    private final LocalBroadcastManager mLBM;

    public EventListener(Context context) {
        mContext = context;

        mLBM = LocalBroadcastManager.getInstance(mContext);

        // 监听环信服务器联系人的信息变化
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
        // 监听群信息的变化
        EMClient.getInstance().groupManager().addGroupChangeListener(GroupChangedListener);
    }

    // 群变化的监听器
    private EMGroupChangeListener GroupChangedListener = new EMGroupChangeListener() {

        //收到 群邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

            // 添加邀请信息

            InvitationInfo invitation = new InvitationInfo();
            invitation.setReason(reason);
            invitation.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_INVITE);
            invitation.setGroup(new GroupInfo(groupName, groupId, inviter));

            Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);

            // 存储有新的邀请状态
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送群信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群申请通知
        @Override
        public void onApplicationReceived(String groupId, String groupName, String applicant, String reason) {

            // 添加邀请信息
            InvitationInfo invitation = new InvitationInfo();
            invitation.setGroup(new GroupInfo(groupName, groupId, applicant));
            invitation.setReason(reason);
            invitation.setStatus(InvitationInfo.InvitationStatus.NEW_GROUP_APPLICATION);

            Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);

            // 存储有新的邀请状态
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送群信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群申请被接受
        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {

            InvitationInfo invitation = new InvitationInfo();
            invitation.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTED);
            invitation.setGroup(new GroupInfo(groupName, groupId, accepter));

            Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);

            // 存储有新的邀请状态
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送群信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群申请被拒绝
        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {

            InvitationInfo invitation = new InvitationInfo();

            invitation.setGroup(new GroupInfo(groupName,groupId,decliner));
            invitation.setReason(reason);
            invitation.setStatus(InvitationInfo.InvitationStatus.GROUP_APPLICATION_DECLINED);

            Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);

            // 存储有新的邀请状态
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送群信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被同意
        @Override
        public void onInvitationAccepted(String s, String s1, String s2) {

            InvitationInfo invitation = new InvitationInfo();
            invitation.setGroup(new GroupInfo(s,s,s1));
            invitation.setReason(s2);
            invitation.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);

            Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);

            // 存储有新的邀请状态
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送群信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被拒绝
        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

            InvitationInfo invitation = new InvitationInfo();
            invitation.setGroup(new GroupInfo(groupId,groupId,invitee));
            invitation.setReason(reason);
            invitation.setStatus(InvitationInfo.InvitationStatus.GROUP_INVITE_DECLINED);

            Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);
            // 存储有新的邀请状态
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送群信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群成员被删除
        @Override
        public void onUserRemoved(String groupId, String groupName) {

        }
        //收到 群被解散
        @Override
        public void onGroupDestroyed(String s, String s1) {

        }

        //收到 群邀请被自动接受
        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {

            InvitationInfo invitation = new InvitationInfo();
            invitation.setGroup(new GroupInfo(groupId,groupId,inviter));
            invitation.setReason(inviteMessage);
            invitation.setStatus(InvitationInfo.InvitationStatus.GROUPO_ACCEPT_APPLICATION);

            Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitation);

            // 存储有新的邀请状态
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送群信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }
    };


    private final EMContactListener emContactListener = new EMContactListener() {

        // 联系人添加了
        @Override
        public void onContactAdded(String hxid) {

            // 更新本地数据库
            Modle.getInstance().getDbManager().getContactTableDao().saveContact(new UserInfo(hxid), true);

            // 发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        // 联系人删除了
        @Override
        public void onContactDeleted(String hxid) {

            // 更新本地数据库
            Modle.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(hxid);
            Modle.getInstance().getDbManager().getInviteTableDao().removeInvitation(hxid);

            // 发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        // 有人给你发送好友邀请
        @Override
        public void onContactInvited(String hxid, String reason) {


            // 更新邀请信息详情
            InvitationInfo invitationInfo = new InvitationInfo();

            invitationInfo.setReason(reason);
            invitationInfo.setUser(new UserInfo(hxid));
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.NEW_INVITE);

            Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            // 有新的邀请
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        // 别人同意了你的邀请
        @Override
        public void onContactAgreed(String hxid) {
            // 更新邀请信息详情
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setStatus(InvitationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);
            invitationInfo.setUser(new UserInfo(hxid));

            Modle.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            // 有新的邀请
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        // 别人拒绝了你的邀请
        @Override
        public void onContactRefused(String s) {

            // 有新的邀请
            SpUtils.getInstace(mContext).save(SpUtils.IS_INVITE_NOTIY, true);

            // 发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };
}
