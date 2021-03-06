package com.pictureit.leumi.main;

import utilities.view.CustomViewPager;
import utilities.view.SoftKeyboard;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.pictureit.leumi.animation.AnimationManager;
import com.pictureit.leumi.main.fragments.AdvanceSearch;
import com.pictureit.leumi.main.fragments.BaseProfileFragment;
import com.pictureit.leumi.main.fragments.HomeFragment;
import com.pictureit.leumi.main.fragments.RootAdvanceSearchFragment;
import com.pictureit.leumi.main.fragments.RootHomeFragment;
import com.pictureit.leumi.main.fragments.RootMyProfileFragment;

public class MainActivity extends FragmentActivity {
	
	private ViewGroup tabsButtonsContainer;
	Fragment fHome, fAdvanceSearch;
	Fragment fMyProfile;
	ViewGroup vgHome, vgMyProfile, vgAdvanceSearch;
	ImageView ivSwipeToOpenWebView;
	WebView wvMoreServices;

	FragmentPagerAdapter mAdapter;
	CustomViewPager mPager;
	
	private final int ROOT_VIEW_HOME = R.id.root_view_home;
	private final int ROOT_VIEW_ADVANCE_SEARCH = R.id.root_view_advance_search;
	private final int ROOT_VIEW_MY_PROFILE = R.id.root_view_my_profile;

	private final int HOME_FRAGMENT_ID = 0;
	private final int ADVANCE_SEARCH_FRAGMENT_ID = 1;
	private final int MY_PROFILE_FRAGMENT_ID = 2;
	
	private boolean isOkToFinishApp = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initUI();

	}
	
