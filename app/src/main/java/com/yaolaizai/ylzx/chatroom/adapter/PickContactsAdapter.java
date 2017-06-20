package com.yaolaizai.ylzx.chatroom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.model.bean.PickContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public class PickContactsAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickContactInfo> mPicks = new ArrayList<>();
    private List<String> mExistingMembers  = new ArrayList<>();


    public PickContactsAdapter(Context context, List<PickContactInfo> picks, List<String> existingMembers) {
        mContext = context;

        if(picks != null && picks.size() >= 0) {
            mPicks.clear();
            mPicks.addAll(picks);
        }

        // 接受群中已经存在的群成员的环信id
        if(existingMembers != null && existingMembers.size() >=0 ) {
            mExistingMembers.clear();
            mExistingMembers.addAll(existingMembers);
        }

    }

    // 获取选中的联系人
    public List<String> getAddMembers(){
        // 准备一个要返回的数据集合
        List<String> names = new ArrayList<>();

        // 遍历集合 选择出选中状态的联系人
        for (PickContactInfo pick: mPicks){

            if(pick.isChecked()) {
                names.add(pick.getUser().getName());
            }
        }

        return names;
    }

    @Override
    public int getCount() {
        return mPicks == null? 0:mPicks.size();
    }

    @Override
    public Object getItem(int position) {
        return mPicks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 创建或获取viewholder
        ViewHolder holder  = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_pick_contacts, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_pick_contacts_name);
            holder.cb_checked = (CheckBox) convertView.findViewById(R.id.cb_item_pick_contacts);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 获取当前item数据
        PickContactInfo pickContactInfo = mPicks.get(position);

        // 显示数据
        holder.tv_name.setText(pickContactInfo.getUser().getName());
        holder.cb_checked.setChecked(pickContactInfo.isChecked());

        if(mExistingMembers.contains(pickContactInfo.getUser().getHxid())) {
            holder.cb_checked.setChecked(true);
            pickContactInfo.setIsChecked(true);
        }

        // 返回view

        return convertView;
    }

    static class ViewHolder{
        TextView tv_name;
        CheckBox cb_checked;
    }
}
