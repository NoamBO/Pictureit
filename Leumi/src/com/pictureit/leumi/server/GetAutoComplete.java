package com.pictureit.leumi.server;

import utilities.server.BaseHttpGet;
import android.content.Context;


public class GetAutoComplete extends BaseHttpGet {

	private String searchType;
	
	public GetAutoComplete(Context ctx, String searchType, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		this.searchType = searchType;
	}
	
	@Override
	protected void onPreExecute() {
		showProgressDialog = false;
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		String request = params[0];
		prepare(request.replace(" ", "+"));
	    return super.doInBackground(params);
	}


	@Override
	protected void prepare(String request) {
		StringBuilder sb = new StringBuilder();
		sb.append(ServerAddresses.AUTOCOMPLETE_METHOD_PATH)
		.append("/").append(searchType)
		.append("?").append(ServerAddresses.TERM).append("=")
		.append(request);
		
		super.prepare(sb.toString());
	}
}
