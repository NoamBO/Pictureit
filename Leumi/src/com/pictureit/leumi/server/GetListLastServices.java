package com.pictureit.leumi.server;

import utilities.server.BaseHttpGet;
import android.content.Context;

public class GetListLastServices extends BaseHttpGet {

	public GetListLastServices(Context ctx, HttpCalback callback) {
		super(ctx);
		this.callback = callback;
	}

	@Override
	protected String doInBackground(String... params) {
		prepare(null);
		return super.doInBackground(params);
	}

	@Override
	protected void prepare(String request) {
		super.prepare(ServerAddresses.GET_SERVICES_LIST);
	}

}
