package com.yaolaizai.ylzx.chatroom.model.bean;

/**
 * Created by ylzx on 2017/6/19.
 */
//用户信息实体类
public class UserInfo {

    private String name;// 名称
    private String hxid;// 环信id
    private String nick;// 昵称
    private String photo;// 头像

    public UserInfo() {

    }

    public UserInfo(String name) {
        this.name = name;
        this.hxid = name;
        this.nick = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHxid() {
        return hxid;
    }

    public void setHxid(String hxid) {
        this.hxid = hxid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", hxid='" + hxid + '\'' +
                ", nick='" + nick + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
