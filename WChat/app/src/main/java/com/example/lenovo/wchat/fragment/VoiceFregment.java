package com.example.lenovo.wchat.fragment;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.wchat.R;
import com.example.lenovo.wchat.Utils.TimerUtil;
import com.example.lenovo.wchat.act.MessageActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lenovo on 2017/5/30.
 */

public class VoiceFregment extends BaseFragment {

    private TextView time;
    private ImageView voice_img;
    private MediaRecorder mc;
    private static String VOICE_PATH;
    private static int voiceTime = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            voiceTime++;
            time.setText(TimerUtil.sendTime(voiceTime));
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    };

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
                        VOICE_PATH = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".amr";
                        prepare();
                        time.setText(TimerUtil.sendTime(voiceTime));
                        handler.sendEmptyMessageDelayed(1, 1000);
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
                        handler.removeMessages(1);
                        time.setText("按下后开始录音");
                        if (voiceTime > 0) {
                            ((MessageActivity) getActivity()).createVoice(VOICE_PATH, voiceTime);
                        } else {
                            Toast.makeText(getActivity(), "按键时间太短", Toast.LENGTH_SHORT).show();
                        }
                        voiceTime = 0;
                        VOICE_PATH = null;
                        break;
                }
                return true;
            }
        });
    }


    private void prepare() {
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
    }

    private void initView(View view) {
        voice_img = (ImageView) view.findViewById(R.id.voice_img);
        time = (TextView) view.findViewById(R.id.voice_time);
    }
}
