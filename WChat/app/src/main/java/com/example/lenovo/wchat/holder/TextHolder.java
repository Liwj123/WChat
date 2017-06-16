package com.example.lenovo.wchat.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.Utils.StringUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

/**
 * 文本消息的ViewHolder
 * Created by Lenovo on 2017/5/23.
 */

public class TextHolder extends RecyclerView.ViewHolder {
    private TextView message_time, message_left_message, message_right_message;
    private ImageView message_left_head, message_right_head;
    private View left_rl, right_rl;

    public TextHolder(View itemView) {
        super(itemView);
        message_time = (TextView) itemView.findViewById(R.id.item_message_time);
        message_left_message = (TextView) itemView.findViewById(R.id.item_message_left_message);
        message_right_message = (TextView) itemView.findViewById(R.id.item_message_right_message);
        message_left_head = (ImageView) itemView.findViewById(R.id.item_message_left_head);
        message_right_head = (ImageView) itemView.findViewById(R.id.item_message_right_head);
        left_rl = itemView.findViewById(R.id.item_message_left_rl);
        right_rl = itemView.findViewById(R.id.item_message_right_rl);
    }

    public void setView(Context context, EMMessage msg) {
        //判断发送消息的人
        if (msg.getFrom().equals(EMClient.getInstance().getCurrentUser())) {
            right_rl.setVisibility(View.VISIBLE);
            left_rl.setVisibility(View.GONE);
            if (msg.getType() == EMMessage.Type.TXT) {
                EMTextMessageBody txtMsg = (EMTextMessageBody) msg.getBody();
                message_right_message.setText(StringUtil.getExpressionString(context,txtMsg.getMessage()));
            }
        } else {
            left_rl.setVisibility(View.VISIBLE);
            right_rl.setVisibility(View.GONE);
            if (msg.getType() == EMMessage.Type.TXT) {
                EMTextMessageBody txtMsg = (EMTextMessageBody) msg.getBody();
                message_left_message.setText(StringUtil.getExpressionString(context,txtMsg.getMessage()));
            }
        }
    }
}
