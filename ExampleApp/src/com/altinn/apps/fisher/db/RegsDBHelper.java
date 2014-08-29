package com.altinn.apps.fisher.db;
/**
 * Currently not in use
 */

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.altinn.apps.fisher.utils.Utils;

public class RegsDBHelper implements IDBHelper {

	private boolean mIsUpdateNeeded = true;
	private ArrayList<String> mRegsList;
	public RegsDBHelper() {
		mRegsList = new ArrayList<String>();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_REGS_DATA + " ("+FT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FT_NAME+" TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGS_DATA);
         onCreate(db);

	}
	
	public static final String TABLE_REGS_DATA = "REGS_TABLE";
	public static final String FT_ID = "id";
	public static final String FT_NAME = "name";	
	
	
	
	
	private long insertData(String  regsNumber){
		mIsUpdateNeeded = true;
		long id = -1;
		SQLiteDatabase db = null;
		try {
			db = DataBaseHelper.getInstance().getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(FT_NAME,""+regsNumber);
			id = db.insert(TABLE_REGS_DATA, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
		
	}
	
	public long updateData(String regsDetails){
		mIsUpdateNeeded = true;
		long id = -1;
		if(!Utils.isNullorEmpty(regsDetails)){
			if(!isAlreadyExsists(regsDetails)){
				id = insertData(regsDetails);
			}		
		}
		return id;
	}
	
	private boolean isAlreadyExsists(String regsDetails){
		boolean result = false;
		getList();
		for(String item : mRegsList){
			if((""+item).equalsIgnoreCase(""+regsDetails)){
				result = true;
				break;
			}
		}
		return result;
		
	}
	
	public long deleteData(long id){
			mIsUpdateNeeded = true;
			SQLiteDatabase db = null;
			try {
				db = DataBaseHelper.getInstance().getWritableDatabase();		
				id = db.delete(TABLE_REGS_DATA,  FT_ID+" = "+id, null);
			} catch (Exception e) {			
				e.printStackTrace();
			}
		
		return id;
	}
	
	public ArrayList<String> getList(){
		if(mIsUpdateNeeded){
			mIsUpdateNeeded = false;
			mRegsList.clear();			
			SQLiteDatabase db = null;
			Cursor result = null;
			try {
				db = DataBaseHelper.getInstance().getReadableDatabase();
				result = db.rawQuery("SELECT "+FT_ID+",  "+FT_NAME+"  FROM  "+TABLE_REGS_DATA+" ORDER BY "+ FT_ID + "", null);
				if(result != null && result.moveToFirst()){					
					do{			
						String shipName = result.getString(result.getColumnIndex(FT_NAME));							
						mRegsList.add(shipName);
						
					}while(result.moveToNext());			
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		return mRegsList;
	}
	
	
	
	public ArrayList<String> getNameList(){
		getList();
		return mRegsList;
	}
	
}
