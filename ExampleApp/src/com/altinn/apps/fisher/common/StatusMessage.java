package com.altinn.apps.fisher.common;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.altinn.apps.fisher.AppContext;
import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.ui.screen.BaseActivity;


/**
 * If in Future we have different status message 
 * Define such messages by implementing IStatusMessage
 *
 */
public class StatusMessage implements IStatusMessage{
	private int mLayoutId = R.layout.status_msg1;
	private String mMessageStr;
	private long mTimeOut = 20*1000;
	private CountDownTimer mCountDownTimer;
	private boolean isCountDownActive = true;
	public StatusMessage(String messageStr){
		mMessageStr = messageStr;
		mCountDownTimer = new CountDownTimer(mTimeOut,1000){
			@Override
			public void onFinish() {	
				isCountDownActive = false;
				AppContext.getInstance().setDisplayStatus(false,null);
			}

			@Override
			public void onTick(long millisUntilFinished) {				
				
			}			
		};
		mCountDownTimer.start();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.common.IStatusMessage#initView(android.app.Activity)
	 */
	public void initView(Activity actvityContext){
		if(actvityContext instanceof BaseActivity){
			RelativeLayout rlParent = (RelativeLayout)actvityContext.findViewById(R.id.statusMessageContainer);			
			if(rlParent == null) return;	
			rlParent.removeAllViews();
			rlParent.setVisibility(View.VISIBLE);
			View statusMessageView = LayoutInflater.from(AppContext.getInstance()).inflate(mLayoutId, null);
			((TextView)statusMessageView.findViewById(R.id.st_title)).setText(mMessageStr);
			statusMessageView.findViewById(R.id.st_close).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					if(isCountDownActive)
						mCountDownTimer.cancel();
					if(AppContext.getInstance().isDisplayStatusVisible()){
						AppContext.getInstance().setDisplayStatus(false,null);
					}				
				}
			});
			rlParent.addView(statusMessageView);
		}
	}

}
