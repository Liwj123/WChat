package com.example.lenovo.wchat.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.wchat.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

/**
 * Created by Lenovo on 2017/5/23.
 */

public class ImageHolder extends RecyclerView.ViewHolder {

    private TextView message_time;
    private ImageView message_left_head, message_right_head, message_left_message, message_right_message;
    private View left_rl, right_rl;

    public ImageHolder(View itemView) {
        super(itemView);
        message_time = (TextView) itemView.findViewById(R.id.item_message_time);
        message_left_message = (ImageView) itemView.findViewById(R.id.item_message_left_message);
        message_right_message = (ImageView) itemView.findViewById(R.id.item_message_right_message);
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
            //判断消息的类型
            if (msg.getType() == EMMessage.Type.IMAGE) {
                //取出图片消息体
                EMImageMessageBody imgBody = (EMImageMessageBody) msg.getBody();
                Glide.with(context)
                        .load(imgBody.getLocalUrl())
                        .into(message_right_message);
            }
        } else {
            left_rl.setVisibility(View.VISIBLE);
            right_rl.setVisibility(View.GONE);
            if (msg.getType() == EMMessage.Type.IMAGE) {
                EMImageMessageBody imgBody = (EMImageMessageBody) msg.getBody();
                Glide.with(context)
                        .load(imgBody.getThumbnailUrl())
                        .into(message_left_message);
            }
        }
    }
}
