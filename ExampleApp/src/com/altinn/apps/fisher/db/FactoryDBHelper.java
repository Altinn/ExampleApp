package com.altinn.apps.fisher.db;
/**
 * This helper is used to store FirmDetails associated in User Profile
 * 
 */

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.altinn.apps.fisher.settings.FactoryDetails;
import com.altinn.apps.fisher.settings.SettingItem;

public class FactoryDBHelper implements IDBHelper {

	private boolean mIsUpdateNeeded = true;
	private ArrayList<String> mFactoryNameList;
	//private ArrayList<String> mFactoryNumberList;
	ArrayList<SettingItem> mFactoryList;
	
	//public static final String str = "CREATE TABLE IF NOT EXISTS " + TABLE_FACTORY_DATA + " ("+FT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FT_NAME+" TEXT, "+FT_NUMBER+" TEXT, "+FT_ADDRESS+" TEXT,"+FT_STREET+" TEXT ,"+FT_POSTAL+" TEXT);";

	public FactoryDBHelper() {
		mFactoryNameList = new ArrayList<String>();
		//mFactoryNumberList = new ArrayList<String>();
		mFactoryList = new ArrayList<SettingItem> ();
	}

	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.db.IDBHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FACTORY_DATA + " ("+FT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FT_NAME+" TEXT, "+FT_NUMBER+" TEXT, "+FT_ADDRESS+" TEXT,"+FT_STREET+" TEXT ,"+FT_POSTAL+" TEXT);");
	
	   	}

	/*
	 * (non-Javadoc)
	 * @see com.altinn.apps.fisher.db.IDBHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACTORY_DATA);
         onCreate(db);

	}
	
	public static final String TABLE_FACTORY_DATA = "FACTORY_TABLE";
	public static final String FT_ID = "id";
	public static final String FT_NAME = "name";
	public static final String FT_NUMBER = "number";
	public static final String FT_ADDRESS = "address";
	public static final String FT_STREET = "street";
	public static final String FT_POSTAL= "postal";
	
	
	
	public long insertData(FactoryDetails factoryDetails){
		mIsUpdateNeeded = true;
		long id = -1;
		SQLiteDatabase db = null;
		try {
			db = DataBaseHelper.getInstance().getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(FT_NAME,""+factoryDetails.getFactoryName());
			values.put(FT_NUMBER,""+factoryDetails.getFactoryNumber());
//			values.put(FT_ADDRESS,factoryDetails.getFactoryAddress());
//			values.put(FT_STREET,factoryDetails.getFactoryPlace());
//			values.put(FT_POSTAL,factoryDetails.getFactoryPostal());
			
			values.put(FT_ADDRESS,"");
			values.put(FT_STREET,"");
			values.put(FT_POSTAL,"");
			
			id = db.insert(TABLE_FACTORY_DATA, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
		
	}
	
	public long updateData(FactoryDetails factoryDetails){
		mIsUpdateNeeded = true;
		long id = -1;
		if(factoryDetails.getId() <= 0){
			id = insertData(factoryDetails);
		}else{
			id = factoryDetails.getId();
			SQLiteDatabase db = null;
			try {
				db = DataBaseHelper.getInstance().getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(FT_NAME,""+factoryDetails.getFactoryName());
				values.put(FT_NUMBER,""+factoryDetails.getFactoryNumber());
//				values.put(FT_ADDRESS,factoryDetails.getFactoryAddress());
//				values.put(FT_STREET,factoryDetails.getFactoryPlace());
//				values.put(FT_POSTAL,factoryDetails.getFactoryPostal());
				
				values.put(FT_ADDRESS,"");
				values.put(FT_STREET,"");
				values.put(FT_POSTAL,"");
				db.update(TABLE_FACTORY_DATA, values, FT_ID+" = "+id, null);
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
				id = db.delete(TABLE_FACTORY_DATA,  FT_ID+" = "+id, null);
			} catch (Exception e) {			
				e.printStackTrace();
			}
		
		return id;
	}
	
	public ArrayList<SettingItem> getList(){
		if(mIsUpdateNeeded){
			mIsUpdateNeeded = false;
			mFactoryList.clear();
			mFactoryNameList.clear();
			//mFactoryNumberList.clear();
			
			SQLiteDatabase db = null;
			Cursor result = null;
			try {
				db = DataBaseHelper.getInstance().getReadableDatabase();
				result = db.rawQuery("SELECT "+FT_ID+",  "+FT_NAME+",  "+FT_NUMBER+",  "+FT_ADDRESS+",  "+FT_STREET +" , "+FT_POSTAL+" FROM  "+TABLE_FACTORY_DATA+" ORDER BY "+ FT_ID + "", null);
				if(result != null && result.moveToFirst()){					
					do{							
								FactoryDetails factData = new FactoryDetails();								
								factData.setId(result.getInt(result.getColumnIndex(FT_ID)));
								String factName = result.getString(result.getColumnIndex(FT_NAME));
								factData.setFactoryName(factName);								
								String factNumber = result.getString(result.getColumnIndex(FT_NUMBER));
								factData.setFactoryNumber(factNumber);
								
//								factData.setFactoryAddress(result.getString(result.getColumnIndex(FT_ADDRESS)));
//								factData.setFactoryPlace(result.getString(result.getColumnIndex(FT_STREET)));
//								factData.setFactoryPostal(result.getString(result.getColumnIndex(FT_POSTAL)));
								mFactoryList.add(factData);		
								
								mFactoryNameList.add(factNumber +" - " +factName);
								//mFactoryNumberList.add(factNumber);
						
					}while(result.moveToNext());			
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		return mFactoryList;
	}
	
	public SettingItem getItem(int id){
		SettingItem item = null; 
		getList();
		for(SettingItem temp:mFactoryList ){
			if(temp.getId() == id){
				item = temp;
				break;
			}
		}
		return item;
		
	}
	
	/**
	 * This method clubs factory-name and number and send that list
	 * which is used in spinner item of ReportReceivedFishActivity
	 * @return
	 */
	public ArrayList<String> getNameList(){
		getList();
		return mFactoryNameList;
	}
	
//	public ArrayList<String> getNumberList(){
//		getList();
//		return mFactoryNumberList;
//	}
	
	

}
