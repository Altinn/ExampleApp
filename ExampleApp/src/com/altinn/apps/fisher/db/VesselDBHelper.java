package com.altinn.apps.fisher.db;

/**
 * Currently not in use
 * 
 */
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.altinn.apps.fisher.settings.SettingItem;
import com.altinn.apps.fisher.settings.VesselsDetails;

public class VesselDBHelper implements IDBHelper {

	private boolean mIsUpdateNeeded = true;
	private ArrayList<String> mVesselNumberList;
	private ArrayList<SettingItem> mVesselList;
	private ArrayList<String> mVesselNameList;
	public VesselDBHelper() {
		mVesselNameList = new ArrayList<String>();
		mVesselList = new ArrayList<SettingItem> ();
		mVesselNumberList =  new ArrayList<String>();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_VESSEL_DATA + " ("+FT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FT_NAME+" TEXT, "+FT_NUMBER+" TEXT, "+FT_DESCRIPTION+" TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_VESSEL_DATA);
         onCreate(db);

	}
	
	public static final String TABLE_VESSEL_DATA = "VESSEL_TABLE";
	public static final String FT_ID = "id";
	public static final String FT_NAME = "name";	
	public static final String FT_NUMBER = "number";	
	public static final String FT_DESCRIPTION = "description";	
	
	
	
	
	public long insertData(VesselsDetails vesselDetails){
		mIsUpdateNeeded = true;
		long id = -1;
		SQLiteDatabase db = null;
		try {
			db = DataBaseHelper.getInstance().getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(FT_NAME,""+vesselDetails.getVesselName());	
			values.put(FT_NUMBER,""+vesselDetails.getVesselNumber());		
			values.put(FT_DESCRIPTION,vesselDetails.getVesselDescription());
			id = db.insert(TABLE_VESSEL_DATA, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
		
	}
	
	public long updateData(VesselsDetails vesselDetails){
		mIsUpdateNeeded = true;
		long id = -1;
		if(vesselDetails.getId() <= 0){
			id = insertData(vesselDetails);
		}else{
			id = vesselDetails.getId();
			SQLiteDatabase db = null;
			try {
				db = DataBaseHelper.getInstance().getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(FT_NAME,""+vesselDetails.getVesselName());	
				values.put(FT_NUMBER,""+vesselDetails.getVesselNumber());		
				values.put(FT_DESCRIPTION,vesselDetails.getVesselDescription());
				db.update(TABLE_VESSEL_DATA, values, FT_ID+" = "+id, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	
	public long deleteData(long id){
			mIsUpdateNeeded = true;
			SQLiteDatabase db = null;
			try {
				db = DataBaseHelper.getInstance().getWritableDatabase();		
				id = db.delete(TABLE_VESSEL_DATA,  FT_ID+" = "+id, null);
			} catch (Exception e) {			
				e.printStackTrace();
			}
		
		return id;
	}
	
	public ArrayList<SettingItem> getList(){
		if(mIsUpdateNeeded){
			mIsUpdateNeeded = false;
			mVesselList.clear();
			mVesselNameList.clear();	
			mVesselNumberList.clear();
			SQLiteDatabase db = null;
			Cursor result = null;
			try {
				db = DataBaseHelper.getInstance().getReadableDatabase();
				result = db.rawQuery("SELECT "+FT_ID+",  "+FT_NAME+",  "+FT_DESCRIPTION+" FROM  "+TABLE_VESSEL_DATA+" ORDER BY "+ FT_ID + "", null);
				if(result != null && result.moveToFirst()){					
					do{							
								VesselsDetails vesselData = new VesselsDetails();								
								vesselData.setId(result.getInt(result.getColumnIndex(FT_ID)));
								String vesselName = result.getString(result.getColumnIndex(FT_NAME));
								String vesselNumber = result.getString(result.getColumnIndex(FT_NUMBER));
								vesselData.setVesselName(vesselName);	
								vesselData.setVesselNumber(vesselNumber);
								vesselData.setVesselDescription(result.getString(result.getColumnIndex(FT_DESCRIPTION)));
								mVesselList.add(vesselData);
								mVesselNameList.add(vesselName);
								mVesselNumberList.add(vesselNumber);
						
					}while(result.moveToNext());			
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		return mVesselList;
	}
	
	public SettingItem getItem(int id){
		SettingItem item = null; 
		getList();
		for(SettingItem temp:mVesselList ){
			if(temp.getId() == id){
				item = temp;
				break;
			}
		}
		return item;
		
	}
	
	public ArrayList<String> getNameList(){
		getList();
		return mVesselNameList;
	}
	
	public ArrayList<String> getNumberList(){
		getList();
		return mVesselNumberList;
	}
	
}
