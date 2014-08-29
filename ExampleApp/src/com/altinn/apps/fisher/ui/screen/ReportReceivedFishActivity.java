package com.altinn.apps.fisher.ui.screen;
/**
 * This screen allows user to enter details of form what ever is needed to send.
 * 
 * Some of the items are invisible initially, once user fills the details then other stuffs also made available.
 * Before coming this screen, it is assumed that user is ever-logged in, because if user is not logged in then 
 * we can't assign unit-name.
 * 
 * 
 */

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.altinn.apps.fisher.AppContext;
import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.common.StatusMessage;
import com.altinn.apps.fisher.db.DataBaseHelper;
import com.altinn.apps.fisher.db.FactoryDBHelper;
import com.altinn.apps.fisher.db.FormDBHelper;
import com.altinn.apps.fisher.models.ReportInfoData;
import com.altinn.apps.fisher.models.UserProfile;
import com.altinn.apps.fisher.net.TaskNotifier;
import com.altinn.apps.fisher.net.tasks.SendReportTask;
import com.altinn.apps.fisher.ui.component.DurationTimePickDialog;
import com.altinn.apps.fisher.utils.PreferenceUtils;
import com.altinn.apps.fisher.utils.Utils;

public class ReportReceivedFishActivity extends BaseActivity implements TaskNotifier {
	private final String TAG = "ReportReceivedFishActivity";
	private boolean mViewMode;//true indicates editing is disabled
    private AutoCompleteTextView mEdtAutoVesselName,mEdtAutoFirmNumber;//,mEdtAutoVesselNumber;
    private Spinner mSpinnerFishCategory,mSpinnerFirmName;//,mSpinnerFirmNumber;
   // private DatePickerDialog mDatePickerDlg;
   // private TimePickerDialog mTimePickerDlg;
    private EditText mEdtTime, mEdtDate,mEdtUnit;
    private ArrayAdapter<String> mFishCategoryAdapter, mFirmNameAdapter, mFirmNumberAdapter, mVesselNameAdapter;//, mVesselNumberadapter;;
    private ImageButton mImgBtnDate, mImgBtnTime;
	private View mBtnSave, mBtnSend;
	private SendReportTask mSendTask;
	private ReportInfoData rData;
	private int mFormId  = -1;
	private View mContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		mLayOutId = R.layout.report_received_fish;
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.report_received_fish);
		init();
	}
	
	private void init(){
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			mViewMode = bundle.getBoolean("VIEW_MODE",false);
			mFormId = bundle.getInt("FORM_ID", -1);
		}	
		if(mFormId != -1){
			FormDBHelper helperFormDb = (FormDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FORM);
			rData = (ReportInfoData)helperFormDb.getFormData(mFormId);
		}
		
		setTitle(R.string.menu_receive_reports, R.drawable.navigation_back);
		mContainer = findViewById(R.id.layout_container);
		mEdtUnit = (EditText)findViewById(R.id.edtUnit);
		 mSpinnerFirmName = (Spinner)findViewById(R.id.selectFirmName);
		 mEdtAutoFirmNumber = (AutoCompleteTextView)findViewById(R.id.edtFirmNumber);
		 mEdtAutoVesselName = (AutoCompleteTextView)findViewById(R.id.edtVesselName);
		 //mEdtAutoVesselNumber = (AutoCompleteTextView)findViewById(R.id.edtVesselNumber);
		 
		 mSpinnerFishCategory = (Spinner)findViewById(R.id.fish_selector);
		// mDatePicker = (DatePicker)findViewById(R.id.datePicker);
		 //mTimePicker = (TimePicker)findViewById(R.id.timePicker);
		 
