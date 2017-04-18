package com.lee.privatecustom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lee.privatecustom.R;
import com.lee.privatecustom.adapter.WatchAnimAdapter;
import com.lee.privatecustom.entity.ItemAnim;
import com.lee.privatecustom.entity.RootAnim;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//宝宝看 动画片
public class HomeTab4Fragment extends BaseFragment {
	
	private static final String TAG = "HomeTab4Fragment";
	private Activity mActivity;
	private TextView mMsgTv;
	private String mMsgName;
	private GridView gvWatchMain;
	private List<ItemAnim> watchAnimInfosList;
	private WatchAnimAdapter adapter;
	private ProgressBar pBar;
	private List<ItemAnim> list;
	
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

			switch (msg.what){
				case 3:
					list = (List<ItemAnim>) msg.obj;
					Log.d("TAG","11111111" + list.toString());
					adapter = new WatchAnimAdapter(getActivity(), list);
					gvWatchMain.setAdapter(adapter);
					clickListView();
			}
		}
	};
	//播放故事
	public void clickListView(){
		gvWatchMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Todo
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
		View view = inflater.inflate(R.layout.fragment_home_tab4, container, false);
		gvWatchMain = (GridView) view.findViewById(R.id.gv_watch_animation);
		Log.d("TAG","**************1");
		initDatas();
		Log.d("TAG","**************2");
		return view;
	}
	private void initDatas() {
		watchAnimInfosList = new ArrayList<ItemAnim>();
		doClick();
	}
	private void doClick() {
		new Thread(){
			public void run() {
				try {
					Log.i("TAG","run()");
                   //path为网络路径(查看号码归属地)
					String path="http://bb.shoujiduoduo.com/baby/bb.php?type=getlistv2&act=home&pagesize=30&pid=26";

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
		RootAnim bean = gson.fromJson(results, RootAnim.class);
		Log.d("TAG","22222222222222**************");
		watchAnimInfosList.addAll(bean.getList());
		Log.d("TAG","33333333333333**************");
		Message message = new Message();
		message.what=3;
		message.obj = watchAnimInfosList;
		h.sendMessage(message);
		Log.i("TAG", "watch" + watchAnimInfosList.size());
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initDisplay();
	}
	
	private void initViews(View view) {
	}
	
	private void initDisplay() {
	}

	@Override
	public String getFragmentName() {
		return TAG;
	}

}
