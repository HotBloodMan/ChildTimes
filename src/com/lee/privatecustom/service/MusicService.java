package com.lee.privatecustom.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 1 on 2016/12/4.
 */
public class MusicService extends Service{

    public static final String TAG=MusicService.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    String playUrl=null;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();


        String playMusicUrl = intent.getStringExtra("play");
        mediaPlayer.stop();
        play(playMusicUrl);
        Log.d(TAG," ccc onStartCommand------>>>> ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 播放
     *
     */
    public void play(String playUrl) {
        Log.d(TAG,"play111");
        if(playUrl.startsWith("http://")){
            Log.d(TAG,"play222");
            try {

                mediaPlayer.setDataSource(playUrl);//设置播放的数据源。
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();//准备开始播放 播放的逻辑是c代码在新的线程里面执行。
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show();
            }
        }else{
            Log.d(TAG,"play3333");
            Toast.makeText(this, "文件不存在，请检查文件的路径", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 暂停
     *
     */
//    public void pause() {
//        if("继续".equals(bt_pause.getText().toString())){
//            mediaPlayer.start();
//            bt_pause.setText("暂停");
//            return;
//        }
//        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
//            mediaPlayer.pause();
//            bt_pause.setText("继续");
//        }
//    }


    //    /**
//     * 停止
//     * @param view
//     */
    public void stop() {
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
