package com.pictureit.leumi.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.server.parse.JsonToObject;

public class EmploeeProfileFragment extends BaseProfileFragment {

	public static String KEY_SHOW_HOME_BUTTON_ON_PROFILE_SCREEN = "EmploeeProfileFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((MainActivity) getActivity()).lowerTabsButtons();
		if(savedInstanceState == null)
			savedInstanceState = new Bundle();
		savedInstanceState.putBoolean(KEY_SHOW_HOME_BUTTON_ON_PROFILE_SCREEN, true);
		if (mProfile == null) {
			Bundle b = getArguments();
			if (b != null) {
				String s = b.getString(Const.JSON);
				mProfile = JsonToObject.jsonToSingleEmploeeProfile(s);
			}
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
