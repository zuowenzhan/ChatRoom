package com.yaolaizai.ylzx.chatroom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.yaolaizai.ylzx.chatroom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public class GroupListAdapter extends BaseAdapter {
    private Context mContext;
    private List<EMGroup> mEMGroups = new ArrayList<>();

    public GroupListAdapter(Context context) {
        mContext = context;
    }

    // 刷新方法
    public void refresh(List<EMGroup> eMGroups){

        if(eMGroups != null && eMGroups.size() >= 0) {
            // 加载数据
            mEMGroups.clear();
            mEMGroups.addAll(eMGroups);

            // 通知刷新页面
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mEMGroups == null? 0:mEMGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return mEMGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 创建或获取viewhoder
        ViewHolder holder = null;
        
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_group_list, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_group_list_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 获取当前item数据
        EMGroup emGroup = mEMGroups.get(position);

        // 显示数据
        holder.tv_name.setText(emGroup.getGroupName());


        // 返回convertview
        return convertView;
    }

    static class  ViewHolder{
        TextView tv_name;
    }
}
