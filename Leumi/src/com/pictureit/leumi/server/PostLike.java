package com.pictureit.leumi.server;

import org.json.JSONException;
import org.json.JSONObject;

import com.pictureit.leumi.main.Const;
import com.pictureit.leumi.main.R;

import android.content.Context;
import utilities.server.BaseHttpPost;

public class PostLike extends BaseHttpPost {

	public PostLike(Context ctx, HttpCalback calback) {
		super(ctx);
		this.callback = calback;
	}

	@Override
	protected String doInBackground(String... params) {
		if(mMainJson == null)
			return null;
		prepare(null);
		return super.doInBackground(params);
	}	
	
	public void like(String serviceId, String like) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Const.SYSTEM_IDENTITY, serviceId);
			jsonObject.put(Const.SYSTEM_IDENTITY_DESC, Const.SERVICE_CLICK);
			jsonObject.put("Val", like);
			mMainJson = jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.execute();
	}
	
	@Override
	protected void prepare(String request) {
		StringBuilder sb = new StringBuilder();
		sb.append(ServerAddresses.HOST).append(ServerAddresses.BASE_PATH)
				.append(ServerAddresses.LIKING_METHOD_PATH);

		setUrl(sb.toString());
	}

}