//		 ImageButton settingsBtn = (ImageButton)findViewById(R.id.btn_settings);
//		 settingsBtn.setOnClickListener(new OnClickListener() {			
//			@Override
//			public void onClick(View v) {				
//				startActivity(SettingsActivity.class);
//			}
//		});
		 
		 mEdtTime = (EditText)findViewById(R.id.edtTimeInput);
		 mEdtDate = (EditText)findViewById(R.id.edtDateInput);
		 
		 mImgBtnDate = (ImageButton)findViewById(R.id.datePicker);
		 mImgBtnDate.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				OnDateSetListener dateSetListner = new OnDateSetListener(){
					@Override
					public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
						monthOfYear+=1;
						mEdtDate.setText(""+(dayOfMonth <= 9?"0"+dayOfMonth:dayOfMonth)+"."+(monthOfYear <= 9?"0"+monthOfYear:monthOfYear)+"."+year);
					}
					
				};
				DatePickerDialog datePickerDialog = new DatePickerDialog(ReportReceivedFishActivity.this, dateSetListner, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				//datePickerDialog.getDatePicker().setCalendarViewShown(true);
				//datePickerDialog.getDatePicker().setSpinnersShown(false);
				
				datePickerDialog.show();
			}
		});
		
		 mImgBtnTime = (ImageButton)findViewById(R.id.timePicker);
		 mImgBtnTime.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					Calendar cal = Calendar.getInstance();
					OnTimeSetListener timeSetListner = new OnTimeSetListener(){
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							mEdtTime.setText(""+(hourOfDay <= 9?"0"+hourOfDay:hourOfDay)+"."+(minute <= 9?"0"+minute:minute));
										
						}
						
					};
					DurationTimePickDialog timePickerDialog = new DurationTimePickDialog(ReportReceivedFishActivity.this,timeSetListner, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true,15);
					//TimePickerDialog timePickerDialog = new TimePickerDialog(ReportReceivedFishActivity.this,timeSetListner, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true);
					timePickerDialog.show();
					//updateTimeInterval(timePickerDialog);
				}
			});
		 mBtnSave = findViewById(R.id.btnSave);
		 mBtnSend = findViewById(R.id.btnSend);
		 mBtnSave.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				doSave();	
				finish();
			}
		});
		 mBtnSend.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					if(Utils.isNetworkAvailable(ReportReceivedFishActivity.this)){
						doSend();		
					}else{
						displayDialog(NO_NETWORK_DIALOG_ID);
					}
							
				}
			});
		 
		 
		 mSpinnerFirmName.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if(position == 0){
					mContainer.setVisibility(View.GONE);
				}else{
					mContainer.setVisibility(View.VISIBLE);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
		 
		
		prefillData(); 
		initBgTask();
		initValues();
	}
	
	private void initValues(){
		
				
	
		//ReportInfoData rData = (ReportInfoData)PreferenceUtils.getInstance().getInfoFormData(HomeActivityNew.MENU_REPORT_RECEIVED);
		if(rData != null){
			mSpinnerFirmName.setSelection(rData.mFirmNameIndex);
			mEdtAutoFirmNumber.setText(rData.mRegsNumber);
			mSpinnerFishCategory.setSelection(rData.mFishTypeIndex);
			mEdtAutoVesselName.setText(rData.mShipName);
			mEdtDate.setText(rData.mDateStr);
			mEdtTime.setText(rData.mTimeStr);
			mEdtUnit.setText(rData.mUnitName);
		}
		if(Utils.isNullorEmpty(mEdtUnit.getText().toString())){
			UserProfile userProfile = PreferenceUtils.getInstance().getUserProfileData();
			if(userProfile == null){ 
				if(!AppContext.TEST_MODE){
					finish();
					return;
				}else{
					 userProfile = new UserProfile();
					 userProfile.initTestProfile();
					 PreferenceUtils.getInstance().setUserProfileData(userProfile);
				}
			}
			
			int index = userProfile.getActiveUnitIndex();
			ArrayList<String> unitList =  userProfile.getUnitListNames();
			if(index < unitList.size()){
				mEdtUnit.setText(unitList.get(index));
			}
		}
		if(rData == null)
			rData = new ReportInfoData();
		
		if(mSpinnerFirmName.getSelectedItemPosition() == 0){
			mContainer.setVisibility(View.GONE);
		}else{
			mContainer.setVisibility(View.VISIBLE);
		}
	}
	
		
	private void doSave(){
		//ReportInfoData rData = (ReportInfoData)PreferenceUtils.getInstance().getInfoFormData(HomeActivityNew.MENU_REPORT_RECEIVED);
	
		rData.mFormType = HomeActivity.MENU_REPORT_RECEIVED;	
		rData.mCreateId = System.currentTimeMillis();
		rData.mDateStr = mEdtDate.getText().toString();
		rData.mTimeStr = mEdtTime.getText().toString();
		rData.mFirmNameIndex = mSpinnerFirmName.getSelectedItemPosition();
		rData.mFirmName = (String)mSpinnerFirmName.getSelectedItem();
		rData.mUnitName = mEdtUnit.getText().toString();
		//rData.mRegsNumberIndex = mSpinnerFirmNumber.getSelectedItemPosition();
		//rData.mRegsNumber = (String)mSpinnerFirmNumber.getSelectedItem();
		rData.mRegsNumber = mEdtAutoFirmNumber.getText().toString();
		
		rData.mFishTypeIndex = mSpinnerFishCategory.getSelectedItemPosition();
		rData.mFishName = (String)mSpinnerFishCategory.getSelectedItem();
		
		rData.mShipName = mEdtAutoVesselName.getText().toString();
		
		//PreferenceUtils.getInstance().setInfoFormData(rData);
		FormDBHelper helperFormDb = (FormDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FORM);
		long id  = helperFormDb.updateFormData(rData);
		rData.mId = (int)id;
		
//		RegsDBHelper regsHelper = (RegsDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_REGS_NUMBER);
//		regsHelper.updateData(rData.mRegsNumber );
//		VesselDBHelper vesselHelper = (VesselDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_SHIP_NAME);
//		vesselHelper.updateData(new VesselsDetails(-1,rData.mShipName,"",""));
	}
	
	
	
	private void doSend(){
		doSave();
		PreferenceUtils.getInstance().updateHistoryWord(PreferenceUtils.PREF_KEY_REGS_NUMBERS, mEdtAutoFirmNumber.getText().toString());
		PreferenceUtils.getInstance().updateHistoryWord(PreferenceUtils.PREF_KEY_VESSEL_NAMES, mEdtAutoVesselName.getText().toString());
		callWebService(rData.mId);
		
	}
	
	
	
	public void callWebService(Object... arguments){
		if(mSendTask != null){
			mSendTask.cancel(true);
		}
		mSendTask = new SendReportTask(this,this);
		mSendTask.execute(arguments);
	}
	
	public void onSuccess(){
		//PreferenceUtils.getInstance().setInfoFormData(new ReportInfoData());
//		init();
//		mFormId = -1;
//		rData = null;
		finish();
		
		String id = "";
		if(mSendTask != null)
			id = mSendTask.getMessageSendId();
		String msgStr = getResources().getString(R.string.msg_report_sent);
		msgStr = String.format(msgStr, id);
		StatusMessage msg = new StatusMessage(msgStr);
		AppContext.getInstance().setDisplayStatus(true, msg);
	}
	
	
	
	
	
	
	
	
	
	
	
	private void initBgTask(){
		
//		(new AsyncTask(){
//			@Override
//			protected Object doInBackground(Object... arg0) {
//				ArrayList<InfoData> warningInfoList =  DataBaseHelper.getInstance().getFormList(HomeActivity.MENU_ID_ITEM1,!mViewMode);
//				ArrayList<InfoData> dangerInfoList =  DataBaseHelper.getInstance().getFormList(HomeActivity.MENU_ID_ITEM2,!mViewMode);
//				ArrayList<ReportItem> reportListW = new ArrayList<ReportItem> ();
//				ArrayList<ReportItem> reportListD = new ArrayList<ReportItem> ();
//				if(warningInfoList != null){					
//					for(InfoData infoData : warningInfoList){
//						reportListW.add(new ReportItem(infoData));
//					}
//					
//				}
//				if(dangerInfoList != null){					
//					for(InfoData infoData : dangerInfoList){
//						reportListD.add(new ReportItem(infoData));
//					}
//					
//				}
//				mWarningAdapter = new ReportAdapter(reportListW);
//				mDangerAdapter = new ReportAdapter(reportListD);
//				return null;
//			}
//			
//			@Override
//			protected void onPostExecute(Object obj) {
//				mListWarnig.setAdapter(mWarningAdapter);
//				mListDanger.setAdapter(mDangerAdapter);
//			}
//			
//		}).execute("");
		
	}

	@Override
	public void onError(int message) {
		String msgStr = getResources().getString(R.string.msg_report_sent_failed);
		msgStr = String.format(msgStr, ""+message);
		StatusMessage msg = new StatusMessage(msgStr);
		AppContext.getInstance().setDisplayStatus(true, msg);
		
	}

	@Override
	public void onProgress() {
		// TODO Auto-generated method stub
		
	}
	
	private void prefillData(){
		FactoryDBHelper helperFactory = (FactoryDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FACTORY);
		ArrayList<String> nameList =  (ArrayList<String>)helperFactory.getNameList().clone();
		nameList.add(0, getString(R.string.lbl_no_recipt_selected));
		mFirmNameAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,R.id.spinner_item, nameList);
		mSpinnerFirmName.setAdapter(mFirmNameAdapter);
		
