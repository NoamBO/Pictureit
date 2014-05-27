package utilities.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.pictureit.leumi.server.ServerAddresses;

import android.content.Context;

//Written by @Noam Bar-Oz

public class BaseHttpGet extends HttpBase {
	
	protected HttpCallback callback;
	protected String response;
	
	public BaseHttpGet(Context ctx) {
		super(ctx);
	}

	@Override
	protected String doInBackground(String... params) {
		HttpGet httpGet = new HttpGet(getUrl());
		httpGet.setHeader(HTTP.CONTENT_TYPE, HTTP.PLAIN_TEXT_TYPE);
		httpGet.setParams(getParams());
		try {
			setResponse(new DefaultHttpClient().execute(httpGet));
			response = processEntity();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		return response;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if(callback != null)
			callback.onAnswerReturn(result);
		super.onPostExecute(result);
	}
	
	@Override
	protected void prepare(String request) {
		StringBuilder sb = new StringBuilder();
		sb.append(ServerAddresses.HOST)
		.append(ServerAddresses.BASE_PATH)
		.append(request);
		setUrl(sb.toString());
	}
}
