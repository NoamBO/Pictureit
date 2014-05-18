package com.pictureit.leumi.main;

import java.util.HashMap;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.webkit.WebView;

public class ExtraServicesWebViewManager {

	private static boolean isExtraServicesVisible;
	
	private static int getHeightToExpand(int heightPersents, Activity activity, HashMap<String, View> additionalViews) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		
		double persents = heightPersents/100f;
		
		int height = display.getHeight();	
		int targetHeight = (int)(height*persents);
		
		if(additionalViews != null) {
			if(additionalViews.containsKey("add"))
			{
				View v = additionalViews.get("add");
				v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				int eh = v.getMeasuredHeight();
				targetHeight =+ eh;
			}
			if(additionalViews.containsKey("remove"))
			{
				View v = additionalViews.get("remove");
				v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				int eh = v.getMeasuredHeight();
				targetHeight =- eh;
			}
		}
		
		return targetHeight;
	}
	
	public static void expandExtraServicesByY(View container, final View view, Activity activity) {
		expandExtraServicesByY(container, view, activity, null);
	}
	
	public static void expandExtraServicesByY(View container, final View view, final Activity activity, HashMap<String, View> additionalViews) {
		if(isExtraServicesVisible)
			return;
		int targetHeight = getHeightToExpand(65, activity, additionalViews);
		
		container.animate().yBy(-targetHeight).setListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				view.setVisibility(View.VISIBLE);
				WebView webView = (WebView) activity.findViewById(R.id.wv_main_webview);
				webView.loadUrl("http://m.leumi.co.il");;
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				isExtraServicesVisible = true;
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
	}
	
	public static boolean collapseExtraServicesByY(View container, final View view, Activity activity) {
		return collapseExtraServicesByY(container, view, activity, null);
	}
	
	public static boolean collapseExtraServicesByY(View container, final View view, Activity activity, HashMap<String, View> additionalViews) {
		if(!isExtraServicesVisible)
			return false;
		
		View swipeButton = activity.findViewById(R.id.iv_btn_open_webview);
		
		int targetHeight = getHeightToExpand(65, activity, additionalViews);
				//-swipeButton.getMeasuredHeight();
		
		container.animate().yBy(targetHeight).setListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				isExtraServicesVisible = false;
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		
		return true;
	}
}
