package com.example.lenovo.wchat.act;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import com.example.lenovo.wchat.R;

import java.io.IOException;

/**
 * Created by Lenovo on 2017/6/14.
 */

public class VideoPlayActivity extends BaseActivity {

    private SurfaceView surface;
    private SeekBar seek;
    private MediaPlayer mediaPlayer;
    private String uri;
    private static final int PROGRESS_CHANGED_TIME = 1;
    private static final int VISIBLE_TIME = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS_CHANGED_TIME:
                    seek.setProgress(mediaPlayer.getCurrentPosition());
                    handler.sendEmptyMessageDelayed(PROGRESS_CHANGED_TIME, 500);
                    break;
                case VISIBLE_TIME:
                    seek.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
        initView();
        getIntentData();
        setSurfaceView();
        handler.sendEmptyMessageDelayed(VISIBLE_TIME,3000);
        surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    seek.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageDelayed(VISIBLE_TIME,3000);
                }
                return false;
            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

    }

    private void setSurfaceView() {
        surface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDisplay(surface.getHolder());
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(uri);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                            seek.setMax(mediaPlayer.getDuration());
                            handler.sendEmptyMessageDelayed(PROGRESS_CHANGED_TIME, 500);
                        }
                    });

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mediaPlayer.seekTo(0);
                            mediaPlayer.start();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });

    }

    private void getIntentData() {
        uri = getIntent().getStringExtra("video");
    }

    private void initView() {
        surface = (SurfaceView) findViewById(R.id.video_surface);
        seek = (SeekBar) findViewById(R.id.video_seek);
    }

}
