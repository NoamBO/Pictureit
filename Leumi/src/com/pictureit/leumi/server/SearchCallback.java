package com.pictureit.leumi.server;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.main.fragments.ResultsFragment;
import com.pictureit.leumi.server.parse.JsonToObject;

import utilities.HttpBase.HttpCalback;

public class SearchCallback implements HttpCalback {

	private static Activity mActivity;
	
	private static ArrayList<AutoCompleteTextView> mEditTextArray;
	
	public static HttpCalback getCallback(Activity activity, ArrayList<AutoCompleteTextView> editTextArray) {
		mActivity = activity;
		mEditTextArray = editTextArray;
		return new SearchCallback();
	}
	
	public static HttpCalback getCallback(Activity activity) {
		mActivity = activity;
		mEditTextArray = null;
		return new SearchCallback();
	}
	
		@Override
		public void onAnswerReturn(Object answer) {
			if(!JsonToObject.isStatusOk((String) answer)){
				Dialogs.generalDialog(mActivity, mActivity.getResources().getString(R.string.impossible_to_load_service));
				return;
			}
			
			if(mEditTextArray != null) {
				for (int i = 0; i < mEditTextArray.size(); i++) {
					mEditTextArray.get(i).setText("");
				}
				mEditTextArray = null;
			}
			Bundle b = new Bundle();
			b.putString(Const.JSON, (String) answer);
			
			ResultsFragment f = new ResultsFragment();
			f.setArguments(b);
			((MainActivity)mActivity).addFragment(f);
		};
	}

