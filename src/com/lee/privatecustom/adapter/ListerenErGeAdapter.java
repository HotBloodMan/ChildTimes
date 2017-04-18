package com.lee.privatecustom.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lee.privatecustom.R;
import com.lee.privatecustom.entity.ItemListeren;

import java.util.List;

/**
 * Created by 1 on 2016/12/1.
 */
public class ListerenErGeAdapter extends BaseAdapter{


    private Context context;
    List<ItemListeren> listlisteren;
    LayoutInflater inflater;

    public ListerenErGeAdapter(Context context, List<ItemListeren> list) {
        this.context = context;
        this.listlisteren = list;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listlisteren.size()-1;
    }

    @Override
    public Object getItem(int position) {
        return listlisteren.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_layout_erge_babylisteren,parent,false);
            vh=new ViewHolder();
            vh.erId= (TextView) convertView.findViewById(R.id.tv_babylisteren_er_id);
            vh.erName= (TextView) convertView.findViewById(R.id.tv_main_babylisteren_er_name);
            vh.erArtist= (TextView) convertView.findViewById(R.id.tv_main_babylisteren_er_artist);
            //vh.erPlayCount= (TextView) convertView.findViewById(R.id.tv_main_babylisteren_er_playcount);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        ItemListeren itemListeren = listlisteren.get(position+1);
        Log.d("TAG"," itemListeren--->"+itemListeren.toString());
        vh.erId.setText(position+1+"");
        vh.erName.setText(itemListeren.getName());
        vh.erArtist.setText(itemListeren.getArtist());
        //vh.erPlayCount.setText(itemListeren.getPlaycnt());
        return convertView;
    }
    class ViewHolder {
        TextView erId,erName,erArtist,erPlayCount;
    }
}