//	private void crashTest() {
//		ArrayList<String> arr = null;
//		arr.get(0);
//	}

	private void initUI() {
		mPager = (CustomViewPager) findViewById(R.id.pager);
		mAdapter = new myPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		
		vgHome = (ViewGroup) findViewById(R.id.button1);
		vgMyProfile = (ViewGroup) findViewById(R.id.button3);
		vgAdvanceSearch = (ViewGroup) findViewById(R.id.button2);
		
		wvMoreServices = (WebView) findViewById(R.id.wv_main_webview);
		
		ivSwipeToOpenWebView = (ImageView) findViewById(R.id.iv_btn_open_webview);
		tabsButtonsContainer = (ViewGroup) findViewById(R.id.tabsButtonsHost1);
		fHome = new HomeFragment();
		fAdvanceSearch = new AdvanceSearch();
		fMyProfile = new BaseProfileFragment();

		initListeners();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void initWebView(final String url) {
		wvMoreServices.getSettings().setJavaScriptEnabled(true);
		wvMoreServices.setWebViewClient(new WebViewClient()
		{
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        if (url != null && url.startsWith("http") && !url.contains("generalMobilePage/android")) {
		            view.getContext().startActivity(
		                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		            return true;
		        } else {
		            return false;
		        }
		    }
		});
		wvMoreServices.loadUrl(url);
	}
	
	public void onBanerTouch(String url){
		if (!url.startsWith("https://") && !url.startsWith("http://")){
		    url = "http://" + url;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
	
	public void addFragment(Fragment f) {
		
		int res = 0;
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		switch (mPager.getCurrentItem()) {
		case 0:
			res = ROOT_VIEW_HOME;
			break;
		case 1:
			res = ROOT_VIEW_ADVANCE_SEARCH;
			break;
		case 2:
			res = ROOT_VIEW_MY_PROFILE;
			break;
		}
		
			
		t.replace(res, f);
		t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		t.addToBackStack(null);
		mPager.setPagingEnabled(false);
		t.commit();
		
		SoftKeyboard.hideSoftKeyboard(MainActivity.this);
		
	}
	
	public void goHome() {
		getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		mPager.setCurrentItem(HOME_FRAGMENT_ID);
		mPager.setPagingEnabled(true);
		returnTabsButtons();
	}

	public void lowerTabsButtons() {
		if(tabsButtonsContainer.getVisibility() == View.VISIBLE)
			AnimationManager.collapse(tabsButtonsContainer, 200);
	}

	private void returnTabsButtons() {
		if(tabsButtonsContainer
				.getVisibility() == View.GONE)
			AnimationManager.expand(tabsButtonsContainer, 200);
	}

	public void replaceTab(int fragmentId) {
		onTabChange(fragmentId);
		mPager.setCurrentItem(fragmentId);
	}

	private void initListeners() {
		vgHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				replaceTab(HOME_FRAGMENT_ID);
			}
		});

		vgAdvanceSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				replaceTab(ADVANCE_SEARCH_FRAGMENT_ID);
			}
		});

		vgMyProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				replaceTab(MY_PROFILE_FRAGMENT_ID);
			}
		});

		ivSwipeToOpenWebView.setOnTouchListener(new OnSwipeTouchListener(
				getApplicationContext()) {
			@Override
			public void onSwipeTop() {
				showWebView();
			}

			@Override
			public void onSwipeBottom() {
				hideWebView();
			}
		});
		
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			int to = -1;
			@Override
			public void onPageSelected(int arg0) {
				to = arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				hideWebView();
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if(to != -1){
					onTabChange(to);
					to = -1;
				}
			}
		});
	}

	protected boolean hideWebView() {
		return ExtraServicesWebViewManager.collapse(MainActivity.this, wvMoreServices);
	}

	protected void showWebView() {
		if(LocalStorageManager.getInstance().systemAddition.AdditionServiceUrl!=null)
			wvMoreServices.loadUrl(LocalStorageManager.getInstance().systemAddition.AdditionServiceUrl);
		ExtraServicesWebViewManager.expand(wvMoreServices, MainActivity.this);
	}

	private void onTabChange(int tabPosition) {

		vgHome.setBackgroundResource(0);
		vgAdvanceSearch.setBackgroundResource(0);
		vgMyProfile.setBackgroundResource(0);
		
		switch (tabPosition) {
		case HOME_FRAGMENT_ID:
			clearAdvanceSearchTextViews();
			vgHome.setBackgroundResource(R.drawable.tb_selectedtab);
			break;
		case ADVANCE_SEARCH_FRAGMENT_ID:
			vgAdvanceSearch.setBackgroundResource(R.drawable.tb_selectedtab);
			clearHomeTextView();
			break;
		case MY_PROFILE_FRAGMENT_ID:
			clearAdvanceSearchTextViews();
			vgMyProfile.setBackgroundResource(R.drawable.tb_selectedtab);
			break;
		}
		isOkToFinishApp = false;
		hideWebView();
	}
	
	private void clearHomeTextView() {
		Fragment fragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
		if(fragment instanceof HomeFragment){
			((HomeFragment) fragment).clearFreeSearchEditText();
		}
	}
	
	private void clearAdvanceSearchTextViews() {
		Fragment fragment = getSupportFragmentManager().findFragmentByTag("AdvanceSearch");
		 if(fragment instanceof AdvanceSearch) {
			 ((AdvanceSearch) fragment).removeCallbacks();
		 }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		final int LENGTH_SHORT = 2000; // 2 seconds
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if(hideWebView())
				return false;

			if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
				mPager.setPagingEnabled(true);
				returnTabsButtons();
			}

			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				if(isOkToFinishApp)
					this.finish();
				else {
					Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
					isOkToFinishApp = true;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							isOkToFinishApp = false;
						}
					}, LENGTH_SHORT);
				}
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	private class myPagerAdapter extends FragmentPagerAdapter {

		private final int TOTAL_PAGES = 3;
		
		public myPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				return new RootHomeFragment();
			case 1:
				return new RootAdvanceSearchFragment();
			case 2:
				return new RootMyProfileFragment();
			}
			return null;
		}

		@Override
		public int getCount() {
			return TOTAL_PAGES;
		}
		
	}

}
