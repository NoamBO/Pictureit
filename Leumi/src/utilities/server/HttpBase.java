package utilities.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.pictureit.leumi.main.Dialogs;
import com.pictureit.leumi.main.R;
import com.pictureit.leumi.main.Settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

//Written by @Noam Bar-Oz

public abstract class HttpBase extends AsyncTask<String, String, Object>{

	private HttpResponse response;
	private String url;
	private int statusCode;
	protected Context ctx;
	protected ProgressDialog mProgressDialog;
	protected boolean showProgressDialog = true;

	public HttpBase(Context ctx){
		this.ctx = ctx;
	}

	
	@Override
	protected abstract String doInBackground(String... params);

	protected boolean isConnectionAvailable(Context context) {
		if(!Settings.isNetworkAvailable(context)) {
			Dialogs.generalDialog((Activity) context, context.getResources().getString(R.string.no_internet_connection));
			return false;
		}
		return true;
	}
	
	@Override
	protected void onPreExecute() {
		if(!isConnectionAvailable(ctx)) {
			this.cancel(true);
			return;
		}
		
		if(showProgressDialog) {
			mProgressDialog = new ProgressDialog(ctx);
	        mProgressDialog.setMessage("please wait...");
	        mProgressDialog.show();
	        mProgressDialog.setCancelable(false);
	        mProgressDialog.setCanceledOnTouchOutside(false);
		}
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if(mProgressDialog!=null)
			mProgressDialog.cancel();
		super.onPostExecute(result);
	}
	
	protected HttpParams getParams(){
		int TIMEOUT_MILLISEC = 15000;
		HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
	    HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		return httpParams;
	}

	protected abstract void prepare(String request); 
	
	public String processEntity() {
		if(response!=null){
				InputStream instream;
				try {
					statusCode = response.getStatusLine().getStatusCode();
					instream = response.getEntity().getContent();
					String result= convertStreamToString(instream);
					
					return result;
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
		}
		return "";
	}

	private  String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public interface HttpCallback{
		public void onAnswerReturn(Object object);
	}
	
}
