package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;

import utilities.HttpBase.HttpCalback;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pictureit.leumi.animation.AnimationManager;
import com.pictureit.leumi.main.CallSmsEMailMenager;
import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.PostServiceRegistration;
import com.pictureit.leumi.server.SearchCallback;
import com.pictureit.leumi.server.parse.HourOperatation;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.NameValue;
import com.pictureit.leumi.server.parse.Service;
import com.pictureit.leumi.server.parse.Service.ContactInfo;

public class ServiceFragment extends BaseRegularFragmentNotMain {
	
	private Service mService;
	private TextView tvResponsibleParty, tvLike;
	private ImageButton ibCall, ibMail, ibFollow, ibLike;
	private ViewGroup vgCommunication, vgOpenHours, vgLinkToService, vgResponsibleParty;
	private ViewGroup sliderOpenHours, sliderCommunication, sliderLinkToService;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.service_layout, null);

		if(mService == null) {
			showErrorDialog();
			return v;
		}
		
		TextView tvTitle = findView(v, R.id.tv_service_title);
		TextView tvDescription = findView(v, R.id.tv_service_description);
		TextView tvCommunication = findView(v, R.id.tv_service_communication_options);
		TextView tvCommunicationName = findView(v, R.id.tv_service_communication_name);
		TextView tvOpenHours = findView(v, R.id.tv_service_houres_of_service);
		TextView tvOpenDays = findView(v, R.id.tv_service_days_of_service);
		
		TextView tvLinkToService = findView(v, R.id.tv_service_link_to_service);
		tvResponsibleParty = findView(v, R.id.tv_service_responsible_party);
		
		ibCall = findView(v, R.id.ib_service_call);
		ibMail = findView(v, R.id.ib_service_email);
		ibFollow = findView(v, R.id.ib_service_follow);
		ibLike = findView(v, R.id.ib_service_like);
		
		vgCommunication = findView(v, R.id.rl_service_clickable_header_communication_options);
		vgOpenHours = findView(v, R.id.rl_service_clickable_header_hour_opening);
		vgLinkToService = findView(v, R.id.rl_service_clickable_header_link_to_service);
		vgResponsibleParty = findView(v, R.id.rl_service_clickable_header_responsible_party);
		sliderOpenHours = findView(v, R.id.ll_service_dropdown_days_of_service);
		sliderCommunication = findView(v, R.id.ll_service_dropdown_communication_options);
		sliderLinkToService = findView(v, R.id.ll_service_dropdown_link_to_service);
		
		tvTitle.setText(mService.BllBusinessService);
		setTvResponsibleParty(tvResponsibleParty ,mService.ServiceOwner, mService.ServiceUnitCode);
		setTvCommunication(mService.ContactInfo, tvCommunication, tvCommunicationName);
		setTvOpenHours(mService.ServiceHourOperatation, tvOpenHours, tvOpenDays);
		setTvLinkToService(tvLinkToService, mService.ServiceUrl);
