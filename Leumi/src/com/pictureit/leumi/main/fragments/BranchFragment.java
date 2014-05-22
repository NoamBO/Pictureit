package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pictureit.leumi.main.CallSmsEMailMenager;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.SearchCallback;
import com.pictureit.leumi.server.parse.Branch;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.NameValue;
import com.pictureit.leumi.server.parse.Service.ContactInfo;

public class BranchFragment extends FragmentWithoutTabs {

	private static final Integer[] NUMBERS_VALID_BANK = {10, 34};
	
	private TextView tvBranch;
	private ImageButton ibCall, ibNavigate;
	private Branch mBranch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(getArguments() == null)
			return super.onCreateView(inflater, container, savedInstanceState);
		
		if(mBranch == null)
			mBranch = JsonToObject.jsonToBranch(getArguments().getString(Const.JSON));
		
		View v = inflater.inflate(R.layout.branch_layout, null);
		
		tvBranch = findView(v, R.id.tv_branch_name);
		TextView tvTelephoneName = findView(v, R.id.tv_branch_communication_name);
		TextView tvTelephoneNumber = findView(v, R.id.tv_branch_communication_options);
		TextView tvAddress = findView(v, R.id.tv_branch_address);
		TextView tvOpenDays = findView(v, R.id.tv_branch_days_of_operation);
		TextView tvOpenHours = findView(v, R.id.tv_branch_houres_of_operation);
		ibCall = findView(v, R.id.ib_branch_call);
		ibNavigate = findView(v, R.id.ib_branch_navigate);
		
		setCommunicationTextViews(tvTelephoneNumber, tvTelephoneName);
		setTvAddress(tvAddress);
		setOpenHoursTextViews(tvOpenHours, tvOpenDays);
		setTvBranchNameFunctionality(tvBranch);
		
		return v;
	}
	
	private void setTvBranchNameFunctionality(TextView textView) {
		if(Arrays.asList(NUMBERS_VALID_BANK).contains(Integer.valueOf(mBranch.BankNum))) {
			textView.setText(
		            Html.fromHtml(
		                "<u>"+mBranch.BankName+"</u> "));
			
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PostSearch search = new PostSearch(getActivity(), SearchCallback.getCallback(getActivity()));
					search.getBranchEmployeesForDepartmentCode(mBranch.DepartmentCode);
				}
			});
		} else {
			textView.setText(mBranch.BankName);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		ibCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<NameValue> nums = new ArrayList<NameValue>();
				if(mBranch.BranchPhone != null && mBranch.BranchPhone.length() >0){
					nums.add(new NameValue(getResources().getString(R.string.cellphone), mBranch.BranchPhone));
				}
				CallSmsEMailMenager.call(nums, getActivity());
			}
		});
		ibNavigate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try
				{
				   String url = "waze://?q="+mBranch.BranchStreet+"+"+mBranch.BranchCity;
				    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
				   startActivity( intent );
				}
				catch ( ActivityNotFoundException ex  )
				{
				  Intent intent =
				    new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
				  startActivity(intent);
				}
			}
		});
	}
	
	private void setOpenHoursTextViews(TextView tvOpenHours, TextView tvOpenDays) {
		ScreensHelper.setOpenHouersTextViews(mBranch.BranchHourOperatation, tvOpenHours, tvOpenDays, getActivity());
	}

	private void setTvAddress(TextView tvAddress) {
		StringBuilder sb = new StringBuilder();
		if(!mBranch.BranchStreet.equals(""))
			sb.append(mBranch.BranchStreet);
		if(!mBranch.BranchCity.equals(""))
			sb.append(" , ").append(mBranch.BranchCity);
		if(!mBranch.BranchZipCode.equals(""))
			sb.append(" , ").append(mBranch.BranchZipCode);
		
		tvAddress.setText(sb.toString());
	}
	
	private void setCommunicationTextViews(TextView tvCommunication, TextView tvCommunicationName) {
		ArrayList<ContactInfo> contactInfo = new ArrayList<ContactInfo>();
		if(!mBranch.BranchPhone.equals(""))
			contactInfo.add(new ContactInfo(mBranch.BranchPhone, JsonToObject.CONTACT_INFO_CONTACT_VALUE_PHONE));
		if(!mBranch.BranchFax.equals(""))
			contactInfo.add(new ContactInfo(mBranch.BranchFax, JsonToObject.CONTACT_INFO_CONTACT_VALUE_FAX));
		ScreensHelper.setCommunicationText(contactInfo, tvCommunication, tvCommunicationName, getActivity());
	}
}
