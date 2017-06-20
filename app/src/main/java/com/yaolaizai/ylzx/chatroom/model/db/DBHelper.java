package com.yaolaizai.ylzx.chatroom.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yaolaizai.ylzx.chatroom.model.dao.ContactTable;
import com.yaolaizai.ylzx.chatroom.model.dao.InviteTable;


/**
 * Created by Administrator on 2016/7/9.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 1 ;

    public DBHelper(Context context, String name) {
        super(context, name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建联系人的表
        db.execSQL(ContactTable.CREATE_TAB);
        // 存放邀请信息的表
        db.execSQL(InviteTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
