package com.example.lenovo.wchat.holder;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.act.VideoPlayActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Lenovo on 2017/6/14.
 */

public class VideoHolder extends RecyclerView.ViewHolder {
    private TextView message_time;
    private ImageView message_left_head, message_right_head, message_left_message, message_right_message;
    private View left_rl, right_rl, left_play, right_play;

    public VideoHolder(View itemView) {
        super(itemView);
        message_time = (TextView) itemView.findViewById(R.id.item_message_time);
        message_left_message = (ImageView) itemView.findViewById(R.id.item_message_left_message);
        message_right_message = (ImageView) itemView.findViewById(R.id.item_message_right_message);
        message_left_head = (ImageView) itemView.findViewById(R.id.item_message_left_head);
        message_right_head = (ImageView) itemView.findViewById(R.id.item_message_right_head);
        left_rl = itemView.findViewById(R.id.item_message_left_rl);
        right_rl = itemView.findViewById(R.id.item_message_right_rl);
        left_play = itemView.findViewById(R.id.item_video_left);
        right_play = itemView.findViewById(R.id.item_video_right);
    }

    public void setView(final Context context, final EMMessage msg) {
        //判断发送消息的人
        if (msg.getFrom().equals(EMClient.getInstance().getCurrentUser())) {
            right_rl.setVisibility(View.VISIBLE);
            left_rl.setVisibility(View.GONE);
            //判断消息的类型
            if (msg.getType() == EMMessage.Type.VIDEO) {
                EMVideoMessageBody videoBody = (EMVideoMessageBody) msg.getBody();
                String url = videoBody.getLocalUrl();
                if (url == null) {
                    url = videoBody.getRemoteUrl();
                }
                Glide.with(context)
                        .load(url)
                        .override(360, 720)
                        .into(message_right_message);
                final String finalUrl = url;
                right_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intentToVideo(context, finalUrl);
                    }
                });
            }
        } else {
            left_rl.setVisibility(View.VISIBLE);
            right_rl.setVisibility(View.GONE);
            if (msg.getType() == EMMessage.Type.VIDEO) {
                final EMVideoMessageBody videoBody = (EMVideoMessageBody) msg.getBody();
                final String url = videoBody.getRemoteUrl();
                Glide.with(context)
                        .load(url)
                        .override(360, 720)
                        .into(message_left_message);


                left_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = videoBody.getLocalUrl();
                        if (new File(path).exists()) {
                            intentToVideo(context, path);
                        } else {
                            //不存在则下载
                            final String locaPath = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".mp4";

                            //下载所需的密匙，固定格式
                            HashMap<String, String> map = new HashMap<String, String>();
                            if (!TextUtils.isEmpty(videoBody.getSecret())) {
                                map.put("share-secret", videoBody.getSecret());
                            }

                            //下载方法
                            EMClient.getInstance()
                                    .chatManager()
                                    .downloadFile(
                                            videoBody.getRemoteUrl()//下载的访问路径
                                            , locaPath//存储的路径
                                            , map//下载所需的密钥
                                            , new EMCallBack() {//下载的回调
                                                @Override
                                                public void onSuccess() {
                                                    //修改消息的本地路径
                                                    ((EMVideoMessageBody) msg.getBody()).setLocalUrl(locaPath);
                                                    //修改消息
                                                    EMClient.getInstance()
                                                            .chatManager()
                                                            .updateMessage(msg);
                                                }

                                                @Override
                                                public void onError(int i, String s) {

                                                }

                                                @Override
                                                public void onProgress(int i, String s) {

                                                }
                                            }
                                    );
                        }
                    }
                });
            }
        }
    }

    private void intentToVideo(Context context, String Url) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("video", Url);
        context.startActivity(intent);
    }
}
