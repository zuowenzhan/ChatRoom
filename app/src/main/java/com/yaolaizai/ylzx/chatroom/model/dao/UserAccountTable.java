package com.yaolaizai.ylzx.chatroom.model.dao;

/**
 * Created by ylzx on 2017/6/19.
 *  创建用户账号数据库表语句
 */
    public class UserAccountTable {
        public static final String TAB_NAME = "tab_account";
        public static final String COL_NAME = "name";
        public static final String COL_HXID = "hxid";
        public static final String COL_NICK = "nick";
        public static final String COL_PHOTO = "photo";


        public static final String CREATE_TABLE = "create table "
                + UserAccountTable.TAB_NAME +"("
                + UserAccountTable.COL_HXID + " text primary key, "
                + UserAccountTable.COL_NAME + " text, "
                + UserAccountTable.COL_NICK + " text, "
                + UserAccountTable.COL_PHOTO + " text);";
    }

