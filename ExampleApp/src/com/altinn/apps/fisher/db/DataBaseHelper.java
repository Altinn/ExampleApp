package com.altinn.apps.fisher.db;

/**
 * This is a ParentHelper class
 * All kind of helper class can be initiated from here and respective helper can be generated
 * Respective helper will work on respective table operation(delete/insert/update/search etc).
 * 
 */


import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.altinn.apps.fisher.AppContext;



public class DataBaseHelper extends SQLiteOpenHelper {
	public static final String DB_PATH = "/data/data/com.altinn.apps.fisher/databases/";
	private static DataBaseHelper instance;
	public static final String DB_NAME = "fisher_app.db";
	private static final int DB_VERSION = 1;
	private final Context myContext;
	private FormDBHelper mFormDBHelper;
	private FactoryDBHelper mFactoryDBHelper;
	private FishCategoryDBHelper mFishDBHelper;//Not in use
	private VesselDBHelper mVesselDBHelper;//Not in use
	private RegsDBHelper mRegsDBHelper;//Not in use
	public static final int TABLE_HEPLER_FORM = 100;
	public static final int TABLE_HEPLER_FACTORY = 101;
	public static final int TABLE_HEPLER_FISH_CATEGORY = 102;
	public static final int TABLE_HEPLER_SHIP_NAME = 103;
	public static final int TABLE_HEPLER_REGS_NUMBER = 104;
	
	private ArrayList<IDBHelper> mHelperList;

	private DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.myContext = context;
		
		mHelperList = new ArrayList<IDBHelper>();
		initHelpers();
	}
	
	private void initHelpers(){
		mFormDBHelper = new FormDBHelper();
		mHelperList.add(mFormDBHelper);
		
		mFactoryDBHelper = new FactoryDBHelper();
		mHelperList.add(mFactoryDBHelper);
		
		mFishDBHelper = new FishCategoryDBHelper();
		mHelperList.add(mFishDBHelper);
		
		mVesselDBHelper = new VesselDBHelper();
		mHelperList.add(mVesselDBHelper);
		
		mRegsDBHelper = new RegsDBHelper();
		mHelperList.add(mRegsDBHelper);
	}
	
	
	
	public IDBHelper getDBHelper(int tableHelper){
		IDBHelper helper = null;
		switch(tableHelper){
		case TABLE_HEPLER_FORM:
			helper = mFormDBHelper;
			break;
		case TABLE_HEPLER_FACTORY:
			helper = mFactoryDBHelper;
			break;
			
		case TABLE_HEPLER_FISH_CATEGORY:
			helper = mFishDBHelper;
			break;
			
		case TABLE_HEPLER_SHIP_NAME:
			helper = mVesselDBHelper;
			break;
			
		case TABLE_HEPLER_REGS_NUMBER:
			helper = mRegsDBHelper;
			break;
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 for(IDBHelper helper:mHelperList){
			 helper.onCreate(db);
		 }
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 for(IDBHelper helper:mHelperList){
			 helper.onUpgrade(db,arg1,arg2);
		 }
	}
	
	public static synchronized DataBaseHelper getInstance() {
		if(instance != null){
			return instance;
		} else{
			instance = new DataBaseHelper(AppContext.getInstance());
			return instance;
		}
	}
	


}
