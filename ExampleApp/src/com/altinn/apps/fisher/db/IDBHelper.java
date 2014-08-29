package com.altinn.apps.fisher.db;
/**
 * A Global helper interface for all type of DBhelper to achieve abstraction
 * This interface offers to implement functionality by each table in their own constraints
 * 
 */
import android.database.sqlite.SQLiteDatabase;

public interface IDBHelper {

	/**
	 * On create of table this method will be invoked by DataBaseHelper 
	 * So that respective table can be created as per their skeleton and 
	 * Remove dependency from DataBaseHelper
	 * 
	 * This is delegated by DataBaseHelper class when Android framework called onCreate method SQLiteOpenHelper
	 * @param db
	 */
	public void onCreate(SQLiteDatabase db);
	
	/**
	 * This method invoked by DataBaseHelper and operation executed on respective table if onUpgared called by android framework
	 * So that respective table can be upgraded as per their skeleton/constraints and 
	 * Remove dependency from DataBaseHelper 
	 * This is delegated by DataBaseHelper class when Android framework called onUpgrade method SQLiteOpenHelper
 	 *
	 * @param db
	 * @param arg1
	 * @param arg2
	 */
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2);
}
