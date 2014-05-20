package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;

import utilities.BaseFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.pictureit.leumi.main.AutoCompleteTextViewHandler;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.SearchCallback;

public class AdvanceSearch extends BaseFragment {

	private AutoCompleteTextView etFirstName, etLastName, etFreeText, etFreeUnitSearch,
			etSearchByJob;
	
	private ImageButton ibSearch;
	
	private ArrayList<AutoCompleteTextView> autoCompleteTextViewArray;
	private ArrayList<AutoCompleteTextViewHandler> handlersArray;
	
	public void removeCallbacks() {
		if(handlersArray != null)
			for (int i = 0; i < handlersArray.size(); i++) {
				handlersArray.get(i).cancelTask();
			}
	}

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
		
		autoCompleteTextViewArray = new ArrayList<AutoCompleteTextView>();
		autoCompleteTextViewArray.add(etFirstName);
		autoCompleteTextViewArray.add(etLastName);
		autoCompleteTextViewArray.add(etFreeText);
		autoCompleteTextViewArray.add(etFreeUnitSearch);
		autoCompleteTextViewArray.add(etSearchByJob);
		
		handlersArray = new ArrayList<AutoCompleteTextViewHandler>();
		AutoCompleteTextViewHandler a = new AutoCompleteTextViewHandler();
		a.setListener(getActivity(), etFirstName, Const.FIRST_NAME);
		AutoCompleteTextViewHandler b = new AutoCompleteTextViewHandler();
		b.setListener(getActivity(), etLastName, Const.LAST_NAME);
		AutoCompleteTextViewHandler c = new AutoCompleteTextViewHandler();
		c.setListener(getActivity(), etFreeText, Const.FREE_TEXT);
		AutoCompleteTextViewHandler d = new AutoCompleteTextViewHandler();
		d.setListener(getActivity(), etFreeUnitSearch, Const.DEPARTMENT);
		AutoCompleteTextViewHandler e = new AutoCompleteTextViewHandler();
		e.setListener(getActivity(), etSearchByJob, Const.JOB);
		
		handlersArray.add(a);
		handlersArray.add(b);
		handlersArray.add(c);
		handlersArray.add(d);
		handlersArray.add(e);
		
		ibSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PostSearch httpSearch = new PostSearch(getActivity(), SearchCallback.getCallback(getActivity(), autoCompleteTextViewArray));
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
