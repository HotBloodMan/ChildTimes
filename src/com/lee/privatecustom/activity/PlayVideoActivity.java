package com.lee.privatecustom.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lee.privatecustom.R;

import java.io.IOException;

public class PlayVideoActivity extends Activity implements View.OnClickListener {


  //  private EditText et_path;

    private MediaPlayer mediaPlayer;

    private Button bt_play,bt_pause,bt_stop,bt_replay;

    private SurfaceView sv;
    private SurfaceHolder holder;

    private int position;
    //private String filepath;

    String playUrl;
    String videoDuration;
    private ImageButton videoForward;
    private ImageButton videoPlay;
    private ImageButton videoNext;
    private ImageButton videoPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
       //拿数据
        Intent intent = getIntent();
        playUrl = intent.getStringExtra("play");
        videoDuration = intent.getStringExtra("duration");

//        bt_play = (Button) findViewById(R.id.bt_play);
//        bt_pause = (Button) findViewById(R.id.bt_pause);
//        bt_stop = (Button) findViewById(R.id.bt_stop);
//        bt_replay = (Button) findViewById(R.id.bt_replay);

        videoForward = (ImageButton) findViewById(R.id.playvideo_forward);
        videoPlay = (ImageButton) findViewById(R.id.playvideo_play);
        videoNext = (ImageButton) findViewById(R.id.playvideo_next);
        videoPause = (ImageButton) findViewById(R.id.playvideo_pause);

        videoForward.setOnClickListener(this);
        videoPlay.setOnClickListener(this);
        videoNext.setOnClickListener(this);
        videoPause.setOnClickListener(this);

        //得到surfaceview
        sv = (SurfaceView) findViewById(R.id.sv);
        //得到显示界面内容的容器
        holder = sv.getHolder();
        //在低版本模拟器上运行记得加上下面的参数。不自己维护双缓冲区，而是等待多媒体播放框架主动的推送数据。
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("destoryed");
                if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    position = mediaPlayer.getCurrentPosition();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                System.out.println("created");
                if(position>0){//记录的有播放进度。
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(playUrl);//设置播放的数据源。
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDisplay(holder);
                        mediaPlayer.prepare();//准备开始播放 播放的逻辑是c代码在新的线程里面执行。
                        mediaPlayer.start();
                        mediaPlayer.seekTo(position);
                        bt_play.setEnabled(false);
                        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                bt_play.setEnabled(true);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                System.out.println("changed");
            }
        });

    }

    /**
     * 播放
     *
     */
    public void play() {
        //File file = new File(filepath);
        if(playUrl.startsWith("http://")){
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(playUrl);//设置播放的数据源。
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay(holder);
                mediaPlayer.prepare();//准备开始播放 播放的逻辑是c代码在新的线程里面执行。
                mediaPlayer.start();
                bt_play.setEnabled(false);
                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        bt_play.setEnabled(true);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "文件不存在，请检查文件的路径", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 暂停
     *
     */
    public void pause() {
        if("继续".equals(bt_pause.getText().toString())){
            mediaPlayer.start();
            bt_pause.setText("暂停");
            return;
        }
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            bt_pause.setText("继续");
        }
    }
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
//    /**
//     * 重播
//     * @param view
//     */
//    public void replay(View view) {
//        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
//            mediaPlayer.seekTo(0);
//        }else{
//            play(view);
//        }
//        bt_pause.setText("暂停");
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.playvideo_forward:
                int t=position-5;
                if(t>=10){
                    mediaPlayer.seekTo(position-5);
                }
            break;

            case R.id.playvideo_play:
//               if(mediaPlayer!=null&&mediaPlayer.isPlaying()) {
//                   mediaPlayer.stop();
//                   videoPlay.setVisibility(View.GONE);
//                   videoPause.setVisibility(View.VISIBLE);
//                   mediaPlayer.release();
//                   mediaPlayer = null;
//               }
                play();
                videoPlay.setVisibility(View.GONE);
                videoPause.setVisibility(View.VISIBLE);
            break;
            case R.id.playvideo_pause:
                mediaPlayer.pause();
                videoPlay.setVisibility(View.VISIBLE);
                videoPause.setVisibility(View.GONE);
                break;
            case R.id.playvideo_next:
                int s=position+5;
                if(s<=100){
                    mediaPlayer.seekTo(position-5);
                }
                mediaPlayer.seekTo(s);
            break;
        }
    }
}
