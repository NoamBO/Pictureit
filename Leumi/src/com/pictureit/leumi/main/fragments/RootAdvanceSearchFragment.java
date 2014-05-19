package com.pictureit.leumi.main.fragments;

import com.pictureit.leumi.main.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RootAdvanceSearchFragment extends Fragment {

	private AdvanceSearch advanceSearchFragment;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.advance_search_root_layout, null);
		advanceSearchFragment = new AdvanceSearch();
		FragmentTransaction t = getFragmentManager().beginTransaction();
		t.replace(R.id.root_view_advance_search, advanceSearchFragment, "AdvanceSearch");
		t.commit();
		return v;
	}

}
