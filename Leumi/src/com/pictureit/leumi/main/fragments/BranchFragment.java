package com.pictureit.leumi.main.fragments;

import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import utilities.BaseFragment;

public class BranchFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((MainActivity)getActivity()).lowerTabsButtons();
		View v = inflater.inflate(R.layout.branch_layout, null);
		return v;
	}
}
