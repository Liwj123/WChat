package com.example.lenovo.wchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.holder.ImageHolder;
import com.example.lenovo.wchat.holder.TextHolder;
import com.example.lenovo.wchat.holder.VideoHolder;
import com.example.lenovo.wchat.holder.VoiceHolder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

/**
 * Created by Lenovo on 2017/5/9.
 */

public class MessageAdapter extends RecyclerView.Adapter {

    private List<EMMessage> list;
    private Context context;

    public MessageAdapter(List<EMMessage> list, Context context) {
        this.list = list;
        this.context = context;
    }

    /**
     * 返回要加载的视图
     *
     * @param parent
     * @param viewType
     * @return recyclerView将要加载的视图资源
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new TextHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_message, parent, false));
            case 1:
                return new ImageHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_image, parent, false));
            case 2:
                return new VideoHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_video, parent, false));
            case 3:
                return new VoiceHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_voice, parent, false));
        }
        return new TextHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_message, parent, false));
    }

    /**
     * 判断所接收的消息类型，当做createViewHolder的参数
     *
     * @param position item的下标
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        EMMessage msg = list.get(position);
        switch (msg.getType()) {
            case TXT:
                return 0;
            case IMAGE:
                return 1;
            case VIDEO:
                return 2;
            case VOICE:
                return 3;
        }
        return -1;
    }

    /**
     * 给ViewHolder设置数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //得到当前的item数据
        EMMessage msg = list.get(position);
        //判断当前holder是什么类型
        if (holder instanceof TextHolder) {
            //强转并调用设置数据的方法
            ((TextHolder) holder).setView(context, msg);
        } else if (holder instanceof ImageHolder) {
            ((ImageHolder) holder).setView(context, msg);
        } else if (holder instanceof VoiceHolder) {
            ((VoiceHolder) holder).setView(context, msg);
        } else if (holder instanceof VideoHolder) {
            ((VideoHolder) holder).setView(context, msg);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
