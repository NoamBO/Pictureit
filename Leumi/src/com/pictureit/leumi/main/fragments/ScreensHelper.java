package com.pictureit.leumi.main.fragments;

import java.util.ArrayList;

import utilities.view.AutoResizeTextViewSetter;
import android.app.Activity;
import android.widget.TextView;

import com.pictureit.leumi.main.R;
import com.pictureit.leumi.server.parse.HourOperatation;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.Service.ContactInfo;

public class ScreensHelper {
	
	public static String setOpenHouersTextViews(ArrayList<HourOperatation> serviceHourOperatation, TextView tvOpenHours, TextView tvOpenDays, Activity activity) {
			
			final String CLOSE = "Close";
			
			ArrayList<HourOperatation> s = serviceHourOperatation;
			StringBuilder sbHours = new StringBuilder();
			StringBuilder sbDays = new StringBuilder();
			for (int i = 0; i < serviceHourOperatation.size(); i++) {
				sbDays.append(getDayString(s.get(i).DayInWeek, activity));
				if (!s.get(i).CloseOpen.equalsIgnoreCase(CLOSE)) {
					if (s.get(i).MFrom != null
							&& !s.get(i).MFrom.equalsIgnoreCase(""))
						sbHours.append(activity.getText(R.string.from) + "- ")
								.append(s.get(i).MFrom)
								.append(" "
										+ activity.getText(R.string.till)
										+ " ").append(s.get(i).MTo);
					if (s.get(i).EFrom != null
							&& !s.get(i).EFrom.equalsIgnoreCase(""))
						sbHours.append(
								" , " + activity.getText(R.string.from)
										+ "- ")
								.append(s.get(i).EFrom)
								.append(" "
										+ activity.getText(R.string.till)
										+ " ").append(s.get(i).ETo);
				} else {
					sbHours.append(activity.getText(R.string.close));
				}
				if (i < serviceHourOperatation.size()){
					sbHours.append("\n");
					sbDays.append("\n");
				}
			}
			tvOpenDays.setText(sbDays.toString());
			tvOpenHours.setText(sbHours.toString());
		
			return sbDays.toString();
	}

	public static String setCommunicationText(
			ArrayList<ContactInfo> contactInfo, TextView tvCommunication,
			TextView tvCommunicationName, Activity activity) {
		StringBuilder sbAddress = new StringBuilder();
		StringBuilder sbAddressType = new StringBuilder();
		for (int i = 0; i < contactInfo.size(); i++) {
			if(contactInfo.get(i) == null)
				continue;
			if (contactInfo.get(i).contact
					.equalsIgnoreCase(JsonToObject.CONTACT_INFO_CONTACT_VALUE_PHONE)) {
				sbAddressType.append(activity.getText(R.string.tel_num));
				sbAddress.append(contactInfo.get(i).Value);
			} else if (contactInfo.get(i).contact
					.equalsIgnoreCase(JsonToObject.CONTACT_INFO_CONTACT_VALUE_E_MAIL)) {
				sbAddressType.append(activity.getText(R.string.email));
				sbAddress.append(contactInfo.get(i).Value);

			} else if (contactInfo.get(i).contact
					.equalsIgnoreCase(JsonToObject.CONTACT_INFO_CONTACT_VALUE_FAX)) {
				sbAddressType.append(activity.getText(R.string.fax));
				sbAddress.append(contactInfo.get(i).Value);
			}

			if (i < contactInfo.size()) {
				sbAddress.append("\n");
				sbAddressType.append("\n");
			}
		}
		tvCommunication.setText(sbAddress.toString());
		tvCommunicationName.setText(sbAddressType.toString());
		return sbAddressType.toString();
	}
	
	private static String getDayString(String dayInWeek, Activity activity) {
		switch (Integer.valueOf(dayInWeek)) {
		case 1:
			return activity.getText(R.string.yom_a)+"  ";
		case 2:
			return activity.getText(R.string.yom_b)+"  ";
		case 3:
			return activity.getText(R.string.yom_c)+" "+"  ";
		case 4:
			return activity.getText(R.string.yom_d)+"  ";
		case 5:
			return activity.getText(R.string.yom_e)+"  ";
		case 6:
			return activity.getText(R.string.yom_f)+"  "+"  ";
		case 7:
			return activity.getText(R.string.yom_g)+"  ";
		default:
			break;
		}
		return "";
	}
}
