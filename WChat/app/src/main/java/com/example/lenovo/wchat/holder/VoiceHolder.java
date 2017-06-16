package com.example.lenovo.wchat.holder;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.Utils.TimerUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;

import java.io.IOException;

/**
 * Created by Lenovo on 2017/6/9.
 */

public class VoiceHolder extends RecyclerView.ViewHolder {

    private MediaPlayer mediaPlayer;
    private TextView message_time,message_left_time,message_right_time;
    private ImageView message_left_head, message_right_head, message_left_message, message_right_message;
    private View left_rl, right_rl, voice_ll;

    public VoiceHolder(View itemView) {
        super(itemView);
        voice_ll = itemView.findViewById(R.id.item_message_ll);
        message_time = (TextView) itemView.findViewById(R.id.item_message_time);
        message_left_message = (ImageView) itemView.findViewById(R.id.item_message_left_message);
        message_right_message = (ImageView) itemView.findViewById(R.id.item_message_right_message);
        message_left_head = (ImageView) itemView.findViewById(R.id.item_message_left_head);
        message_right_head = (ImageView) itemView.findViewById(R.id.item_message_right_head);
        left_rl = itemView.findViewById(R.id.item_message_left_rl);
        right_rl = itemView.findViewById(R.id.item_message_right_rl);
        message_left_time = (TextView) itemView.findViewById(R.id.item_message_left_time);
        message_right_time = (TextView) itemView.findViewById(R.id.item_message_right_time);
    }
    public void setView(Context context, final EMMessage msg) {
        //判断发送消息的人
        if (msg.getFrom().equals(EMClient.getInstance().getCurrentUser())) {
            right_rl.setVisibility(View.VISIBLE);
            left_rl.setVisibility(View.GONE);
            //判断消息的类型
            if (msg.getType() == EMMessage.Type.VOICE) {
                EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) msg.getBody();
                int length = voiceBody.getLength();
                message_right_time.setText(TimerUtil.voiceTime(length));
            }
        } else {
            left_rl.setVisibility(View.VISIBLE);
            right_rl.setVisibility(View.GONE);
            if (msg.getType() == EMMessage.Type.VOICE) {
                EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) msg.getBody();
                int length = voiceBody.getLength();
                message_left_time.setText(TimerUtil.voiceTime(length));
            }
        }

        voice_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMVoiceMessageBody body = (EMVoiceMessageBody) msg.getBody();
                if(msg.getFrom().equals(EMClient.getInstance().getCurrentUser())){
                    String url = body.getLocalUrl();
                    play(url);
                }else{
                    String url = body.getRemoteUrl();
                    play(url);
                }
            }
        });
    }

    private void play(String url){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
