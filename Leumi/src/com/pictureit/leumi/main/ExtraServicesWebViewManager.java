package com.pictureit.leumi.main;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

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
	
	public static boolean collapse(Activity activity, final View webView) {
		if(!isExtraServicesVisible)
			return false;
		
		final int targetHeight = AnimationManager.getHeightToExpand(65, activity, null);
		Animation a = new Animation()
		    {
		        @Override
		        protected void applyTransformation(float interpolatedTime, Transformation t) {
		            if(interpolatedTime == 1){
		            	webView.setVisibility(View.GONE);
		            }else{
		            	webView.getLayoutParams().height = targetHeight - (int)(targetHeight * interpolatedTime);
		            	webView.requestLayout();
		            }
		        }

		        @Override
		        public boolean willChangeBounds() {
		            return true;
		        }
		    };

		    a.setAnimationListener(new BaseAnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					isExtraServicesVisible = false;
				}
			});
		    a.setDuration(SLIDE_DOWN_DURATION);
		    webView.startAnimation(a);
	return true;
	}

}
