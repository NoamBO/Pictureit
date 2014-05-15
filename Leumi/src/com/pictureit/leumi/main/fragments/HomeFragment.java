package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;
import java.util.List;

import utilities.OutgoingCommunication;
import utilities.HttpBase.HttpCalback;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.leumi.main.BaseEditTextListener;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.main.R.id;
import com.pictureit.leumi.main.R.layout;
import com.pictureit.leumi.server.GetAutoComplete;
import com.pictureit.leumi.server.GetBrunch;
import com.pictureit.leumi.server.GetListLastServices;
import com.pictureit.leumi.server.GetService;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.ServerAddresses;
import com.pictureit.leumi.server.parse.Emploee;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.LeumiService;
import com.pictureit.leumi.server.parse.Service;

public class HomeFragment extends Fragment {

	private boolean mToggle;
	private ListView lvAutoCompleteSearch, lvServicesList;
	private EditText etSearch;
	private ImageButton ibTellUsYouDidntFindService;
	private ImageButton ibSearch;
	private ArrayList<Emploee> emploees;
	
	private AutocomplteListViewAnapter adapter;

	private ArrayList<LeumiService> mLastServicesList;

	private HttpCalback autoCompleteCallback = new HttpCalback() {

		@Override
		public void onAnswerReturn(Object answer) {

			if (answer != null) {
				emploees = JsonToObject.jsonToAutoComplete((String) answer);
				adapter = new AutocomplteListViewAnapter(
						getActivity(), android.R.layout.simple_list_item_1, emploees);
				lvAutoCompleteSearch.setAdapter(adapter);
			}
		}
	};

	private HttpCalback getLastServicesCallback = new HttpCalback() {

		@Override
		public void onAnswerReturn(Object answer) {
			if (answer != null) {
				mLastServicesList = JsonToObject.jsonToLastServicesList((String) answer);
				setServicesList();
			}

		}
	};
	
	private void setServicesList() {
		LastServicesListViewAdapter adapter = new LastServicesListViewAdapter(
				getActivity(), android.R.layout.simple_list_item_1,
				mLastServicesList);
		lvServicesList.setAdapter(adapter);
	}
	
