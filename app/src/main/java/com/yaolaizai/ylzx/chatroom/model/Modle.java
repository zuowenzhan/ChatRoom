package com.yaolaizai.ylzx.chatroom.model;

import android.content.Context;
import android.util.Log;

import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;
import com.yaolaizai.ylzx.chatroom.model.dao.UserAccountDao;
import com.yaolaizai.ylzx.chatroom.model.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ylzx on 2017/6/19.
 */
public class Modle {

    private Context mContext;
    private UserAccountDao userAccountDao;
    private DBManager dbManager;
    // 创建一个全局线程池
    ExecutorService executorService = Executors.newCachedThreadPool();


    private Modle() {

    }

    private static Modle instance = new Modle();

    // 获取模型层对象
    public static Modle getInstance() {
        return instance;
    }

    // 初始化
    public void init(Context context){
        mContext = context;

        // 初始化用户账号数据库dao
        userAccountDao = new UserAccountDao(mContext);

        EventListener eventListener = new EventListener(mContext);
    }

    // 获取用户账号数据库的操作类
    public UserAccountDao getUserAccountDao(){
        return  userAccountDao;
    }

    // 获取全局线程池方法
    public ExecutorService getGlobleThreadPool(){
        return executorService;
    }

    // 登录成功后
    public void loginSuccess(UserInfo account) {

        if (account == null) {
            return;
        }

        if (dbManager != null) {
            dbManager.close();
        }

        // 创建数据库的管理类
        dbManager = new DBManager(mContext, account.getHxid());

        Log.e("TAG", "登录成功");
    }

    public DBManager getDbManager(){
        return dbManager;
    }
}
