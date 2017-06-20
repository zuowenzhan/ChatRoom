package com.yaolaizai.ylzx.chatroom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yaolaizai.ylzx.chatroom.R;
import com.yaolaizai.ylzx.chatroom.model.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public class GroupDetailAdapter extends BaseAdapter {
    private Context mContext;
    private boolean mIsCanModify;       // 表示可以添加和删除好友
    private boolean mIsDeleteModel;     //  是否是删除模式 true:删除模式  false:非删除模式
    private List<UserInfo> mUsers = new ArrayList<>();
    private OnGroupDetailListener mOnGroupDetailListener;

    public GroupDetailAdapter(Context context, boolean isCanModify, OnGroupDetailListener onGroupDetailListener) {
        mContext = context;
        mIsCanModify = isCanModify;

        mOnGroupDetailListener = onGroupDetailListener;
        // 初始化Users集合
        initUsers();
    }

    private void initUsers() {
        UserInfo add = new UserInfo("add");
        UserInfo delete = new UserInfo("delete");
        mUsers.add(delete);
        mUsers.add(0, add);
    }

    // 刷新的方法
    public void refresh(List<UserInfo> users) {
        // 校验
        if (users != null && users.size() >= 0) {
            mUsers.clear();

            initUsers();

            mUsers.addAll(0, users);
        }

        notifyDataSetChanged();
    }

    // 获取当前的模式
    public boolean ismIsDeleteModel() {
        return mIsDeleteModel;
    }

    // 设置当前的模式
    public void setmIsDeleteModel(boolean mIsDeleteModel) {
        this.mIsDeleteModel = mIsDeleteModel;
    }

    @Override
    public int getCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 创建或获取viewHolder
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_group_detail, null);
            holder.photo = (ImageView) convertView.findViewById(R.id.iv_member_photo);
            holder.name = (TextView) convertView.findViewById(R.id.tv_member_name);
            holder.delete = (ImageView) convertView.findViewById(R.id.iv_member_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            
            if(position !=  getCount() -1 && position !=getCount() -2){
                
                if(holder.name.getVisibility() == View.GONE) {
                    holder = new ViewHolder();
                    convertView = View.inflate(mContext, R.layout.item_group_detail, null);
                    holder.photo = (ImageView) convertView.findViewById(R.id.iv_member_photo);
                    holder.name = (TextView) convertView.findViewById(R.id.tv_member_name);
                    holder.delete = (ImageView) convertView.findViewById(R.id.iv_member_delete);
                    convertView.setTag(holder);
                }
            }

        }

        // 获取当前item数据

        // 显示数据
        // 判断是否有修改的权限
        if (mIsCanModify) {// 修改模式

            // 1 监听事件
            if (position == getCount() - 1) {// 减号位置
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mIsDeleteModel) {
                            mIsDeleteModel = true;
                            notifyDataSetChanged();
                        }
                    }
                });
            } else if (position == getCount() - 2) {// 加号位置
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 逻辑复杂 不再这操作
                        mOnGroupDetailListener.onAddMembers();
                    }
                });
            } else {// 群成员的点击事件
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 不再这操作
                        mOnGroupDetailListener.onDeleteMember(mUsers.get(position));
                    }
                });
            }

            // 2 显示布局
            if (position == getCount() - 1) {// 减号
                if(mIsDeleteModel) {
                    convertView.setVisibility(View.INVISIBLE);
                }else {
                    convertView.setVisibility(View.VISIBLE);

                    // 设置数据
                    holder.photo.setImageResource(R.mipmap.em_smiley_minus_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.GONE);
                }
            } else if (position == getCount() - 2) {
                if(mIsDeleteModel) {
                    convertView.setVisibility(View.INVISIBLE);
                }else {
                    convertView.setVisibility(View.VISIBLE);

                    holder.photo.setImageResource(R.mipmap.em_smiley_add_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.name.setVisibility(View.GONE);
                }
            }else {
                // 获取当前item数据
                UserInfo user = mUsers.get(position);
                convertView.setVisibility(View.VISIBLE);
                holder.name.setVisibility(View.VISIBLE);
                holder.photo.setVisibility(View.VISIBLE);

                holder.name.setText(user.getName());
                holder.photo.setImageResource(R.drawable.em_default_avatar);

                if(mIsDeleteModel) {
                    // 布局设置
                    holder.delete.setVisibility(View.VISIBLE);
                }else {
                    holder.delete.setVisibility(View.GONE);
                }
            }
        } else {
            // 展示数据
            if (position == getCount() - 1 || position == getCount() - 2) {
                convertView.setVisibility(View.INVISIBLE);
            } else {
                convertView.setVisibility(View.VISIBLE);
                UserInfo userInfo = mUsers.get(position);

                holder.name.setText(userInfo.getName());
                holder.photo.setImageResource(R.drawable.em_default_avatar);
                holder.delete.setVisibility(View.GONE);
            }
        }

        // 返回view
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        ImageView photo;
        ImageView delete;
    }

    public interface OnGroupDetailListener {
        // 添加成员的方法
        void onAddMembers();

        // 删除群成员方法
        void onDeleteMember(UserInfo user);
    }
}
