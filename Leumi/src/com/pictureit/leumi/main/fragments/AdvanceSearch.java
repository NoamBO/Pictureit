package com.pictureit.leumi.main.fragments;

import utilities.BaseFragment;
import utilities.HttpBase.HttpCalback;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.pictureit.leumi.main.AutoCompleteTextViewHandler;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.SearchCallback;
import com.pictureit.leumi.server.parse.JsonToObject;

public class AdvanceSearch extends BaseFragment {

	private AutoCompleteTextView etFirstName, etLastName, etFreeText, etFreeUnitSearch,
			etSearchByJob;
	
	private ImageButton ibSearch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.advance_search, container, false);

		etFirstName = findView(v, R.id.et_advance_search_first_name);
		etLastName = findView(v, R.id.et_advance_search_last_name);
		etFreeText = findView(v, R.id.et_advance_search_free_search);
		etFreeUnitSearch = findView(v, R.id.et_advance_search_free_unit_search);
		etSearchByJob = findView(v, R.id.et_advance_search_job);
		ibSearch = findView(v, R.id.ib_advance_search_search);

		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		new AutoCompleteTextViewHandler().setListener(getActivity(), etFirstName, Const.FIRST_NAME);;
		new AutoCompleteTextViewHandler().setListener(getActivity(), etLastName, Const.LAST_NAME);
		new AutoCompleteTextViewHandler().setListener(getActivity(), etFreeText, Const.FREE_TEXT);
		new AutoCompleteTextViewHandler().setListener(getActivity(), etFreeUnitSearch, Const.DEPARTMENT);
		new AutoCompleteTextViewHandler().setListener(getActivity(), etSearchByJob, Const.JOB);
		
		ibSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PostSearch httpSearch = new PostSearch(getActivity(), SearchCallback.getCallback(getActivity()));
				httpSearch.getEmployeesForAdvanceSearch(etLastName.getText().toString()
						, etFirstName.getText().toString()
						, etFreeUnitSearch.getText().toString()
						, etSearchByJob.getText().toString()
						, etFreeText.getText().toString()
						);
				
			}
		});

	}

}
