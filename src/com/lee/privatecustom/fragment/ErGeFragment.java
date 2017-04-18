package com.lee.privatecustom.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lee.privatecustom.R;
import com.lee.privatecustom.adapter.ListerenErGeAdapter;
import com.lee.privatecustom.entity.ItemListeren;
import com.lee.privatecustom.entity.RootListeren;
import com.lee.privatecustom.service.MusicService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 宝宝听 儿歌
 */
public class ErGeFragment extends Fragment {

    public static final  String TAG=ErGeFragment.class.getSimpleName();
    List<ItemListeren> listerenErGeInfoList;
    private List<ItemListeren> list;

    private ListView lvListerenFrament;
    ListerenErGeAdapter adapter;

    public ErGeFragment() {
        // Required empty public constructor
    }
    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 3:
                    list = (List<ItemListeren>) msg.obj;
                    for(int i=1;i<list.size();i++){
                        String name = list.get(i).getName();
                        Log.d(TAG,"ccc----->>name "+name.toString());
                        String downurl = list.get(i).getDownurl();
                        Log.d(TAG,"ccc----->>downurl "+downurl.toString());
                    }
                    Log.d("TAG","erge----11111111" + list.toString());
                    adapter = new ListerenErGeAdapter(getActivity(), list);
                    lvListerenFrament.setAdapter(adapter);
                    clickListView();
            }
        }
    };
    private void clickListView() {
        lvListerenFrament.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String playurl = list.get(position+1).getDownurl();
                Log.d(TAG," ErGeFragment----->>playurl "+playurl);
                double duration = list.get(position).getDuration();
                Intent intent = new Intent(getActivity(), MusicService.class);
                intent.putExtra("play",playurl);
                intent.putExtra("duration",duration);
                getActivity().startService(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_er_ge, container, false);
        lvListerenFrament = (ListView) view.findViewById(R.id.lv_babylisteren_fragment_main_erge);
        Log.d("TAG","erge----**************1");
        initDatas();
        Log.d("TAG","erge----**************2");
        return  view;
    }
    private void initDatas() {
        listerenErGeInfoList = new ArrayList<ItemListeren>();
        doClick();
    }

    private void doClick() {
        new Thread(){
            public void run() {
                try {
                    Log.i("TAG","run()");
                    //path为网络路径(查看号码归属地)
                    String path="http://bb.shoujiduoduo.com/baby/bb.php?type=getlist&pagesize=30&listid=5";

                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    //将流转换为字符串
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while((line=br.readLine())!=null){
                        sb.append(line);
                    }
                    String results = sb.toString();
                    Log.d("TAG","results="+results);
                    getJsonData(results);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }
    private void getJsonData(String results) {
        //json解析部分
        Gson gson = new Gson();
        Log.d("TAG","erge----11111111111111**************");
        RootListeren rootListeren = gson.fromJson(results, RootListeren.class);
        Log.d("TAG","erge----22222222222222**************");
        listerenErGeInfoList.addAll(rootListeren.getList());
        Log.d("TAG","erge----33333333333333**************");
        Message message = new Message();
        message.what=3;
        message.obj = listerenErGeInfoList;
        h.sendMessage(message);
        Log.i("TAG", "erge----watch" + listerenErGeInfoList.size());
    }
}
