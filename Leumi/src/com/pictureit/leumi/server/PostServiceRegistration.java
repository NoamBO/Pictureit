package com.pictureit.leumi.server;

import org.json.JSONException;
import org.json.JSONObject;

import com.pictureit.leumi.main.Const;

import android.content.Context;
import utilities.server.BaseHttpPost;

public class PostServiceRegistration extends BaseHttpPost {

	public PostServiceRegistration(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
	}
	
	@Override
	protected String doInBackground(String... params) {
		if (mMainJson == null)
			return null;
		prepare(null);
		return super.doInBackground(params);
	}
	
	public void registerToService(String identity, boolean isRegister) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Const.SYSTEM_IDENTITY, identity);
			jsonObject.put(Const.SYSTEM_IDENTITY_DESC, Const.SERVICE_CLICK);
			jsonObject.put("Val", isRegister ? "0" : "1");
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
