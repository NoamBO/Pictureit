package com.pictureit.leumi.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

public class Dialogs {

	public static void generalDialog(Activity activity, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message);
		Dialog d = builder.create();
		d.show();
	}
}
