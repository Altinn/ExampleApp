package com.altinn.apps.fisher.models;
/**
 * 
 * This class holds the content of user profile
 * 
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.altinn.apps.fisher.net.jsobj.OrganisationObj;

public class UserProfile {

	private String mName;
	private String mTitle;
	private String mPhoneNumber;
	private String mEmail;
	private ArrayList<OrganisationObj> mListUnits;
	private int mActiveUnitIndex;
	public UserProfile() {
		mListUnits = new ArrayList<OrganisationObj>();
		
	}
	
	/**
	 * This method creates instance of this class by using saved byte stream [De-Marshaling of object]
	 * @param data
	 */
	public UserProfile(byte[] data) {
		mListUnits = new ArrayList<OrganisationObj>();	
		intData(data);
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		this.mTitle = title;
	}
	public String getPhoneNumber() {
		return mPhoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.mPhoneNumber = phoneNumber;
	}
	public String getEmail() {
		return mEmail;
	}
	public void setEmail(String email) {
		this.mEmail = email;
	}
	
	/*USED FOR Displaying VELG ENHIT [Select Unit] */	 
	public void addUnit(OrganisationObj item){
		mListUnits.add(item);
	}
	
	public ArrayList<OrganisationObj> getUnitList(){
		return mListUnits;
	}
	
	/**
	 * This method returns list of unit name which is displayed in the 
	 * drop-down spinner of UserProfileActivity.
	 * @return
	 */
	public ArrayList<String> getUnitListNames(){
		ArrayList<String> strList = new ArrayList<String>();
		if(mListUnits != null){
			for(OrganisationObj temp:mListUnits){
				strList.add(temp.mName);
			}
		}
		return strList;
	}
	
	/**
	 * This method returns the organization number corresponding to the unit name
	 * @param unitName
	 * @return
	 */
	public String getOrganizationNumber(String unitName){
		String orgNumber = null;
		ArrayList<String> unitListName = getUnitListNames();
		int index = 0;
		for(String unitNameTemp:unitListName){
			if(unitNameTemp.equalsIgnoreCase(unitName)){
				if(mListUnits != null){
					orgNumber = mListUnits.get(index).mOrganizationNumber;
				}
			}
			index++;
		}		
		return orgNumber;
	}
	
	public void clearList(){
		mListUnits.clear();
	}
	
	
	public int getActiveUnitIndex() {
		return mActiveUnitIndex;
	}
	public void setActiveUnitIndex(int activeUnitIndex) {
		this.mActiveUnitIndex = activeUnitIndex;
	}
	
	
	/**
	 * At present WEB-Service not available to get the user info like name/title/phone/email
	 * At present we have a web-service to get mListUnits through UserProfileTask, 
	 * In future this web-service need to modified to retrieve user details too.
	 * 
	 */
	public void initTestProfile(){
		mName = "Navn Navnesen";
		mTitle = "Title";
		mPhoneNumber = "+47 000 00 000";
		mEmail = "navn.navnesen@fornebuoppdrett.no";
		
		OrganisationObj obj1 = new OrganisationObj();
		obj1.mName = "Fiskekompaniet A/S";
		obj1.mOrganizationNumber= "FiskeMottak1";
		obj1.mMessageLink="Not Specified";
		addUnit(obj1);
		
		OrganisationObj obj2 = new OrganisationObj();
		obj2.mName = "Fiskekompaniet B/S";
		obj2.mOrganizationNumber= "FiskeMottak2";
		obj2.mMessageLink="Not Specified";
		addUnit(obj2);
		
							
	}
	
	/**
	 * Actual De-Marshaling done here
	 * @param dataBytes
	 */
	private void intData(byte[] dataBytes){
		if(dataBytes != null && dataBytes.length > 0){
			ByteArrayInputStream bis = new ByteArrayInputStream(dataBytes);
			DataInputStream dis = new DataInputStream(bis);
			try {
				mName = dis.readUTF();
				mTitle = dis.readUTF();
				mPhoneNumber = dis.readUTF();
				mEmail = dis.readUTF();
				mActiveUnitIndex = dis.readInt();
				int listCount = dis.readInt();
				for(int i = 0 ; i <listCount; i++ ){
					OrganisationObj org = new OrganisationObj();
					org.mName = dis.readUTF();
					org.mOrganizationNumber = dis.readUTF();
					org.mMessageLink = dis.readUTF();	
					org.mType = dis.readUTF();	
					mListUnits.add(org);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(NullPointerException npe){
				
			}
			finally{
				try {
					if(dis != null)
						dis.close();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(bis != null)
						bis.close();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}
	
	/**
	 * Marshaling of object done here.
	 * Object state converted into byte-stream
	 * @return
	 */
	public byte[] getBytes(){
		byte[] dataBytes = null;
		
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			try {
				 dos.writeUTF(mName);
				 dos.writeUTF(mTitle);
				 dos.writeUTF(mPhoneNumber);
				 dos.writeUTF(mEmail);
				 dos.writeInt(mActiveUnitIndex);
				int listCount = mListUnits.size();
				dos.writeInt(listCount);
				for(int i = 0 ; i <listCount; i++ ){
					OrganisationObj org = mListUnits.get(i);
					dos.writeUTF(""+org.mName);
					dos.writeUTF(""+org.mOrganizationNumber);
					dos.writeUTF(""+org.mMessageLink);
					dos.writeUTF(""+org.mType);
				}
				dos.flush();
				bos.flush();
				dataBytes = bos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}catch(NullPointerException npe){
				
			}
			finally{
				try {
					if(dos != null)
						dos.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(bos != null)
						bos.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		return dataBytes;
	}
	
	
	

}
