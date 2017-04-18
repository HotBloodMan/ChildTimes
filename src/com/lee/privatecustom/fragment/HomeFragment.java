package com.lee.privatecustom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.privatecustom.R;
import com.lee.privatecustom.view.TopIndicator;
import com.lee.privatecustom.view.TopIndicator.OnTopIndicatorListener;

public class HomeFragment extends BaseFragment implements OnTopIndicatorListener {

	public static final String TAG = "HomeFragment";
	private Activity mActivity;
	private TextView mTitleTv;
	private ViewPager mViewPager;
	private TabPagerAdapter mPagerAdapter;
	private TopIndicator mTopIndicator;

	public static HomeFragment newInstance() {
		HomeFragment homeFragment = new HomeFragment();

		return homeFragment;
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
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		return view;
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
		mTitleTv = (TextView) view.findViewById(R.id.title_tv);
		mTitleTv.setText(R.string.babywatch);
		
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		mPagerAdapter = new TabPagerAdapter(getFragmentManager());
		
		mTopIndicator = (TopIndicator) view.findViewById(R.id.top_indicator);
		mTopIndicator.setOnTopIndicatorListener(this);
	}
	
	private void initDisplay() {
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.invalidate();
		mPagerAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public String getFragmentName() {
		return TAG;
	}

	private class TabPagerAdapter extends FragmentStatePagerAdapter implements
			ViewPager.OnPageChangeListener {

		public TabPagerAdapter(FragmentManager fm) {
			super(fm);
			mViewPager.setOnPageChangeListener(this);
		}

		@Override
		public Fragment getItem(int position) {
			
			
//			HomeTabFragment fragment = (HomeTabFragment) Fragment
//				.instantiate(mActivity,
//						HomeTabFragment.class.getName());
//		   fragment.setMsgName("message name " + position);

			Log.d(TAG,"cc 77777777777 getItem.pos="+position);
			if(position==0){
				return new HomeTabFragment();
			}else if(position==1){
				return new HomeTab2Fragment();
			}else if(position==2){
				return new HomeTab4Fragment();
			}
			
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
		}

		@Override
		public void onPageSelected(int position) {
			mTopIndicator.setTabsDisplay(mActivity, position);
			switch (position) {
			case 0:
				Toast.makeText(getActivity(), "...0000",Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(getActivity(), "...1111",Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(getActivity(), "...2222",Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void onIndicatorSelected(int index) {
		mViewPager.setCurrentItem(index);
	}

}
