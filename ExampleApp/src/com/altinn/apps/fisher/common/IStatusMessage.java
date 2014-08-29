package com.altinn.apps.fisher.common;

/**
 * This is the interface which can be inherited by all 
 * kind of status message, this will take care of its representation in 
 * respective BaseActivity
 * 
 * All the implementor should initialize their View and functionality
 */

import android.app.Activity;

public interface IStatusMessage {
	/*
	 * This method invokes the respective status message view
	 * This method called inside BaseActivity onResume state
	 * if conditions are in favor then view will be displayed
	 */
	public void initView(Activity activityContext);

}
