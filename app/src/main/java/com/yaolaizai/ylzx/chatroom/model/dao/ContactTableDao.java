package com.yaolaizai.ylzx.chatroom.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;
import com.yaolaizai.ylzx.chatroom.model.db.DBHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
// 联系人数据库的操作类
public class ContactTableDao {

    DBHelper mHelper;

    public ContactTableDao(DBHelper helper) {
        mHelper = helper;
    }



    // 获取所有联系人
    public List<UserInfo> getContacts() {
        List<UserInfo> contacts = new ArrayList<>();

        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 查询联系人
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_IS_CONTACT + "=1";
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            UserInfo contact = new UserInfo();

            contact.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            contact.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            contact.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            contact.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));

            contacts.add(contact);
        }

        // 关闭cursor
        cursor.close();

        // 返回
        return contacts;
    }

    // 通过环信id获取用户联系人信息
    public List<UserInfo> getContactsByHx(List<String> hxIds) {
        // 校验
        if (hxIds == null || hxIds.size() <= 0) {
            return null;
        }

        List<UserInfo> contacts = new ArrayList<>();
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        for (String hxid : hxIds) {
            // 查询
            String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_HXID + " =?";
            Cursor cursor = db.rawQuery(sql, new String[]{hxid});

            if (cursor.moveToNext()) {
                UserInfo contact = new UserInfo();

                contact.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
                contact.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
                contact.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));

                contacts.add(contact);
            }

            // 关闭cursor
            cursor.close();
        }


        // 返回
        return contacts;
    }

    // 通过环信id获取联系人单个信息
    public UserInfo getContactByHx(String hxId) {
        // 校验
        if (hxId == null) {
            return null;
        }

        // 获取数据库
        SQLiteDatabase db = mHelper.getReadableDatabase();

        // 查询
        String sql = "select * from " + ContactTable.TAB_NAME + " where " + ContactTable.COL_HXID + " =?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});

        UserInfo contact = null;

        if (cursor.moveToNext()) {
            contact = new UserInfo();

            contact.setHxid(cursor.getString(cursor.getColumnIndex(ContactTable.COL_HXID)));
            contact.setName(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NAME)));
            contact.setNick(cursor.getString(cursor.getColumnIndex(ContactTable.COL_NICK)));
            contact.setPhoto(cursor.getString(cursor.getColumnIndex(ContactTable.COL_PHOTO)));

        }

        cursor.close();

        return contact;
    }

    // 保存单个联系人
    public void saveContact(UserInfo user, boolean isMyContact) {
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getWritableDatabase();

        // 执行添加语句
        ContentValues values = new ContentValues();
        values.put(ContactTable.COL_HXID, user.getHxid());
        values.put(ContactTable.COL_NAME, user.getName());
        values.put(ContactTable.COL_NICK, user.getNick());
        values.put(ContactTable.COL_PHOTO, user.getPhoto());
        values.put(ContactTable.COL_IS_CONTACT, isMyContact ? 1 : 0);

        db.replace(ContactTable.TAB_NAME, null, values);
    }


    // 保存联系人信息
    public void saveContacts(Collection<UserInfo> contacts, boolean isMyContact) {
        // 校验
        if (contacts == null || contacts.size() <= 0) {
            return;
        }

        for (UserInfo contact : contacts) {
            saveContact(contact, isMyContact);
        }
    }

    // 删除联系人信息
    public void deleteContactByHxId(String hxId) {
        // 校验
        if (hxId == null) {
            return;
        }

        // 获取数据库
        SQLiteDatabase db = mHelper.getReadableDatabase();

        // 删除操作
        db.delete(ContactTable.TAB_NAME, ContactTable.COL_HXID + "=?", new String[]{hxId});
    }
}
