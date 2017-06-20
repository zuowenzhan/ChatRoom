package com.yaolaizai.ylzx.chatroom.fragment;


import android.content.Intent;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.yaolaizai.ylzx.chatroom.activity.ChatActivity;

import java.util.List;

/**
 * Created by ylzx on 2017/6/19.
 *
 * 消息列表界面
 */
public class ChatFragment extends EaseConversationListFragment {
    @Override
    protected void initView() {
        super.initView();

        // 条目添加事件的监听
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                // 参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());

                // 当前会话类型为群聊
                if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                }

                getActivity().startActivity(intent);
            }
        });

        // 清空当前会话列表数据，准备加载新的数据
        conversationList.clear();

        // 监听会话的变化
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    private EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            // 刷新列表
            EaseUI.getInstance().getNotifier().onNewMesg(list);

            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
}
