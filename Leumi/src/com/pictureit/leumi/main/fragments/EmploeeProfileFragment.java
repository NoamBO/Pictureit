package com.pictureit.leumi.main.fragments;

import com.pictureit.leumi.main.MainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmploeeProfileFragment extends BaseProfileFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((MainActivity)getActivity()).lowerTabsButtons();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}