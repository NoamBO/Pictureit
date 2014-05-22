package com.pictureit.leumi.server;

import android.content.Context;
import utilities.server.BaseHttpGet;

public class GetSystemAddition extends BaseHttpGet {

	public GetSystemAddition(Context ctx, HttpCalback callback) {
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
		sb.append(ServerAddresses.WEB_ADDITION_DATA_METHOD_PATH);
		super.prepare(sb.toString());
	}

}
