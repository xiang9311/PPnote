package com.example.ppnote;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.annotation.TargetApi;
import android.os.Build;

public class SectionActivity extends FragmentActivity implements PageTransformer {
	public static final String TAG = "SectionActivity";
	public static SectionActivity that = null;
	private ViewPager vp_section;
	private RadioButton rb0;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb[];
	
	private SectionPagerAdapter sectionPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_section);
		// Show the Up button in the action bar.
		that = this;
//		setupActionBar();
		
		init();
	}
	
	private void init(){
		vp_section = (ViewPager) findViewById(R.id.vp_section);
		sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
		vp_section.setAdapter(sectionPagerAdapter);
		vp_section.setPageTransformer(true, this);
		vp_section.setCurrentItem(1);
		
		rb = new RadioButton[3];
		rb0 = (RadioButton) findViewById(R.id.radio0);
		rb1 = (RadioButton) findViewById(R.id.radio1);
		rb2 = (RadioButton) findViewById(R.id.radio2);
		rb[0] = rb0;rb[1] = rb1;rb[2] = rb2;
//		rb1.setAlpha(0.5f);
		rb2.setAlpha(0.5f);
		rb0.setAlpha(0.5f);
		
		rb0.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				vp_section.setCurrentItem(0);
			}
		});
		rb1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				vp_section.setCurrentItem(1);
			}
		});
		rb2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				vp_section.setCurrentItem(2);
			}
		});
		vp_section.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				//arg = 0表示什么都没做  1表示正在滑动  2表示滑动完毕
//				Log.d(TAG,"滑动状态："+arg0);
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				arg0 :当前页面，及你点击滑动的页面
//				arg1:当前页面偏移的百分比
//				arg2:当前页面偏移的像素位置
				
				arg1 = arg1 < 0? 0 : arg1;
				arg1 = arg1 > 1? 1 : arg1;
				int current = vp_section.getCurrentItem();    //当前会变化
				int next = arg0;
				if(current == next && current < SectionFragment.COUNT - 1){
					rb[current+1].setAlpha(arg1*0.6f+0.4f);
					rb[current].setAlpha((1-arg1)*0.6f+0.4f);
				}
				else if(current > next){
					rb[current].setAlpha(arg1*0.6f+0.4f);
					rb[next].setAlpha((1-arg1)*0.6f+0.4f);
				}
				else{
//					Log.d(TAG,"不能往右划了");
				}
			}

			@Override
			public void onPageSelected(int arg0) {
			}
			
		});
	}
	
	public class SectionPagerAdapter extends FragmentPagerAdapter{

		public SectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Log.d(TAG,"sectionpag getItem: " + arg0);
			Fragment fragment = new SectionFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(SectionFragment.ARG_SECTION_NUMBER, arg0);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.view.ViewPager.PageTransformer#transformPage(android.view.View, float)
	 */
	@Override
	public void transformPage(View view, float position) {
		/**
		 * 当前页面是0，左边是-1、-2、、、右边是1、2、3、、、
		 */
		float minAlpha = 0.5f;
		if(position < -1){//[-Infinity,-1)
			view.setAlpha(0*(1.0f-minAlpha) + minAlpha);
		}
		else if(position <= 0){// [-1,0]  
			view.setAlpha((1+position)*(1.0f-minAlpha) + minAlpha);
				
		}
		else if(position <= 1){// (0,1]  
			view.setAlpha((1-position)*(1.0f-minAlpha) + minAlpha);
		}
		else{
			view.setAlpha(0*(1.0f-minAlpha) + minAlpha);
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.section, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
