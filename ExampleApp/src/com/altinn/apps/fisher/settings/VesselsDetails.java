package com.altinn.apps.fisher.settings;
/**
 * Class not in use
 *
 */
public class VesselsDetails  implements SettingItem{

	private int mID;
	private String mVesselName;
	private String mVesselNumber;
	private String mVesselDescription;
	
	public VesselsDetails(int id, String vName,  String vNumber, String vDesc){
		mID = id;
		mVesselName = vName;
		mVesselNumber = vNumber;
		mVesselDescription = vDesc;
	}
	
	public VesselsDetails(){
		
	}
	
	public String getVesselName() {
		return mVesselName;
	}
	public void setVesselName(String mVesselName) {
		this.mVesselName = mVesselName;
	}
	
	public String getVesselNumber() {
		return mVesselNumber;
	}
	public void setVesselNumber(String mVesselNumber) {
		this.mVesselNumber = mVesselNumber;
	}
	public int getId() {
		return mID;
	}
	public void setId(int mID) {
		this.mID = mID;
	}
	public String getVesselDescription() {
		return mVesselDescription;
	}
	public void setVesselDescription(String mVesselDescription) {
		this.mVesselDescription = mVesselDescription;
	}
}
