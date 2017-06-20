package com.yaolaizai.ylzx.chatroom.base;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.yaolaizai.ylzx.chatroom.model.Modle;



/**
 * Created by ylzx on 2017/6/16.
 */
public class BaseAppliction extends Application{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();

        mContext=this;

        EMOptions options = new EMOptions();
        // 不自动接受群邀请信息
        options.setAutoAcceptGroupInvitation(false);
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        // 初始化EaseUI
        EaseUI.getInstance().init(this,options);

        // 初始化模型层数据
        Modle.getInstance().init(this);

    }

    public static Context getApplication(){
        return  mContext;
    }
}
