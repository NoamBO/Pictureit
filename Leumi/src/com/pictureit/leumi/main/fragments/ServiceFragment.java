package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;
import java.util.List;

import utilities.server.HttpBase.HttpCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pictureit.leumi.animation.AnimationManager;
import com.pictureit.leumi.main.CallSmsEMailMenager;
import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.MainActivity;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.main.Settings;
import com.pictureit.leumi.server.PostLike;
import com.pictureit.leumi.server.PostSearch;
import com.pictureit.leumi.server.PostServiceRegistration;
import com.pictureit.leumi.server.SearchCallback;
import com.pictureit.leumi.server.parse.HourOperatation;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.NameValue;
import com.pictureit.leumi.server.parse.Profile;
import com.pictureit.leumi.server.parse.Service;
import com.pictureit.leumi.server.parse.Service.ContactInfo;
import com.pictureit.leumi.server.parse.Service.LikingData;
import com.pictureit.leumi.server.parse.ServiceRegistration;

public class ServiceFragment extends FragmentWithoutTabs {
	
	private Service mService;
	private TextView tvResponsibleParty, tvLike;
	private ImageButton ibCall, ibMail, ibFollow, ibLike;
	private ViewGroup vgCommunication, vgOpenHours, vgLinkToService, vgResponsibleParty, vgServiceManager;
	private ViewGroup sliderOpenHours, sliderCommunication, sliderLinkToService;
	private ListView lvManagersList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.service_layout, container, false);

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
		sliderCommunication = findView(v, R.id.rl_service_dropdown_communication_options);
		sliderLinkToService = findView(v, R.id.ll_service_dropdown_link_to_service);
		
		lvManagersList = findView(v, R.id.lv_service_managers);
		vgServiceManager = findView(v, R.id.rl_service_clickable_header_service_manager);
		
		tvTitle.setText(mService.BllBusinessService);
		setTvDescription(mService.BllDescription, tvDescription);
		setTvResponsibleParty(tvResponsibleParty ,mService.ServiceOwner, mService.ServiceUnitCode);
		setTvCommunication(mService.ContactInfo, tvCommunication, tvCommunicationName);
		setTvOpenHours(mService.ServiceHourOperatation, tvOpenHours, tvOpenDays);
		setTvLinkToService(tvLinkToService, mService.ServiceUrl);
		setTvLike();
		getServiceManagersList();
		onFollowChange();
		
		return v;
	}
	
	private void getServiceManagersList() {
		PostSearch postSearch = new PostSearch(getActivity(), new HttpCallback() {
			
			@Override
			public void onAnswerReturn(String answer) {
				if(JsonToObject.isStatusOk(answer)){
					ArrayList<Profile> managers = JsonToObject.jsonToUserProfilesArrayList(answer);
					if(managers.size() > 0)
						setServiceManagersList(managers);
				}
			}
		});
		postSearch.getManagersWithArray(mService.ServiceManagerIds);
	}

	private void setTvDescription(String bllDescription, TextView textView) {
		if (bllDescription != null && !bllDescription.equalsIgnoreCase(""))
			textView.setText(bllDescription);
	}

	private void setTvLike() {
		tvLike.setText(mService.LikingData.LikingCount);
		if(mService.LikingData.Liking.equalsIgnoreCase(Const.ALREADY_LIKE))
			ibLike.setBackgroundResource(R.drawable.likeicongray);
		else
			ibLike.setBackgroundResource(R.drawable.likeiconblue);
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
				onFollowClick();
			}
		});
		ibLike.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View view) {
				PostLike like = new PostLike(getActivity(), new HttpCallback() {
					
					@Override
					public void onAnswerReturn(String answer) {
						LikingData likingData = JsonToObject.jsonToLikingData(answer);
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
		vgServiceManager.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				showHideView(lvManagersList, R.id.iv_service_manager_arrow, view);
			}
		});
	}

	protected void onFollowClick() {
		final boolean alreadyRegister = mService.Register.Status.equalsIgnoreCase(Const.REGISTER_STATUS_REGISTERED);

			new AlertDialog.Builder(getActivity())
			.setTitle(
				alreadyRegister ? R.string.service_unfollow_dialog_title :
						R.string.service_follow_dialog_title
					)
			.setMessage(
				alreadyRegister ? R.string.already_signed_to_this_service
						: R.string.service_follow_question
					)
			.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					follow(alreadyRegister);
				}
			})
			.setNegativeButton("Cancel", null)
			.create().show();
	}
	
	private void follow(boolean alreadyRegister) {
		
		PostServiceRegistration register = new PostServiceRegistration(getActivity(), new HttpCallback() {
			@Override
			public void onAnswerReturn(String answer) {
				if(answer==null)
					return;
				ServiceRegistration s = JsonToObject.jsonToServiceRegistrationResponse(answer);
				if(s == null)
					return;
				String msg = (s != null ? s.Msg
							: getString(R.string.somthing_went_wrong));				

				mService.Register.Status = s.StatusResult;
				onFollowChange();
				new AlertDialog.Builder(getActivity())
				.setMessage(msg)
				.setNeutralButton("Ok", null)
				.create().show();
			}
		});
		register.registerToService(mService.ServiceID, alreadyRegister);
	}

	protected void onFollowChange() {
		final boolean alreadyRegister = mService.Register.Status.equalsIgnoreCase(Const.REGISTER_STATUS_REGISTERED);
		if(alreadyRegister) {
			ibFollow.setBackgroundResource(R.drawable.followicon_unselected);
		} else {
			ibFollow.setBackgroundResource(R.drawable.followicon);
		}
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
			if(mService.ContactInfo.get(i) == null)
				continue;
			if (mService.ContactInfo.get(i).contact.equalsIgnoreCase(JsonToObject.CONTACT_INFO_CONTACT_VALUE_E_MAIL)) {
				CallSmsEMailMenager.sendEMail(mService.ContactInfo.get(i).Value, getActivity());
				break;
			}
		}		
	}

	protected void call() {
		ArrayList<NameValue> nums = new ArrayList<NameValue>();
		for (int i = 0; i < mService.ContactInfo.size(); i++) {
			if(mService.ContactInfo.get(i) == null)
				continue;
			if (mService.ContactInfo.get(i).contact
					.equalsIgnoreCase(JsonToObject.CONTACT_INFO_CONTACT_VALUE_PHONE))
				nums.add(new NameValue(getResources().getString(
						R.string.tel_num), mService.ContactInfo.get(i).Value));
		}
		CallSmsEMailMenager.call(nums, getActivity());

	}

	private void setTvOpenHours(
			ArrayList<HourOperatation> serviceHourOperatation, TextView tvOpenHours, TextView tvOpenDays) {
		
		if(serviceHourOperatation == null || serviceHourOperatation.size() == 0) {
			vgOpenHours.setVisibility(View.GONE);
			return;
		}
		
		int minimumDaysOpenToShowFeature = ScreensHelper.setOpenHouersTextViews(serviceHourOperatation, tvOpenHours, tvOpenDays, getActivity()).length();
		
		if(minimumDaysOpenToShowFeature < 1)
			vgOpenHours.setVisibility(View.GONE);
	}

	private void setTvCommunication(ArrayList<ContactInfo> contactInfo, TextView tvCommunication, TextView tvCommunicationName) {
		if(contactInfo == null || contactInfo.size() == 0) {
			vgCommunication.setVisibility(View.GONE);
			return;
		}
		int minimumNumbersToShowFeature = ScreensHelper.setCommunicationText(contactInfo, tvCommunication, tvCommunicationName, getActivity()).length();
		
		if(minimumNumbersToShowFeature < 1)
			vgCommunication.setVisibility(View.GONE);
	}
	
	private void setServiceManagersList(ArrayList<Profile> managers) {
		vgServiceManager.setVisibility(View.VISIBLE);
		final ManagersListViewAdapter adapter = new ManagersListViewAdapter(getActivity(), 0, managers);
		lvManagersList.setAdapter(adapter);
		lvManagersList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Profile p = (Profile) parent.getItemAtPosition(position);
				String profileJson = new Gson().toJson(p);
				Bundle b = new Bundle();
				b.putString(Const.JSON, profileJson);
				
				Fragment f = new EmploeeProfileFragment();
				f.setArguments(b);
				
				((MainActivity) getActivity()).addFragment(f);
			}
		});
	}
	
	private class ManagersListViewAdapter extends ArrayAdapter<Profile> {
		
		public ManagersListViewAdapter(Context context, int resource, List<Profile> objects) {
			super(context, resource, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if(convertView == null) {
				holder = new ViewHolder();
				convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.row_service_manager, parent, false);
				holder.call = findView(convertView, R.id.ib_row_service_manager_call);
				holder.image = findView(convertView, R.id.iv_row_service_manager);
				holder.name = findView(convertView, R.id.tv_row_service_manager_name);
				holder.sms = findView(convertView, R.id.ib_row_service_manager_sms);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final Profile p = getItem(position);
			holder.call.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ArrayList<NameValue> nums = new ArrayList<NameValue>();
					if(p.L144WorkPhone != null && p.L144WorkPhone.length() >0){
						nums.add(new NameValue(getResources().getString(R.string.office), p.L144WorkPhone));
					}
					if(p.L144MobileCellular != null && p.L144MobileCellular.length() >0){
						nums.add(new NameValue(getResources().getString(R.string.cellphone), p.L144MobileCellular));
					}
					if(p.L144BankCellular != null && p.L144BankCellular.length() >0){
						nums.add(new NameValue(getResources().getString(R.string.cellphone), p.L144BankCellular));
					}
					CallSmsEMailMenager.call(nums, getActivity());
				}
			});
			if(p.L144WorkerPictureUrl != null)
				imageLoader.displayImage(p.L144WorkerPictureUrl, holder.image, options);
			
			holder.name.setText(p.L144LastName+" "+p.L144FirstName);
			
			holder.sms.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CallSmsEMailMenager.sendSms(p.L144MobileCellular, getActivity());
				}
			});
			return convertView;
		}
		
	}
	
	private static class ViewHolder {
		ImageView image;
		ImageButton call;
		ImageButton sms;
		TextView name;
		
	}

}
