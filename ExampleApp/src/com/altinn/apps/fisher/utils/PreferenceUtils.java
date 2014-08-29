package com.altinn.apps.fisher.utils;

/**
 * This class is used to save, key value pairs in Preferences.
 */

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import com.altinn.apps.fisher.AppContext;
import com.altinn.apps.fisher.models.CaughtInfoData;
import com.altinn.apps.fisher.models.InfoData;
import com.altinn.apps.fisher.models.ReportInfoData;
import com.altinn.apps.fisher.models.UserProfile;
import com.altinn.apps.fisher.ui.screen.HomeActivity;


public class PreferenceUtils {
	private static PreferenceUtils mInstance;
	private static final String PREFERENC_STORAGE = "com.altinn.apps.fisher.PSTORE";
	private static final String PREF_KEY_USER_REGISTERED = "PREF_KEY_USER_REGISTERED";
	private static final String PREF_KEY_USER_ID = "PREF_KEY_USER_ID";
	private static final String PREF_KEY_RECEIVED_DATA = "PREF_KEY_RECEIVED_DATA";
	private static final String PREF_KEY_CAUGHT_DATA = "PREF_KEY_CAUGHT_DATA";
	public static final String PREF_KEY_REGS_NUMBERS = "PREF_KEY_REGS_NUMBERS";
	public static final String PREF_KEY_VESSEL_NAMES = "PREF_KEY_VESSEL_NAMES";
	public static final String PREF_KEY_USER_PROFILE = "PREF_KEY_USER_PROFILE";
	public static final String  PREF_KEY_COOKIE_ID = "PREF_KEY_COOKIE_ID";
	public static final String  PREF_KEY_COOKIE_UPDATE_TIME= "PREF_KEY_COOKIE_UPDATE_TIME";
	private SharedPreferences mSharedPreferences;
	private Editor mPreferenceEditor;
	private static final char DELIMETER = '\u0AA0';//This delimiter is used to store/retrieval of arrays.
	
	private PreferenceUtils(){
		mSharedPreferences = AppContext.getInstance().getSharedPreferences(PREFERENC_STORAGE, AppContext.MODE_PRIVATE);
		mPreferenceEditor = mSharedPreferences.edit();		
	}
	public static PreferenceUtils getInstance(){
		if(mInstance == null){
			mInstance = new PreferenceUtils();
					}
		return mInstance;
	}
	
	public boolean isUserRegistered(){
		return getBoolean(PREF_KEY_USER_REGISTERED,false);
		
	}
	
	public void setUserRegistered(boolean status){
		updateBoolean(PREF_KEY_USER_REGISTERED,status );
	}
	
	public String getUserId(){
		return getString(PREF_KEY_USER_ID,"");
		
	}
	
	public void setUserId(String id){
		updateString(PREF_KEY_USER_ID,id );
	}
	
	public void setCookieUpdateTime(long currentTimeMillis) {		
		updateLong(PREF_KEY_COOKIE_UPDATE_TIME, currentTimeMillis);
	}	
	
	public long getCookieUpdateTime() {		
		return getLong(PREF_KEY_COOKIE_UPDATE_TIME, 0);
	}
	
	public void setInfoFormData(InfoData data){
		String value = Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
		String key = null;
		if(data.mFormType == HomeActivity.MENU_REPORT_RECEIVED){
			key = PREF_KEY_RECEIVED_DATA;
		}else if(data.mFormType == HomeActivity.MENU_REPORT_CAUGHT){
			key = PREF_KEY_CAUGHT_DATA;
		}
		updateString(key,value );
	}
	
	public InfoData getInfoFormData(int formType){
		String key = null;
		if(formType == HomeActivity.MENU_REPORT_RECEIVED){
			key = PREF_KEY_RECEIVED_DATA;
		}else if(formType == HomeActivity.MENU_REPORT_CAUGHT){
			key = PREF_KEY_CAUGHT_DATA;
		}
		String value = getString(key, null);
		InfoData infodata = null;
		if(value != null){
			byte[] dataBytes =  Base64.decode(value, Base64.DEFAULT);
			if(dataBytes != null){
				if(formType == HomeActivity.MENU_REPORT_RECEIVED){
					infodata = new  ReportInfoData(dataBytes);
				}else if(formType == HomeActivity.MENU_REPORT_CAUGHT){
					infodata = new  CaughtInfoData(dataBytes);
				}				
			}
		}
		return infodata;
	}
	
