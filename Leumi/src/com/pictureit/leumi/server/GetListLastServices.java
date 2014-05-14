package com.pictureit.leumi.server;

import utilities.BaseHttpGet;
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
		
		StringBuilder sb = new StringBuilder();
		sb.append(ServerAddresses.HOST)
		.append(ServerAddresses.BASE_PATH)
		.append(ServerAddresses.GET_SERVICES_LIST);
		
		setUrl(sb.toString());

	}

}
