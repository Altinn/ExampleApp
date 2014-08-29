package com.altinn.apps.fisher.ui.screen;
/**
 * This screen allows to user to add new factory or Edit factory details
 * 
 * Apart from that this screen shows user-information(name, title, email, contact etc) too.
 * In this screen what ever unit(organization) is select (top drop-down item) that will be used 
 * to send a message on that organization number.
 * 
 * Selected unit name will be made available in ReportReceivedFishActivity.
 * 
 */


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.altinn.apps.fisher.AppContext;
import com.altinn.apps.fisher.R;
import com.altinn.apps.fisher.db.DataBaseHelper;
import com.altinn.apps.fisher.db.FactoryDBHelper;
import com.altinn.apps.fisher.models.UserProfile;
import com.altinn.apps.fisher.net.TaskNotifier;
import com.altinn.apps.fisher.net.tasks.UserProfileTask;
import com.altinn.apps.fisher.settings.FactoryDetails;
import com.altinn.apps.fisher.settings.SettingItem;
import com.altinn.apps.fisher.settings.VesselsDetails;
import com.altinn.apps.fisher.utils.PreferenceUtils;

public class UserProfileActivity extends BaseActivity  implements TaskNotifier{

	private UserProfile mUserProfile;
	private TextView pName,pTitle,pPhone,pEmail;
	private View mAddBtnContainer;
	private Spinner mUnitSpinner;
	
	private ListView mReceptionList;
	private ArrayList<SettingItem> mItemList;
	private SettingsItemAdapter itemAdapter;
	
	private UserProfileTask mProfileTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		mLayOutId = R.layout.myprofile;
		super.onCreate(savedInstanceState);
		
	}
	
	private void init(){		
		setTitle(R.string.context_menu_myproflie,R.drawable.navigation_back);	
		pName = (TextView)findViewById(R.id.pName);
		pTitle = (TextView)findViewById(R.id.pTitle);
		pPhone = (TextView)findViewById(R.id.pPhone);
		pEmail = (TextView)findViewById(R.id.pEmail);
		
		mUnitSpinner = (Spinner)findViewById(R.id.selectUnitName);
		mAddBtnContainer = findViewById(R.id.addContainer);
		mReceptionList = (ListView)findViewById(R.id.factoryList);
		
		
		
		mUserProfile = PreferenceUtils.getInstance().getUserProfileData();
		if(mUserProfile == null){
			if(AppContext.TEST_MODE){
				mUserProfile = new UserProfile();
				mUserProfile.initTestProfile();
				PreferenceUtils.getInstance().setUserProfileData(mUserProfile);
			}else{
				callWebService();
				return;
			}
		}
		
		
		
		
		pName.setText(mUserProfile.getName());
		pTitle.setText(mUserProfile.getTitle());
		pPhone.setText(mUserProfile.getPhoneNumber());
		pEmail.setText(mUserProfile.getEmail());
		
		
		ArrayAdapter<String> unitNameAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,R.id.spinner_item, mUserProfile.getUnitListNames());
		mUnitSpinner.setAdapter(unitNameAdapter);
		mUnitSpinner.setSelection(mUserProfile.getActiveUnitIndex());
		mUnitSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				mUserProfile.setActiveUnitIndex(arg2);
				PreferenceUtils.getInstance().setUserProfileData(mUserProfile);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
		
		
		mAddBtnContainer.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserProfileActivity.this,FactoryDetailsActivity.class);				
				startActivity(intent);				
			}
		});
		
		
		FactoryDBHelper fDB = (FactoryDBHelper)DataBaseHelper.getInstance().getDBHelper(DataBaseHelper.TABLE_HEPLER_FACTORY);
		mItemList = fDB.getList();
		itemAdapter = new SettingsItemAdapter(mItemList);
		mReceptionList.setAdapter(itemAdapter);
		
		mReceptionList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {				
				Intent intent = new Intent(UserProfileActivity.this,FactoryDetailsActivity.class);
				SettingItem item = mItemList.get(index);
				intent.putExtra("ACTION_INDEX", item.getId());
				startActivity(intent);
			}
		});
	}	
	
	private static class ViewHolder{
		TextView lbl1, lbl1_value, lbl2, lbl2_value,lbl3, lbl3_value;
	}
	
	private class SettingsItemAdapter extends BaseAdapter{
		private ArrayList<SettingItem> mSettingList;
		private LayoutInflater mInflater;
		
		public SettingsItemAdapter(ArrayList<SettingItem> itemList){
			mInflater = LayoutInflater.from(UserProfileActivity.this);
			if(itemList == null)
				mSettingList = new ArrayList<SettingItem> ();
			else
				this.mSettingList = itemList;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSettingList.size();
		}

		@Override
		public SettingItem getItem(int index) {
			
			return mSettingList.get(index);
		}

		@Override
		public long getItemId(int index) {
			// TODO Auto-generated method stub
			return index;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			final SettingItem settingItem = mSettingList.get(position);
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.profile_list_item, null);
				holder = new ViewHolder();
				holder.lbl1 = (TextView)convertView.findViewById(R.id.r1lbl);
				holder.lbl1_value = (TextView)convertView.findViewById(R.id.r1lblValue);
				holder.lbl2 = (TextView)convertView.findViewById(R.id.r2lbl);
				holder.lbl2_value = (TextView)convertView.findViewById(R.id.r2lblValue);
				//holder.lbl3 = (TextView)convertView.findViewById(R.id.r3lbl);
				//holder.lbl3_value = (TextView)convertView.findViewById(R.id.r3lblValue);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			if(settingItem instanceof FactoryDetails){
				FactoryDetails factorySettingItem = (FactoryDetails)settingItem;
				holder.lbl1_value.setText(factorySettingItem.getFactoryName());
				holder.lbl2_value.setText(factorySettingItem.getFactoryNumber());
				//holder.lbl3_value.setText(factorySettingItem.getFactoryAddress() +"\n"+factorySettingItem.getFactoryPlace() + " "+factorySettingItem.getFactoryPostal());
				
			}else if(settingItem instanceof VesselsDetails){
				//VesselsDetails vesselItem = (VesselsDetails)settingItem;
				
			}			
			return convertView;
		}		
	}
	
	public void onResume(){
		init();
		super.onResume();
	}
	
	public void callWebService(Object... arguments){
		if(mProfileTask != null){
			mProfileTask.cancel(true);
		}
		mProfileTask = new UserProfileTask(this,this);
		mProfileTask.execute(arguments);
	}
	
	public void onSuccess(){
		PreferenceUtils.getInstance().setUserRegistered(true);	
		init();
		
	}

	@Override
	public void onError(int message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProgress() {
		// TODO Auto-generated method stub
		
	}
	
}
