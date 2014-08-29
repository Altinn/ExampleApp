package com.altinn.apps.fisher.ui.screen;
/**
 * If user is already logged in, then after splash this screen comes in picture
 * 
 * On first launch (user is not logged in)login browser is invoked, and after completion of 
 * Login UserProfile activity is launched. and back operation on this user will be brought into HomeActivity
 * 
 * Any other subsequent launch (currently user is not logged in but earlier in the history he was ever logged in)
 * Login browser is invoked, and after completion of login user can see home screen.
 * 
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.altinn.apps.fisher.AppContext;
import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.net.TaskNotifier;
import com.altinn.apps.fisher.net.tasks.UserProfileTask;
import com.altinn.apps.fisher.utils.PreferenceUtils;
import com.altinn.apps.fisher.utils.Utils;

public class HomeActivity extends BaseActivity implements OnClickListener  , TaskNotifier{

	private final String TAG = "HomeActivity";
	public static final int MENU_REPORT_RECEIVED = 101;
	public static final int MENU_REPORT_CAUGHT = 102;
	//private boolean isSettingsUpdatRequired;
	//private boolean onCreateState = true;
	private UserProfileTask mProfileTask;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mLayOutId = R.layout.home;
		super.onCreate(savedInstanceState);	
		init();
	}

	public void init() {
		findViewById(R.id.rl_report_action).setOnClickListener(this);
		findViewById(R.id.rl_catch_action).setOnClickListener(this);
		setTitle(R.string.title_home,-1);	
		
		if(!AppContext.TEST_MODE){
			if(!PreferenceUtils.getInstance().isUserRegistered()){		
				if(Utils.isNetworkAvailable(this)){
					Intent intentWeb = new Intent(this,BrowserActivity.class);
					intentWeb.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivityForResult(intentWeb, REQUEST_CODE);
				}else{
					displayDialog(NO_NETWORK_DIALOG_ID);
				}			
			}else{
				if(Utils.isNetworkAvailable(this)){		
					if(AppContext.getInstance().isTokenExpired()){
						Intent intentWeb = new Intent(this,BrowserActivity.class);
						intentWeb.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivityForResult(intentWeb, REQUEST_CODE);
					}else if(AppContext.getInstance().isRefreshNeeded()){
						callWebService();//this flow will take care Browser-invocation in case login failed
					}else{
						AppContext.getInstance().startRefreshTask();//Silently update the token, in case of creation of home screen
						//Because other refresh task will be invoked through scheduler AppContext.scheduleRefreshTimer
						//This scheduler refresh the token when needed, token is stale by more than 15 mins.
						//But scheduler's First refresh task will be started after 8 mins if conditions are in favor
					}
				}else{
					displayDialog(NO_NETWORK_DIALOG_ID);
				}
			}
		}
	}
	
	

	public void onDestroy() {
		AppContext.getInstance().exitApp();
		super.onDestroy();

	}
	
	protected void onInfoClicked() {

	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.rl_report_action) {
			startActivity(ReportReceivedFishActivity.class);
		} else if (id == R.id.rl_catch_action) {
			//TODO: At present no action is scheduled for First item of home screen
			//callWebService();
			//Intent intentWeb = new Intent(this,BrowserActivity.class);
			//intentWeb.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			//startActivityForResult(intentWeb, REQUEST_CODE);
		}

	}
	
	public void onResume(){
		super.onResume();		
	}
	
	/**
	 * Action invoke to execute network operation to update user profile, and may have refresh token to authorized user of web-service
	 */
	public void callWebService(Object... arguments){
		if(mProfileTask != null){
			mProfileTask.cancel(true);
		}
		mProfileTask = new UserProfileTask(this,this);
		mProfileTask.execute(arguments);
	}
	
	public void onSuccess(){
	}

	@Override
	public void onError(int message) {	
	}

	@Override
	public void onProgress() {
		// TODO Auto-generated method stub
		
	}
	
}
