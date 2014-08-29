package com.altinn.apps.fisher.db;

/**
 * Currently not in use
 * 
 */
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.altinn.apps.fisher.settings.FishDetails;
import com.altinn.apps.fisher.settings.SettingItem;

public class FishCategoryDBHelper implements IDBHelper {

	private boolean mIsUpdateNeeded = true;
	private ArrayList<String> mFishCategoryNameList;
	ArrayList<SettingItem> mFishCategoryList;
	public FishCategoryDBHelper() {
		mFishCategoryNameList = new ArrayList<String>();
		mFishCategoryList = new ArrayList<SettingItem> ();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FISH_DATA + " ("+FT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FT_NAME+" TEXT,"+FT_DESCRIPTION+" TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_FISH_DATA);
         onCreate(db);

	}
	
	public static final String TABLE_FISH_DATA = "FISH_TABLE";
	public static final String FT_ID = "id";
	public static final String FT_NAME = "name";	
	public static final String FT_DESCRIPTION = "description";
	
	
	
	public long insertData(FishDetails fishDetails){
		mIsUpdateNeeded = true;
		long id = -1;
		SQLiteDatabase db = null;
		try {
			db = DataBaseHelper.getInstance().getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(FT_NAME,""+fishDetails.getFishName());			
			values.put(FT_DESCRIPTION,fishDetails.getFishDescription());
			id = db.insert(TABLE_FISH_DATA, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
		
	}
	
	public long updateData(FishDetails fishDetails){
		mIsUpdateNeeded = true;
		long id = -1;
		if(fishDetails.getId() <= 0){
			id = insertData(fishDetails);
		}else{
			id = fishDetails.getId();
			SQLiteDatabase db = null;
			try {
				db = DataBaseHelper.getInstance().getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(FT_NAME,""+fishDetails.getFishName());			
				values.put(FT_DESCRIPTION,fishDetails.getFishDescription());
				db.update(TABLE_FISH_DATA, values, FT_ID+" = "+id, null);
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
				id = db.delete(TABLE_FISH_DATA,  FT_ID+" = "+id, null);
			} catch (Exception e) {			
				e.printStackTrace();
			}
		
		return id;
	}
	
	public ArrayList<SettingItem> getList(){
		if(mIsUpdateNeeded){
			mIsUpdateNeeded = false;
			mFishCategoryList.clear();
			mFishCategoryNameList.clear();			
			SQLiteDatabase db = null;
			Cursor result = null;
			try {
				db = DataBaseHelper.getInstance().getReadableDatabase();
				result = db.rawQuery("SELECT "+FT_ID+",  "+FT_NAME+",  "+FT_DESCRIPTION+" FROM  "+TABLE_FISH_DATA+" ORDER BY "+ FT_ID + "", null);
				if(result != null && result.moveToFirst()){					
					do{							
								FishDetails factData = new FishDetails();								
								factData.setId(result.getInt(result.getColumnIndex(FT_ID)));
								String factName = result.getString(result.getColumnIndex(FT_NAME));
								factData.setFishName(factName);	
								factData.setFishDescription(result.getString(result.getColumnIndex(FT_DESCRIPTION)));
								mFishCategoryList.add(factData);
								mFishCategoryNameList.add(factName);
						
					}while(result.moveToNext());			
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		return mFishCategoryList;
	}
	
	public SettingItem getItem(int id){
		SettingItem item = null; 
		getList();
		for(SettingItem temp:mFishCategoryList ){
			if(temp.getId() == id){
				item = temp;
				break;
			}
		}
		return item;
		
	}
	
	public ArrayList<String> getNameList(){
		getList();
		return mFishCategoryNameList;
	}
	
}
