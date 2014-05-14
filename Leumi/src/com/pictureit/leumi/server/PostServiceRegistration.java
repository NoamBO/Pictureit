package com.pictureit.leumi.server;

import org.json.JSONException;
import org.json.JSONObject;

import com.pictureit.leumi.main.Const;

import android.content.Context;
import utilities.BaseHttpPost;

public class PostServiceRegistration extends BaseHttpPost {

	public PostServiceRegistration(Context ctx, HttpCalback callback) {
		super(ctx);
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(String... params) {
		if (mMainJson == null)
			return null;
		return super.doInBackground(params);
	}
	
	public void registerToService(String identity) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Const.SYSTEM_IDENTITY, identity);
			jsonObject.put(Const.SYSTEM_IDENTITY_DESC, Const.SERVICE_CLICK);
			mMainJson = jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.execute();
	}

	@Override
	protected void prepare(String request) {
		StringBuilder sb = new StringBuilder();
		sb.append(ServerAddresses.HOST)
			.append(ServerAddresses.BASE_PATH)
			.append(ServerAddresses.SYSTEM_REGISTER_METHOD_PATH);

		setUrl(sb.toString());
	}
}
