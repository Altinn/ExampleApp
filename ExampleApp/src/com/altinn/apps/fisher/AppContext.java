package com.altinn.apps.fisher;

/**
 * This is a class which will first comes in picture when application starts.
 * So here we can have some global functionality which is accessible through out 
 * all the activities in the live session of the application.
 * 
 */


import java.util.Date;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.WindowManager;

import com.altinn.apps.fisher.common.AppConstants;
import com.altinn.apps.fisher.common.IStatusMessage;
import com.altinn.apps.fisher.net.tasks.RefreshTokenTask;
import com.altinn.apps.fisher.ui.screen.BaseActivity;
import com.altinn.apps.fisher.utils.PreferenceUtils;
import com.altinn.apps.fisher.utils.Utils.Log;




public class AppContext extends Application {
	
	private static AppContext mInstance;
	private static BaseActivity mActiveActivity;
	private boolean mDisplayStatusInfo;
	private IStatusMessage mStatusMessage;
	private boolean isLoginExpired;
	public static boolean TEST_MODE = false;//Ideal case keep this variable false , For testing if we want to test only UI, in non - network state in that case we can make this variable true
	
    private CountDownTimer mRefreshTokenTimer;
    private RefreshTokenTask mRefreshTokenTask;
    
    private int mMenuWidth;
	
    /*
     * Here we are starting scheduler for refresh token
     * (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		
	
	}
	
	/**
	 * This method make this class accessible throughout the application environment
	 * @return
	 */
	public static AppContext getInstance(){
		return mInstance;
	}

	
	/**
	 * On Resume of BaseActivity we are setting Active Activity-State by this method
	 * @param baseActivity
	 */
	public void setActiveActivity(BaseActivity baseActivity) {
		mActiveActivity = baseActivity;
		
	}
	
	/**
	 * This method give us reference of current active activity
	 * @return
	 */
	public BaseActivity getActiveActivity() {
		return mActiveActivity;
		
	}
	
	/**
	 * This method called from activity which is at the bottom of activity stack.
	 * On exit we are canceling the timer task.
	 */
	public void exitApp(){
		//Process.killProcess(Process.myPid());
		if( mRefreshTokenTimer != null){
			mRefreshTokenTimer.cancel();
		}
	}

	/**
	 * This method indicates weather status message is visible or not
	 * @return mDisplayStatusInfo
	 */
	public boolean isDisplayStatusVisible() {
		return mDisplayStatusInfo;
	}

	/**
	 * This method makes status message visible on any of the active screen which is a sub class of 
	 * BaseActivity
	 * @param mDisplayStatusInfo
	 * @param msg
	 */
	public void setDisplayStatus(boolean mDisplayStatusInfo, IStatusMessage msg) {
		this.mDisplayStatusInfo = mDisplayStatusInfo;
		this.mStatusMessage = msg;
		if(mActiveActivity != null){
			mActiveActivity.refreshStatus();
		}
	}
	
	/**
	 * This method returns instance of currently active status message
	 * @return mStatusMessage
	 */
	public IStatusMessage getStatusMessage(){
		return mStatusMessage;
	}
	
	/**
	 * Using this method we can set/reset login status
	 * in case of login expired we can call browser interface for login,
	 * And after successful login we can call 'callwebservice' method of respective activity
	 * @param loginStatus
	 */
	public void setLoginExpired(boolean loginStatus){
		isLoginExpired = loginStatus;
	}
	
	/**
	 * This method indicates login status of the user
	 * @return
	 */
	public boolean isLoginExpired(){
		return isLoginExpired;
	}
	
	
	
	/**
	 * This method start refresh token timer task,
	 * this will invoked in every 7+ minutes
	 * if conditions are in favor it will try to refresh a token.
	 * Usually in server configuration, token will expire in 30 mins.
	 * If web service called after half of the time (>15 mins) then new token will be issued.
	 * 
	 * Here we are checking 'isRefreshNeeded() = >15 mins' this condition is in favor to refresh the token.
	 * During this task we are just trying to access a web service(dummy), so that we get a refreshed token, and  user remains logged in
	 *  
	 */
	public void scheduleRefreshTimer(){
		if( mRefreshTokenTimer != null){
			mRefreshTokenTimer.cancel();
		}
		
		mRefreshTokenTimer = new CountDownTimer(Integer.MAX_VALUE, AppConstants.REFRESH_TIMER) {
			public void onTick(long millisUntilFinished) {
				Log.d("scheduleRefreshTimer#onTick(): ", ""+(new Date(System.currentTimeMillis())).toString());
				if(isRefreshNeeded()){		
					startRefreshTask();
				}
			}
			public void onFinish() {
				Log.d("scheduleRefreshTimer#onFinish(): ",""+(new Date(System.currentTimeMillis())).toString());
				
			}
		};
		mRefreshTokenTimer.start();
	}	
	
	/**
	 * When ever we get a refreshed token, then that time get recorded in preferences, to get the next refresh time.
	 * So this method gives indicates that do we really need to refresh a token
	 * @return
	 */
	public boolean isRefreshNeeded(){
		return ( System.currentTimeMillis() - PreferenceUtils.getInstance().getCookieUpdateTime())>AppConstants.REFRESH_AVAILABLE_TIME;
	}
	
	/**
	 * This method returns true if token is stale by 30+ mins.
	 * @return
	 */
	public boolean isTokenExpired(){
		return (System.currentTimeMillis() -  PreferenceUtils.getInstance().getCookieUpdateTime())  >AppConstants.TOKEN_EXPIRE_TIME_MILLIES;
	}
	
	
	/**
	 * Start a refresh token action refer AppContext.scheduleRefreshTimer()
	 */
	public void startRefreshTask(){					
			if(mRefreshTokenTask != null){
				mRefreshTokenTask.cancel(true);
			}
			mRefreshTokenTask = new RefreshTokenTask();
			mRefreshTokenTask.execute();
			Log.d("executeRefreshTask(): ",""+(new Date(System.currentTimeMillis())).toString());
		
	
	}
	
	public int getMenuWidth(){
		if(mMenuWidth == 0){
			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			
			mMenuWidth = (int)(Math.min(width, height)*0.80);//80% screen width
			
		}
		return mMenuWidth;
	}
	


}
