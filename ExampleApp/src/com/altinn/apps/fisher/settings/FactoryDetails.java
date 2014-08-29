package com.altinn.apps.fisher.settings;
/**
 * This class is used to create a new Factory item 
 * This used when user selects Bottom button of UserProfileActicity, or any item of list view in UserProfileActicity
 *
 */
public class FactoryDetails  implements SettingItem{

	private int mID;
	private String mFactoryName;
	//private String mFactoryAddress;
	private String mFactoryNumber;
	//private String mFactoryPostal;
	//private String mFactoryPlace;
	
//	public FactoryDetails(int id, String fName, String fAdd, String fNumber, String fPlace,String postal){
//		mID = id;
//		mFactoryName = fName;
//		mFactoryAddress = fAdd;
//		mFactoryNumber = fNumber;
//		mFactoryPostal = postal;
//		mFactoryPlace = fPlace;
//	}
	
	public FactoryDetails(int id, String fName,  String fNumber){
		mID = id;
		mFactoryName = fName;
		mFactoryNumber = fNumber;
	}
	
	public FactoryDetails(){
		
	}
	
	public String getFactoryName() {
		return mFactoryName;
	}
	public void setFactoryName(String mFactoryName) {
		this.mFactoryName = mFactoryName;
	}
//	public String getFactoryAddress() {
//		return mFactoryAddress;
//	}
//	public void setFactoryAddress(String mFactoryAddress) {
//		this.mFactoryAddress = mFactoryAddress;
//	}
	public String getFactoryNumber() {
		return mFactoryNumber;
	}
	public void setFactoryNumber(String mFactoryNumber) {
		this.mFactoryNumber = mFactoryNumber;
	}
	public int getId() {
		return mID;
	}
	public void setId(int mID) {
		this.mID = mID;
	}
//	public String getFactoryPlace() {
//		return mFactoryPlace;
//	}
//	public void setFactoryPlace(String mFactoryDescription) {
//		this.mFactoryPlace = mFactoryDescription;
//	}
//
//	public String getFactoryPostal() {
//		return mFactoryPostal;
//	}

//	public void setFactoryPostal(String mFactoryPostal) {
//		this.mFactoryPostal = mFactoryPostal;
//	}
}
