package com.example.lenovo.wchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.Utils.StringUtil;
import com.example.lenovo.wchat.callback.RecyclerViewClick;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Lenovo on 2017/4/27.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<EMConversation> list;
    private RecyclerViewClick clickListener;
    private Map<String, String> testStr;

    public void setTextStr(Map<String, String> testStr) {
        this.testStr = testStr;
        notifyDataSetChanged();
    }

    public RecyclerAdapter(Context context, ArrayList<EMConversation> list) {
        this.context = context;
        this.list = list;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll;
        private ImageView iv;
        private TextView name;
        private TextView chat;
        private TextView time;
        private TextView info;
        private TextView caogao;

        //持有view中的控件对象
        public MyViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            iv = (ImageView) itemView.findViewById(R.id.item_chat_iv);
            name = (TextView) itemView.findViewById(R.id.item_chat_name);
            chat = (TextView) itemView.findViewById(R.id.item_chat_chat);
            time = (TextView) itemView.findViewById(R.id.item_chat_time);
            info = (TextView) itemView.findViewById(R.id.item_chat_info);
            caogao = (TextView) itemView.findViewById(R.id.item_chat_caogao);
        }
    }

    //实例化VeiwHolder，加载视图
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_chat, parent, false));
    }

    //绑定数据，加载每一个item中的信息
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //获取当前数据源对象
        EMConversation mData = list.get(position);
        //获得最后一条消息
        EMMessage lastMsg = mData.getLastMessage();
        //获得消息类型
        EMMessage.Type type = lastMsg.getType();
        //获得消息时间
        lastMsg.getMsgTime();
        //判断消息的类型
        switch (type) {
            case TXT:
                EMTextMessageBody txtMsg = (EMTextMessageBody) lastMsg.getBody();
                //消息内容
                holder.chat.setText(StringUtil.getExpressionString(context, txtMsg.getMessage()));
                break;
            case IMAGE:
                holder.chat.setText("[图片]");
                break;
            case VIDEO:
                holder.chat.setText("[视频]");
                break;
            case VOICE:
                holder.chat.setText("[语音]");
                break;
        }


        //获得未读消息数
        int unread = mData.getUnreadMsgCount();
        //会话名
        String userName = mData.getUserName();

        holder.name.setText(userName);

        if (unread > 99) {
            holder.info.setText("99+");
        } else {
            holder.info.setText("" + unread);
        }

        if (testStr != null && !TextUtils.isEmpty(testStr.get(userName))) {
            holder.chat.setText(testStr.get(userName));
            holder.caogao.setVisibility(View.VISIBLE);
        } else {
            holder.caogao.setVisibility(View.GONE);
        }

//        holder.info.setText(unread);

//        holder.name.setText("姓名");
//        holder.chat.setText("收到了最新消息");
//        holder.time.setText("12:12");
//        holder.info.setText("1");
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickListener(position);
            }
        });

        holder.ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return clickListener.onItemLongClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(RecyclerViewClick clickListener) {
        this.clickListener = clickListener;
    }
}
