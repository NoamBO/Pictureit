package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;

import utilities.BaseFragment;
import utilities.server.HttpBase.HttpCallback;
import utilities.view.AutoResizeTextView;
import utilities.view.AutoResizeTextViewSetter;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pictureit.leumi.animation.AnimationManager;
import com.pictureit.leumi.main.CallSmsEMailMenager;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.SearchCallback;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.NameValue;
import com.pictureit.leumi.server.parse.Profile;

public class BaseProfileFragment extends BaseFragment {

	private HttpCallback callback = new HttpCallback() {
		
		@Override
		public void onAnswerReturn(String answer) {
		if(!JsonToObject.isStatusOk(answer)) {
			showErrorDialog();
			return;
		}
			ResultsFragment f = new ResultsFragment();
			Bundle b = new Bundle();
			b.putString(Const.JSON, answer);
			f.setArguments(b);
			((MainActivity)getActivity()).addFragment(f);
		}
	};
	
	public BaseProfileFragment() {
	}

	private ImageButton ibHome;
	private ImageView arrowHierarchy;
	private ViewGroup vgHierarchyHeader;
	private TextView tvUnit;
	protected Profile mProfile;
	private ImageButton ibCall, ibSms, ibMail;
	private ListView mListView;
	private ArrayList<NameValue> mArrayList;

	@Override
	public void onDetach() {
		imageLoader.clearMemoryCache();
		super.onDetach();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.profile_screen, container, false);
		
		if(savedInstanceState != null) {
			if(savedInstanceState.getBoolean(EmploeeProfileFragment.KEY_SHOW_HOME_BUTTON_ON_PROFILE_SCREEN)) {
				ibHome = findView(v, R.id.ib_go_home_profile_screen);
				ibHome.setVisibility(View.VISIBLE);
			}
		}

		ImageView profilePic = findView(v, R.id.iv_profile_screen);
		
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

		if (mProfile != null) {
			if(mProfile.L144FirstName != null && mProfile.L144LastName!=null)
			tvProfileName.setText(mProfile.L144FirstName + " "
					+ mProfile.L144LastName);
			if(mProfile.L144WorkPhone!=null)
			tvLineNum.setText(mProfile.L144WorkPhone);
			if(mProfile.L144BankCellular!=null)
			tvCellPhoneNum.setText(mProfile.L144BankCellular);
			if(mProfile.L144Fax2Mail!=null)
			tvFaxNum.setText(mProfile.L144Fax2Mail);
			if(mProfile.WorkEmeil!=null)
			tvEMail.setText(mProfile.WorkEmeil);
			if(mProfile.L144Job!=null)
			tvJob.setText(mProfile.L144Job);

			if (mProfile.L144Department != null && !mProfile.L144Department.equalsIgnoreCase(""))
				tvUnit.setText(
			            Html.fromHtml(
			                "<u>"+mProfile.L144Department+"</u> "));
				
			initListview(mProfile.L144Lineage, mProfile.L144OrgNameLineage);
			imageLoader.displayImage(mProfile.L144WorkerPictureUrl, profilePic, options);
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
		if(ibHome != null)
			ibHome.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((MainActivity) getActivity()).goHome();
				}
			});
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
				if(mProfile.L144BankCellular != null && mProfile.L144BankCellular.length() >0){
					nums.add(new NameValue(getResources().getString(R.string.cellphone), mProfile.L144BankCellular));
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
				search(mArrayList.get(position).value);
			}
		});
		// TODO
		tvUnit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					search(mArrayList.get(mArrayList.size() - 1).value);
				}
			});
	}
	
	private void search(String unitID) {
		PostSearch httpPost = new PostSearch(getActivity(), SearchCallback.getCallback(getActivity()));
		httpPost.getBranchEmployeesForDepartmentCode(unitID);
	}
	
	protected void changeHierarchyListVisibility() {
		if (mListView.getVisibility() == View.GONE) {
			arrowHierarchy.setBackgroundResource(R.drawable.arrowup);
			//AnimationManager.expand(mListView);
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
			return mArrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mArrayList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View arg01, ViewGroup listView) {
			View v = getActivity().getLayoutInflater().inflate(R.layout.hierarchy_row, null);
			AutoResizeTextView tvName = (AutoResizeTextView) v.findViewById(R.id.tv_listview_row_name);
			ImageView ivArrowLeft = (ImageView) v.findViewById(R.id.iv_listview_row_arrow_left);
			RelativeLayout parent = (RelativeLayout) v.findViewById(R.id.rl_hierarchy_row_parent);
			if(position == 0
					|| position == 1)
				ivArrowLeft.setVisibility(View.GONE);
			
			parent.setPadding(0, 10, position*8, 10);
			AutoResizeTextViewSetter.setText(tvName, mArrayList.get(position).name);
			return v;
		}
		
	}
}
