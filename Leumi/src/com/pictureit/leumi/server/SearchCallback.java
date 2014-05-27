package com.pictureit.leumi.server;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.main.fragments.EmploeeProfileFragment;
import com.pictureit.leumi.main.fragments.ResultsFragment;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.Profile;

import utilities.server.HttpBase.HttpCallback;

public class SearchCallback implements HttpCallback {

	private static Activity mActivity;

	private static ArrayList<AutoCompleteTextView> mEditTextArray;
	
	private static AutoCompleteTextView mSingleAutoCompleteTextView;

	public static HttpCallback getCallback(Activity activity,
			ArrayList<AutoCompleteTextView> editTextArray) {
		mActivity = activity;
		mEditTextArray = editTextArray;
		mSingleAutoCompleteTextView = null;
		return new SearchCallback();
	}

	public static HttpCallback getCallback(Activity activity) {
		mActivity = activity;
		mEditTextArray = null;
		mSingleAutoCompleteTextView = null;
		return new SearchCallback();
	}
	
	public static HttpCallback getCallback(Activity activity, AutoCompleteTextView textView) {
		mActivity = activity;
		mEditTextArray = null;
		mSingleAutoCompleteTextView = textView;
		return new SearchCallback();
	}

	@Override
	public void onAnswerReturn(String answer) {
		if (answer != null && JsonToObject.isStatusOk(answer)) {

			if (mEditTextArray != null) {
				for (int i = 0; i < mEditTextArray.size(); i++) {
					mEditTextArray.get(i).setText("");
				}
				mEditTextArray = null;
			} else if(mSingleAutoCompleteTextView != null)
				mSingleAutoCompleteTextView.setText("");

			ArrayList<Profile> p = JsonToObject.jsonToUserProfilesArrayList(answer);
			Bundle b = new Bundle();
			if(p.size() == 0) {
				showNO_RESULTSdialog();
				return;
			}
			
			if (p.size() > 1) {
				
				b.putString(Const.JSON, answer);
				Fragment f = new ResultsFragment();
				f.setArguments(b);
				((MainActivity) mActivity).addFragment(f);
			} else {
				
				Fragment f = new EmploeeProfileFragment();
				b.putString(Const.JSON,	new Gson().toJson(p.get(0), Profile.class));
				f.setArguments(b);
				((MainActivity) mActivity).addFragment(f);
			}

		} else {
			Dialogs.generalDialog(mActivity, mActivity.getResources()
					.getString(R.string.impossible_to_load_service));
		}

	};
	
	private void showNO_RESULTSdialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle(R.string.search_results)
		.setMessage(R.string.no_results)
		.setNeutralButton("Ok", null)
		.create()
		.show();
	}

}
