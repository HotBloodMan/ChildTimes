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
import com.lee.privatecustom.adapter.WatchStoryAdapter;
import com.lee.privatecustom.entity.Item;
import com.lee.privatecustom.entity.Root;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//宝宝看 故事
public class HomeTab2Fragment extends BaseFragment {
	
	private static final String TAG = "HomeTab2Fragment";
	private Activity mActivity;
	private String mMsgName;
	private ListView lvWatchMain;
	private List<Item> watchStoryInfosList;
	private WatchStoryAdapter adapter;
	private ProgressBar pBar;
	private List<Item> list;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}
	Handler h=new Handler(){
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what){
				case 2:
					list = (List<Item>) msg.obj;
					Log.d("TAG","11111111" + list.toString());
					adapter = new WatchStoryAdapter(getActivity(), list);
					lvWatchMain.setAdapter(adapter);
					clickListView();
			}
		}
	};
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home_tab2, container, false);
		lvWatchMain = (ListView) view.findViewById(R.id.lv_watch_main_story);
		pBar = (ProgressBar) view.findViewById(R.id.pBar);
		pBar.setVisibility(View.VISIBLE);
		Log.d("TAG","**************1");
		//initDatas();
		watchStoryInfosList = new ArrayList<Item>();
		doClick();
		adapter = new WatchStoryAdapter(getActivity(), watchStoryInfosList);
		pBar.setVisibility(View.GONE);
		Log.d("TAG","**************2");
		lvWatchMain.setAdapter(adapter);
		Log.d("TAG","**************3");
		return view;
	}
	private void initDatas() {
		watchStoryInfosList = new ArrayList<Item>();
		doClick();
		adapter = new WatchStoryAdapter(getActivity(), watchStoryInfosList);
	}
	private void doClick() {
		new Thread(){
			public void run() {
				try {
					Log.i("TAG","run()");
//path为网络路径(查看号码归属地)
					String path="http://bb.shoujiduoduo.com/baby/bb.php?type=getvideos&pagesize=30&collectid=27";

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
		watchStoryInfosList.addAll(bean.getList());
		Log.d("TAG","33333333333333**************");
		Message message = new Message();
		message.what=2;
		message.obj = watchStoryInfosList;
		h.sendMessage(message);
		Log.i("TAG", "watch" + watchStoryInfosList.size());
//		Iterator<Item> i = watchSongInfosList.iterator();
//		while (i.hasNext()) {
//			Item item = i.next();
//			Log.i("TAG", "item = " + item.getName());
//		}
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	private void initViews(View view) {
	}
	@Override
	public String getFragmentName() {
		return TAG;
	}

}