//		tvLike.setText(mService.LikingData.LikingCount);
		
		return v;
	}
	
	private void setTvResponsibleParty(TextView tvResponsibleParty2, String serviceOwner, String serviceUnitCode) {
		if(serviceUnitCode != null && serviceOwner != null
				&& !serviceUnitCode.equals("") && !serviceOwner.equals("")) {
			tvResponsibleParty2.setText(serviceOwner +" - "+serviceUnitCode);
		} else {
			vgResponsibleParty.setVisibility(View.GONE);
		}
	}

	private void setTvLinkToService(TextView tvLink, String url) {
		if (url != null && !url.equalsIgnoreCase("")) {
			tvLink.setText(
		            Html.fromHtml(
		                "<a href="+url+">"+url+"</a> "));
			tvLink.setMovementMethod(LinkMovementMethod.getInstance());
		} else {
			vgLinkToService.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mService!=null)
			initListeners();
	}
	
	private void initListeners() {
		ibCall.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				call();
			}
		});
		ibMail.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				email(mService.ContactInfo);
			}
		});
		ibFollow.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				follow();
			}
		});
		ibLike.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
			}
		});
		vgCommunication.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				showHideView(sliderCommunication);
			}
		});
		vgOpenHours.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				showHideView(sliderOpenHours);
			}
		});
		vgLinkToService.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				showHideView(sliderLinkToService);
			}
		});
		vgResponsibleParty.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				showHideView(tvResponsibleParty);
			}
		});
		tvResponsibleParty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchPeopleInResponsibleUnit();
			}
		});
	}

	protected void follow() {
		if(true)
			return;
		@SuppressWarnings("unused")
		PostServiceRegistration register = new PostServiceRegistration(getActivity(), new HttpCalback() {
			@Override
			public void onAnswerReturn(Object object) {
				// TODO Auto-generated method stub
				
			}
		});
		register.registerToService("identity");
	}

	protected void searchPeopleInResponsibleUnit() {
		PostSearch postSearchPeopleInResponsibleUnit = new PostSearch(getActivity(), SearchCallback.getCallback(getActivity()));
		postSearchPeopleInResponsibleUnit.getEmployeeForDepartment(mService.ServiceOwner);
	}

	protected void showHideView(View slider) {
		if(slider.getVisibility() == View.VISIBLE)
			AnimationManager.collapse(slider, 300);
		else
			AnimationManager.expand(slider, 300);
	}

	protected void email(ArrayList<ContactInfo> ci) {
		if(ci == null)
			Dialogs.generalDialog(getActivity(), (String) getText(R.string.no_email_found));
		for (int i = 0; i < mService.ContactInfo.size(); i++) {
			if (mService.ContactInfo.get(i).contact.equalsIgnoreCase(JsonToObject.CONTACT_INFO_CONTACT_VALUE_E_MAIL)) {
				CallSmsEMailMenager.sendEMail(mService.ContactInfo.get(i).Value, getActivity());
			}
		}		
	}

	protected void call() {
		ArrayList<NameValue> nums = new ArrayList<NameValue>();
		for (int i = 0; i < mService.ContactInfo.size(); i++) {
			if (mService.ContactInfo.get(i).contact
					.equalsIgnoreCase(JsonToObject.CONTACT_INFO_CONTACT_VALUE_PHONE))
				nums.add(new NameValue(getResources().getString(
						R.string.tel_num), mService.ContactInfo.get(i).Value));
		}
		CallSmsEMailMenager.call(nums, getActivity());

	}

	private void setTvOpenHours(
			ArrayList<HourOperatation> serviceHourOperatation, TextView tvOpenHours, TextView tvOpenDays) {
		
		final String CLOSE = "Close";
		
		if(serviceHourOperatation == null) {
			vgOpenHours.setVisibility(View.GONE);
			return;
		}
		
		ArrayList<HourOperatation> s = serviceHourOperatation;
		StringBuilder sbHours = new StringBuilder();
		StringBuilder sbDays = new StringBuilder();
		for (int i = 0; i < serviceHourOperatation.size(); i++) {
			if (!s.get(i).CloseOpen.equalsIgnoreCase(CLOSE)) {
				sbDays.append(getDayString(s.get(i).DayInWeek));
				if (s.get(i).MFrom != null
						&& !s.get(i).MFrom.equalsIgnoreCase(""))
					sbHours.append(getResources().getString(R.string.from) + "- ")
							.append(s.get(i).MFrom)
							.append(" "
									+ getResources().getString(R.string.till)
									+ " ").append(s.get(i).MTo);
				if (s.get(i).EFrom != null
						&& !s.get(i).EFrom.equalsIgnoreCase(""))
					sbHours.append(
							" , " + getResources().getString(R.string.from)
									+ "- ")
							.append(s.get(i).EFrom)
							.append(" "
									+ getResources().getString(R.string.till)
									+ " ").append(s.get(i).ETo);
				if (i < serviceHourOperatation.size()){
					sbHours.append("\n");
					sbDays.append("\n");
				}
			}
		}
		tvOpenDays.setText(sbDays.toString());
		tvOpenHours.setText(sbHours.toString());
		if(sbDays.length() < 1)
			vgOpenHours.setVisibility(View.GONE);
	}

	private void setTvCommunication(ArrayList<ContactInfo> contactInfo, TextView tvCommunication, TextView tvCommunicationName) {
		if(contactInfo == null) {
			vgCommunication.setVisibility(View.GONE);
			return;
		}
		
		StringBuilder sbAddress = new StringBuilder();
		StringBuilder sbAddressType = new StringBuilder();
		for (int i = 0; i < contactInfo.size(); i++) {
			if(contactInfo.get(i).contact.equalsIgnoreCase(JsonToObject.CONTACT_INFO_CONTACT_VALUE_PHONE)) {
				sbAddressType.append(getText(R.string.tel_num));
				sbAddress.append(contactInfo.get(i).Value);
			} else if (contactInfo.get(i).contact.equalsIgnoreCase(JsonToObject.CONTACT_INFO_CONTACT_VALUE_E_MAIL)) {
				sbAddressType.append(getText(R.string.email));
				sbAddress.append(contactInfo.get(i).Value);
			}

			if (i < contactInfo.size()){
				sbAddress.append("\n");
				sbAddressType.append("\n");
			}
		}
		tvCommunication.setText(sbAddress.toString());
		tvCommunicationName.setText(sbAddressType.toString());
		
		if(sbAddressType.length() < 1)
			vgCommunication.setVisibility(View.GONE);
	}

	public void setData(Service service) {
		this.mService = service;
	}

	private String getDayString(String dayInWeek) {
		switch (Integer.valueOf(dayInWeek)) {
		case 1:
			return getResources().getString(R.string.yom_a)+"  ";
		case 2:
			return getResources().getString(R.string.yom_b)+"  ";
		case 3:
			return getResources().getString(R.string.yom_c)+" "+"  ";
		case 4:
			return getResources().getString(R.string.yom_d)+"  ";
		case 5:
			return getResources().getString(R.string.yom_e)+"  ";
		case 6:
			return getResources().getString(R.string.yom_f)+"  "+"  ";
		case 7:
			return getResources().getString(R.string.yom_g)+"  ";
		default:
			break;
		}
		return "";
	}
}
