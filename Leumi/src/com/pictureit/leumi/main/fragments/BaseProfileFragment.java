package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;

import utilities.BaseFragment;
import utilities.HttpBase.HttpCalback;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pictureit.leumi.animation.AnimationManager;
import com.pictureit.leumi.main.CallSmsEMailMenager;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.SearchCallback;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.NameValue;
import com.pictureit.leumi.server.parse.Profile;

public class BaseProfileFragment extends BaseFragment {

	private HttpCalback callback = new HttpCalback() {
		
		@Override
		public void onAnswerReturn(Object answer) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public BaseProfileFragment() {
	}

	private ImageView arrowHierarchy;
	private ViewGroup vgHierarchyHeader;
	private TextView tvUnit;
	private Profile mProfile;
	private ImageButton ibCall, ibSms, ibMail;
	private ListView mListView;
	private ArrayList<NameValue> mArrayList;
	private ScrollView scrollView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.profile_screen, container, false);

		tvUnit = (TextView) v.findViewById(R.id.tv_profile_unit);
		TextView tvProfileName = (TextView) v
				.findViewById(R.id.tv_profile_name);
		TextView tvLineNum = (TextView) v.findViewById(R.id.tv_profile_tel_num);
		TextView tvCellPhoneNum = (TextView) v
				.findViewById(R.id.tv_profile_cell_num);
		TextView tvFaxNum = (TextView) v.findViewById(R.id.tv_profile_fax);
		TextView tvEMail = (TextView) v.findViewById(R.id.tv_profile_email);
		TextView tvJob = (TextView) v.findViewById(R.id.tv_profile_job);
		vgHierarchyHeader = (ViewGroup) v.findViewById(R.id.rl_profile_hierarchy);
		arrowHierarchy = (ImageView) v.findViewById(R.id.iv_profile_hierarchy_arrow);
		
		ibCall = (ImageButton) v.findViewById(R.id.ib_profile_call);
		ibSms = (ImageButton) v.findViewById(R.id.ib_profile_send_sms);
		ibMail = (ImageButton) v.findViewById(R.id.ib_profile_email);
		mListView = (ListView) v.findViewById(R.id.lv_profile_organization_hierarchy);
		scrollView = findView(v, R.id.profile_scrollView);
		
		if (mProfile == null) {
			Bundle b = getArguments();
			if (b != null) {
				String s = b.getString(Const.JSON);
				mProfile = JsonToObject.jsonToUserProfile(s);
			}
		}
		if (mProfile != null) {
			tvProfileName.setText(mProfile.L144FirstName + " "
					+ mProfile.L144LastName);
			tvLineNum.setText(mProfile.L144WorkPhone);
			tvCellPhoneNum.setText(mProfile.L144MobileCellular);
			tvFaxNum.setText(mProfile.L144Fax2Mail);
			tvEMail.setText(mProfile.WorkEmeil);
			tvJob.setText(mProfile.L144Job);

			if (mProfile.L144Department != null && !mProfile.L144Department.equalsIgnoreCase(""))
				tvUnit.setText(
			            Html.fromHtml(
			                "<u>"+mProfile.L144Department+"</u> "));
				
			initListview(mProfile.L144Lineage, mProfile.L144OrgNameLineage);
		}
		return v;
	}

	private void initListview(String code, String headers) {
		mArrayList = JsonToObject.parseHeirarchy(code, headers);
		thisFragmentListviewAdapter adapter = new thisFragmentListviewAdapter();
		mListView.setAdapter(adapter);
		AnimationManager.setListViewHeightBasedOnChildren(mListView);
	}

	@Override
	public void onResume() {
		super.onResume();
		ibCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<NameValue> nums = new ArrayList<NameValue>();
				if(mProfile.L144WorkPhone != null && mProfile.L144WorkPhone.length() >0){
					nums.add(new NameValue(getResources().getString(R.string.office), mProfile.L144WorkPhone));
				}
				if(mProfile.L144MobileCellular != null && mProfile.L144MobileCellular.length() >0){
					nums.add(new NameValue(getResources().getString(R.string.cellphone), mProfile.L144MobileCellular));
				}
				CallSmsEMailMenager.call(nums, getActivity());
			}
		});
		
		ibSms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CallSmsEMailMenager.sendSms(mProfile.L144MobileCellular, getActivity());
			}
		});
		
		ibMail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CallSmsEMailMenager.sendEMail(mProfile.WorkEmeil, getActivity());
			}
		});
		
		vgHierarchyHeader.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeHierarchyListVisibility();
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if (position == 0 || position == 1)
					return;
				PostSearch httpPost = new PostSearch(getActivity(), callback);
				httpPost.getBranchEmployeesForDepartmentCode(mArrayList.get(position).value);
			}
		});
		tvUnit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					PostSearch postSearchPeopleInResponsibleUnit = new PostSearch(getActivity(), SearchCallback.getCallback(getActivity()));
					postSearchPeopleInResponsibleUnit.getEmployeeForDepartment(mProfile.L144Department);
				}
			});
	}
	
	protected void changeHierarchyListVisibility() {
		if (mListView.getVisibility() == View.GONE) {
			arrowHierarchy.setBackgroundResource(R.drawable.arrowleft);
			mListView.setVisibility(View.VISIBLE);
		} else {
			mListView.setVisibility(View.GONE);
			arrowHierarchy.setBackgroundResource(R.drawable.arrowdown);
		}
	}

	private class thisFragmentListviewAdapter extends BaseAdapter{

		public thisFragmentListviewAdapter() {}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mArrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mArrayList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg01, ViewGroup listView) {
			View v = getActivity().getLayoutInflater().inflate(R.layout.hierarchy_row, null);
			TextView tvName = (TextView) v.findViewById(R.id.tv_listview_row_name);
			ImageView ivArrowDown = (ImageView) v.findViewById(R.id.iv_listview_row_arrow_down);
			ImageView ivArrowLeft = (ImageView) v.findViewById(R.id.iv_listview_row_arrow_left);
			
			if(position == 0
					|| position == 1)
				ivArrowLeft.setVisibility(View.GONE);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = position*8;
			params.gravity = Gravity.CENTER_VERTICAL;
			ivArrowDown.setLayoutParams(params);
			
			tvName.setText(mArrayList.get(position).name);
			return v;
		}
		
	}
}