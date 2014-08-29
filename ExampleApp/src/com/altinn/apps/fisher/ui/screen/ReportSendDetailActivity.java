package com.altinn.apps.fisher.ui.screen;
/**
 * This screen comes in picture when user selects 
 * Sent report from list-view of reports (thorough context menu)
 * 
 * In this screen sent related data are presented to the user
 * 
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.widget.TextView;

import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.db.DataBaseHelper;
import com.altinn.apps.fisher.db.FormDBHelper;
import com.altinn.apps.fisher.models.ReportInfoData;

public class ReportSendDetailActivity extends BaseActivity {

	private int mFormId = -1;
	private ReportInfoData rData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		mLayOutId = R.layout.report_send_fish;
		super.onCreate(savedInstanceState);
		init();
	}
	
	private void init(){		
		setTitle(R.string.context_menu_myreports, R.drawable.navigation_back);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mFormId = bundle.getInt("FORM_ID", -1);
		}	
		if(mFormId != -1){
			FormDBHelper helperFormDb = (FormDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FORM);
			rData = (ReportInfoData)helperFormDb.getFormData(mFormId);
		}else{
			finish();
		}
		if(rData != null){
			long dateMillies = rData.mSendId;			
			
			((TextView)findViewById(R.id.edtReception)).setText(rData.mFirmName);
			((TextView)findViewById(R.id.edtShipBrand)).setText(rData.mRegsNumber);
			((TextView)findViewById(R.id.edtShipName)).setText(rData.mShipName);
			((TextView)findViewById(R.id.edtFishType)).setText(rData.mFishName);
			
			String msgId = rData.getResponsId(false);
			((TextView)findViewById(R.id.edtMsgId)).setText(msgId);
			
			Date date = new Date(dateMillies);
			String dateStr = "";
			String timeStr = "";
			
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd. MMMM yyyy");
			dateStr = sdfDate.format(date);
			
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH.mm");
			timeStr = sdfTime.format(date);
			
			dateStr = dateStr.toLowerCase();
			if(dateStr.startsWith("0")){
				dateStr = dateStr.substring(1);
			}
			
			((TextView)findViewById(R.id.sendDateLblValue)).setText(dateStr);
			((TextView)findViewById(R.id.sendTimeLblValue)).setText(timeStr);
		}
	}	
	
}
