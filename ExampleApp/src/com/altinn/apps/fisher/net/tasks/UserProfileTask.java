
package com.altinn.apps.fisher.net.tasks;
/**
 * User profile will be retrieved from this Asynch task
 * 
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.Context;

import com.altinn.apps.fisher.common.AppConstants;
import com.altinn.apps.fisher.models.UserProfile;
import com.altinn.apps.fisher.net.AbstractWorkerTask;
import com.altinn.apps.fisher.net.CookieHelper;
import com.altinn.apps.fisher.net.JSParser;
import com.altinn.apps.fisher.net.ParseManager;
import com.altinn.apps.fisher.net.TaskNotifier;
import com.altinn.apps.fisher.net.jsobj.JsonObj;
import com.altinn.apps.fisher.net.jsobj.OrganisationObj;
import com.altinn.apps.fisher.ui.screen.BaseActivity;
import com.altinn.apps.fisher.utils.PreferenceUtils;



public class UserProfileTask extends AbstractWorkerTask {

	private Context context;
	private TaskNotifier notfier;
	private int error_code = AppConstants.ERROR_TYPE_NONE;

	public UserProfileTask(Context context, TaskNotifier notifier) {
		super(context, notifier);
		this.context = context;
		this.notfier = notifier;
		
	}
	
	@Override
	protected void onPreExecute(){
		((BaseActivity)context).displayDialog(BaseActivity.SHOW_LOADING_USER_PROFILE);
		super.onPreExecute();
	}
	
	@Override
	protected JSONObject doInBackground(Object... params) {
		System.out.println("DO IN BACK GROUND UserProfileTask");
		JSONObject jsonResponse = null;
		String userProfileResp= null;
		String orgResp = getOrgInfo();
		if(orgResp != null){
			userProfileResp = getProfileInfo(params);
			if(userProfileResp != null && error_code == AppConstants.ERROR_TYPE_NONE){
				updateUserProfile(orgResp,userProfileResp);
				jsonResponse = new JSONObject();
			}
		}
			
		return jsonResponse;
		
	}
	
	private String getOrgInfo(Object... params){
		String response = null;
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams connectionParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(connectionParams, AppConstants.TIME_OUT);
		HttpConnectionParams.setSoTimeout(connectionParams, AppConstants.TIME_OUT);
		httpClient.setParams(connectionParams);		
		try{
			
			HttpGet get = new HttpGet(AppConstants.ORGLIST_WEBSERVICE_URL);
			HttpResponse getResponse = null;

			
			
			//Header header = new BasicHeader("Cookie",".ASPXAUTH=7201266DEFD8ABA7C4530AC3E5330FB0FA2197176CFBA5FDB37027CCB2B8FD54578A9649D4E9829C9B54B772D7357C3B2659947BBF39170C365E70DE3A2E12FF93E57BBBD550C478E87FAB4790A02BA3BE1BA93E8574C8B93DA786424576A5FDC298F43729566497D1BC12739E39251D29A8EAE0A0672B5F29412ACC527AA8161823154170017E98007E1DE001DC5CBB73BF22A3C4AACD4624373FDF1644E477C872069AE7A04EFA311DFCA88258D551F47BC2B0");
			//get.addHeader(header);
			get.addHeader(CookieHelper.getInstance().getCookieHeader());
			
			
			get.setHeader("Content-Type", "application/json");
			get.setHeader("Accept", "application/json");
			get.setHeader(AppConstants.API_KEY_HEADER, AppConstants.API_KEY_HEADER_VALUE);
			getResponse = httpClient.execute(get);
			CookieHelper.getInstance().updateCokkieFromHeaders(getResponse);
			
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
				System.out.println("\n****\n"+ response);
				if(response.toLowerCase().indexOf("<html>") == -1){
													
				}else{
					error_code = AppConstants.ERROR_TYPE_LOGIN_EXPIRE;
					response = null;
				}
				
				
			}
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
		
		return response;
		
	}

	/**
	 * Presently we do not have any web service
	 * Which will give user's email, name, phone etc
	 * Internal content need to be modified, URL needs to be changed
	 * 
	 * @param params
	 * @return
	 */
	private String getProfileInfo(Object... params){
		String response = null;	
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams connectionParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(connectionParams, AppConstants.TIME_OUT);
		HttpConnectionParams.setSoTimeout(connectionParams, AppConstants.TIME_OUT);
		httpClient.setParams(connectionParams);		
		try{
			
			HttpGet get = new HttpGet(AppConstants.ORGLIST_WEBSERVICE_URL);
			HttpResponse getResponse = null;

			get.addHeader(CookieHelper.getInstance().getCookieHeader());
			get.setHeader("Content-Type", "application/json");
			get.setHeader("Accept", "application/json");
			getResponse = httpClient.execute(get);
			CookieHelper.getInstance().updateCokkieFromHeaders(getResponse);
			
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
				System.out.println("\n****\n"+ response);
				if(response.toLowerCase().indexOf("<html>") == -1){
												
				}else{
					error_code = AppConstants.ERROR_TYPE_LOGIN_EXPIRE;
					response = null;
				}
				
				
			}
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
		
		return response;
		
	}

	
	

	@Override 
	protected void onPostExecute(JSONObject result) {
		((BaseActivity)context).hideDialog(BaseActivity.SHOW_LOADING_USER_PROFILE);
		if(error_code == AppConstants.ERROR_TYPE_LOGIN_EXPIRE){
			getNotifier().onLoginExpire();
			//Handled in base activity, and invokes browser,and after completion of login 
			//callwebservice method will be invoked from respective screen to get the pending result before login expire.
		}else if(null == result){
			getNotifier().onError(error_code);
		} else{			
			getNotifier().onSuccess();			
		}
	}
	
	private void updateUserProfile(String jsResponseOrgStr, String jsUserInfo){
		if(jsResponseOrgStr != null){
			
			JSParser jsParser = (JSParser) ParseManager.getParser(ParseManager.PARSER_TYPE_JSON);
			ArrayList<JsonObj> listOrg = jsParser.parseList(jsResponseOrgStr, JSParser.PARSE_TYPE_OBJ_ORG);
			
			//TODO parse data and assign values from json response.
			UserProfile userProfileNew = new UserProfile();
			userProfileNew.initTestProfile();//To be removed, parse it from jsUserInfo
			
			int activeIndex = 0;
			UserProfile oldProfile = PreferenceUtils.getInstance().getUserProfileData();
			if(oldProfile != null &&  oldProfile.getEmail().equalsIgnoreCase(userProfileNew.getEmail())){
				int activeIndexDB =  oldProfile.getActiveUnitIndex();
				if(userProfileNew.getUnitList().size()>activeIndex){
					activeIndex = activeIndexDB;
				}
			}
			if(listOrg != null){
				userProfileNew.clearList();				
				for(int i = 0 ; i  < listOrg.size(); i++){
					OrganisationObj org = (OrganisationObj)listOrg.get(i);
					if(org != null ){
						userProfileNew.addUnit(org);
					}
				}
			}
			userProfileNew.setActiveUnitIndex(activeIndex);
			PreferenceUtils.getInstance().setUserProfileData(userProfileNew);
			
		}
		
	}
}
