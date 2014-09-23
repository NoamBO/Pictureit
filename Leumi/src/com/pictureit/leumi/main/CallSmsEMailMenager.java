package com.pictureit.leumi.main;

import java.util.ArrayList;

import utilities.OutgoingCommunication;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.pictureit.leumi.server.parse.NameValue;

public class CallSmsEMailMenager {
	
	public static void sendEMail(String email, Context ctx) {
		if(email == null)
			return;
		if(email.length() > 0) {
			OutgoingCommunication.sendEMail((Activity) ctx, email);
		}
		else {
			Dialogs.generalDialog((Activity) ctx, ctx.getString(R.string.no_email_found));
		}
	}

	public static void sendSms(String phoneNum, Context ctx) {
		if(phoneNum == null)
			return;
		if(phoneNum.length() > 0) {
			OutgoingCommunication.sendSms((Activity) ctx, phoneNum);
		}
		else {
			Dialogs.generalDialog((Activity) ctx, ctx.getResources().getString(R.string.no_phone_num_found));
		}
	}

	public static void call(final ArrayList<NameValue> nums, final Context ctx) {
		if(nums == null)
			return;
		
		if(nums.size() < 1) {
			Dialogs.generalDialog((Activity) ctx, ctx.getResources().getString(R.string.no_phone_num_found));
			return;
		}

		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < nums.size(); i++) {
			al.add(nums.get(i).name+"   "+nums.get(i).value);
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		String[] s = new String[al.size()];
		s = al.toArray(s);
		builder.setSingleChoiceItems(s, 0, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				OutgoingCommunication.call((Activity) ctx, nums.get(which).value);
				dialog.dismiss();
			}
		});
		builder.setTitle(ctx.getResources().getString(R.string.make_a_call));
		Dialog d = builder.create();
		d.show();
		
	}
}
