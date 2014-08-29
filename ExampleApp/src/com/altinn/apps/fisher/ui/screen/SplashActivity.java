package com.altinn.apps.fisher.ui.screen;
/**
 * This is first screen appears to the user
 * This screen is visible for 2 seconds, and after that HomeActivity is pushed
 * On HomeActivity it is decided what next activity will brings infront of user
 * 
 */


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.altinn.apps.fisher.AppContext;
import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.net.jsobj.FormObj;
import com.altinn.apps.fisher.net.jsobj.MessageObj;


public class SplashActivity extends Activity {

	private static final int STOPSPLASH = 0;
	private final String TAG = "SplashActivity";
	
	// time in milliseconds
	private static final long SPLASHTIME = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash); 

		// start timer
		Message msg = new Message();
		msg.what = STOPSPLASH;
		
		
		splashHandler.sendMessageDelayed(msg, SPLASHTIME);
		
		if(!AppContext.TEST_MODE)
			AppContext.getInstance().scheduleRefreshTimer();
		
		
	}

	// handler for splash screen
	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case STOPSPLASH:										
					Intent intnt = new Intent(SplashActivity.this,HomeActivity.class);
					startActivity(intnt);
					finish();
					
//					invokeTest();
//					finish();
					break;
			}
			super.handleMessage(msg);
		}
	};

	public void onBackPressed() {
		// preventing the user from closing the application when the Splash
		// Screen is active
		//if(TESTMODE) finish();
		
	};
	
//	private void invokeTest(){
//
//		String test  = createJSONPost();
//		System.out.println(""+test);
//	}
//	
//	public String createJSONPost(){
//		String result = "";
//		
//		MessageObj jsMsgPost = new MessageObj();
//		jsMsgPost.mType = "FormTask";
//		jsMsgPost.mServiceCode="3689";
//		jsMsgPost.mServiceEdition=131113;
//		
//		FormObj formObj1 = new FormObj();
//		formObj1.mType = "MainForm";
//		formObj1.mDataFormatId="4302";
//		formObj1.mDataFormatVersion="36777";
//		formObj1.mFormData="%1$s";
//		jsMsgPost.mFormList.add(formObj1);
//		
//		result = jsMsgPost.createJson().toString();
//		result = String.format(result, getXMLData(formObj1));
//		
//		return result;
//	}
//	
//	private String getXMLData(FormObj formObj){
//		String result = null;
//		try {
//			InputStream is = AppContext.getInstance().getAssets().open("xml/postmsg.xml");
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int ch = -1;
//			while( (ch = is.read(buffer)) != -1){
//				bos.write(buffer,0,ch);
//			}
//			bos.flush();
//			result = new String(bos.toByteArray(),"UTF-8");
//			result = String.format(result, formObj.mDataFormatId,formObj.mDataFormatVersion,"HJERKINN OG FJELLSTRAND REGNSKAP","123456789","Navn Navnesen","torsk","2014-05-22T09:15:00");
//			bos.close();
//			is.close();
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//		
//	}
//	
//	private void testDialog(){
//		Calendar cal = Calendar.getInstance();
//		OnTimeSetListener timeSetListner = new OnTimeSetListener(){
//			@Override
//			public void onTimeSet(TimePicker view, int hourOfDay,
//					int minute) {
//											
//			}
//			
//		};
//		DurationTimePickDialog timePickerDialog = new DurationTimePickDialog(SplashActivity.this,timeSetListner, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true,15);
//		//TimePickerDialog timePickerDialog = new TimePickerDialog(ReportReceivedFishActivity.this,timeSetListner, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true);
//		timePickerDialog.show();
//	}
	
	
}
