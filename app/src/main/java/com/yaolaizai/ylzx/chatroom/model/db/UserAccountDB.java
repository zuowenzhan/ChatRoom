package com.yaolaizai.ylzx.chatroom.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yaolaizai.ylzx.chatroom.model.dao.UserAccountTable;

/**
 * Created by ylzx on 2017/6/19.
 * 创建数据库
 */
public class UserAccountDB extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;

    public UserAccountDB(Context context) {
        super(context, "account.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserAccountTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
