package com.pictureit.leumi.main.fragments;

import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.server.parse.JsonToObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragmentKindAutocomplete extends BaseProfileFragment {
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

	((MainActivity)getActivity()).lowerTabsButtons();
	
	if (mProfile == null) {
		Bundle b = getArguments();
		if (b != null) {
			String s = b.getString(Const.JSON);
			mProfile = JsonToObject.jsonToUserProfile(s);
		}
	}
	
	return super.onCreateView(inflater, container, savedInstanceState);
}
}
