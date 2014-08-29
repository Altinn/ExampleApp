package com.altinn.apps.fisher.settings;
/**
 * Class not in use
 *
 */
public class FishDetails implements SettingItem{

	private int mID;
	private String mFishName;
	private String mFishDescription;
	
	public FishDetails(int id, String fName,  String fDesc){
		mID = id;
		mFishName = fName;
		mFishDescription = fDesc;
	}
	
	public FishDetails(){
		
	}
	
	public String getFishName() {
		return mFishName;
	}
	public void setFishName(String mFishName) {
		this.mFishName = mFishName;
	}
	
	public int getId() {
		return mID;
	}
	public void setId(int mID) {
		this.mID = mID;
	}
	public String getFishDescription() {
		return mFishDescription;
	}
	public void setFishDescription(String mFishDescription) {
		this.mFishDescription = mFishDescription;
	}
}
