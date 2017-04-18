package com.lee.privatecustom.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lee.privatecustom.R;
import com.lee.privatecustom.adapter.ListerenStoryAdapter;
import com.lee.privatecustom.entity.ItemListeren;
import com.lee.privatecustom.entity.RootListeren;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 宝宝听 故事
 */
public class StoryFragment extends Fragment {
    List<ItemListeren> listerenStoryInfoList;
    private List<ItemListeren> list;

    private ListView lvListerenFrament;
    ListerenStoryAdapter adapter;

    public StoryFragment() {
        // Required empty public constructor
    }
    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 3:
                    list = (List<ItemListeren>) msg.obj;
                    Log.d("TAG","erge----11111111" + list.toString());
                    adapter = new ListerenStoryAdapter(getActivity(), list);
                    lvListerenFrament.setAdapter(adapter);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        lvListerenFrament = (ListView) view.findViewById(R.id.lv_babylisteren_fragment_main_story);
        Log.d("TAG","erge----**************1");
        initDatas();
        Log.d("TAG","erge----**************2");
        return view;
    }
    private void initDatas() {
        listerenStoryInfoList = new ArrayList<ItemListeren>();
        doClick();
    }
    private void doClick() {
        new Thread(){
            public void run() {
                try {
                    Log.i("TAG","run()");
                    //path为网络路径(查看号码归属地)
                    String path="http://bb.shoujiduoduo.com/baby/bb.php?type=getlist&pagesize=30&listid=6";

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
        listerenStoryInfoList.addAll(rootListeren.getList());
        Log.d("TAG","erge----33333333333333**************");
        Message message = new Message();
        message.what=3;
        message.obj = listerenStoryInfoList;
        h.sendMessage(message);
        Log.i("TAG", "erge----watch" + listerenStoryInfoList.size());
    }

}
