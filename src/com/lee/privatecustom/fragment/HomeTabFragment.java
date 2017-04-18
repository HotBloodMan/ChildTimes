package com.lee.privatecustom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.lee.privatecustom.R;
import com.lee.privatecustom.activity.ShowVideoActivity;
import com.lee.privatecustom.adapter.WatchSongAdapter;
import com.lee.privatecustom.entity.Item;
import com.lee.privatecustom.entity.Root;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeTabFragment extends BaseFragment {
	
	private static final String TAG = "HomeTabFragment";
	private Activity mActivity;
	private String mMsgName;
	private ListView lvWatchMain;
	private List<Item> watchSongInfosList;
	private List<Item> list;
	private WatchSongAdapter adapter;
	private ProgressBar pBar;
	public void setMsgName(String msgName) {
		this.mMsgName = msgName;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	Handler h=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			pBar.setVisibility(View.GONE);
			switch (msg.what){
				case 1:
					list = (List<Item>) msg.obj;
					Log.d("TAG","11111111" + list.toString());
					adapter = new WatchSongAdapter(getActivity(), list);
					lvWatchMain.setAdapter(adapter);
					clickListView();
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home_tab, container, false);
		lvWatchMain = (ListView) view.findViewById(R.id.lv_watch_main_erge);
		pBar = (ProgressBar) view.findViewById(R.id.pBar);
		Log.d("TAG","**************1");
		watchSongInfosList = new ArrayList<Item>();
		doClick();
		return view;
	}
	//播放儿歌
	public void clickListView(){
		lvWatchMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String playurl = list.get(position).getDownurl();
				double duration = list.get(position).getDuration();
				Intent intent = new Intent(getActivity(), ShowVideoActivity.class);
				intent.putExtra("play",playurl);
				intent.putExtra("duration",duration);
				startActivity(intent);
			}
		});
	}

	private void initDatas() {
		watchSongInfosList = new ArrayList<Item>();
		doClick();
		adapter = new WatchSongAdapter(getActivity(), watchSongInfosList);
	}

	private void doClick() {
		pBar.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				try {
					Log.i("TAG","run()");
//path              为网络路径(查看号码归属地)
					String path="http://bb.shoujiduoduo.com/baby/bb.php?type=getvideos&pagesize=30&collectid=29";

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
		Log.d("TAG","11111111111111**************");
		Root bean = gson.fromJson(results, Root.class);
		Log.d("TAG","22222222222222**************");
		watchSongInfosList.addAll(bean.getList());
		Log.d("TAG","33333333333333**************");
		Message message = new Message();
		message.what=1;
		message.obj = watchSongInfosList;
		h.sendMessage(message);
		Log.i("TAG", "watch" + watchSongInfosList.size());
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initDisplay();
	}
	
	private void initDisplay() {
	}

	@Override
	public String getFragmentName() {
		return TAG;
	}

}