	public void setUserProfileData(UserProfile data){
		String value = Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
		updateString(PREF_KEY_USER_PROFILE,value );
	}
	
	public UserProfile getUserProfileData(){
		String value = getString(PREF_KEY_USER_PROFILE, null);
		UserProfile userProfile = null;
		if(value != null){
			byte[] dataBytes =  Base64.decode(value, Base64.DEFAULT);
			if(dataBytes != null){				
				userProfile = new UserProfile(dataBytes);
			}
		}
		return userProfile;
	}
	
	public void updateHistoryWord(String key, String value){
		if(value == null || value.trim().length()==0){
			return;
		}
		value = value.trim();
		String[] strArr = getStringArray(key);
		boolean matchFound = false;
		if(strArr != null){
			for(String temp:strArr){
				if(temp.equalsIgnoreCase(value)){
					matchFound = true;
					break;
				}
			}
		}
		if(!matchFound){
			if(strArr == null || strArr.length == 0){
				strArr = new String[]{value};
			}else{
				String[] tempArray = new String[strArr.length+1];
				System.arraycopy(strArr, 0, tempArray, 0, strArr.length);
				tempArray[strArr.length] = value;	
				strArr = tempArray;
			}
			updateStringArray(key,strArr);
		}
	}
	
	public String[] getHistoryWords(String key){
		String[] result = getStringArray(key);
		if(result == null){
			result = new String[]{};
		}
		return result;
	}
	
	
	private boolean getBoolean(String key, boolean defValue){
		defValue = mSharedPreferences.getBoolean(key, defValue);
		return defValue;
	}
    
	private void updateStringArray(String key, String[] value){
		if(value != null && value.length >0){
			StringBuffer sb = new StringBuffer("");
			for(String strVal : value){
				sb.append(strVal).append(DELIMETER);
			}
			sb.deleteCharAt(sb.length()-1);
			updateString(key,sb.toString());
		}	
	}
	
	private String[] getStringArray(String key){
		String[] result = null;
		String value = getString(key, null);
		if(value != null && value.length() >0){
			result = TextUtils.split(value, ""+DELIMETER);
		}	
		return result;
	}
	
	public String getCookies(){
		return getString(PREF_KEY_COOKIE_ID,"");		
	}
	public void setCookies(String cookieStr){
		updateString(PREF_KEY_COOKIE_ID,cookieStr);		
	}
	
	private void updateBoolean(String key, boolean value){
		//Editor editor = mSharedPreferences.edit();
		mPreferenceEditor.putBoolean(key, value);
		mPreferenceEditor.commit();
	}
	
	private int getInt(String key, int defValue){
		defValue = mSharedPreferences.getInt(key, defValue);
		return defValue;
	}
	
	private void updateInt(String key, int value){
		//Editor editor = mSharedPreferences.edit();
		mPreferenceEditor.putInt(key, value);
		mPreferenceEditor.commit();
	}
	
	private long getLong(String key, long defValue){
		defValue = mSharedPreferences.getLong(key, defValue);
		return defValue;
	}
	
	private void updateLong(String key, long value){
		//Editor editor = mSharedPreferences.edit();
		mPreferenceEditor.putLong(key, value);
		mPreferenceEditor.commit();
		
		
	}
	
	
	
	private String getString(String key, String defValue){
		defValue = mSharedPreferences.getString(key, defValue);
		return defValue;
	}
	
	private void updateString(String key, String value){
		//Editor editor = mSharedPreferences.edit();
		mPreferenceEditor.putString(key, value);
		mPreferenceEditor.commit();
	}
	
	

}
