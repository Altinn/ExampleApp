
package com.altinn.apps.fisher.net.tasks;
/**
 * This is an Async Task used to refresh a token, 
 * and executed from scheduler of AppContext
 * This task is executed in background
 */
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;

import com.altinn.apps.fisher.common.AppConstants;
import com.altinn.apps.fisher.net.CookieHelper;



public class RefreshTokenTask extends AsyncTask {

	
	private int error_code = AppConstants.ERROR_TYPE_NONE;

	public RefreshTokenTask() {	
		
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		System.out.println("DO IN BACK GROUND REFRESH-TOKEN-Task");		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams connectionParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(connectionParams, AppConstants.TIME_OUT);
		HttpConnectionParams.setSoTimeout(connectionParams, AppConstants.TIME_OUT);
		httpClient.setParams(connectionParams);
		try{
			
			HttpGet get = new HttpGet(AppConstants.LOGIN_WEBSERVICE_URL);
			HttpResponse getResponse = null;
			get.addHeader(CookieHelper.getInstance().getCookieHeader());
			get.setHeader("Content-Type", "application/json");
			get.setHeader("Accept", "application/json");
			get.setHeader(AppConstants.API_KEY_HEADER, AppConstants.API_KEY_HEADER_VALUE);
			getResponse = httpClient.execute(get);
			CookieHelper.getInstance().updateCokkieFromHeaders(getResponse);//This is thing we are setting in every web-service call to avoid un-authenticated user 
		} catch(ConnectTimeoutException e){
			error_code = AppConstants.ERROR_TYPE_CONN_TIME_OUT;
			return null;
		} catch(SocketTimeoutException e){
			error_code = AppConstants.ERROR_TYPE_CONN_TIME_OUT;
			return null;
		} catch(SSLPeerUnverifiedException sslpuve){
			error_code = AppConstants.ERROR_TYPE_LOGIN_EXPIRE;			
		}catch(Exception e){			
			e.printStackTrace();
		}
		
		return null;
	}

	
	

	@Override 
	protected void onPostExecute(Object result) {
		
	}
	
	
}
