package com.altinn.apps.fisher.ui.screen;
/**
 * This is an activity which can be inherited by all the screens.
 * This activity includes some functions which is needed by most of the activities.
 *
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.altinn.apps.fisher.AppContext;
import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.common.IStatusMessage;
import com.altinn.apps.fisher.common.MenuItem;
import com.altinn.apps.fisher.utils.PreferenceUtils;

public class BaseActivity extends MenuNavigationActivity {
	private final String TAG = "BaseActivity";

	public static final byte NO_NETWORK_DIALOG_ID = 100;
	public static final byte SHOW_LOADING = 101;
	public static final byte EXIT_APP = 102;
	public static final byte LOGIN_FIALIED = 103;
	public static final byte UPDATE_GPS_SETTING = 104;
	public static final byte LOGIN_EXPIRED = 105;
	public static final byte SHOW_LOADING_USER_PROFILE = 106;
	public static final byte SHOW_REPORT_SENDING = 107;
	
	public int REQUEST_CODE = 100;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
	}
	
	



	private int activeDialogId = -1;

	/**
	 * Dialog Display will be used from this method 
	 * This method will take care of displaying of a message
	 * if currently some dialog is active then first it remove the existing dialog then 
	 * it will display the desired dialog.
	 * @param id
	 */
	@SuppressWarnings("deprecation")
	public void displayDialog(int id) {
		try {
			if (activeDialogId != -1) {
				hideDialog(activeDialogId);
				showDialog(id);
				activeDialogId = id;
			} else {
				showDialog(id);
				activeDialogId = id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dialog removal can be handled from this class 
	 * and will handle any error conditions.
	 * @param id
	 */
	@SuppressWarnings("deprecation")
	public void hideDialog(int id) {
		try {
			if (id != -1) {
				removeDialog(id);
				activeDialogId = -1;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Android framework method which will take care of creation of dialogs.
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case NO_NETWORK_DIALOG_ID:
			return new AlertDialog.Builder(this)
					.setMessage(R.string.dialog_no_network)
					.setPositiveButton(R.string.dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									if(!PreferenceUtils.getInstance().isUserRegistered()){
										finish();
									}
								}
							}).create();
		case SHOW_LOADING:
			ProgressDialog dialog = new ProgressDialog(BaseActivity.this);
			dialog.setMessage(getString(R.string.dialog_progress));
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			return dialog;
		case EXIT_APP:
			return new AlertDialog.Builder(this)
					.setMessage((R.string.dialog_exit))
					.setPositiveButton(R.string.dialog_yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
									// logout, ONLY if login was successful
									// if(null !=
									// MyAppContext.getInstance().getAuthId()){
									// new
									// LogoutTask(ctx).execute(MyAppContext.getInstance().getAuthId());
									// }
									// MyAppContext.getInstance().stopDataRefreshTimer();

									// Below step is done to exit the app...
									finish();
									System.exit(0);
									// End exit....
								}
							})
					.setNegativeButton(R.string.dialog_no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							}).create();
			case LOGIN_FIALIED:
			
			return new AlertDialog.Builder(this)
			.setMessage(R.string.dialog_login_failed)
			.setPositiveButton(R.string.dialog_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
							dialog.dismiss();
						}
					}).create();
			
			case LOGIN_EXPIRED:
				return new AlertDialog.Builder(this)
				.setMessage(R.string.dialog_login_expired)
				.setCancelable(true)
				.setPositiveButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.dismiss();