	private HttpCalback getServiceCallback = new HttpCalback() {

		@Override
		public void onAnswerReturn(Object answer) {
			if (answer != null) {
				Bundle b = new Bundle();
				String s = answer.toString();
				b.putString(Const.JSON, s);
				
				ServiceFragment f = new ServiceFragment();
				f.setData((Service) answer);
				f.setArguments(b);
				((MainActivity)getActivity()).addFragment(f);
				
			}

		}
	};

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.home_screen, container, false);
		etSearch = (EditText) v.findViewById(R.id.et_search);
		etSearch.setText("");
		lvAutoCompleteSearch = (ListView) v
				.findViewById(R.id.lv_main_autocomplete_search);
		lvServicesList = (ListView) v.findViewById(R.id.lv_main_services_list);

		ibTellUsYouDidntFindService = (ImageButton) v.findViewById(R.id.ib_main_no_service_found);
		ibSearch = (ImageButton) v.findViewById(R.id.ib_search);
		
		if (mLastServicesList != null && mLastServicesList.size() > 0) {
			setServicesList();
		} else {
			GetListLastServices getListLastServices = new GetListLastServices(
					getActivity(), getLastServicesCallback);
			getListLastServices.execute();
		}
		mToggle = false;
		return v;
	}


	@Override
	public void onResume() {
		super.onResume();
		
		etSearch.addTextChangedListener(new BaseEditTextListener() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
//				if (mToggle) {
//					mToggle = false;
//					return;
//				}
				String searchString = etSearch.getText().toString();
				if (searchString.length() < 1 && lvAutoCompleteSearch != null && emploees!= null) {
					emploees.removeAll(emploees);
					adapter.notifyDataSetChanged();
					return;
				}

				GetAutoComplete autoComplete = new GetAutoComplete(
						getActivity(), Const.MOBILE_FULL, autoCompleteCallback);
				autoComplete.execute(searchString);
//				mToggle = true;
			}
		});
		
		lvServicesList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				LeumiService item = (LeumiService) adapter.getItemAtPosition(position);
				if(item.stype.equalsIgnoreCase(Const.SERVICE_CLICK)) {
					getServiceClick(item.sid);
				}
				
			}
		});
		
		ibTellUsYouDidntFindService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OutgoingCommunication.sendEMail(getActivity(), Const.LEUMI_EMAIL_ADDRESS);
			}
		});
		
		ibSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String searchText = etSearch.getText().toString();
				if(searchText.length() < 1)
					return;
				
				PostSearch postSearch = new PostSearch(getActivity(), new HttpCalback() {
					
					@Override
					public void onAnswerReturn(Object answer) {
						if(JsonToObject.isStatusOk((String) answer)) {
							Bundle b = new Bundle();
							b.putString(Const.JSON, (String) answer);
							
							Fragment f = new ResultsFragment();
							f.setArguments(b);
							((MainActivity) getActivity()).addFragment(f);
						}
					}
				});
				postSearch.getEmployeesForFreeText(searchText);
			}
		});
		
		lvAutoCompleteSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Emploee e = (Emploee) parent.getItemAtPosition(position);
				onAutocopletionPressed(e);
			}
		});
		
	}

	protected void onAutocopletionPressed(Emploee emploee) {
		final Fragment f;
		boolean snifSelected = emploee.SearchType.equalsIgnoreCase(Const.SYSTEM_TYPE_SNIF);
		if(!snifSelected)
			f = new ProfileFragmentKindAutocomplete();
		else
			f = new BranchFragment();
		
		HttpCalback callback = new HttpCalback() {
			
			@Override
			public void onAnswerReturn(Object answer) {
				if(answer == null)
					return;

				Bundle args = new Bundle();
				args.putString(Const.JSON, answer.toString());
				f.setArguments(args);
				((MainActivity)getActivity()).addFragment(f);
				etSearch.setText("");
			}
		};
		
		if(!snifSelected) {
			PostSearch postSearch = new PostSearch(getActivity(), callback);
			postSearch.getEmploeeForSearchID(emploee.SearchID);
		}
		else {
			GetBrunch getBranch = new GetBrunch(getActivity(), callback);
			getBranch.execute(emploee.SearchID);
		}
		
	}

	private void getServiceClick(int sid) {
		GetService getService = new GetService(getActivity(), getServiceCallback);
		getService.execute(String.valueOf(sid));
	}

	private class AutocomplteListViewAnapter extends ArrayAdapter<Emploee> {

		

		public AutocomplteListViewAnapter(Context context, int resource,
				List<Emploee> emploees) {
			super(context, resource, emploees);
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getActivity().getLayoutInflater().inflate(
					R.layout.simple_textview, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.textview1);
			textView.setText(emploees.get(position).SearchKey);
			return convertView;
		}

		@Override
		public int getCount() {
			return emploees.size();
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Emploee getItem(int position) {
			return emploees.get(position);
		}

	}

	private class LastServicesListViewAdapter extends
			ArrayAdapter<LeumiService> {

		List<LeumiService> list;

		public LastServicesListViewAdapter(Context context, int resource,
				List<LeumiService> list) {
			super(context, resource, list);
			this.list = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getActivity().getLayoutInflater().inflate(
					R.layout.simple_textview, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.textview1);

			textView.setBackgroundColor(getResources().getColor(
					android.R.color.white));
			
			setRowText(textView, position);
			return convertView;
		}

		public void setRowText(TextView textView, int position) {
			StringBuilder sb = new StringBuilder();
			sb.append(getItem(position).sname).append(" : ")
					.append(getItem(position).managername);

			String s = "<u>"+sb.toString()+"</u>";
			textView.setText(Html.fromHtml(s));
			textView.setTextColor(getResources().getColor(R.color.dodgerblue));

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public LeumiService getItem(int position) {
			return list.get(list.size() - 1 - position);
		}

	}

}
