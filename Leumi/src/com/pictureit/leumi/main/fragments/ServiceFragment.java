package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;

import utilities.server.HttpBase.HttpCallback;
import utilities.view.AutoResizeTextView;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pictureit.leumi.animation.AnimationManager;
import com.pictureit.leumi.main.CallSmsEMailMenager;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.main.Settings;
import com.pictureit.leumi.server.PostLike;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.PostServiceRegistration;
import com.pictureit.leumi.server.SearchCallback;
import com.pictureit.leumi.server.parse.HourOperatation;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.NameValue;
import com.pictureit.leumi.server.parse.Service;
import com.pictureit.leumi.server.parse.Service.ContactInfo;
import com.pictureit.leumi.server.parse.Service.LikingData;

public class ServiceFragment extends FragmentWithoutTabs {
	
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
			Bundle b = getArguments();
			String json = b.getString(Const.JSON);
			mService = JsonToObject.jsonGetService(json);
		}
		
		TextView tvTitle = findView(v, R.id.tv_service_title);
		TextView tvDescription = findView(v, R.id.tv_service_description);
		TextView tvCommunication = findView(v, R.id.tv_service_communication_options);
		TextView tvCommunicationName = findView(v, R.id.tv_service_communication_name);
		TextView tvOpenHours = findView(v, R.id.tv_service_houres_of_service);
		TextView tvOpenDays = findView(v, R.id.tv_service_days_of_service);
		tvLike = findView(v, R.id.tv_service_likes_count);
		
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
		setTvLike();
		
		return v;
	}
	
	private void setTvLike() {
		tvLike.setText(mService.LikingData.LikingCount);
		if(mService.LikingData.Liking.equalsIgnoreCase(Const.ALREADY_LIKE))
			ibLike.setBackgroundResource(R.drawable.likeiconblue);
		else
			ibLike.setBackgroundResource(R.drawable.likeicongray);
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
				PostLike like = new PostLike(getActivity(), new HttpCallback() {
					
					@Override
					public void onAnswerReturn(Object object) {
						LikingData likingData = JsonToObject.jsonToLikingData((String) object);
						if(Integer.valueOf(likingData.LikingCount) > Integer.valueOf(mService.LikingData.LikingCount))
							likingData.Liking = Const.LIKING_REQUEST_TYPE_LIKE;
						else
							likingData.Liking = Const.LIKING_REQUEST_TYPE_UNLIKE;
						mService.LikingData = likingData;
						setTvLike();
					}
				});
				String likeRequest;
				if(mService.LikingData.Liking.equals(Const.ALREADY_LIKE))
					likeRequest = Const.LIKING_REQUEST_TYPE_UNLIKE;
				else
					likeRequest = Const.LIKING_REQUEST_TYPE_LIKE;
				like.like(mService.ServiceID, likeRequest);
					
			}
		});
		vgCommunication.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				showHideView(sliderCommunication, R.id.iv_communication_options_arrow, view);
			}
		});
		vgOpenHours.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				showHideView(sliderOpenHours, R.id.iv__hour_opening_arrow, view);
			}
		});
		vgLinkToService.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				showHideView(sliderLinkToService, R.id.iv_link_to_service_arrow, view);
			}
		});
		vgResponsibleParty.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				showHideView(tvResponsibleParty, R.id.iv_responsible_party_arrow, view);
			}
		});
		tvResponsibleParty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchPeopleInResponsibleUnit();
			}
		});
	}


	@SuppressWarnings("unused")
	protected void follow() {
		if(true)
			return;

		if(mService.Register.Status .equalsIgnoreCase(Const.REGISTER_STATUS_REGISTERED)) {
			new AlertDialog.Builder(getActivity())
			.setTitle(R.string.impossible_to_load_service)
			.setMessage(R.string.already_signed_to_this_service)
			.create().show();
			return;
		}
		
		// TODO signin to service
		PostServiceRegistration register = new PostServiceRegistration(getActivity(), new HttpCallback() {
			@Override
			public void onAnswerReturn(Object object) {
				
			}
		});
		register.registerToService("identity");
	}

	protected void searchPeopleInResponsibleUnit() {
		PostSearch postSearchPeopleInResponsibleUnit = new PostSearch(getActivity(), SearchCallback.getCallback(getActivity()));
		postSearchPeopleInResponsibleUnit.getEmployeeForDepartment(mService.ServiceOwner);
	}

	protected void showHideView(View slider, int arrowResId, View button) {
		ViewGroup vg = (ViewGroup) button.getParent();
		ImageView arrow = findView(vg, arrowResId);
		if (slider.getVisibility() == View.VISIBLE) {
			arrow.setBackgroundResource(R.drawable.arrowdown);
			AnimationManager.collapse(slider,
					Settings.SERVICE_VIEWS_COLLAPSE_ADDITIONAL_TIME);
		} else {
			arrow.setBackgroundResource(R.drawable.arrowup);
			AnimationManager.expand(slider,
					Settings.SERVICE_VIEWS_EXPAND_ADDITIONAL_TIME);
		}
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
		
		if(serviceHourOperatation == null) {
			vgOpenHours.setVisibility(View.GONE);
			return;
		}
		
		int minimumDaysOpenToShowFeature = ScreensHelper.setOpenHouersTextViews(serviceHourOperatation, tvOpenHours, tvOpenDays, getActivity()).length();
		
		if(minimumDaysOpenToShowFeature < 1)
			vgOpenHours.setVisibility(View.GONE);
	}

	private void setTvCommunication(ArrayList<ContactInfo> contactInfo, TextView tvCommunication, TextView tvCommunicationName) {
		if(contactInfo == null) {
			vgCommunication.setVisibility(View.GONE);
			return;
		}
		int minimumNumbersToShowFeature = ScreensHelper.setCommunicationText(contactInfo, tvCommunication, tvCommunicationName, getActivity()).length();
		
		if(minimumNumbersToShowFeature < 1)
			vgCommunication.setVisibility(View.GONE);
	}

}
