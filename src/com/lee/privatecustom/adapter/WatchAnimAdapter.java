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
import com.lee.privatecustom.entity.ItemAnim;
import com.lee.privatecustom.utils.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by 1 on 2016/11/23.
 */
public class WatchAnimAdapter  extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<ItemAnim> listWatchAnim;

    public WatchAnimAdapter(Context context, List<ItemAnim> watchSongInfosList) {
        this.context = context;
        this.listWatchAnim = watchSongInfosList;
        Log.d("TAG","listWatchSong==>>> "+watchSongInfosList.toString());
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //试图初始化ImageLoader
        if(ImageLoader.isFirst()){
            ImageLoader.init(context);
        }
    }

    @Override
    public int getCount() {
        return listWatchAnim.size()-1 ;
    }

    @Override
    public Object getItem(int i) {
        return listWatchAnim.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_watch_anim_layout,viewGroup,false);
            vh=new ViewHolder();
            vh.ivAnimName= (ImageView) convertView.findViewById(R.id.iv_anima);
            vh.tvAnimScore= (TextView) convertView.findViewById(R.id.score_anima);
            vh.tvAnimName= (TextView) convertView.findViewById(R.id.tv_anima_name);
            vh.tvAnimAlbum= (TextView) convertView.findViewById(R.id.tv_anim_album);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        ItemAnim item1 = listWatchAnim.get(i+1);
        Log.d("TAG","itme***55555555555= "+item1.toString());
        vh.tvAnimName.setText(item1.getName());
        vh.tvAnimScore.setText(item1.getScore()+"");
        vh.tvAnimAlbum.setText(item1.getTracks()+"集");

        String pic = item1.getPic();

        //防止图片频繁变动
        ImageLoader.loadImage(vh.ivAnimName,pic);
//        Bitmap bit=download(pic);
//        vh.ivAnimName.setImageBitmap(bit);
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
        ImageView ivAnimName;
        TextView tvAnimScore,tvAnimName,tvAnimAlbum;
    }
}
