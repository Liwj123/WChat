package com.example.lenovo.wchat.fragment;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lenovo.wchat.R;

import java.io.IOException;

/**
 * Created by Lenovo on 2017/5/30.
 */

public class VoiceFregment extends BaseFragment {

    private ImageView voice_img;
    private MediaRecorder mc;
    private static final String VOICE_PATH = "sdcard/luyin.3gp";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        voice_img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            // 实例化录音机类
                            mc = new MediaRecorder();
                            // 设置mc要采集的音源
                            mc.setAudioSource(MediaRecorder.AudioSource.MIC);
                            // 设置mc保存输出的格式
                            mc.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                            // 设置mc保存的文件的位置
                            mc.setOutputFile(VOICE_PATH);
                            // 设置mc文件的编码格式（和文件的输出格式要一致）
                            mc.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            // 录音机准备
                            mc.prepare();
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        // 开启录音机
                        mc.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 判断mc的状态是否为空
                        if (mc != null) {
                            // 关闭录音机
                            mc.stop();
                            // 将录音机资源释放
                            mc.release();
                            // 从新给mc赋值成空，方便再一次的使用
                            mc = null;
                        }

                        break;
                }
                return true;
            }
        });
    }

    private void initView(View view) {
        voice_img = (ImageView) view.findViewById(R.id.voice_img);
    }
}
