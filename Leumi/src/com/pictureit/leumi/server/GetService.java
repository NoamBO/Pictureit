package com.pictureit.leumi.server;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.pictureit.leumi.server.parse.JsonToObject;
import com.pictureit.leumi.server.parse.Service;

import utilities.BaseHttpGet;
import android.content.Context;
import android.util.Log;

public class GetService extends BaseHttpGet {

	public GetService(Context ctx, HttpCalback callback) {
		super(ctx);
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(String... params) {
		prepare(params[0]);
		return super.doInBackground(params);
	}
	
	@Override
	protected void onPostExecute(Object result) {
			Service s = JsonToObject.jsonGetService((String) result);
		super.onPostExecute(s);
	}
	
	@Override
	protected void prepare(String request) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(ServerAddresses.GET_SERVICE_METHOD_PATH)
		.append(ServerAddresses.GET_SERVICE_ACTION_PATH)
		.append("?").append(ServerAddresses.ID)
		.append("=").append(request);
		
		super.prepare(sb.toString());
	}

}
