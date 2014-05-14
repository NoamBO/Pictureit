package com.pictureit.leumi.main;

import java.util.HashMap;

import utilities.SoftKeyboard;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.pictureit.leumi.animation.AnimationManager;
import com.pictureit.leumi.main.fragments.AdvanceSearch;
import com.pictureit.leumi.main.fragments.HomeFragment;
import com.pictureit.leumi.main.fragments.BaseProfileFragment;

public class MainActivity extends Activity {

	private ViewGroup container;
	private ViewGroup tabsButtonsContainer;
	Fragment fHome, fAdvanceSearch;
	Fragment fMyProfile;
	ViewGroup vgHome, vgMyProfile, vgAdvanceSearch;
	ImageView ivSwipeToOpenWebView;
	WebView wvMoreServices;
	private int mTabPicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initUI();

	}

	private void initUI() {
		container = (ViewGroup) findViewById(R.id.container);
		vgHome = (ViewGroup) findViewById(R.id.button1);
		vgMyProfile = (ViewGroup) findViewById(R.id.button3);
		vgAdvanceSearch = (ViewGroup) findViewById(R.id.button2);
		wvMoreServices = (WebView) findViewById(R.id.wv_main_webview);
		ivSwipeToOpenWebView = (ImageView) findViewById(R.id.iv_btn_open_webview);
		tabsButtonsContainer = (ViewGroup) findViewById(R.id.tabsButtonsHost1);

		fHome = new HomeFragment();
		fAdvanceSearch = new AdvanceSearch();
		fMyProfile = new BaseProfileFragment();

		mTabPicked = 1;
		addFragment(fHome);

		initListeners();
	}

	public void addFragment(Fragment f) {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.container, f);
		transaction.addToBackStack(null);

		transaction.commit();
		
		SoftKeyboard.hideSoftKeyboard(MainActivity.this);
		
	}

	public void lowerTabsButtons() {
		if(tabsButtonsContainer.getVisibility() == View.VISIBLE)
			AnimationManager.collapse(tabsButtonsContainer, 200);
	}

	private void returnTabsButtons() {
		if(tabsButtonsContainer
				.getVisibility() == View.GONE)
			AnimationManager.expand(tabsButtonsContainer, 200);
//		tabsButtonsContainer.setVisibility(View.VISIBLE);
	}

	public void replaceTab(Fragment f) {
		if (f == null)
			return;
		onTabChange();
		addFragment(f);
	}

	private void initListeners() {
		vgHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Const.FRAGMENT_NAME_HOME == mTabPicked)
					return;
				replaceTab(fHome);
				mTabPicked = 1;
				vgHome.setBackgroundResource(R.drawable.tb_selectedtab);
			}
		});

		vgAdvanceSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Const.FRAGMENT_NAME_ADVANCED_SEARCH == mTabPicked)
					return;
				replaceTab(fAdvanceSearch);
				mTabPicked = 2;
				vgAdvanceSearch.setBackgroundResource(R.drawable.tb_selectedtab);
			}
		});

		vgMyProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Const.FRAGMENT_NAME_MY_PROFILE == mTabPicked)
					return;
				replaceTab(fMyProfile);
				mTabPicked = 3;
				vgMyProfile.setBackgroundResource(R.drawable.tb_selectedtab);
			}
		});

		container.setOnTouchListener(new OnSwipeTouchListener(
				getApplicationContext()) {
			@Override
			public void onSwipeLeft() {
				getWantedTabOnSwipeLeft();
			}

			@Override
			public void onSwipeRight() {
				getWantedTabOnSwipeRight();
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
	}

	protected void hideWebView() {
//		if(tabsButtonsContainer.getVisibility() == View.GONE) {
//			HashMap<String, View> hm = new HashMap<String, View>();
//			hm.put("remove", tabsButtonsContainer);
//			AnimationManager.collapseExtraServicesByY(findViewById(R.id.ll_test),
//					wvMoreServices, MainActivity.this, hm);
//			}
//		else
		AnimationManager.collapseExtraServicesByY(findViewById(R.id.ll_test),
				wvMoreServices, MainActivity.this);
	}

	protected void showWebView() {
//		if(tabsButtonsContainer.getVisibility() == View.GONE) {
//			HashMap<String, View> hm = new HashMap<String, View>();
//			hm.put("add", tabsButtonsContainer);
//			AnimationManager.expandExtraServicesByY(findViewById(R.id.ll_test),
//					wvMoreServices, MainActivity.this, hm);
//			}
//		else
		AnimationManager.expandExtraServicesByY(findViewById(R.id.ll_test),
				wvMoreServices, MainActivity.this);
	}

	private void getWantedTabOnSwipeLeft() {
		switch (mTabPicked) {
		case Const.FRAGMENT_NAME_HOME:
			vgAdvanceSearch.performClick();
			break;
		case Const.FRAGMENT_NAME_ADVANCED_SEARCH:
			vgMyProfile.performClick();
			break;
		default:
			break;
		}

	}

	private void getWantedTabOnSwipeRight() {
		switch (mTabPicked) {
		case Const.FRAGMENT_NAME_ADVANCED_SEARCH:
			vgHome.performClick();
			break;
		case Const.FRAGMENT_NAME_MY_PROFILE:
			vgAdvanceSearch.performClick();
			break;
		default:
			break;
		}
	}

	private void onTabChange() {
		getFragmentManager().executePendingTransactions();
		getFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
		vgHome.setBackgroundResource(0);
		vgAdvanceSearch.setBackgroundResource(0);
		vgMyProfile.setBackgroundResource(0);
		hideWebView();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (getFragmentManager().getBackStackEntryCount() > 1
					&& tabsButtonsContainer.getVisibility() == View.GONE)
				returnTabsButtons();

			if (getFragmentManager().getBackStackEntryCount() == 1) {
				this.finish();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
