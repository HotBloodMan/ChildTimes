package com.lee.privatecustom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lee.privatecustom.R;
import com.lee.privatecustom.fragment.AnimFragment.OnFragmentDismissListener;


//宝宝听Fragment
public class CategoryFragment extends BaseFragment implements
		OnFragmentDismissListener, RadioGroup.OnCheckedChangeListener {

	private static final String TAG = "CategoryFragment";
	private Activity mActivity;
	private TextView mTitleTv;
	private RadioGroup rgBaByListeren;
	public static int mIndex=0;

	public static CategoryFragment newInstance() {
		CategoryFragment categoryFragment = new CategoryFragment();

		return categoryFragment;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_category, container,
				false);
		rgBaByListeren = (RadioGroup) view.findViewById(R.id.rg_babylisteren);
		iniFragment();
		rgBaByListeren.setOnCheckedChangeListener(this);
		return view;
	}

	Fragment[] mFragments;
	private void iniFragment() {
		//儿歌
		ErGeFragment ergeFragment=new ErGeFragment();
		//故事
		StoryFragment storyFragment = new StoryFragment();
		//添加到数组中
		mFragments=new Fragment[]{ergeFragment,storyFragment};
		FragmentManager fragmentManager = getFragmentManager();
		//开启事务
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.add(R.id.fl_babylisteren,ergeFragment);
		ft.commit();
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

		mTitleTv = (TextView) view.findViewById(R.id.title_tv);
		mTitleTv.setText(R.string.babylistener);


	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public String getFragmentName() {
		return TAG;
	}

	private void dismissAnimFragment() {
		getFragmentManager().popBackStack();
	}

	@Override
	public void onFragmentDismiss() {
		dismissAnimFragment();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int id = group.getCheckedRadioButtonId();
		switch (id){
			case R.id.rb_babylisteren_fragment:
				setSearchFragment(0);
				break;
			case R.id.rb_babystory_fragment:
				setSearchFragment(1);
		}
	}

	private void setSearchFragment(int index) {
		if(mIndex==index){
			return;
		}
		//======
		FragmentTransaction fts = getFragmentManager().beginTransaction();
		fts.hide(mFragments[mIndex]);
		if(!mFragments[index].isAdded()){
			fts.add(R.id.fl_babylisteren,mFragments[index]).show(mFragments[index]);
		}else{
			fts.show(mFragments[index]);
		}
		fts.commit();
		mIndex=index;
	}
}
