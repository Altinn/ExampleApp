
package com.altinn.apps.fisher.net.tasks;
/**
 * Class not in use
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;

import com.altinn.apps.fisher.common.AppConstants;
import com.altinn.apps.fisher.net.AbstractWorkerTask;
import com.altinn.apps.fisher.net.TaskNotifier;



public class LoginTask extends AbstractWorkerTask {

	private Context context;
	private TaskNotifier notfier;
	private int error_code = AppConstants.ERROR_TYPE_NONE;

	public LoginTask(Context context, TaskNotifier notifier) {
		super(context, notifier);
		this.context = context;
		this.notfier = notifier;
	}
	
	@Override
	protected JSONObject doInBackground(Object... params) {
		System.out.println("DO IN BACK GROUND");
		JSONObject jsonResponse = null;
		//DefaultHttpClient httpClient = new MyHttpClient(AppContext.getInstance());
		DefaultHttpClient httpClient = new DefaultHttpClient();
		

		HttpParams connectionParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(connectionParams, AppConstants.TIME_OUT);
		HttpConnectionParams.setSoTimeout(connectionParams, AppConstants.TIME_OUT);
		httpClient.setParams(connectionParams);
		
		

		//JSONObject json = new JSONObject();
		try{
//			json.put("username", (String) params[0]);
//			json.put("password", (String) params[1]);

			HttpGet get = new HttpGet(AppConstants.LOGIN_WEBSERVICE_URL);
			HttpResponse getResponse = null;

			//StringEntity stringEntity = new StringEntity("");
			//stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			get.setHeader(HTTP.CONTENT_TYPE, "application/json");
			getResponse = httpClient.execute(get);

			String response = null;
			if(getResponse != null){
				HttpEntity responseEntity = getResponse.getEntity();
				InputStream inputStream = responseEntity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while((line = reader.readLine()) != null){
					sb.append(line + "\n");
				}
				inputStream.close();

				response = sb.toString();
				jsonResponse = new JSONObject(response);
			}
		} catch(ConnectTimeoutException e){
			error_code = AppConstants.ERROR_TYPE_CONN_TIME_OUT;
			return null;
		} catch(SocketTimeoutException e){
			error_code = AppConstants.ERROR_TYPE_CONN_TIME_OUT;
			return null;
		} catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonResponse;
	}

	
	protected JSONObject doInBackground1(Object... params) {
		System.out.println("DO IN BACK GROUND");
		JSONObject jsonResponse = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		
		
		

		HttpParams connectionParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(connectionParams, AppConstants.TIME_OUT);
		HttpConnectionParams.setSoTimeout(connectionParams, AppConstants.TIME_OUT);
		httpClient.setParams(connectionParams);

		JSONObject json = new JSONObject();
		try{
			json.put("username", (String) params[0]);
			json.put("password", (String) params[1]);

			HttpGet httpget = new HttpGet("https://st01.altinn.basefarm.net/api/my/messages");
			HttpResponse getResponse = null;

		
			httpget.setHeader(HTTP.CONTENT_TYPE, "application/json");
			getResponse = httpClient.execute(httpget);
			String response = null;
			if(getResponse != null){
				
				HttpEntity responseEntity = getResponse.getEntity();
				
				InputStream inputStream = responseEntity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while((line = reader.readLine()) != null){
					sb.append(line + "\n");
				}
				inputStream.close();

				response = sb.toString();
				jsonResponse = new JSONObject(response);
			}
		} catch(ConnectTimeoutException e){
			error_code = AppConstants.ERROR_TYPE_CONN_TIME_OUT;
			return null;
		} catch(SocketTimeoutException e){
			error_code = AppConstants.ERROR_TYPE_CONN_TIME_OUT;
			return null;
		} catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonResponse;
	}

	@Override 
	protected void onPostExecute(JSONObject result) {
		if(error_code == AppConstants.ERROR_TYPE_LOGIN_EXPIRE){
			getNotifier().onLoginExpire();
		}else if(null == result){
			getNotifier().onError(error_code);
		} else{
			getNotifier().onSuccess();			
		}
	}
}
