package com.pictureit.leumi.server;

import android.app.Activity;
import android.os.Bundle;

import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.main.fragments.ResultsFragment;
import com.pictureit.leumi.server.parse.JsonToObject;

import utilities.HttpBase.HttpCalback;

public class SearchCallback implements HttpCalback {

	private static Activity mActivity;
	
	public static HttpCalback getCallback(Activity activity) {
		mActivity = activity;
		return new SearchCallback();
	}
	
		@Override
		public void onAnswerReturn(Object answer) {
			if(!JsonToObject.isStatusOk((String) answer)){
				Dialogs.generalDialog(mActivity, mActivity.getResources().getString(R.string.impossible_to_load_service));
				return;
			}
			Bundle b = new Bundle();
			b.putString(Const.JSON, (String) answer);
			
			ResultsFragment f = new ResultsFragment();
			f.setArguments(b);
			((MainActivity)mActivity).addFragment(f);
		};
	}

