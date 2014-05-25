package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import utilities.BaseFragment;
import utilities.OutgoingCommunication;
import utilities.server.HttpBase.HttpCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import com.pictureit.leumi.server.GetSystemAddition;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.SearchCallback;
import com.pictureit.leumi.server.parse.Emploee;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.LeumiService;
import com.pictureit.leumi.server.parse.SystemAddition;
import com.pictureit.leumi.server.parse.SystemAddition.Baner;

public class HomeFragment extends BaseFragment {

	private ListView lvServicesList;
	private AutoCompleteTextView etSearch;
	private ImageButton ibTellUsYouDidntFindService;
	private ImageButton ibSearch;
	private ImageButton ibBaner;
	private AutoCompleteTextViewHandler actvHandler;
	private Baner mBaner;
	private TextView tvHelp;

	private ArrayList<LeumiService> mLastServicesList;
	private SystemAddition mSystemAddition;
	private boolean initialServiceListPending, initialSystemAdditionPending;
	
	private Handler handler = new Handler();
	private final Runnable setBanerData = new Runnable(){
	    int i = -1;
		public void run(){
	        try {
	        	i++;
	        	imageLoader.displayImage(LocalStorageManager.getInstance().systemAddition.baners.get(i).BanerImage, ibBaner);
	        	mBaner = LocalStorageManager.getInstance().systemAddition.baners.get(i);
	        	if(i == LocalStorageManager.getInstance().systemAddition.baners.size() -1)
	        		i = -1;
	            handler.postDelayed(this, 10 * 1000);    
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }   
	    }
	};
	
	private final BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
	    @Override
	        public void onReceive(Context context, Intent intent) {
	    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = cm.getActiveNetworkInfo();
	        if (info != null) {
	            if (info.isConnected()) {
	                if(LocalStorageManager.getInstance().homeServicesList == null) {
	                	setServicesListAndBaners();
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

	private HttpCallback getLastServicesCallback = new HttpCallback() {

		@Override
		public void onAnswerReturn(Object answer) {
			if (JsonToObject.isStatusOk((String) answer)) {
				mLastServicesList = JsonToObject.jsonToLastServicesList((String) answer);
				LocalStorageManager.getInstance().homeServicesList = mLastServicesList;
				initialServiceListPending = false;
			}
			setServicesListAndBaners();
		}
	};
	
	private HttpCallback systemAdditionCallback = new HttpCallback() {
		
		@Override
		public void onAnswerReturn(Object answer) {
			if (JsonToObject.isStatusOk((String) answer)) {
				mSystemAddition = JsonToObject.jsonToSystemAddition((String) answer);
				LocalStorageManager.getInstance().systemAddition = mSystemAddition;
				initialSystemAdditionPending = false;
			}
			setServicesListAndBaners();
		}
	};

	private void setServicesListAndBaners() {
		if (LocalStorageManager.getInstance().homeServicesList != null)
			mLastServicesList = LocalStorageManager.getInstance().homeServicesList;
		if(LocalStorageManager.getInstance().systemAddition != null)
			mSystemAddition = LocalStorageManager.getInstance().systemAddition;
		if (!(mLastServicesList != null && mSystemAddition!= null)) {
			if(!Settings.isNetworkAvailable(getActivity())) {
				registerInternetReceiver();
				return;
			}
			if(mLastServicesList == null && !initialServiceListPending) {
				initialServiceListPending = true;
				GetListLastServices getListLastServices = new GetListLastServices(
						getActivity(), getLastServicesCallback);
				getListLastServices.execute();
			}
			if(mSystemAddition == null && !initialSystemAdditionPending) {
				initialSystemAdditionPending = true;
				GetSystemAddition getSystemAddition = new GetSystemAddition(getActivity(), systemAdditionCallback);
				getSystemAddition.execute();
			}
			return;
		}
		LastServicesListViewAdapter adapter = new LastServicesListViewAdapter(
				getActivity(), android.R.layout.simple_list_item_1,
				mLastServicesList);
		lvServicesList.setAdapter(adapter);
		setSystemAddition();
	}
	
	private void setSystemAddition() {
		((MainActivity) getActivity()).initWebView(mSystemAddition.AdditionServiceUrl);
		handler.post(setBanerData);
	}

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.home_screen, container, false);
		
		etSearch = (AutoCompleteTextView) v.findViewById(R.id.et_search);
		etSearch.setText("");
		actvHandler = new AutoCompleteTextViewHandler();
		actvHandler.setListener(getActivity(), etSearch,Const.MOBILE_FULL);
		ibBaner = findView(v, R.id.ib_main_banner);
		tvHelp = findView(v, R.id.tv_main_help);
		
		lvServicesList = (ListView) v.findViewById(R.id.lv_main_services_list);

		ibTellUsYouDidntFindService = (ImageButton) v.findViewById(R.id.ib_main_no_service_found);
		ibSearch = (ImageButton) v.findViewById(R.id.ib_search);
		setServicesListAndBaners();
		
		return v;
	}


	@Override
	public void onResume() {
		super.onResume();
		tvHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String url = LocalStorageManager.getInstance().systemAddition.PageHelpUrl;
				((MainActivity) getActivity()).onBanerTouch(url);
			}
		});
		ibBaner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String url = mBaner.BanerUrl;
				((MainActivity) getActivity()).onBanerTouch(url);
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
				
