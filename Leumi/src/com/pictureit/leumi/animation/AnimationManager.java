package com.pictureit.leumi.animation;

import java.util.HashMap;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pictureit.leumi.main.R;

public class AnimationManager {
	
	
	public static int getHeightToExpand(int heightPersents, Activity activity, HashMap<String, View> additionalViews) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		
		double persents = heightPersents/100f;
		
		@SuppressWarnings("deprecation")
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
	


	public static void slideInUp(Context ctx, View v, BaseAnimationListener l) {
		Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_up);
		anim.setAnimationListener(l);
		v.startAnimation(anim);
	}

	public static void slideOutDown(Context ctx, View v, BaseAnimationListener l) {
		Animation anim = AnimationUtils.loadAnimation(ctx,
				R.anim.slide_out_down);
		anim.setAnimationListener(l);
		v.startAnimation(anim);
	}

	public static void expand(View v) {
		expand(v, 0, null);
	}

	public static void expand(final View v, int additionalDurationTime) {
		expand(v, additionalDurationTime, null);
	}
	public static void expand(final View v, int additionalDurationTime, BaseAnimationListener animationListener) {
		final int targetHeight;
		if (v instanceof ListView) {
			targetHeight = getListViewHeight((ListView) v);
		} else {
			v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			targetHeight = v.getMeasuredHeight();
		}
		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
						: (int) (targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		if(animationListener != null) {
			a.setAnimationListener(animationListener);
		}
		a.setDuration((int) (targetHeight / v.getContext().getResources()
				.getDisplayMetrics().density)
				+ additionalDurationTime);
		v.startAnimation(a);
	}
	
	public static void collapse(final View v) {
		collapse(v, 0);
	}
			
	public static void collapse(final View v, int additionalDurationTime) {
	    final int initialHeight = v.getMeasuredHeight();

	    Animation a = new Animation()
	    {
	        @Override
	        protected void applyTransformation(float interpolatedTime, Transformation t) {
	            if(interpolatedTime == 1){
	                v.setVisibility(View.GONE);
	            }else{
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
	                v.requestLayout();
	            }
	        }

	        @Override
	        public boolean willChangeBounds() {
	            return true;
	        }
	    };

	    // 1dp/ms
	    a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density)+additionalDurationTime);
	    v.startAnimation(a);
	}


	
	public static int getListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View child = listAdapter.getView(i, null, listView);
            child.setLayoutParams(new LayoutParams(
            	    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            
            child.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            
            totalHeight += child.getMeasuredHeight();
        }
        return totalHeight;
	}

    public static void setListViewHeightBasedOnChildren(ListView listView) {
    	int totalHeight = getListViewHeight(listView);
    	ListAdapter listAdapter = listView.getAdapter();
    	ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
