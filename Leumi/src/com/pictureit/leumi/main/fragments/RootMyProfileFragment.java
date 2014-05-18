package com.pictureit.leumi.main.fragments;

import com.pictureit.leumi.main.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RootMyProfileFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.my_profile_root_layout, null);
		FragmentTransaction t = getFragmentManager().beginTransaction();
		t.replace(R.id.root_view_my_profile, new BaseProfileFragment());
		t.commit();
		return v;
	}
}
