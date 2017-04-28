package com.example.lenovo.wchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.wchat.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/4/27.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> list;

    public RecyclerAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView name;
        private TextView chat;
        private TextView time;
        private TextView info;

        //持有view中的控件对象
        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.item_chat_iv);
            name = (TextView) itemView.findViewById(R.id.item_chat_name);
            chat = (TextView) itemView.findViewById(R.id.item_chat_chat);
            time = (TextView) itemView.findViewById(R.id.item_chat_time);
            info = (TextView) itemView.findViewById(R.id.item_chat_info);
        }
    }

    //实例化VeiwHolder，加载视图
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_chat, parent, false));
    }

    //绑定数据，加载每一个item中的信息
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText("联系人");
        holder.chat.setText("收到了最新消息");
        holder.time.setText("12:12");
        holder.info.setText("1");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
