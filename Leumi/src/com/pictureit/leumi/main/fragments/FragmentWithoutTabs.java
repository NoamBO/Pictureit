package com.pictureit.leumi.main.fragments;

import utilities.BaseFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;

public class FragmentWithoutTabs extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((MainActivity)getActivity()).lowerTabsButtons();
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ImageButton ibHome = findView(getView(), R.id.ib_go_home);

		ibHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).goHome();
			}
		});
	}
}
