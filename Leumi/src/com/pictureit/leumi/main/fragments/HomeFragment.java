package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;
import java.util.List;

import utilities.BaseFragment;
import utilities.HttpBase.HttpCalback;
import utilities.OutgoingCommunication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.pictureit.leumi.main.AutoCompleteTextViewHandler;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.LocalStorageManager;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.main.Settings;
import com.pictureit.leumi.server.GetBrunch;
import com.pictureit.leumi.server.GetListLastServices;
import com.pictureit.leumi.server.GetService;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.parse.Emploee;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.LeumiService;
import com.pictureit.leumi.server.parse.Service;

public class HomeFragment extends BaseFragment {

	private ListView lvServicesList;
	private AutoCompleteTextView etSearch;
	private ImageButton ibTellUsYouDidntFindService;
	private ImageButton ibSearch;

	private ArrayList<LeumiService> mLastServicesList;
	
	private final BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
	    @Override
	        public void onReceive(Context context, Intent intent) {
	    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = cm.getActiveNetworkInfo();
	        if (info != null) {
	            if (info.isConnected()) {
	                if(LocalStorageManager.homeServicesList == null) {
	                	setServicesList();
	                	getActivity().unregisterReceiver(this);
	                }
	            } 
	        } 
	        }
	    };
	    
	    public void registerInternetReceiver() {
			IntentFilter mNetworkStateFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
			getActivity().registerReceiver(mNetworkStateReceiver , mNetworkStateFilter);
		}

	private HttpCalback getLastServicesCallback = new HttpCalback() {

		@Override
		public void onAnswerReturn(Object answer) {
			if (answer != null) {
				mLastServicesList = JsonToObject.jsonToLastServicesList((String) answer);
				LocalStorageManager.homeServicesList = mLastServicesList;
				setServicesList();
			}

		}
	};

	private void setServicesList() {
		if (LocalStorageManager.homeServicesList != null)
			mLastServicesList = LocalStorageManager.homeServicesList;
		if (!(mLastServicesList != null && mLastServicesList.size() > 0)) {
			if(!Settings.isNetworkAvailable(getActivity())) {
				registerInternetReceiver();
				return;
			}
			GetListLastServices getListLastServices = new GetListLastServices(
					getActivity(), getLastServicesCallback);
			getListLastServices.execute();
			return;
		}

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
		
		etSearch = (AutoCompleteTextView) v.findViewById(R.id.et_search);
		etSearch.setText("");
		new AutoCompleteTextViewHandler().setListener(getActivity(), etSearch,Const.MOBILE_FULL);
//		lvAutoCompleteSearch = (ListView) v
//				.findViewById(R.id.lv_main_autocomplete_search);
		lvServicesList = (ListView) v.findViewById(R.id.lv_main_services_list);

		ibTellUsYouDidntFindService = (ImageButton) v.findViewById(R.id.ib_main_no_service_found);
		ibSearch = (ImageButton) v.findViewById(R.id.ib_search);
		setServicesList();
		
		return v;
	}


	@Override
	public void onResume() {
		super.onResume();

		
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
		
		etSearch.setOnItemClickListener(new OnItemClickListener() {

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
		String requestType = emploee.SearchType;

		if(requestType.equalsIgnoreCase(Const.SYSTEM_TYPE_SNIF))
			f = new BranchFragment();
		else if(requestType.equalsIgnoreCase(Const.FIRST_LAST_NAME))
			f = new ProfileFragmentKindAutocomplete();
		else if(requestType.equalsIgnoreCase(Const.DEPARTMENT))
			f = new ResultsFragment();
		else
			return;
		HttpCalback callback = new HttpCalback() {
			
			@Override
			public void onAnswerReturn(Object answer) {
				if(answer == null)
					return;
				if(!JsonToObject.isStatusOk(answer.toString())) {
					showErrorDialog();
					return;
				}
				Bundle args = new Bundle();
				args.putString(Const.JSON, answer.toString());
				f.setArguments(args);
				((MainActivity)getActivity()).addFragment(f);
				etSearch.setText("");
			}
		};
		
//		if(!snifSelected) {
//			PostSearch postSearch = new PostSearch(getActivity(), callback);
//			postSearch.getEmploeeForSearchID(emploee.SearchID);
//		}
//		else {
//			GetBrunch getBranch = new GetBrunch(getActivity(), callback);
//			getBranch.execute(emploee.SearchID);
//		}
		
	}

	private void getServiceClick(int sid) {
		GetService getService = new GetService(getActivity(), getServiceCallback);
		getService.execute(String.valueOf(sid));
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
		public View getView(final int position, View convertView, ViewGroup parent) {
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