//								Intent intent = new Intent(Intent.ACTION_VIEW);
//								intent.setData(Uri.parse(AppConstants.LOGIN_WEBSERVICE_URL));
//								startActivity(intent);
								
								Intent intentWeb = new Intent(BaseActivity.this,BrowserActivity.class);
								//intentWeb.setData(Uri.parse(AppConstants.LOGIN_WEBSERVICE_URL));
								startActivityForResult(intentWeb,REQUEST_CODE);
							}
						}).create();
				
			
			case UPDATE_GPS_SETTING:
				return new AlertDialog.Builder(this)
				.setMessage(R.string.dialog_gps_setting_disabled)
				.setPositiveButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.dismiss();
								Intent settingsIntent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(settingsIntent);
							}
						}).create();
			case SHOW_LOADING_USER_PROFILE:
				dialog = new ProgressDialog(BaseActivity.this);
				dialog.setMessage(getString(R.string.dialog_progress_user_profile));
				dialog.setIndeterminate(true);
				dialog.setCancelable(false);
				return dialog;
				
			case SHOW_REPORT_SENDING:
				dialog = new ProgressDialog(BaseActivity.this);
				dialog.setMessage(getString(R.string.dialog_progress_report_sending));
				dialog.setIndeterminate(true);
				dialog.setCancelable(false);
				return dialog;
		}
		return null;
	}
	
	/**
	 * Utility method to starts an activity
	 * @param activityClass
	 */
	public void startActivity(Class activityClass){
		//if(activityClass != this.getClass())
		{
			if(!(this instanceof HomeActivity)){
				finish();
			}
			Intent intent = new Intent(this,activityClass);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
	
	/**
	 * Utility method to starts an activity
	 * @param activityClass
	 * @param bundle
	 */
	public void startActivity(Class activityClass, Bundle bundle){		
			if(!(this instanceof HomeActivity)){
				finish();
			}
			Intent intent = new Intent(this,activityClass);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if(bundle != null){
				intent.putExtras(bundle);
			}
			startActivity(intent);
	}
	
	
	
	/**
	 * Current state of active activity has been set from this method
	 * Also here we are checking if status message need to be shown
	 * Status message is either timed out or closed.
	 */
	@Override
	protected void onResume(){
		super.onResume();
		AppContext.getInstance().setActiveActivity(this);		
		refreshStatus();
	}
	
	
	/**
	 * A stub for network operation, here we can start respective network async task
	 * and it is implemented by respective subclasses, and might be invoked again in 
	 * case of login-expire occured
	 * @param arguments
	 */
	public void callWebService(Object... arguments){
		
	}
	
	/**
	 * A dialog appear when login expires
	 */
	public void onLoginExpire(){
		AppContext.getInstance().setLoginExpired(true);
		displayDialog(LOGIN_EXPIRED);
	}
	
	
	
	/**
	 * executes while click performed on left-title-bar-icon
	 */
	protected void onInfoClicked() {
		finish();
	}
	/**
	 * Action initiated when user selects any of the slider menu 
	 */
	public void onContextExecute(MenuItem menuItem) {
		switch(menuItem.getMenuId()){
		case CONTEXT_MENU_MY_REPORT:
			startActivity(ReportActivity.class);
			break;
		case CONTEXT_MENU_MY_PROFILE:
			startActivity(UserProfileActivity.class);
			break;
		case CONTEXT_MENU_INFORMATION:
			//super.startActivity(new Intent(this,InformationActivity.class));
			startActivity(InformationActivity.class);
			break;
			
		}
	}

	/**
	 * Using this method status message will displayed at the top of activity 
	 * which are the sub-class of base activity
	 */
	public void refreshStatus() {
		this.findViewById(R.id.statusMessageContainer).setVisibility(View.GONE);
		if(AppContext.getInstance().isDisplayStatusVisible()){
			IStatusMessage msg = AppContext.getInstance().getStatusMessage();
			if(msg != null)
				msg.initView(this);
		}
		
	}
	/**
	 * This method was invoked when login-browser-activity finishes its job
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == REQUEST_CODE) {
		     if(resultCode == RESULT_OK){ 
		    	 AppContext.getInstance().setLoginExpired(false);
		    	 if(!PreferenceUtils.getInstance().isUserRegistered()){		//Will execute once we back from login window
		    		 PreferenceUtils.getInstance().setUserRegistered(true);
		 			Intent intent = new Intent(this,UserProfileActivity.class);		
		 			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 			startActivity(intent);
			 	  }else{		 			
			 		  callWebService();
			 	  }
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		  }
		}//onActivityResult


	
	

}
