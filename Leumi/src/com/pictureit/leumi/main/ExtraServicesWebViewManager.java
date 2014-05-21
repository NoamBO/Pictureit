package com.pictureit.leumi.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout.LayoutParams;

import com.pictureit.leumi.animation.AnimationManager;
import com.pictureit.leumi.animation.BaseAnimationListener;

public class ExtraServicesWebViewManager {
	
	private static boolean isExtraServicesVisible = false;
	private static final int SLIDE_UP_DURATION = 500;
	private static final int SLIDE_DOWN_DURATION = 500;
	
	static boolean loadingFinished = true;
	static boolean redirect = false;

	public static boolean expand(final View v, Activity activity) {
		if(isExtraServicesVisible)
			return false;
		
		final int targetHeight = AnimationManager.getHeightToExpand(65, activity, null);

	
		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? targetHeight
						: (int) (targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setAnimationListener(new BaseAnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				isExtraServicesVisible = true;
			}
		});
		a.setDuration(SLIDE_UP_DURATION);
		v.startAnimation(a);
		return true;
	}
	
//	public static boolean slideUp(Activity acivity, View container , final View webView) {
//		if(isExtraServicesVisible)
//			return false;
//		
//		int targetHeight = AnimationManager.getHeightToExpand(65, acivity, null);
//		LayoutParams params = (LayoutParams) webView.getLayoutParams();
//		params.height = targetHeight;
//		webView.setLayoutParams(params);
//		
//		Animation a = new TranslateAnimation(0f, 0f, targetHeight, 0.1f);
//		a.setInterpolator(new BounceInterpolator());
//		a.setDuration(SLIDE_UP_DURATION);
//		a.setAnimationListener(new BaseAnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				webView.setVisibility(View.VISIBLE);
//				isExtraServicesVisible = true;
//			
//			}
//
//		});
//		container.startAnimation(a);
//		return true;
//	}
	
	public static boolean slideDown(Activity acivity, final View container , final View webView) {
		if(!isExtraServicesVisible)
			return false;
		
		int targetHeight = AnimationManager.getHeightToExpand(65, acivity, null);
		Animation a = new TranslateAnimation(0f, 0f, 0, targetHeight);
		a.setDuration(SLIDE_DOWN_DURATION);
		
		a.setAnimationListener(new BaseAnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isExtraServicesVisible = false;
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				webView.setVisibility(View.GONE);
				container.clearAnimation();
				container.requestFocus();
			}
		});
		a.setFillAfter(true);
		container.startAnimation(a);
		return true;
	}

//	public static void expandExtraServicesByY(View container, final View view, Activity activity) {
//		expandExtraServicesByY(container, view, activity, null);
//	}
//	
//	public static void expandExtraServicesByY(View container, final View view, final Activity activity, HashMap<String, View> additionalViews) {
//		if(isExtraServicesVisible)
//			return;
//
//		int targetHeight = AnimationManager.getHeightToExpand(65, activity, null);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, targetHeight);
//		view.setLayoutParams(params);
//		
//		//int targetHeight = (int) PixelsConverter.convertDpToPixel(350f, activity);
//		container.animate().yBy(-targetHeight).setListener(new BaseAnimatorListener() {
//			
//			@Override
//			public void onAnimationStart(Animator animation) {
//				view.setVisibility(View.VISIBLE);
//				WebView webView = (WebView) activity.findViewById(R.id.wv_main_webview);
//				webView.loadUrl("http://m.leumi.co.il");
//			}
//			
//			@Override
//			public void onAnimationEnd(Animator animation) {
//				isExtraServicesVisible = true;
//			}
//			
//		});
//	}
//	
//	public static boolean collapseExtraServicesByY(View container, final View view, Activity activity) {
//		return collapseExtraServicesByY(container, view, activity, null);
//	}
//	
//	public static boolean collapseExtraServicesByY(View container, final View view, Activity activity, HashMap<String, View> additionalViews) {
//		if(!isExtraServicesVisible)
//			return false;
//				
//		//int targetHeight = (int) PixelsConverter.convertDpToPixel(350f, activity);
//		int targetHeight = AnimationManager.getHeightToExpand(65, activity, null);
//		
//		container.animate().yBy(targetHeight).setListener(new BaseAnimatorListener() {
//			
//			@Override
//			public void onAnimationEnd(Animator animation) {
//				isExtraServicesVisible = false;
//			}
//		});
//		
//		return true;
//	}
//	
//	private static class BaseAnimatorListener implements AnimatorListener {
//		@Override public void onAnimationCancel(Animator animation) {}
//		@Override public void onAnimationEnd(Animator animation) {}
//		@Override public void onAnimationRepeat(Animator animation) {}
//		@Override public void onAnimationStart(Animator animation) {}
//	}

}
