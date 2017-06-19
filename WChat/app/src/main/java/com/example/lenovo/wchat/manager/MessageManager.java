package com.example.lenovo.wchat.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.lenovo.wchat.callback.IMessageList;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.id.message;

/**
 * Created by Lenovo on 2017/5/10.
 */

public class MessageManager {
    private IMessageList iMessageList;
    private static MessageManager messageManager = new MessageManager();

    public static MessageManager getInstance() {
        return messageManager;
    }

    public IMessageList getiMessageList() {
        return iMessageList;
    }

    public void setiMessageList(IMessageList iMessageList) {
        this.iMessageList = iMessageList;
    }

    /**
     * 创建图片消息并发送
     *
     * @param path 图片路径
     * @param name 接收方的用户名
     * @param type
     */
    public EMMessage createImage(String path, String name, EMMessage.ChatType type) {
        EMMessage msg = EMMessage.createImageSendMessage(path
                , false  //是否发送原图
                , name);
        sendMessage(msg, type);
        return msg;
    }

    /**
     * 创建文本消息并发送
     *
     * @param content 上下文
     * @param name    接收方的用户名
     * @param type
     */
    public EMMessage createTxt(String content, String name, EMMessage.ChatType type) {
        EMMessage msg = EMMessage.createTxtSendMessage(content, name);
        sendMessage(msg, type);
        return msg;
    }

    /**
     * 创建语音消息并发送
     *
     * @param path 语音路径
     * @param time 录音时间
     * @param name 接收方的用户名
     * @param type
     */
    public EMMessage createVoice(String path, int time, String name, EMMessage.ChatType type) {
        EMMessage msg = EMMessage.createVoiceSendMessage(path, time, name);
        sendMessage(msg, type);
        return msg;
    }

    /**
     * 创建视频消息并发送
     *
     * @param data
     * @param name
     * @param type
     */
    public EMMessage createVideo(Context context, Intent data, String name, EMMessage.ChatType type) {
        String videoPath = getVideoPath(context, data);
        int videoTiem = getVideoTime(videoPath);
        File file = getVideoImg(videoPath);
        EMMessage msg = EMMessage.createVideoSendMessage(videoPath
                , file.getAbsolutePath()
                , videoTiem
                , name);
        sendMessage(msg, type);
        return msg;
    }

    /**
     * 得到视频的第一张图片
     *
     * @param videoPath
     * @return
     */
    private File getVideoImg(String videoPath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(videoPath);
        Bitmap bitmap = mmr.getFrameAtTime(1000);

        String videoImgPath = "/" + System.currentTimeMillis() + ".jpg";
        File file = new File(Environment.getExternalStorageDirectory()
                , videoImgPath);

        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG
                    , 50
                    , fileOutputStream);
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
                bitmap.recycle();
                bitmap = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 得到视频的时间
     *
     * @param videoPath
     * @return
     */
    private int getVideoTime(String videoPath) {
        int videoTiem = 0;
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(videoPath);
            videoTiem = player.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.reset();
        player.release();
        player = null;
        return videoTiem;
    }

    /**
     * 得到视频的存储路径
     *
     * @param data
     * @return
     */
    private String getVideoPath(Context context, Intent data) {
        String videoPath = null;
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(data.getData()
                , new String[]{MediaStore.Video.Media.DATA}
                , null
                , null
                , null);

        if (c != null) {
            if (c.moveToNext()) {
                int index = c.getColumnIndex(MediaStore.Video.Media.DATA);
                videoPath = c.getString(index);
            }
        }
        return videoPath;
    }

    private void sendMessage(EMMessage msg, EMMessage.ChatType type) {
        msg.setChatType(type);
        EMClient.getInstance().chatManager().sendMessage(msg);

        //实现在消息界面刷新消息列表
        MessageManager.getInstance().getiMessageList().refChatList();
        //发送消息监听
        msg.setMessageStatusCallback(new EMCallBack() {
            //发送成功的监听
            @Override
            public void onSuccess() {
                Log.e("message", "onSuccess");
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

}
