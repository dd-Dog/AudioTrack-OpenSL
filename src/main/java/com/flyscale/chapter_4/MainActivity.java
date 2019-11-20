package com.flyscale.chapter_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        System.loadLibrary("soundtrack");
    }

    private static String TAG = "MainActivity";
    private OpenSLESSoundPlayer openSLESSoundPlayer;
    private Button audioTrackPlayBtn;
    private Button audioTrackStopBtn;
    private Button openSLESPlayBtn;
    private Button openSLESStopBtn;
    /**
     * 要播放的文件路径
     **/
    private static String playFilePath = "/mnt/sdcard/tonghuazhen.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openSLESSoundPlayer = new OpenSLESSoundPlayer();
        Log.d(this.getClass().getSimpleName(), OpenSLESSoundPlayer.getStringFromJNI("Hello!"));
        findAndBindView();
    }


    private void findAndBindView() {
        audioTrackPlayBtn = (Button) findViewById(R.id.play_audiotrack_btn);
        audioTrackStopBtn = (Button) findViewById(R.id.stop_audiotrack_btn);
        openSLESPlayBtn = (Button) findViewById(R.id.play_opensl_es_btn);
        openSLESStopBtn = (Button) findViewById(R.id.stop_opensl_es_btn);

        audioTrackPlayBtn.setOnClickListener(this);
        audioTrackStopBtn.setOnClickListener(this);
        openSLESPlayBtn.setOnClickListener(this);
        openSLESStopBtn.setOnClickListener(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                openslStart();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                openslStop();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        /*    case R.id.play_audiotrack_btn:
                Log.i(TAG, "Click AudioTrack Play Btn");
                openSLESSoundPlayer = new NativeMp3PlayerController();
                openSLESSoundPlayer.setHandler(handler);
                openSLESSoundPlayer.setAudioDataSource(playFilePath);
                openSLESSoundPlayer.start();
                break;
            case R.id.stop_audiotrack_btn:
                Log.i(TAG, "Click AudioTrack Stop Btn");
                // 普通AudioTrack的停止播放
                if (null != openSLESSoundPlayer) {
                    openSLESSoundPlayer.stop();
                    openSLESSoundPlayer = null;
                }
                break;*/
            case R.id.play_opensl_es_btn:
                openslStart();
                break;
            case R.id.stop_opensl_es_btn:
                openslStop();
                break;

        }
    }

    private void openslStop() {
        Log.i(TAG, "Click OpenSL ES Stop Btn");
        if (null != openSLESSoundPlayer) {
            openSLESSoundPlayer.stop();
            openSLESSoundPlayer = null;
        }
    }

    private void openslStart() {
        Log.i(TAG, "Click OpenSL ES Play Btn");
        // OpenSL EL初始化播放器
        if (openSLESSoundPlayer!=null) {
            openSLESSoundPlayer.setAudioDataSource(playFilePath, 0.2f);
            // OpenSL EL进行播放
            openSLESSoundPlayer.play();
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 计算当前时间
            int _time = Math.max(msg.arg1, 0) / 1000;
            int total_time = Math.max(msg.arg2, 0) / 1000;
            float ratio = (float) _time / (float) total_time;
            Log.i(TAG, "Play Progress : " + ratio);
        }
    };

}
