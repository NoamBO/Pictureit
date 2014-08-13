package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pictureit.leumi.main.CallSmsEMailMenager;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.NameValue;
import com.pictureit.leumi.server.parse.Profile;

public class ResultsFragment extends FragmentWithoutTabs {

	ArrayList<Profile> mArrayList;
	private ListView lvFilterByUnit, lvFilterByJob;
	private ListView lvMainList;
	private TextView tvResultsCount;
	private ArrayList<String> departmentArray, jobArray;

	private ResultsAdapter mMainAdapter;
	private ViewGroup vgJobDropDown, vgDepartmentDropDown;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.search_results_layout, null);

		if (mArrayList == null) {
			Bundle b = getArguments();
			mArrayList = JsonToObject.jsonToSearchResults(b
					.getString(Const.JSON));

		}
		lvMainList = findView(view, R.id.lv_search_results_main_list);
		lvFilterByJob = findView(view, R.id.lv_search_results_filter_by_job);
		lvFilterByUnit = findView(view, R.id.lv_search_results_filter_by_unit);
		tvResultsCount = findView(view, R.id.tv_search_results_count);

		vgJobDropDown = findView(view, R.id.ll_search_results_filter_by_job);
		vgDepartmentDropDown = findView(view,
				R.id.ll_search_results_filter_by_unit);

		setLists();
		
		mMainAdapter = new ResultsAdapter(getActivity(), android.R.layout.simple_list_item_2, mArrayList);
		lvMainList.setAdapter(mMainAdapter);
		setResultsCountBox();
		return view;
	}

	private void setResultsCountBox() {
		tvResultsCount.setText(String.valueOf(mMainAdapter.getCount()));
	}
	
	@Override
	public void onDestroyView() {
		//mMainAdapter.imageLoader.clearCache();
		super.onDestroyView();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setDropdownListviewListener(lvFilterByJob, "job");
		setDropdownListviewListener(lvFilterByUnit, "unit");

		vgJobDropDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lvFilterByJob.getVisibility() == View.VISIBLE)
					lvFilterByJob.setVisibility(View.GONE);
				else
					lvFilterByJob.setVisibility(View.VISIBLE);

			}
		});
		vgDepartmentDropDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (lvFilterByUnit.getVisibility() == View.VISIBLE)
					lvFilterByUnit.setVisibility(View.GONE);
				else
					lvFilterByUnit.setVisibility(View.VISIBLE);

			}
		});
		lvMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				Profile p = (Profile) adapter.getItemAtPosition(position);
				String profileJson = new Gson().toJson(p);
				Bundle b = new Bundle();
				b.putString(Const.JSON, profileJson);
				
				Fragment f = new EmploeeProfileFragment();
				f.setArguments(b);
				
				((MainActivity) getActivity()).addFragment(f);
			}
		});
	}

	private void setDropdownListviewListener(ListView listView, final String tag) {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				DropDownAdapter adapter = ((DropDownAdapter) parent
						.getAdapter());
				if (adapter.selectedPosition == position) {
					adapter.selectedPosition = -1;
					addFilter(tag, "");
				} else {
					addFilter(tag, adapter.getItem(position));
					adapter.selectedPosition = position;
				}
				adapter.notifyDataSetChanged();

				if (lvFilterByUnit.getVisibility() == View.VISIBLE)
					lvFilterByUnit.setVisibility(View.GONE);
				if (lvFilterByJob.getVisibility() == View.VISIBLE)
					lvFilterByJob.setVisibility(View.GONE);
			}
		});
	}

	protected void addFilter(String listType, String filter) {
		if(listType.equalsIgnoreCase("job")) {
			if(filter != null) {
				mMainAdapter.setFilterJob(filter);
			}
		}
		else {
			if(filter != null) {
				mMainAdapter.setFilterDepartment(filter);
			}
		}
		mMainAdapter.getFilter().filter(filter);
				

	}

	private void setLists() {
		departmentArray = new ArrayList<String>();
		jobArray = new ArrayList<String>();
		for (int i = 0; i < mArrayList.size(); i++) {
			String department = mArrayList.get(i).L144Department;
			String job = mArrayList.get(i).L144Job;
			if (!department.equalsIgnoreCase("") && !departmentArray.contains(department))
				departmentArray.add(department);
			if (!job.equalsIgnoreCase("") && !jobArray.contains(job))
				jobArray.add(job);
		}

		lvFilterByJob.setAdapter(new DropDownAdapter(getActivity(),
				android.R.layout.simple_list_item_2, jobArray));
		lvFilterByUnit.setAdapter(new DropDownAdapter(getActivity(),
				android.R.layout.simple_list_item_2, departmentArray));
	}
	
	@Override
	public void onDetach() {
		imageLoader.clearMemoryCache();
		super.onDetach();
	}
	
	public class ResultsAdapter extends ArrayAdapter<Profile>{

		private ArrayList<Profile> arrayList;
		private ArrayList<Profile> itemsAll;
	    private ArrayList<Profile> suggestions;
		private Context ctx;
		
		private String filterDepartment ="";
		private String filterJob ="";
		
		@SuppressWarnings("unchecked")
		public ResultsAdapter(Context context,
				int textViewResourceId, ArrayList<Profile> objects) {
			super(context, textViewResourceId, objects);
			arrayList = objects;
			itemsAll = (ArrayList<Profile>) arrayList.clone();
			suggestions = new ArrayList<Profile>();
			
			ctx = context;

		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null) {
				holder = new ViewHolder();
				convertView = ((Activity)ctx).getLayoutInflater().inflate(R.layout.search_result_row, null);
				holder.profileImage = findView(convertView, R.id.iv_search_result_image);
				holder.name = findView(convertView, R.id.tv_search_result_name);
				holder.unit = findView(convertView, R.id.tv_search_result_unit_name);
				holder.job = findView(convertView, R.id.tv_search_result_job_name);
				holder.call = findView(convertView, R.id.ib_search_result_make_call);
				holder.sms = findView(convertView, R.id.ib_search_result_send_sms);
				holder.email = findView(convertView, R.id.ib_search_result_send_email);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.name.setText(getItem(position).L144FirstName +" "+ getItem(position).L144LastName);
			holder.job.setText(getItem(position).L144Job);
			holder.unit.setText(getItem(position).L144Department);
			holder.call.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<NameValue> nums = new ArrayList<NameValue>();
					if(getItem(position).L144WorkPhone != null && getItem(position).L144WorkPhone.length() >0){
						nums.add(new NameValue(getResources().getString(R.string.office), getItem(position).L144WorkPhone));
					}
					if(getItem(position).L144MobileCellular != null && getItem(position).L144MobileCellular.length() >0){
						nums.add(new NameValue(getResources().getString(R.string.cellphone), getItem(position).L144MobileCellular));
					}
					CallSmsEMailMenager.call(nums, ctx);
				}
			});
			holder.sms.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CallSmsEMailMenager.sendSms(getItem(position).L144MobileCellular, ctx);
				}
			});
			holder.email.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CallSmsEMailMenager.sendEMail(getItem(position).WorkEmeil, ctx);
				}
			});
			
			String picUrl = getItem(position).L144WorkerPictureUrl;
			imageLoader.displayImage(picUrl, holder.profileImage, options);
			
			return convertView;
		}
		
		@Override
		public Filter getFilter() {
			// TODO Auto-generated method stub
			return filter;
		}
		
		Filter filter = new Filter() {
			
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				@SuppressWarnings("unchecked")
				ArrayList<Profile> filteredList = (ArrayList<Profile>) results.values;
	            if(results != null && results.count > 0) {
	                clear();
	                for (Profile p : filteredList) {
	                    add(p);
	                }
	                notifyDataSetChanged();
	                setResultsCountBox();
	            }		
			}
			
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if(filterDepartment != null || filterJob != null) {
	                arrayList.clear();
	                suggestions.clear();
	                for (Profile profile : itemsAll) {
	                    if(profile.L144Department.startsWith(filterDepartment)
	                    		&& profile.L144Job.startsWith(filterJob)){
	                        suggestions.add(profile);
	                    }
	                }
	                FilterResults filterResults = new FilterResults();
	                filterResults.values = suggestions;
	                filterResults.count = suggestions.size();
	                return filterResults;
	            } else {
	                return new FilterResults();
	            }
			}
		};
		
		public void setFilterJob(String filter) {
			filterJob = filter;
		}
		
		public void setFilterDepartment(String filter) {
			filterDepartment = filter;
		}
		
	}

	public class DropDownAdapter extends ArrayAdapter<String> {

		private ArrayList<String> arrayList;

		private Context ctx;

		public int selectedPosition = -1;

		public DropDownAdapter(Context context, int resource,
				ArrayList<String> objects) {
			super(context, resource, objects);
			arrayList = objects;
			ctx = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DropDownViewHolder holder;
			if (convertView == null) {
				holder = new DropDownViewHolder();
				convertView = ((Activity) ctx).getLayoutInflater().inflate(
						R.layout.drop_down_search_row, null);
				holder.textView = findView(convertView, R.id.tv_drop_down_row);
				holder.imageView = findView(convertView, R.id.iv_drop_down_row);

				convertView.setTag(holder);
			} else {
				holder = (DropDownViewHolder) convertView.getTag();
			}

			holder.imageView.setVisibility(View.GONE);

			holder.textView.setText(arrayList.get(position));

			if (selectedPosition == position)
				holder.imageView.setVisibility(View.VISIBLE);

			return convertView;
		}

		@Override
		public String getItem(int position) {
			return arrayList.get(position);
		}

		@Override
		public int getCount() {
			return arrayList.size();
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	static class DropDownViewHolder {
		TextView textView;
		ImageView imageView;
	}
	
	static class ViewHolder {
		ImageView profileImage;
		TextView name;
		TextView unit;
		TextView job;
		ImageButton sms;
		ImageButton call;
		ImageButton email;
	}
}