				PostSearch postSearch = new PostSearch(getActivity(), new HttpCallback() {
					
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
	
	public void clearFreeSearchEditText() {
		if(actvHandler!=null)
			actvHandler.cancelTask();
	}

	protected void onAutocopletionPressed(Emploee emploee) {
		String searchType = emploee.SearchType;
		String systemType = emploee.SystemType;

		if (systemType.equalsIgnoreCase(Const.PEOPLE)) {
			if (searchType.equalsIgnoreCase(Const.JOB))
				searchForJob(emploee.SearchID);
			else if (searchType.equalsIgnoreCase(Const.FIRST_LAST_NAME))
				searchForPersons(emploee.SearchID);
			else if (searchType.equals(Const.SYSTEM_TYPE_SNIF))
				searchForSnif(emploee.SearchID);
			else if (searchType.equals(Const.DEPARTMENT))
				searchForDepartment(emploee.SearchID);
		} else if (systemType.equalsIgnoreCase(Const.SERVICE_CLICK)
				&& searchType.equalsIgnoreCase(""))
			searchForService(emploee.SearchID);
		else if(systemType.equalsIgnoreCase(Const.SYSTEM_TYPE_SNIF_OTHER_BASK) &&
				searchType.equalsIgnoreCase(""))
			searchForSnif(emploee.SearchID);

//		} else if(searchType.equalsIgnoreCase(Const.JOB)) {
//			PostSearch searchForDepartment = new PostSearch(getActivity(), callback);
//			searchForDepartment.getEmployeeForDepartment(emploee.SearchKey);

		
	}
	
	private void searchForJob(String searchID) {
		PostSearch searchForJob = new PostSearch(getActivity(), SearchCallback.getCallback(getActivity(), etSearch));
		searchForJob.getEmployeesForJob(searchID);
	}
	
	private void searchForDepartment(String searchID) {
		PostSearch searchForDepartment = new PostSearch(getActivity(),  SearchCallback.getCallback(getActivity(), etSearch));
		searchForDepartment.getBranchEmployeesForDepartmentCode(searchID);
	}

	private void searchForPersons(String searchId) {
		PostSearch postSearch = new PostSearch(getActivity(),  SearchCallback.getCallback(getActivity(), etSearch));
		postSearch.getEmploeeForSearchID(searchId);
	}
	
	private void searchForSnif(String searchID) {
		GetBrunch getBranch = new GetBrunch(getActivity(), new HttpCallback() {
			@Override
			public void onAnswerReturn(Object object) {
				etSearch.setText("");
				if(object == null) {
					showErrorDialog();
					return;
				}
				if(!JsonToObject.isStatusOk((String) object)) {
					showErrorDialog();
					return;
				}
				Fragment f = new BranchFragment();
				Bundle b = new Bundle();
				b.putString(Const.JSON, (String) object);
				f.setArguments(b);
				((MainActivity)getActivity()).addFragment(f);
			}
		});
		getBranch.execute(searchID);
	}
	
	private void searchForService(String searchID) {
		GetService getService = new GetService(getActivity(), getCallbackForServiceClick());
		getService.execute(searchID);
	}

	private HttpCallback getCallbackForServiceClick() {
		return new HttpCallback() {

			@Override
			public void onAnswerReturn(Object answer) {
				if (answer == null) {
					showErrorDialog();
					return;
				}
				try {
					JSONObject j = new JSONObject(answer.toString());
					if (!JsonToObject.isStatusOk(j.getString("result"))) {
						showErrorDialog();
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					showErrorDialog();
					return;
				}
				etSearch.setText("");
				Bundle b = new Bundle();
				String s = answer.toString();
				b.putString(Const.JSON, s);

				ServiceFragment f = new ServiceFragment();
				f.setArguments(b);
				((MainActivity) getActivity()).addFragment(f);
			}
		};
	}

	private void getServiceClick(int sid) {
		GetService getService = new GetService(getActivity(), getCallbackForServiceClick());
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
