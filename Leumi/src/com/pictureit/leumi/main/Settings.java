package com.pictureit.leumi.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Settings {

	public static int SERVICE_VIEWS_COLLAPSE_ADDITIONAL_TIME = 200;

	public static int SERVICE_VIEWS_EXPAND_ADDITIONAL_TIME = 200;

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
