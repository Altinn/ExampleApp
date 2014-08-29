
package com.altinn.apps.fisher.net.tasks;
/**
 * Report of received fish will be sent by using this
 * Async Task
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.Context;

import com.altinn.apps.fisher.common.AppConstants;
import com.altinn.apps.fisher.db.DataBaseHelper;
import com.altinn.apps.fisher.db.FormDBHelper;
import com.altinn.apps.fisher.models.InfoData;
import com.altinn.apps.fisher.models.ReportInfoData;
import com.altinn.apps.fisher.models.UserProfile;
import com.altinn.apps.fisher.net.AbstractWorkerTask;
import com.altinn.apps.fisher.net.CookieHelper;
import com.altinn.apps.fisher.net.TaskNotifier;
import com.altinn.apps.fisher.ui.screen.BaseActivity;
import com.altinn.apps.fisher.utils.PreferenceUtils;



public class SendReportTask extends AbstractWorkerTask {

	private Context context;
	private TaskNotifier notfier;
	private int error_code = AppConstants.ERROR_TYPE_NONE;
	private String messageSendId = "";
	private final int MESSAGE_SENT_SUCCESS = 201;

	public SendReportTask(Context context, TaskNotifier notifier) {
		super(context, notifier);
		this.context = context;
		this.notfier = notifier;
	}
	
	@Override
	protected void onPreExecute(){
		((BaseActivity)context).displayDialog(BaseActivity.SHOW_REPORT_SENDING);
		super.onPreExecute();
	}

	@Override
	protected JSONObject doInBackground(Object... params) {
		System.out.println("DO IN BACK GROUND SEND REPORT");
		JSONObject jsonResponse = null;
		int formId = (Integer)params[0];
		if(formId >=  0){
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpParams connectionParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(connectionParams, AppConstants.TIME_OUT);
			HttpConnectionParams.setSoTimeout(connectionParams, AppConstants.TIME_OUT);
			httpClient.setParams(connectionParams);
			FormDBHelper formHelper = (FormDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FORM);
			InfoData frmData = formHelper.getFormData(formId);			
			String postDataStr = frmData.createJSONPost();
			System.out.println("" +postDataStr);
			
			try{
				
				String post_msgurl = AppConstants.POST_MESSAGE_WEBSERVICE_URL;
				String orgNumber = null;
				if(frmData instanceof ReportInfoData){
					ReportInfoData rData = (ReportInfoData)frmData;
					UserProfile userProfile = PreferenceUtils.getInstance().getUserProfileData();
					orgNumber = userProfile.getOrganizationNumber(rData.mUnitName);
					if(orgNumber != null){
						post_msgurl = String.format(post_msgurl, orgNumber);
					}else{
						throw new Exception("USER PROFILE NOT SET");
					}
				}
				HttpPost post = new HttpPost(post_msgurl);
				HttpResponse getResponse = null;
				StringEntity stringEntity = new StringEntity(postDataStr, "UTF-8");
				
				//Header header = new BasicHeader("Cookie",".ASPXAUTH=7201266DEFD8ABA7C4530AC3E5330FB0FA2197176CFBA5FDB37027CCB2B8FD54578A9649D4E9829C9B54B772D7357C3B2659947BBF39170C365E70DE3A2E12FF93E57BBBD550C478E87FAB4790A02BA3BE1BA93E8574C8B93DA786424576A5FDC298F43729566497D1BC12739E39251D29A8EAE0A0672B5F29412ACC527AA8161823154170017E98007E1DE001DC5CBB73BF22A3C4AACD4624373FDF1644E477C872069AE7A04EFA311DFCA88258D551F47BC2B0");
				//post.addHeader(header);
				post.addHeader(CookieHelper.getInstance().getCookieHeader());
				
				post.setHeader("Content-Type", "application/hal+json");
				post.setHeader("Accept", "application/json");
				post.setHeader(AppConstants.API_KEY_HEADER, AppConstants.API_KEY_HEADER_VALUE);
				post.setEntity(stringEntity);
				getResponse = httpClient.execute(post);
				String responseId = CookieHelper.getInstance().updateCokkieFromHeaders(getResponse);//Some times we get ?Authe token while accessing webservice
				String response = null;
				if(getResponse != null){
					int status = 0;
					if(getResponse.getStatusLine() != null)
						status = getResponse.getStatusLine().getStatusCode();
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
					System.out.println("\nSTATUS:"+ status);
					System.out.println("\n****\n"+ response);
					if(response.toLowerCase().indexOf("<html>") == -1){//if user is not logged in or login expired we do get html-content for ID-PORTAL-Login-web page
						if(status == MESSAGE_SENT_SUCCESS){
							//201 (Server Response Indication): indicates record inserted successfully
							//500 : Record is not proper check with rest client
							jsonResponse = new JSONObject();
							frmData.mSendId = System.currentTimeMillis();
							frmData.mResponseId = ""+responseId;
							formHelper.updateFormData(frmData);	
							
							String messageId = frmData.getResponsId(false);
							setMessageSendId(messageId);
						}else{
							error_code = status;
						}
					}else{
						error_code = AppConstants.ERROR_TYPE_LOGIN_EXPIRE;
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
			
			
		}
		//TODO put this code at end of try block
		
		return jsonResponse;
	}
	
	

	@Override 
	protected void onPostExecute(JSONObject result) {
		((BaseActivity)context).hideDialog(BaseActivity.SHOW_REPORT_SENDING);
		if(error_code == AppConstants.ERROR_TYPE_LOGIN_EXPIRE){
			getNotifier().onLoginExpire();
		}else if(null == result){
			getNotifier().onError(error_code);
		} else{			
			getNotifier().onSuccess();			
		}
	}

	public String getMessageSendId() {
		return messageSendId;
	}

	public void setMessageSendId(String messageSendId) {
		this.messageSendId = messageSendId;
	}
	
	
}
