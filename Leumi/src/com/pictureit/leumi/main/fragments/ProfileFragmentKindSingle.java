package com.pictureit.leumi.main.fragments;

import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.parse.JsonToObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ProfileFragmentKindSingle extends BaseProfileFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((MainActivity)getActivity()).lowerTabsButtons();
		
		if (mProfile == null) {
			Bundle b = getArguments();
			if (b != null) {
				String s = b.getString(Const.JSON);
				mProfile = JsonToObject.jsonToSingleEmploeeProfile(s);
			}
		}
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
//	@Override
//	public void onResume() {
//		super.onResume();
//		ImageButton ibHome = findView(getView(), R.id.ib_go_home_profile_screen);
//	ibHome.setVisibility(View.VISIBLE);
//		ibHome.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				((MainActivity) getActivity()).goHome();
//			}
//		});
//	}
}
