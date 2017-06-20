package com.yaolaizai.ylzx.chatroom.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.yaolaizai.ylzx.chatroom.MainActivity;
import com.yaolaizai.ylzx.chatroom.fragment.ChatFragment;
import com.yaolaizai.ylzx.chatroom.fragment.FoundFragment;
import com.yaolaizai.ylzx.chatroom.fragment.MaillistFragment;
import com.yaolaizai.ylzx.chatroom.fragment.MineFragment;

/**
 * Created by ylzx on 2017/6/19.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private final int PAGER_COUNT = 4;
    private Context mContext;

    private ChatFragment chatFragment = null;
    private Fragment foundFragment = null;
    private MaillistFragment maillistFragment = null;
    private MineFragment mineFragment = null;

    public MyFragmentPagerAdapter(FragmentManager fm,Context context) {
           super(fm);
        this.mContext = context;
        chatFragment = new ChatFragment();
        foundFragment = new FoundFragment();
        maillistFragment = new MaillistFragment();
        mineFragment = new MineFragment();


    }




    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {

            case MainActivity.PAGE_ONE:

                   // 消息列表
                fragment = chatFragment;

                break;

            case MainActivity.PAGE_TWO:
                 //通讯录
                fragment = maillistFragment;


                break;
            case MainActivity.PAGE_THREE:
                 //发现
                fragment = foundFragment;


                break;
            case MainActivity.PAGE_FOUR:

                       //我的界面
                    fragment = mineFragment;

                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {

        return PAGER_COUNT;
    }


    @Override
    public Object instantiateItem(ViewGroup vg, int position) {

        return super.instantiateItem(vg, position);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

}
