package com.yaolaizai.ylzx.chatroom.model.db;

import android.content.Context;

import com.yaolaizai.ylzx.chatroom.model.dao.ContactTableDao;
import com.yaolaizai.ylzx.chatroom.model.dao.InviteTableDao;

/**
 * Created by ylzx on 2017/6/19.
 * 数据库管理类
 */
public class DBManager {


    private final DBHelper dbHelper;
    private final ContactTableDao contactTableDao;
    private final InviteTableDao inviteTableDao;

    public DBManager(Context context, String name) {
        dbHelper = new DBHelper(context, name);

        // 创建联系人操作类
        contactTableDao = new ContactTableDao(dbHelper);

        // 创建邀请信息操作类
        inviteTableDao = new InviteTableDao(dbHelper);
    }

    public InviteTableDao getInviteTableDao(){
        return inviteTableDao;
    }

    public ContactTableDao getContactTableDao(){
        return contactTableDao;
    }

    public void close() {
        dbHelper.close();
    }
}
