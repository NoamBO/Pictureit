package com.pictureit.leumi.server;

import android.content.Context;
import utilities.server.BaseHttpGet;

public class GetCurrentUserData extends BaseHttpGet {

	public GetCurrentUserData(Context ctx, HttpCalback callback) {
		super(ctx);
		this.callback = callback;
	}

	@Override
	protected String doInBackground(String... params) {
		prepare(ServerAddresses.PERSONAL_DATA_METHOD_PATH);
		return super.doInBackground(params);
	}
}
