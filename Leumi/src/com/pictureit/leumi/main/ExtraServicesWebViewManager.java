package com.pictureit.leumi.main;

import java.util.HashMap;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.pictureit.leumi.animation.AnimationManager;

public class ExtraServicesWebViewManager {

	private static boolean isExtraServicesVisible;
	

	public static void expandExtraServicesByY(View container, final View view, Activity activity) {
		expandExtraServicesByY(container, view, activity, null);
	}
	
	public static void expandExtraServicesByY(View container, final View view, final Activity activity, HashMap<String, View> additionalViews) {
		if(isExtraServicesVisible)
			return;

		int targetHeight = AnimationManager.getHeightToExpand(65, activity, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, targetHeight);
		view.setLayoutParams(params);
		
		//int targetHeight = (int) PixelsConverter.convertDpToPixel(350f, activity);
		container.animate().yBy(-targetHeight).setListener(new BaseAnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				view.setVisibility(View.VISIBLE);
				WebView webView = (WebView) activity.findViewById(R.id.wv_main_webview);
				webView.loadUrl("http://m.leumi.co.il");
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				isExtraServicesVisible = true;
			}
			
		});
	}
	
	public static boolean collapseExtraServicesByY(View container, final View view, Activity activity) {
		return collapseExtraServicesByY(container, view, activity, null);
	}
	
	public static boolean collapseExtraServicesByY(View container, final View view, Activity activity, HashMap<String, View> additionalViews) {
		if(!isExtraServicesVisible)
			return false;
				
		//int targetHeight = (int) PixelsConverter.convertDpToPixel(350f, activity);
		int targetHeight = AnimationManager.getHeightToExpand(65, activity, null);
		
		container.animate().yBy(targetHeight).setListener(new BaseAnimatorListener() {
			
			@Override
			public void onAnimationEnd(Animator animation) {
				isExtraServicesVisible = false;
			}
		});
		
		return true;
	}
	
	private static class BaseAnimatorListener implements AnimatorListener {
		@Override public void onAnimationCancel(Animator animation) {}
		@Override public void onAnimationEnd(Animator animation) {}
		@Override public void onAnimationRepeat(Animator animation) {}
		@Override public void onAnimationStart(Animator animation) {}
	}

}
