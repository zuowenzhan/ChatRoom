package com.yaolaizai.ylzx.chatroom;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.yaolaizai.ylzx.chatroom.adapter.MyFragmentPagerAdapter;
import com.yaolaizai.ylzx.chatroom.base.BaseActvity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActvity  implements ViewPager.OnPageChangeListener {

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    @BindView(R.id.iv_home_xiaoxi)
    ImageView ivHomeXiaoxi;
    @BindView(R.id.unread_msg_number)
    TextView unreadMsgNumber;
    @BindView(R.id.rl_home_xiaoxi)
    RelativeLayout rlHomeXiaoxi;
    @BindView(R.id.iv_home_tongxunlu)
    ImageView ivHomeTongxunlu;
    @BindView(R.id.rl_home_tongxunlu)
    RelativeLayout rlHomeTongxunlu;
    @BindView(R.id.iv_home_shenpi)
    ImageView ivHomeShenpi;
    @BindView(R.id.rl_home_shenpi)
    RelativeLayout rlHomeShenpi;
    @BindView(R.id.iv_home_wode)
    ImageView ivHomeWode;
    @BindView(R.id.rl_home_wode)
    RelativeLayout rlHomeWode;
    @BindView(R.id.ll_bottom_home)
    LinearLayout llBottomHome;
    @BindView(R.id.div_tab_bar)
    View divTabBar;

    private MyFragmentPagerAdapter mAdapter;
    private ViewPager vpager;

    @Override
    public void initView() {

        setContentView(R.layout.activity_main);

        // 加载所有群信息
        EMClient.getInstance().groupManager().loadAllGroups();
        // 加载所有会话信息
        EMClient.getInstance().chatManager().loadAllConversations();

        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),getApplicationContext());

        bindViews();
    }

    @Override
    public void initData() {

        //默认显示消息列表
        setcheck(PAGE_ONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public void setcheck(int i) {
        switch (i) {

            case PAGE_ONE:


                cleanview();

                ivHomeXiaoxi.setImageResource(R.mipmap.xiaoxi_yes);

                break;


            case PAGE_TWO:

                cleanview();

                ivHomeTongxunlu.setImageResource(R.mipmap.tongxunlu_yes);


                break;

            case PAGE_THREE:

                cleanview();

                ivHomeShenpi.setImageResource(R.mipmap.shenpi_yes);


                break;

            case PAGE_FOUR:

                cleanview();

                ivHomeWode.setImageResource(R.mipmap.wode_yes);

                break;

        }


    }

    @OnClick({R.id.rl_home_xiaoxi, R.id.rl_home_tongxunlu, R.id.rl_home_shenpi, R.id.rl_home_wode})

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rl_home_xiaoxi:

                vpager.setCurrentItem(PAGE_ONE);

                setcheck(PAGE_ONE);

                break;

            case R.id.rl_home_tongxunlu:

                vpager.setCurrentItem(PAGE_TWO);

                setcheck(PAGE_TWO);

                break;

            case R.id.rl_home_shenpi:

                vpager.setCurrentItem(PAGE_THREE);
                setcheck(PAGE_THREE);

                break;

            case R.id.rl_home_wode:

                vpager.setCurrentItem(PAGE_FOUR);

                setcheck(PAGE_FOUR);


                break;
        }
    }

    private void cleanview() {

        ivHomeXiaoxi.setImageResource(R.mipmap.xiaoxi_no);
        ivHomeTongxunlu.setImageResource(R.mipmap.tongxunlu_no);
        ivHomeShenpi.setImageResource(R.mipmap.shenpi_no);
        ivHomeWode.setImageResource(R.mipmap.wode_no);
    }
    private void bindViews() {
        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
        vpager.setOffscreenPageLimit(4);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕

        if (state == 2) {

            switch (vpager.getCurrentItem()) {

                case PAGE_ONE:

                    setcheck(PAGE_ONE);

                    break;

                case PAGE_TWO:

                    setcheck(PAGE_TWO);
                    break;

                case PAGE_THREE:

                    setcheck(PAGE_THREE);

                    break;

                case PAGE_FOUR:

                    setcheck(PAGE_FOUR);

                    break;
            }
        }
    }
}
