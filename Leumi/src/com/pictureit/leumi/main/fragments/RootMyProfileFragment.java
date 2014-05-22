package com.pictureit.leumi.main.fragments;

import utilities.BaseFragment;
import utilities.server.HttpBase.HttpCalback;

import com.google.gson.Gson;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.LocalStorageManager;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.GetCurrentUserData;
import com.pictureit.leumi.server.parse.JsonToObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RootMyProfileFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.my_profile_root_layout, null);
		
		if(LocalStorageManager.getInstance().currentUserProfile == null) {
			GetCurrentUserData getCurrentUserData = new GetCurrentUserData(getActivity(), new HttpCalback() {
				
				@Override
				public void onAnswerReturn(Object object) {
					if(object != null && JsonToObject.isStatusOk((String) object)) {
						LocalStorageManager.getInstance().currentUserProfile = JsonToObject.jsonToUserProfilesArrayList((String) object).get(0);
						launchProfileFragment();
					} else {
						showErrorDialog();
					}
				}
			});
			getCurrentUserData.execute();
		} else {
			launchProfileFragment();
		}
		return v;
	}
	
	private void launchProfileFragment() {
		Bundle b = new Bundle();
		b.putString(Const.JSON , new Gson().toJson(LocalStorageManager.getInstance().currentUserProfile));
		FragmentTransaction t = getFragmentManager().beginTransaction();
		Fragment f = new CurrentUserProfileFragment();
		f.setArguments(b);
		t.replace(R.id.root_view_my_profile, f);
		t.commit();
	}
}
