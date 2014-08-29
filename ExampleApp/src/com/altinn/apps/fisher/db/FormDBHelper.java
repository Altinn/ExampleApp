package com.altinn.apps.fisher.db;
/**
 * This helper is used to store fields associated in Sending-Form [ReportReceivedFishActivity]
 * 
 */
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.altinn.apps.fisher.models.CaughtInfoData;
import com.altinn.apps.fisher.models.InfoData;
import com.altinn.apps.fisher.models.ReportInfoData;
import com.altinn.apps.fisher.ui.screen.HomeActivity;
import com.altinn.apps.fisher.utils.Utils;

public class FormDBHelper implements IDBHelper{

	public FormDBHelper() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FORMS_DATA + " ("+FT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FT_CREATE_ID+" TEXT, "+FT_SEND_ID+" TEXT, "+FT_FORM_TYPE+" INTEGER, "+FT_RESPONSE_ID+" TEXT, "+FT_FORM_DATA+" BLOB);");
         //db.execSQL("CREATE TABLE " + TABLE_BREADCRUMBS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, time REAL, lat REAL, long REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMS_DATA);
         onCreate(db);
	}
	
	public static final String TABLE_FORMS_DATA = "FORM_TABLE";
	public static final String FT_ID = "id";
	public static final String FT_CREATE_ID = "create_id";
	public static final String FT_SEND_ID = "send_id";
	public static final String FT_FORM_TYPE = "form_type";
	public static final String FT_RESPONSE_ID = "response_id";
	public static final String FT_FORM_DATA = "form_data";
	
	
	
	public ArrayList<InfoData> getFormList(int formType, boolean isDraft){
		ArrayList<InfoData> mArrayList = new ArrayList<InfoData>();
		SQLiteDatabase db = null;
		Cursor result = null;
		try {
			db = DataBaseHelper.getInstance().getReadableDatabase();
			result = db.rawQuery("SELECT "+FT_ID+",  "+FT_CREATE_ID+",  "+FT_SEND_ID+",  "+FT_FORM_TYPE+",  "+FT_RESPONSE_ID+", "+FT_FORM_DATA+" FROM  "+TABLE_FORMS_DATA+" WHERE "+FT_FORM_TYPE+" =  "+formType +" ORDER BY "+ (isDraft?FT_CREATE_ID:FT_SEND_ID) + " DESC ", null);
			if(result != null && result.moveToFirst()){					
				do{
						byte[] blobData = result.getBlob(result.getColumnIndex(FT_FORM_DATA));
						if(blobData != null){
							InfoData infoData = null;
							if(formType == HomeActivity.MENU_REPORT_RECEIVED){
								infoData = new ReportInfoData(blobData);
							}else if(formType == HomeActivity.MENU_REPORT_CAUGHT){
								infoData = new CaughtInfoData(blobData);
							}
							infoData.mFormType = formType;
							infoData.mId =  result.getInt(result.getColumnIndex(FT_ID));
							infoData.mFormType =  result.getInt(result.getColumnIndex(FT_FORM_TYPE));
							
							String createDateStr=  result.getString(result.getColumnIndex(FT_CREATE_ID));
							if(!Utils.isNullorEmpty(createDateStr)){
								infoData.mCreateId =  Long.parseLong(createDateStr);
							}
							
							String sendDateStr=  result.getString(result.getColumnIndex(FT_SEND_ID));
							infoData.mResponseId =  result.getString(result.getColumnIndex(FT_RESPONSE_ID));
							
							if(!Utils.isNullorEmpty(sendDateStr)){
								infoData.mSendId =  Long.parseLong(sendDateStr);
							}
							
							
							if(infoData != null){
								if(isDraft){
									if(infoData.mSendId  <= 0)
										mArrayList.add(infoData);
								}else{
									if(infoData.mSendId  > 0)
										mArrayList.add(infoData);
								}
							}
						}
					
					
				}while(result.moveToNext());			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mArrayList;
	}
	
	public InfoData getFormData(long id){
		InfoData infoData = null;
		SQLiteDatabase db = null;
		Cursor result = null;
		try {
			db = DataBaseHelper.getInstance().getReadableDatabase();
			result = db.rawQuery("SELECT "+FT_ID+",  "+FT_CREATE_ID+",  "+FT_SEND_ID+",  "+FT_FORM_TYPE+",  "+FT_RESPONSE_ID+", "+FT_FORM_DATA+" FROM  "+TABLE_FORMS_DATA+" WHERE "+FT_ID+" =  "+id, null);
			if(result != null && result.moveToFirst()){	
						byte[] blobData = result.getBlob(result.getColumnIndex(FT_FORM_DATA));
						if(blobData != null){
							int formType = result.getInt(result.getColumnIndex(FT_FORM_TYPE));
							if(formType == HomeActivity.MENU_REPORT_RECEIVED){
								infoData = new ReportInfoData(blobData);
							}else if(formType == HomeActivity.MENU_REPORT_CAUGHT){
								infoData = new CaughtInfoData(blobData);
							}
							infoData.mFormType = formType;
							infoData.mId =  result.getInt(result.getColumnIndex(FT_ID));														
							String createDateStr=  result.getString(result.getColumnIndex(FT_CREATE_ID));
							if(!Utils.isNullorEmpty(createDateStr)){
								infoData.mCreateId =  Long.parseLong(createDateStr);
							}
							
							String sendDateStr=  result.getString(result.getColumnIndex(FT_SEND_ID));
							if(!Utils.isNullorEmpty(sendDateStr)){
								infoData.mSendId =  Long.parseLong(sendDateStr);
							}	
							infoData.mResponseId =  result.getString(result.getColumnIndex(FT_RESPONSE_ID));
						}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoData;
		
	}
	
	public long insertFormData(InfoData frmData){
		long id = -1;
		SQLiteDatabase db = null;
		try {
			db = DataBaseHelper.getInstance().getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(FT_CREATE_ID,""+frmData.mCreateId);
			values.put(FT_SEND_ID,""+frmData.mSendId);
			values.put(FT_FORM_TYPE,frmData.mFormType);
			values.put(FT_FORM_DATA,frmData.getBytes());
			values.put(FT_RESPONSE_ID,frmData.mResponseId);
			id = db.insert(TABLE_FORMS_DATA, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
		
	}
	
	public long updateFormData(InfoData frmData){
		long id = -1;
		if(frmData.mId <= 0){
			id = insertFormData(frmData);
		}else{
			id = frmData.mId;
			SQLiteDatabase db = null;
			try {
				db = DataBaseHelper.getInstance().getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(FT_CREATE_ID,""+frmData.mCreateId);
				values.put(FT_SEND_ID,""+frmData.mSendId);
				values.put(FT_FORM_TYPE,frmData.mFormType);
				values.put(FT_FORM_DATA,frmData.getBytes());
				values.put(FT_RESPONSE_ID,frmData.mResponseId);
				db.update(TABLE_FORMS_DATA, values, FT_ID+" = "+id, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	
	public long deleteFormData(long id){
		
			SQLiteDatabase db = null;
			try {
				db = DataBaseHelper.getInstance().getWritableDatabase();		
				id = db.delete(TABLE_FORMS_DATA,  FT_ID+" = "+id, null);
			} catch (Exception e) {			
				e.printStackTrace();
			}
		
		return id;
	}

}