//		FishCategoryDBHelper fishCategoryHelper = (FishCategoryDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FISH_CATEGORY);
//		ArrayList<String> fishCategoryrList =  (ArrayList<String>)fishCategoryHelper.getNameList().clone();
//		fishCategoryrList.add(0, getString(R.string.lbl_choose_fish));
		String[] fishCategoryrList = getResources().getStringArray(R.array.fish_types);
		mFishCategoryAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,R.id.spinner_item,fishCategoryrList);
		mSpinnerFishCategory.setAdapter(mFishCategoryAdapter);
		
		//mFirmNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,new String[]{"Internation Fish Corporation","Fresh Fish Factory","Ocean sea Food distributor"});
		
		
//		RegsDBHelper regsHelper = (RegsDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_REGS_NUMBER);
//		ArrayList<String> regsNameList =  (ArrayList<String>)regsHelper.getNameList();
		String[] regsNameList = PreferenceUtils.getInstance().getHistoryWords(PreferenceUtils.PREF_KEY_REGS_NUMBERS);
		mFirmNumberAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,R.id.spinner_item,regsNameList);
		mEdtAutoFirmNumber.setAdapter(mFirmNumberAdapter);
		mEdtAutoFirmNumber.setThreshold(1);
		
//		VesselDBHelper vesselHelper = (VesselDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_SHIP_NAME);
//		ArrayList<String> vesselNameList =  (ArrayList<String>)vesselHelper.getNameList();
		String[] vesselNameList = PreferenceUtils.getInstance().getHistoryWords(PreferenceUtils.PREF_KEY_VESSEL_NAMES);
		mVesselNameAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,R.id.spinner_item,vesselNameList);
		mEdtAutoVesselName.setAdapter(mVesselNameAdapter);
//		mEdtAutoVesselName.setHint("Enter \'A\' OR \'B\'");
		mEdtAutoVesselName.setThreshold(1);
	}
	
	
	

	
}
