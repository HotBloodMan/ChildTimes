package com.lee.privatecustom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.privatecustom.R;
import com.lee.privatecustom.entity.Item;
import com.lee.privatecustom.utils.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by 1 on 2016/10/25.
 */
public class WatchStoryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Item> listWatchSong;

    public WatchStoryAdapter(Context context, List<Item> watchSongInfosList) {
        this.context = context;
        this.listWatchSong = watchSongInfosList;
        Log.d("TAG","listWatchSong==>>> "+watchSongInfosList.toString());
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //试图初始化ImageLoader
        if(ImageLoader.isFirst()){
            ImageLoader.init(context);
        }
    }

    @Override
    public int getCount() {
        return listWatchSong.size()-6 ;
    }

    @Override
    public Object getItem(int i) {
        return listWatchSong.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_layout_story_babywatch,viewGroup,false);
            vh=new ViewHolder();
            vh.ivMainWatchPic = (ImageView)convertView. findViewById(R.id.iv_main_watch_story_name);
            vh.ivMainWatchRightDownLoad = (ImageView) convertView.findViewById(R.id.iv_main_watch_story_right_down);
            vh.tvWatchStoryName = (TextView) convertView.findViewById(R.id.tv_main_watch_story_name);
            vh.tvMainWatchPlayCount = (TextView) convertView.findViewById(R.id.tv_main_watch_playcount);
            vh.tvMainWatchArtist = (TextView) convertView.findViewById(R.id.tv_main_watch_artist);
            convertView.setTag(vh);
        }else{
             vh= (ViewHolder) convertView.getTag();
        }

        Item item = listWatchSong.get(i+6);
        Log.d("TAG","itme***55555555555= "+item.toString());
        vh.tvWatchStoryName.setText(i+1+"."+item.getName());
        vh.tvMainWatchArtist.setText(item.getArtist());
        vh.tvMainWatchPlayCount.setText(item.getPlaycnt()+"");
        //vh.ivMainWatchPic.setImageBitmap(BitmapFactory.de);
       //防止图片频繁变动
        String pic = item.getPic();
        ImageLoader.loadImage(vh.ivMainWatchPic,pic);
//        Bitmap bit=download(pic);
//        vh.ivMainWatchPic.setImageBitmap(bit);
        return convertView;
    }

    Bitmap bits = null;
    private Bitmap download(final String pic) {

        new Thread(){
            public void run() {
                try {
//step2. 创建能够进行网络访问的对象(HttpClient对象/HttpURLConnection对象)
                    URL url = new URL(pic);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//step3. 设定网络访问的方式(GET/POST)
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
//step4. 设定参数
//step5. 发起真正的请求，获得响应
                    connection.connect();
                    InputStream is = connection.getInputStream();
//step6. 解析响应内容
                    bits= BitmapFactory.decodeStream(is);
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }.start();

        return bits;
    }

    class ViewHolder{
        ImageView ivMainWatchPic,ivMainWatchRightDownLoad;
        TextView tvWatchStoryName,tvMainWatchPlayCount,tvMainWatchArtist;
    }
}
