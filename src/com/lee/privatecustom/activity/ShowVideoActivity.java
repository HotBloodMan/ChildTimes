package com.lee.privatecustom.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lee.privatecustom.R;
import com.lee.privatecustom.view.CustomMediaController;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class ShowVideoActivity extends Activity implements MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private Uri uri;
    private ProgressBar pb;
    private TextView downloadRateView,loadRateView;
    private MediaController mMediaController;
    private CustomMediaController mCustomMediaController;
    private VideoView mVideoView;

    String playUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = ShowVideoActivity.this.getWindow();
        window.setFlags(flag,flag);
        System.out.println("ShowVideoActivity---->>1111111111111");
        //必须写这个，初始化加载库文件
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_show_video);
        Intent intent = getIntent();
        playUrl = intent.getStringExtra("play");
        System.out.println("ShowVideoActivity---->>setContentView");
        initView();
        initData();
    }
    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.buffer);
        mMediaController = new MediaController(this);
        mCustomMediaController = new CustomMediaController(this, mVideoView, this);
        pb =  (ProgressBar) findViewById(R.id.probar);
        downloadRateView = (TextView) findViewById(R.id.download_rate);
        loadRateView= (TextView) findViewById(R.id.load_rate);
    }
    //初始化数据
    private void initData(){
//        String video = getIntent().getStringExtra("video");
//        String title = getIntent().getStringExtra("title");
//        mCustomMediaController.setVideoName(title);
        uri= Uri.parse(playUrl);
        mVideoView.setVideoURI(uri);//设置视频播放地址
        mVideoView.setMediaController(mCustomMediaController);
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//高画质
        mMediaController.show(5000);
        mVideoView.requestFocus();
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what){
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if(mVideoView.isPlaying()){
                    mVideoView.pause();
                    pb.setVisibility(View.VISIBLE);
                    downloadRateView.setText("");
                    loadRateView.setText("");
                    downloadRateView.setVisibility(View.VISIBLE);
                    loadRateView.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                mVideoView.start();
                pb.setVisibility(View.GONE);
                downloadRateView.setVisibility(View.GONE);
                loadRateView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                downloadRateView.setText(""+extra+"kb/s"+" ");
                break;
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        loadRateView.setText(percent+"%");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //屏幕切换时，设置全屏
        if(mVideoView!=null){
            mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE,0);
        }
        super.onConfigurationChanged(newConfig);
    }
}
