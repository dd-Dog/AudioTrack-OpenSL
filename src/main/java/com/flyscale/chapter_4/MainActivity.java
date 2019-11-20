package com.flyscale.chapter_4;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
    }

    private static int SAMPLE_RATE = 44100;
    private static String PCM_FILE = "tonghuazhen_part.pcm";


    private void playPCMWithAudioTrack(String pcmFilePath) {


        //获取最小缓冲区大小
        int minBufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,    //采样率
                AudioFormat.CHANNEL_OUT_STEREO, //双声道
                AudioFormat.ENCODING_PCM_16BIT  //采样格式
        );

        //初始化AudioTrack对象
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,   //媒体类型
                SAMPLE_RATE,  //采样率
                AudioFormat.CHANNEL_IN_STEREO, //双声道
                AudioFormat.ENCODING_PCM_16BIT, //采样格式
                minBufferSize,  //缓冲区大小
                AudioTrack.MODE_STREAM  //流式加载
        );

        //先启动播放
        audioTrack.play();

        //从文件读取PCM数据
        InputStream is = null;
        DataInputStream dis = null;
        try {
            byte[] buffer = new byte[minBufferSize * 3];
            is = getAssets().open(pcmFilePath);
            dis = new DataInputStream(is);
            int readCount = 0;
            while (dis.available() > 0) {
                readCount = dis.read(buffer);

                Log.d(MainActivity.class.getSimpleName(), "readCount=" + readCount);
                if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                    continue;
                }
                if (readCount != 0 && readCount != -1) {
                    audioTrack.write(buffer, 0, readCount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            audioTrack.stop();
            audioTrack.release();
            try {
                if (is != null)
                    is.close();
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    playPCMWithAudioTrack(PCM_FILE);
                }
            }).start();
        }
    }
}
