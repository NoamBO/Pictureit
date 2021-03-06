package com.pictureit.leumi.server;

import utilities.server.BaseHttpGet;
import android.content.Context;

public class GetService extends BaseHttpGet {

	public GetService(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(String... params) {
		prepare(params[0]);
		return super.doInBackground(params);
	}
	
	@Override
	protected void prepare(String request) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(ServerAddresses.GET_SERVICE_METHOD_PATH)
		.append(ServerAddresses.SERVICE_CLICK_ACTION_PATH)
		.append("?").append(ServerAddresses.ID)
		.append("=").append(request);
		
		super.prepare(sb.toString());
	}

}
