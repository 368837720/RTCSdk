package com.yaya.demo.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yaya.demo.R;
import com.yaya.sdk.tlv.protocol.message.TextMessageNotify;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ober on 2016/11/17.
 */
class SimpleChatAdapter extends BaseAdapter {

    private List<TextMessageNotify> mData;

    SimpleChatAdapter() {}

    void setData(List<TextMessageNotify> data) {
        mData = data;
        notifyDataSetChanged();
    }

    void addData(TextMessageNotify msg) {
        if(mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_msg, parent, false);
            holder = new Holder();
            holder.tvUser = (TextView) convertView.findViewById(R.id.tv_user);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        TextMessageNotify msg = (TextMessageNotify) getItem(position);
        holder.tvUser.setText(String.valueOf(msg.getYunvaId()) + ":");
        holder.tvContent.setText(msg.getText() == null ? "" : msg.getText());
        return convertView;
    }

    static class Holder {
        TextView tvUser;
        TextView tvContent;
    }
}
