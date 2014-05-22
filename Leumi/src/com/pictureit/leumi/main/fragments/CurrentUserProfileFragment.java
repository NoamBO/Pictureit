package com.pictureit.leumi.main.fragments;

import utilities.server.HttpBase.HttpCalback;

import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.server.GetCurrentUserData;
import com.pictureit.leumi.server.parse.JsonToObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CurrentUserProfileFragment extends BaseProfileFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
